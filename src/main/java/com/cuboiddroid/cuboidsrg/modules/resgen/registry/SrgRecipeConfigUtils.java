package com.cuboiddroid.cuboidsrg.modules.resgen.registry;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class SrgRecipeConfigUtils {
    public static SrgRecipeConfig loadFromJson(JsonObject json) {
        String input = JSONUtils.getAsString(json, "input");
        ResourceLocation id = new ResourceLocation(CuboidResourceGenMod.MOD_ID, "resource_generating_" + input.replace(":", "_"));
        boolean enabled = JSONUtils.getAsBoolean(json, "enabled");

        String output = JSONUtils.getAsString(json, "output");

        Float tickMultiplier = JSONUtils.getAsFloat(json, "tickMultiplier");
        Float countMultiplier = JSONUtils.getAsFloat(json, "countMultiplier");

        return new SrgRecipeConfig(id, enabled, input, output, tickMultiplier, countMultiplier);
    }

    public static JsonObject writeToJson(SrgRecipeConfig config) {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", config.getEnabled());
        json.addProperty("input", config.getInput());
        json.addProperty("output", config.getOutput());
        json.addProperty("tickMultiplier", config.getTickMultiplier());
        json.addProperty("countMultiplier", config.getCountMultiplier());

        return json;
    }

    public static SrgRecipeConfig getSrgRecipeConfigForSingularity(ItemStack stack) {
        String id = stack.hasTag() ? stack.getTag().getString("Id") : "";
        if (!id.isEmpty()) {
            return SrgRecipeConfigsRegistry.getInstance()
                    .getSrgRecipeConfigById(new ResourceLocation(CuboidResourceGenMod.MOD_ID, "resource_generating_" + id.replace(":", "_")));
        }

        return null;
    }
}
