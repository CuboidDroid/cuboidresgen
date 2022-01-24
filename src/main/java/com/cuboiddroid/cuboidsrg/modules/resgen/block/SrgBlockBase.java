package com.cuboiddroid.cuboidsrg.modules.resgen.block;

import com.cuboiddroid.cuboidsrg.modules.resgen.registry.AvailableDimensionsCache;
import com.cuboiddroid.cuboidsrg.modules.resgen.tile.SrgTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public abstract class SrgBlockBase extends Block {

    private static final VoxelShape VOXEL_SHAPE = Stream.of(
            Block.box(3, 2, 6, 13, 3, 10), Block.box(3, 13, 6, 13, 14, 10),Block.box(4, 3, 6, 12, 5, 10),
            Block.box(4, 11, 6, 12, 13, 10), Block.box(4.4, 10, 6.2, 11.6, 11.9, 9.8),
            Block.box(4.76, 9, 6.38, 11.24, 10, 9.620000000000001), Block.box(4.76, 6, 6.38, 11.24, 7, 9.620000000000001),
            Block.box(4.922000000000001, 7, 6.461, 11.078, 9, 9.539000000000001), Block.box(4.4, 4, 6.2, 11.6, 5.9, 9.8),
            Block.box(2, 1, 6, 14, 2, 10), Block.box(2, 14, 6, 14, 15, 10), Block.box(6, 2, 3, 10, 3, 6),
            Block.box(6, 13, 3, 10, 14, 6), Block.box(6, 3, 4, 10, 5, 6), Block.box(6, 11, 4, 10, 13, 6),
            Block.box(6.2, 10, 4.4, 9.8, 11.9, 6.2), Block.box(6.38, 9, 4.76, 9.620000000000001, 10, 6.38),
            Block.box(6.38, 6, 4.76, 9.620000000000001, 7, 6.38), Block.box(6.461, 7, 4.922000000000001, 9.539000000000001, 9, 6.461),
            Block.box(6.2, 4, 4.4, 9.8, 5.9, 6.2), Block.box(6, 1, 2, 10, 2, 6), Block.box(6, 14, 2, 10, 15, 6),
            Block.box(6, 2, 10, 10, 3, 13), Block.box(6, 13, 10, 10, 14, 13), Block.box(6, 3, 10, 10, 5, 12),
            Block.box(6, 11, 10, 10, 13, 12), Block.box(6.2, 10, 9.8, 9.8, 11.9, 11.6),
            Block.box(6.38, 9, 9.620000000000001, 9.620000000000001, 10, 11.24),
            Block.box(6.38, 6, 9.620000000000001, 9.620000000000001, 7, 11.24),
            Block.box(6.461, 7, 9.539000000000001, 9.539000000000001, 9, 11.078),
            Block.box(6.2, 4, 9.8, 9.8, 5.9, 11.6), Block.box(6, 1, 10, 10, 2, 14), Block.box(6, 14, 10, 10, 15, 14),
            Block.box(10, 2, 10, 12, 3, 12), Block.box(10, 13, 10, 12, 14, 12), Block.box(10, 3, 10, 11, 5, 11),
            Block.box(10, 11, 10, 11, 13, 11), Block.box(9.8, 10, 9.8, 10.7, 11.9, 10.7),
            Block.box(9.620000000000001, 9, 9.620000000000001, 10.43, 10, 10.43),
            Block.box(9.620000000000001, 6, 9.620000000000001, 10.43, 7, 10.43),
            Block.box(9.539000000000001, 7, 9.539000000000001, 10.308499999999999, 9, 10.308499999999999),
            Block.box(9.8, 4, 9.8, 10.7, 5.9, 10.7), Block.box(10, 1, 10, 13, 2, 13), Block.box(10, 14, 10, 13, 15, 13),
            Block.box(4, 2, 10, 6, 3, 12), Block.box(4, 13, 10, 6, 14, 12), Block.box(5, 3, 10, 6, 5, 11),
            Block.box(5, 11, 10, 6, 13, 11), Block.box(5.3, 10, 9.8, 6.2, 11.9, 10.7),
            Block.box(5.57, 9, 9.620000000000001, 6.38, 10, 10.43),
            Block.box(5.57, 6, 9.620000000000001, 6.38, 7, 10.43),
            Block.box(5.6915000000000004, 7, 9.539000000000001, 6.461, 9, 10.308499999999999),
            Block.box(5.3, 4, 9.8, 6.2, 5.9, 10.7), Block.box(3, 1, 10, 6, 2, 13), Block.box(3, 14, 10, 6, 15, 13),
            Block.box(4, 2, 4, 6, 3, 6), Block.box(4, 13, 4, 6, 14, 6), Block.box(5, 3, 5, 6, 5, 6),
            Block.box(5, 11, 5, 6, 13, 6), Block.box(5.3, 10, 5.3, 6.2, 11.9, 6.2),
            Block.box(5.57, 9, 5.57, 6.38, 10, 6.38), Block.box(5.57, 6, 5.57, 6.38, 7, 6.38),
            Block.box(5.6915000000000004, 7, 5.6915000000000004, 6.461, 9, 6.461),
            Block.box(5.3, 4, 5.3, 6.2, 5.9, 6.2), Block.box(3, 1, 3, 6, 2, 6), Block.box(3, 14, 3, 6, 15, 6),
            Block.box(10, 2, 4, 12, 3, 6), Block.box(10, 13, 4, 12, 14, 6), Block.box(10, 3, 5, 11, 5, 6),
            Block.box(10, 11, 5, 11, 13, 6), Block.box(9.8, 10, 5.3, 10.7, 11.9, 6.2), Block.box(9.620000000000001, 9, 5.57, 10.43, 10, 6.38),
            Block.box(9.620000000000001, 6, 5.57, 10.43, 7, 6.38),
            Block.box(9.539000000000001, 7, 5.6915000000000004, 10.308499999999999, 9, 6.461),
            Block.box(9.8, 4, 5.3, 10.7, 5.9, 6.2), Block.box(10, 1, 3, 13, 2, 6), Block.box(10, 14, 3, 13, 15, 6),
            Block.box(0, 1, 14, 2, 15, 16), Block.box(14, 1, 14, 16, 15, 16), Block.box(0, 1, 0, 2, 15, 2),
            Block.box(14, 1, 0, 16, 15, 2), Block.box(0, 15, 0, 16, 16, 16), Block.box(0, 0, 0, 16, 1, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final int LIGHT_VALUE_WHEN_PRODUCING = 14;

    public SrgBlockBase(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("cuboidsrg.hover_text.singularity_resource_generator"));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? LIGHT_VALUE_WHEN_PRODUCING : 0;
    }

    /**
     * @param state - the block state
     * @param world - the current world
     * @return the new, appropriate tile entity instance
     */
    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.LIT, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        if (level.isClientSide) {
            // return success on client so player swings their hand
            return ActionResultType.SUCCESS;
        }

        this.interactWith(level, pos, player);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World level, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof SrgTileEntityBase) {
            SrgTileEntityBase srgTileEntity = (SrgTileEntityBase) tileEntity;
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return srgTileEntity.getDisplayName();
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return srgTileEntity.createContainer(i, level, pos, playerInventory, playerEntity);
                }
            };

            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = level.getBlockEntity(pos);

            if (tileentity instanceof SrgTileEntityBase) {
                InventoryHelper.dropContents(level, pos, ((SrgTileEntityBase) tileentity).getContentDrops());

                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean p_220069_6_) {
        if (pos.above().equals(fromPos))
            ((SrgTileEntityBase)world.getBlockEntity(pos)).updateAutoEjectTarget();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT);
    }
}
