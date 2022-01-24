package com.cuboiddroid.cuboidsrg.modules.resgen.recipe;

import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
import com.cuboiddroid.cuboidsrg.modules.resgen.tile.SrgTileEntityBase;
import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import com.cuboiddroid.cuboidsrg.setup.ModRecipeSerializers;
import com.cuboiddroid.cuboidsrg.setup.ModRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ResourceGeneratingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private Ingredient singularity;
    private ItemStack result;
    private float workTimeMultiplier;
    private float outputMultiplier;

    public ResourceGeneratingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        // CuboidResourceGenMod.LOGGER.info("---> Loaded " + this.toString());
    }

    public ResourceGeneratingRecipe(
            ResourceLocation recipeId,
            Ingredient singularity,
            ItemStack result,
            float workTimeMultiplier,
            float outputMultiplier)
    {
        this.recipeId = recipeId;
        this.singularity = singularity;
        this.result = result;
        this.outputMultiplier = outputMultiplier;
        this.workTimeMultiplier = workTimeMultiplier;
    }

    /*
    @Override
    public String toString () {

        // Overriding toString is not required, it's just useful for debugging.
        return "ResourceGeneratingRecipe [ingredient=" + this.ingredient + ", addition=" + this.addition +
                ", result=" + this.result + ", id=" + this.recipeId + "]";
    }
    */

    /**
     * Get the input ingredient (singularity)
     *
     * @return The input ingredient (singularity)
     */
    public Ingredient getSingularity() {
        return this.singularity;
    }

    /**
     * Get the Iron SRG image as the toast symbol
     *
     * @return
     */
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.IRON_SRG.get());
    }

    /**
     * Get the recipe's resource location (recipe ID)
     *
     * @return the recipe ID as a ResourceLocation
     */
    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    /**
     * Get the recipe serializer
     *
     * @return the IRecipeSerializer for the ResourceGeneratingRecipe
     */
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.RESOURCE_GENERATING.get();
    }

    /**
     * Get the recipe type for the ResourceGeneratingRecipe
     *
     * @return The IRecipeType for this recipe
     */
    public IRecipeType<?> getType() {
        return ModRecipeTypes.RESOURCE_GENERATING;
    }

    /**
     * Checks if there is a recipe match for the ingredient in the tile entities input slot
     *
     * @param inv   The SRG entity
     * @param level The level / world
     * @return true if there is a match, otherwise false
     */
    @Override
    public boolean matches(IInventory inv, World level) {
        Boolean isMatch = this.singularity.test(inv.getItem(SrgTileEntityBase.SINGULARITY_INPUT));

        if (!isMatch)
            return false;

        QuantumSingularity recipeSingularity = QuantumSingularityUtils.getSingularity(this.singularity.getItems()[0]);

        QuantumSingularity inputSingularity = QuantumSingularityUtils.getSingularity(inv.getItem(SrgTileEntityBase.SINGULARITY_INPUT));

        return recipeSingularity.getId() == inputSingularity.getId();
    }

    /**
     * DO NOT use this method - use the "getResults" method instead for this custom recipe.
     *
     * @param inventory
     * @return
     */
    @Override
    public ItemStack assemble(IInventory inventory) {
        return this.getResultItem();
    }

    /**
     * Checks if the recipe can fit in the machine. As this recipe is for single input, we'll just say yes!
     *
     * @param gridWidth
     * @param gridHeight
     * @return always returns true
     */
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return true;
    }

    /**
     * DO NOT use this method - use the "getResults" method instead for this custom recipe.
     *
     * @return
     */
    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    /**
     * Indicates that this recipe has special processing to Forge/Minecraft
     *
     * @return
     */
    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * Gets the work time multiplier. e.g. if 2, then this SRG recipe takes twice
     * as many ticks per operation.
     *
     * @return the work time multiplier for this recipe
     */
    public float getWorkTimeMultiplier() {
        return this.workTimeMultiplier;
    }

    /**
     * Gets the output multiplier. e.g. if 0.5, then this SRG recipe produces half
     * the usual amount of items per operation, or if 1.5 it produces 50% more than usual.
     *
     * @return the output multiplier for this recipe
     */
    public float getOutputMultiplier() {
        return this.outputMultiplier;
    }

    // ---- Serializer ----

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ResourceGeneratingRecipe> {

        @Override
        public ResourceGeneratingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ResourceGeneratingRecipe recipe = new ResourceGeneratingRecipe(recipeId);

            JsonObject singularityJson = JSONUtils.getAsJsonObject(json, "singularity");
            ResourceLocation singularityItemId = new ResourceLocation(JSONUtils.getAsString(singularityJson, "item"));

            recipe.singularity = Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(singularityItemId), 1));

            JsonObject resultJson = JSONUtils.getAsJsonObject(json, "result");
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(resultJson, "item"));

            recipe.workTimeMultiplier = JSONUtils.getAsFloat(json, "work_time_multiplier", 1.0F);
            recipe.outputMultiplier = JSONUtils.getAsFloat(json, "output_multiplier", 1.0F);

            recipe.result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), 1);
            return recipe;
        }

        @Override
        public ResourceGeneratingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            ResourceGeneratingRecipe recipe = new ResourceGeneratingRecipe(recipeId);
            recipe.singularity = Ingredient.fromNetwork(buffer);
            recipe.result = buffer.readItem();
            recipe.workTimeMultiplier = buffer.readFloat();
            recipe.outputMultiplier = buffer.readFloat();

            return recipe;
        }

        public void toNetwork(PacketBuffer buffer, ResourceGeneratingRecipe recipe) {
            recipe.singularity.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.workTimeMultiplier);
            buffer.writeFloat(recipe.outputMultiplier);
        }
    }
}
