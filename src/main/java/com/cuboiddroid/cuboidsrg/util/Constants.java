package com.cuboiddroid.cuboidsrg.util;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import net.minecraft.util.ResourceLocation;

public class Constants {
    // recipes
    public static final ResourceLocation RESOURCE_GENERATING = CuboidResourceGenMod.getModId("resource_generating");

    // ----------------------------------------------------------------------------
    // utility methods

    private Constants() {
        throw new IllegalAccessError("Utility class only.");
    }
}
