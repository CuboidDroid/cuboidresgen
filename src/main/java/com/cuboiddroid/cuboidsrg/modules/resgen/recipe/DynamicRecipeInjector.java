package com.cuboiddroid.cuboidsrg.modules.resgen.recipe;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.SrgRecipeConfig;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.SrgRecipeConfigsRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public final class DynamicRecipeInjector implements IResourceManagerReloadListener {
    private static RecipeManager recipeManager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        SrgRecipeConfigsRegistry.getInstance().getSrgRecipeConfigs().forEach(config -> {
            if (config.getEnabled()) {
                ResourceGeneratingRecipe recipe = makeResourceGeneratingRecipe(config);
                if (recipe != null) {
                    getRecipeManager().recipes
                            .computeIfAbsent(recipe.getType(), t -> new HashMap<>())
                            .put(recipe.getId(), recipe);
                }
            }
        });
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        recipeManager = event.getDataPackRegistries().getRecipeManager();
        event.addListener(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(RecipesUpdatedEvent event) {
        recipeManager = event.getRecipeManager();
    }

    public static RecipeManager getRecipeManager() {
        if (recipeManager.recipes instanceof ImmutableMap) {
            recipeManager.recipes = new HashMap<>(recipeManager.recipes);
            recipeManager.recipes.replaceAll((t, v) -> new HashMap<>(recipeManager.recipes.get(t)));
        }

        return recipeManager;
    }

    private static ResourceGeneratingRecipe makeResourceGeneratingRecipe(SrgRecipeConfig config) {
        Ingredient ingredient = config.getInputAsIngredient();
        if (ingredient == Ingredient.EMPTY)
            return null;

        ResourceLocation id = config.getId();
        ResourceLocation recipeId = new ResourceLocation(CuboidResourceGenMod.MOD_ID, id.getPath() + "_dynamic_srg");

        return new ResourceGeneratingRecipe(recipeId, ingredient, config.getOutputStack(), config.getTickMultiplier(), config.getCountMultiplier());
    }
}
