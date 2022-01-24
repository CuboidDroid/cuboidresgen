package com.cuboiddroid.cuboidsrg.modules.resgen.registry;

import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class SrgRecipeConfig {

    private final ResourceLocation id;
    private final Boolean enabled;
    private final String input;
    private String output;
    private final Float tickMultiplier;
    private final Float countMultiplier;

    private Ingredient ingredient;
    private ItemStack outputStack;

    public SrgRecipeConfig(
            ResourceLocation id,
            Boolean enabled,
            String input,
            String output,
            Float tickMultiplier,
            Float countMultiplier) {
        this.id = id;
        this.enabled = enabled;
        this.input = input;
        this.output = output;
        this.tickMultiplier = tickMultiplier;
        this.countMultiplier = countMultiplier;

        this.ingredient = Ingredient.EMPTY;
        this.outputStack = ItemStack.EMPTY;
    }

    public ResourceLocation getId() { return id; }

    public Boolean getEnabled() { return enabled; }

    public String getInput() { return input; }

    public Ingredient getInputAsIngredient() {
        if (this.ingredient != Ingredient.EMPTY)
            return this.ingredient;

        ResourceLocation itemId = new ResourceLocation(this.input);
        QuantumSingularity qs = QuantumSingularityRegistry.getInstance().getSingularityById(itemId);
        ItemStack qsStack = QuantumSingularityUtils.getItemForSingularity(qs);
        this.ingredient = Ingredient.of(qsStack);

        return this.ingredient;
    }

    public String getOutput() { return output; }

    public ItemStack getOutputStack() {
        if (this.outputStack != ItemStack.EMPTY)
            return this.outputStack.copy();

        // the singularity input could be a tag, which is not a suitable output, so we might need
        // to pick one of the possibilities
        if (!this.output.startsWith("#")) {
            ResourceLocation itemId = new ResourceLocation(this.output);
            this.outputStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), 1);
            return this.outputStack.copy();
        }

        // starts with # so is a tag - let's use the first item in the tag
        ResourceLocation tagLocation = new ResourceLocation(this.output.substring(1));
        ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagLocation);
        if (tag != null) {
            Ingredient ingredient = Ingredient.of(tag);
            if (ingredient != Ingredient.EMPTY) {
                this.output = this.outputStack.getItem().toString();
                this.outputStack = ingredient.getItems()[0];

                return this.outputStack.copy();
            }
        }

        return null;
    }

    public Float getTickMultiplier() { return tickMultiplier; }

    public Float getCountMultiplier() { return countMultiplier; }

    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeBoolean(this.enabled);
        buffer.writeUtf(this.input);
        buffer.writeUtf(this.output);
        buffer.writeFloat(this.tickMultiplier);
        buffer.writeFloat(this.countMultiplier);
    }

    public static SrgRecipeConfig read(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        Boolean enabled = buffer.readBoolean();
        String input = buffer.readUtf();
        String output = buffer.readUtf();
        Float tickMultiplier = buffer.readFloat();
        Float countMultiplier = buffer.readFloat();

        return new SrgRecipeConfig(id, enabled, input, output, tickMultiplier, countMultiplier);
    }
}
