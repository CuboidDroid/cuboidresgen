package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
  // Singularity Resource Generators

  public static final RegistryObject<IronSrgBlock> IRON_SRG = register(
          "iron_srg", () ->
                  new IronSrgBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<GoldSrgBlock> GOLD_SRG = register(
          "gold_srg", () ->
                  new GoldSrgBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<DiamondSrgBlock> DIAMOND_SRG = register(
          "diamond_srg", () ->
                  new DiamondSrgBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<EmeraldSrgBlock> EMERALD_SRG = register(
          "emerald_srg", () ->
                  new EmeraldSrgBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<NetheriteSrgBlock> NETHERITE_SRG = register(
          "netherite_srg", () ->
                  new NetheriteSrgBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  static void register() {
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> blockSupplier) {
    RegistryObject<T> ret = registerNoItem(name, blockSupplier);
    Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(CuboidResourceGenMod.CUBOIDSRG_ITEM_GROUP)));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return register(name, sup, block -> item(block, renderMethod));
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
    RegistryObject<T> ret = registerNoItem(name, sup);
    Registration.ITEMS.register(name, itemCreator.apply(ret));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> blockSupplier) {
    return Registration.BLOCKS.register(name, blockSupplier);
  }

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return () -> new BlockItem(block.get(), new Item.Properties().tab(CuboidResourceGenMod.CUBOIDSRG_ITEM_GROUP).setISTER(renderMethod));
  }
}
