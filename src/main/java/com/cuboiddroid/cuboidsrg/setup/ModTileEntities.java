package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.modules.resgen.tile.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModTileEntities {
    public static void register() {
    }

    // Resource Generators

    public static final RegistryObject<TileEntityType<IronSrgTileEntity>> IRON_SRG = register(
            "iron_srg", IronSrgTileEntity::new, ModBlocks.IRON_SRG);

    public static final RegistryObject<TileEntityType<GoldSrgTileEntity>> GOLD_SRG = register(
            "gold_srg", GoldSrgTileEntity::new, ModBlocks.GOLD_SRG);

    public static final RegistryObject<TileEntityType<DiamondSrgTileEntity>> DIAMOND_SRG = register(
            "diamond_srg", DiamondSrgTileEntity::new, ModBlocks.DIAMOND_SRG);

    public static final RegistryObject<TileEntityType<EmeraldSrgTileEntity>> EMERALD_SRG = register(
            "emerald_srg", EmeraldSrgTileEntity::new, ModBlocks.EMERALD_SRG);

    public static final RegistryObject<TileEntityType<NetheriteSrgTileEntity>> NETHERITE_SRG = register(
            "netherite_srg", NetheriteSrgTileEntity::new, ModBlocks.NETHERITE_SRG);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            //noinspection ConstantConditions - null in build
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }
}