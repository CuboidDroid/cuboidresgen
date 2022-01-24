package com.cuboiddroid.cuboidsrg;

import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import com.cuboiddroid.cuboidsrg.setup.Registration;
import com.cuboiddroid.cuboidsrg.util.ResourceGenPaths;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CuboidResourceGenMod.MOD_ID)
public class CuboidResourceGenMod
{
    public static final String MOD_ID = "cuboidsrg";

    public static final ItemGroup CUBOIDSRG_ITEM_GROUP = (new ItemGroup("cuboidsrg") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.IRON_SRG.get());
        }
    });

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public CuboidResourceGenMod() {
        ResourceGenPaths.createDirectories();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG, ResourceGenPaths.MOD_CONFIG.resolve("cuboidsrg.toml").toString());

        Registration.register();

        Config.loadConfig(Config.COMMON_CONFIG, ResourceGenPaths.MOD_CONFIG.resolve("cuboidsrg.toml"));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation getModId(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }
}
