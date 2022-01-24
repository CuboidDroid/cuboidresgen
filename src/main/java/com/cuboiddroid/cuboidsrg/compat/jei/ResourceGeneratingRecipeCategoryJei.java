package com.cuboiddroid.cuboidsrg.compat.jei;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.modules.resgen.recipe.ResourceGeneratingRecipe;
import com.cuboiddroid.cuboidsrg.modules.resgen.screen.SrgScreenBase;
import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import com.cuboiddroid.cuboidsrg.util.Constants;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;

public class ResourceGeneratingRecipeCategoryJei implements IRecipeCategory<ResourceGeneratingRecipe> {
    private static final int GUI_START_X = 24;
    private static final int GUI_START_Y = 28;
    private static final int GUI_WIDTH = 170 - GUI_START_X;
    private static final int GUI_HEIGHT = 78 - GUI_START_Y;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated itemBar;
    private final String localizedName;
    private final float ironItemsPerSecond;
    private final float goldItemsPerSecond;
    private final float diamondItemsPerSecond;
    private final float emeraldItemsPerSecond;
    private final float netheriteItemsPerSecond;

    public ResourceGeneratingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SrgScreenBase.GUI, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.IRON_SRG.get()));
        arrow = guiHelper.drawableBuilder(SrgScreenBase.GUI, 184, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        itemBar = guiHelper.drawableBuilder(SrgScreenBase.GUI, 176, 0, 8, 36)
                .buildAnimated(200, IDrawableAnimated.StartDirection.BOTTOM, false);
        localizedName = new TranslationTextComponent("jei.category.cuboidsrg.resource_generating").getString();

        ironItemsPerSecond =
                20.0F * Config.ironItemsPerOperation.get()
                        / Config.ironTicksPerOperation.get();

        goldItemsPerSecond =
                20.0F * Config.goldItemsPerOperation.get()
                        / Config.goldTicksPerOperation.get();

        diamondItemsPerSecond =
                20.0F * Config.diamondItemsPerOperation.get()
                        / Config.diamondTicksPerOperation.get();

        emeraldItemsPerSecond =
                20.0F * Config.emeraldItemsPerOperation.get()
                        / Config.emeraldTicksPerOperation.get();

        netheriteItemsPerSecond =
                20.0F * Config.netheriteItemsPerOperation.get()
                        / Config.netheriteTicksPerOperation.get();
    }

    private static void renderScaledText(MatrixStack matrix, FontRenderer fontRenderer, IReorderingProcessor text, int x, int y, float scale, int color) {
        matrix.pushPose();
        matrix.scale(scale, scale, scale);
        fontRenderer.draw(matrix, text, x / scale, y / scale, color);
        matrix.popPose();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.RESOURCE_GENERATING;
    }

    @Override
    public Class<? extends ResourceGeneratingRecipe> getRecipeClass() {
        return ResourceGeneratingRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ResourceGeneratingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getSingularity()));
        ingredients.setOutputs(VanillaTypes.ITEM, Collections.singletonList(recipe.getResultItem()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ResourceGeneratingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 7, 14);
        itemStacks.init(1, false, 46, 14);

        // Should only be one ingredient...
        itemStacks.set(0, Arrays.asList(recipe.getSingularity().getItems()));
        // Output
        itemStacks.set(1, recipe.getResultItem().copy());
    }

    @Override
    public void draw(ResourceGeneratingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        FontRenderer font = Minecraft.getInstance().font;

        // arrow
        arrow.draw(matrixStack, 48 - GUI_START_X, 43 - GUI_START_Y);

        float factor = recipe.getOutputMultiplier() / recipe.getWorkTimeMultiplier();

        renderScaledText(matrixStack, font, new StringTextComponent(
                "Iron: " + String.format("%.2f",
                        ironItemsPerSecond * factor
                ) + " items/s").getVisualOrderText(), 74, 2, 0.6F, 0x444444);

        renderScaledText(matrixStack, font, new StringTextComponent(
                "Gold: " + String.format("%.2f",
                        goldItemsPerSecond * factor
                ) + " items/s").getVisualOrderText(), 74, 11, 0.6F, 0x444444);

        renderScaledText(matrixStack, font, new StringTextComponent(
                "Diam.: " + String.format("%.2f",
                        diamondItemsPerSecond * factor
                ) + " items/s").getVisualOrderText(), 74, 20, 0.6F, 0x444444);

        renderScaledText(matrixStack, font, new StringTextComponent(
                "Emer.: " + String.format("%.2f",
                        emeraldItemsPerSecond * factor
                ) + " items/s").getVisualOrderText(), 74, 29, 0.6F, 0x444444);

        renderScaledText(matrixStack, font, new StringTextComponent(
                "Neth.: " + String.format("%.2f",
                        netheriteItemsPerSecond * factor
                ) + " items/s").getVisualOrderText(), 74, 38, 0.6F, 0x444444);
    }
}
