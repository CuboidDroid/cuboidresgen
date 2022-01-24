package com.cuboiddroid.cuboidsrg.util;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceGenPaths {
    // paths
    public static final Path MOD_CONFIG = FMLPaths.CONFIGDIR.get().resolve("cuboiddroid/cuboidsrg");
    public static final Path GENERATORS_CONFIG = FMLPaths.CONFIGDIR.get().resolve("cuboiddroid/cuboidsrg/generators");

    public static void createDirectories() {
        try {
            Files.createDirectories(MOD_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create default directories.", e);
        }
    }

    public static void generateDefaultFiles(MinecraftServer server) {
        try {
            createDirectories();
        } catch (Exception e)/*(IOException e)*/ {
            e.printStackTrace();
        }
    }
}
