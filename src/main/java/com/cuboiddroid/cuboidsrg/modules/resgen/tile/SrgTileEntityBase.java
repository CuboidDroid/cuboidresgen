package com.cuboiddroid.cuboidsrg.modules.resgen.tile;

import com.cuboiddroid.cuboidsrg.modules.resgen.recipe.ResourceGeneratingRecipe;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.AvailableDimensionsCache;
import com.cuboiddroid.cuboidsrg.setup.ModRecipeTypes;
import com.cuboiddroid.cuboidsrg.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class SrgTileEntityBase extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler inputItemHandler = createInputHandler();
    private ItemStackHandler outputItemHandler = createOutputHandler();
    private CombinedInvWrapper combinedItemHandler = new CombinedInvWrapper(inputItemHandler, outputItemHandler);

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(() -> combinedItemHandler);

    private LazyOptional<IItemHandler> autoEjectTarget = null;

    public static final int INPUT_SLOTS = 1;
    public static final int OUTPUT_SLOTS = 1;
    public static final int TOTAL_SLOTS = INPUT_SLOTS + OUTPUT_SLOTS;

    public static final int SINGULARITY_INPUT = 0;
    public static final int OUTPUT = 1;

    private int itemsAvailable;
    private int processingTime;
    private int ticksPerOperation;
    private int itemsPerOperation;
    private int maxItems;
    private boolean autoEject;

    private ResourceGeneratingRecipe cachedRecipe = null;

    public SrgTileEntityBase(
            TileEntityType<?> tileEntityType,
            int ticksPerOperation,
            int itemsPerOperation,
            int maxItems,
            boolean autoEject) {
        super(tileEntityType);

        this.itemsAvailable = 0;
        this.processingTime = 0;
        this.ticksPerOperation = ticksPerOperation;
        this.itemsPerOperation = itemsPerOperation;
        this.maxItems = maxItems;
        this.autoEject = autoEject;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
        outputHandler.invalidate();
        combinedHandler.invalidate();
    }

    public abstract boolean isAvailableInDimension(MinecraftServer server, ResourceLocation dimensionId);

    public abstract String getTier();

    @Override
    public void tick() {
        if (level == null || level.isClientSide)
            return;

        if (!isAvailableInDimension(level.getServer(), level.dimension().location())) {
            BlockState blockState = this.level.getBlockState(this.worldPosition);
            if (blockState.getValue(BlockStateProperties.LIT)) {
                this.level.setBlock(this.worldPosition, blockState.setValue(BlockStateProperties.LIT, false),
                        Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
                // ensure we send an update if we change state
                setChanged();
            }

            return;
        }

        ResourceGeneratingRecipe recipe = getRecipe();

        if (recipe == null) {
            this.itemsAvailable = 0;
            this.processingTime = 0;
            setChanged();
        } else {
            if (processingTime > 0) {
                processingTime--;
                if (processingTime <= 0) {
                    if (this.itemsPerOperation > 0) {
                        this.itemsAvailable += (int) Math.ceil(this.itemsPerOperation * recipe.getOutputMultiplier());

                        if (this.itemsAvailable > maxItems)
                            this.itemsAvailable = maxItems;
                    }
                }
                setChanged();
            }

            if (processingTime <= 0) {
                ItemStack generatedItem = getWorkOutput(recipe);

                ItemStack outputStack = combinedItemHandler.getStackInSlot(OUTPUT);
                if (outputStack.isEmpty()) {
                    // empty output stack - so we can just shift over the number available or
                    // the max stack size, whichever is smaller
                    generatedItem.setCount(Math.min(this.itemsAvailable, generatedItem.getMaxStackSize()));
                    this.itemsAvailable -= generatedItem.getCount();
                    this.combinedItemHandler.setStackInSlot(OUTPUT, generatedItem);
                } else if (!outputStack.sameItem(generatedItem)) {
                    // the output stack contains a different item - do nothing!
                    // the available items will increase, but the output slot will need
                    // to be emptied before getting our hands on some of it
                } else {
                    // output stack has stuff in it - how much?
                    int count = outputStack.getCount();

                    // get a copy to work on - we should not change the stack returned from
                    // getStackInSlot(...)
                    ItemStack newOutputStack = outputStack.copy();
                    if (count + this.itemsAvailable < generatedItem.getMaxStackSize()) {
                        // what is in the output stack, plus the number of items available,
                        // is less than the max stack size - so we can move them all across
                        newOutputStack.grow(this.itemsAvailable);
                        this.itemsAvailable = 0;
                    } else {
                        // we've got enough to top up the output stack to max, and have
                        // some left - so work out how many we will move
                        int itemsToMove = generatedItem.getMaxStackSize() - count;
                        newOutputStack.grow(itemsToMove);
                        this.itemsAvailable -= itemsToMove;
                    }

                    combinedItemHandler.setStackInSlot(OUTPUT, newOutputStack);
                }

                doAutoEject();

                // start work on the next one
                processingTime = (int) Math.ceil(ticksPerOperation * recipe.getWorkTimeMultiplier());
            }
        }

        BlockState blockState = this.level.getBlockState(this.worldPosition);
        if (blockState.getValue(BlockStateProperties.LIT) != processingTime > 0) {
            this.level.setBlock(this.worldPosition, blockState.setValue(BlockStateProperties.LIT, processingTime > 0),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Nullable
    public ResourceGeneratingRecipe getRecipe() {
        if (this.level == null
                || this.inputItemHandler.getStackInSlot(0).isEmpty())
            return null;

        // make an inventory
        IInventory inv = getInputsAsInventory();

        if (cachedRecipe == null || !cachedRecipe.matches(inv, this.level)) {
            cachedRecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.RESOURCE_GENERATING, inv, this.level).orElse(null);
        }

        return cachedRecipe;
    }

    private Inventory getInputsAsInventory() {
        return new Inventory(this.inputItemHandler.getStackInSlot(0).copy());
    }

    private ItemStack getWorkOutput(@Nullable ResourceGeneratingRecipe recipe) {
        if (recipe != null) {
            return recipe.getResultItem().copy();
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        inputItemHandler.deserializeNBT(tag.getCompound("invIn"));
        outputItemHandler.deserializeNBT(tag.getCompound("invOut"));
        processingTime = tag.getInt("procTime");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("invIn", inputItemHandler.serializeNBT());
        tag.put("invOut", outputItemHandler.serializeNBT());
        tag.putInt("procTime", processingTime);
        return super.save(tag);
    }

    private ItemStackHandler createInputHandler() {
        return new ItemStackHandler(INPUT_SLOTS) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ModTags.Items.QUANTUM_SINGULARITIES.contains(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                // can only insert singularities in the input slot
                if (!ModTags.Items.QUANTUM_SINGULARITIES.contains(stack.getItem()))
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(OUTPUT_SLOTS) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return !ModTags.Items.QUANTUM_SINGULARITIES.contains(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                // can't insert into the output slot
                return stack;
            }
        };
    }

    /**
     * implementing classes should do something like this:
     * <p>
     * return new TranslationTextComponent("cuboidsrg.container.[identifier]");
     *
     * @return the display name
     */
    public abstract ITextComponent getDisplayName();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            // if side is null, then it's not via automation, so provide access to everything
            if (side == null)
                return combinedHandler.cast();

            // if side is not null, then it's automation - we only want to allow output!
            return outputHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public abstract Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity);

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        this.save(nbtTag);
        this.setChanged();
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        this.load(level.getBlockState(worldPosition), tag);
        this.setChanged();
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2);
    }

    /* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client
     */
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        save(nbtTagCompound);
        return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     */
    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        this.load(blockState, parentNBTTagCompound);
    }

    public IInventory getContentDrops() {
        return getInputsAsInventory();
    }

    public void updateAutoEjectTarget() {
        TileEntity tileEntity = level != null ? level.getBlockEntity(worldPosition.above()) : null;
        if (tileEntity != null) {
            LazyOptional<IItemHandler> lazyOptional = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
            if (lazyOptional.isPresent()) {
                if (this.autoEjectTarget != lazyOptional) {
                    this.autoEjectTarget = lazyOptional;
                    autoEjectTarget.addListener(l -> updateAutoEjectTarget());
                }
            } else autoEjectTarget = LazyOptional.empty();
        } else autoEjectTarget = LazyOptional.empty();
    }

    private LazyOptional<IItemHandler> getAutoEjectTarget() {
        if (autoEjectTarget == null)
            updateAutoEjectTarget();
        return autoEjectTarget;
    }

    private void doAutoEject() {
        if (!this.autoEject)
            return;

        ItemStack outputStack = combinedItemHandler.getStackInSlot(OUTPUT);

        if (outputStack.isEmpty())
            return;

        ItemStack stack = outputStack.copy();
        ItemStack result = getAutoEjectTarget()
                .map(iItemHandler -> ItemHandlerHelper.insertItemStacked(iItemHandler, stack, false))
                .orElse(stack);

        combinedItemHandler.setStackInSlot(OUTPUT, result);
        setChanged();
    }
}
