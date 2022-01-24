package com.cuboiddroid.cuboidsrg.modules.resgen.tile;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.EmeraldSrgContainer;
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

public class EmeraldSrgTileEntity extends SrgTileEntityBase {
    public EmeraldSrgTileEntity() {
        super(ModTileEntities.EMERALD_SRG.get(),
                Config.emeraldTicksPerOperation.get(),
                Config.emeraldItemsPerOperation.get(),
                Config.emeraldInternalBuffer.get() + 64,
                Config.emeraldAutoEject.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuboidsrg.emerald_srg");
    }

    @Override
    public Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new EmeraldSrgContainer(i, level, worldPosition, playerInventory, playerEntity);
    }

    @Override
    public boolean isAvailableInDimension(MinecraftServer server, ResourceLocation dimensionId)
    {
        return AvailableDimensionsCache.isTierEnabledInDimension(server, "emerald", dimensionId);
    }

    @Override
    public String getTier()
    {
        return "emerald";
    }

}