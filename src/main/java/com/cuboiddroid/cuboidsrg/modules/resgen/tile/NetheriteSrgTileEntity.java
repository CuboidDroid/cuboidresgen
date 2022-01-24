package com.cuboiddroid.cuboidsrg.modules.resgen.tile;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.NetheriteSrgContainer;
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

public class NetheriteSrgTileEntity extends SrgTileEntityBase {
    public NetheriteSrgTileEntity() {
        super(ModTileEntities.NETHERITE_SRG.get(),
                Config.netheriteTicksPerOperation.get(),
                Config.netheriteItemsPerOperation.get(),
                Config.netheriteInternalBuffer.get() + 64,
                Config.netheriteAutoEject.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuboidsrg.netherite_srg");
    }

    @Override
    public Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new NetheriteSrgContainer(i, level, worldPosition, playerInventory, playerEntity);
    }

    @Override
    public boolean isAvailableInDimension(MinecraftServer server, ResourceLocation dimensionId)
    {
        return AvailableDimensionsCache.isTierEnabledInDimension(server, "netherite", dimensionId);
    }

    @Override
    public String getTier()
    {
        return "netherite";
    }

}