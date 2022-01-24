package com.cuboiddroid.cuboidsrg.compat.jei;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.*;
import com.cuboiddroid.cuboidsrg.modules.resgen.screen.*;
import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import com.cuboiddroid.cuboidsrg.setup.ModRecipeTypes;
import com.cuboiddroid.cuboidsrg.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class CuboidSrgJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = CuboidResourceGenMod.getModId("plugin/main");

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
            registration.addRecipeCategories(new ResourceGeneratingRecipeCategoryJei(guiHelper));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            registration.addRecipes(getRecipesOfType(ModRecipeTypes.RESOURCE_GENERATING), Constants.RESOURCE_GENERATING);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiClickArea.get()) {
            registration.addRecipeClickArea(IronSrgScreen.class, 52, 41, 10, 18, Constants.RESOURCE_GENERATING);
            registration.addRecipeClickArea(GoldSrgScreen.class, 52, 41, 10, 18, Constants.RESOURCE_GENERATING);
            registration.addRecipeClickArea(DiamondSrgScreen.class, 52, 41, 10, 18, Constants.RESOURCE_GENERATING);
            registration.addRecipeClickArea(EmeraldSrgScreen.class, 52, 41, 10, 18, Constants.RESOURCE_GENERATING);
            registration.addRecipeClickArea(NetheriteSrgScreen.class, 52, 41, 10, 18, Constants.RESOURCE_GENERATING);
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            registration.addRecipeTransferHandler(IronSrgContainer.class, Constants.RESOURCE_GENERATING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(GoldSrgContainer.class, Constants.RESOURCE_GENERATING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(DiamondSrgContainer.class, Constants.RESOURCE_GENERATING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(EmeraldSrgContainer.class, Constants.RESOURCE_GENERATING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(NetheriteSrgContainer.class, Constants.RESOURCE_GENERATING, 0, 2, 2, 36);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_SRG.get()), Constants.RESOURCE_GENERATING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.GOLD_SRG.get()), Constants.RESOURCE_GENERATING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.DIAMOND_SRG.get()), Constants.RESOURCE_GENERATING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMERALD_SRG.get()), Constants.RESOURCE_GENERATING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.NETHERITE_SRG.get()), Constants.RESOURCE_GENERATING);
        }
    }
}
