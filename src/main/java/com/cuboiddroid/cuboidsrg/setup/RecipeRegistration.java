package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.recipe.ResourceGeneratingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CuboidResourceGenMod.MOD_ID)
@Mod.EventBusSubscriber(modid = CuboidResourceGenMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistration {
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

        registry.register(new ResourceGeneratingRecipe.Serializer().setRegistryName(CuboidResourceGenMod.MOD_ID, "resource_generating"));
    }
}
