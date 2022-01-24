package com.cuboiddroid.cuboidsrg.modules.resgen.tile;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.IronSrgContainer;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.AvailableDimensionsCache;
import com.cuboiddroid.cuboidsrg.setup.ModTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class IronSrgTileEntity extends SrgTileEntityBase {
    public IronSrgTileEntity() {
        super(ModTileEntities.IRON_SRG.get(),
                Config.ironTicksPerOperation.get(),
                Config.ironItemsPerOperation.get(),
                Config.ironInternalBuffer.get() + 64,
                Config.ironAutoEject.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuboidsrg.iron_srg");
    }

    @Override
    public Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new IronSrgContainer(i, level, worldPosition, playerInventory, playerEntity);
    }

    @Override
    public boolean isAvailableInDimension(MinecraftServer server, ResourceLocation dimensionId)
    {
        return AvailableDimensionsCache.isTierEnabledInDimension(server, "iron", dimensionId);
    }

    @Override
    public String getTier()
    {
        return "iron";
    }

}