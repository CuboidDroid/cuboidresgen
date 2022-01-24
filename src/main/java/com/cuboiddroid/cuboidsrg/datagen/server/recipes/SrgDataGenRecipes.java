package com.cuboiddroid.cuboidsrg.datagen.server.recipes;

import com.cuboiddroid.cuboidsrg.datagen.server.ModRecipeProvider;
import com.cuboiddroid.cuboidsrg.modules.resgen.block.SrgBlockBase;
import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class SrgDataGenRecipes extends DataGenRecipesBase {
    public static void build(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        srgBasic(provider, consumer,
                ModBlocks.IRON_SRG.get(),
                Blocks.IRON_BLOCK,
                "iron");

        srgBasic(provider, consumer,
                ModBlocks.GOLD_SRG.get(),
                Blocks.GOLD_BLOCK,
                "gold");

        srgBasic(provider, consumer,
                ModBlocks.DIAMOND_SRG.get(),
                Blocks.DIAMOND_BLOCK,
                "diamond");

        srgBasic(provider, consumer,
                ModBlocks.EMERALD_SRG.get(),
                Blocks.EMERALD_BLOCK,
                "emerald");

        srgBasic(provider, consumer,
                ModBlocks.NETHERITE_SRG.get(),
                Blocks.NETHERITE_BLOCK,
                "netherite");

        // upgrades
        srgUpgrade(provider, consumer,
                ModBlocks.GOLD_SRG.get(),
                ModBlocks.IRON_SRG.get(),
                Blocks.GOLD_BLOCK,
                "gold");

        srgUpgrade(provider, consumer,
                ModBlocks.DIAMOND_SRG.get(),
                ModBlocks.GOLD_SRG.get(),
                Blocks.DIAMOND_BLOCK,
                "diamond");

        srgUpgrade(provider, consumer,
                ModBlocks.EMERALD_SRG.get(),
                ModBlocks.DIAMOND_SRG.get(),
                Blocks.EMERALD_BLOCK,
                "emerald");

        srgUpgrade(provider, consumer,
                ModBlocks.NETHERITE_SRG.get(),
                ModBlocks.EMERALD_SRG.get(),
                Blocks.NETHERITE_BLOCK,
                "netherite");
    }

    private static void srgBasic(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, SrgBlockBase output, Block block, String materialName) {
        ShapedRecipeBuilder.shaped(output)
                .define('#', block)
                .define('$', Blocks.BLAST_FURNACE)
                .define('!', Blocks.CRAFTING_TABLE)
                .pattern("###")
                .pattern("$!$")
                .pattern("###")
                .unlockedBy("has_item", provider.hasItem(Blocks.BLAST_FURNACE))
                .save(consumer, modId(materialName + "_srg"));
    }

    private static void srgUpgrade(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, SrgBlockBase output, SrgBlockBase prevTier, Block block, String materialName) {
        ShapedRecipeBuilder.shaped(output)
                .define('#', block)
                .define('$', Blocks.BLAST_FURNACE)
                .define('!', Blocks.CRAFTING_TABLE)
                .define('@', prevTier)
                .pattern(" $#")
                .pattern("#@#")
                .pattern("#! ")
                .unlockedBy("has_item", provider.hasItem(Blocks.BLAST_FURNACE))
                .save(consumer, modId(materialName + "_srg_upgrade"));
    }

}
