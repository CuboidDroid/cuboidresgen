package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.modules.resgen.recipe.ResourceGeneratingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipeSerializers {

    public static final RegistryObject<IRecipeSerializer<?>> RESOURCE_GENERATING =
            Registration.RECIPE_SERIALIZERS.register("resource_generating",
                    ResourceGeneratingRecipe.Serializer::new);

    static void register() {}
}
