package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.recipe.ResourceGeneratingRecipe;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipeTypes {
    public static final IRecipeType<ResourceGeneratingRecipe> RESOURCE_GENERATING =
            IRecipeType.register(CuboidResourceGenMod.MOD_ID + ":resource_generating");

    static void register() {}
}
