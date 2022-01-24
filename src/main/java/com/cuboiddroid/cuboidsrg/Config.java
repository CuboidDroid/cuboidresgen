package com.cuboiddroid.cuboidsrg;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final String CATEGORY_SRGs = "generators";

    public static ForgeConfigSpec.IntValue ironTicksPerOperation;
    public static ForgeConfigSpec.IntValue ironItemsPerOperation;
    public static ForgeConfigSpec.IntValue ironInternalBuffer;
    public static ForgeConfigSpec.BooleanValue ironAutoEject;
    public static ForgeConfigSpec.ConfigValue<? extends String> ironDimensions;
    public static ForgeConfigSpec.BooleanValue ironIsDimensionsBlacklist;

    public static ForgeConfigSpec.IntValue goldTicksPerOperation;
    public static ForgeConfigSpec.IntValue goldItemsPerOperation;
    public static ForgeConfigSpec.IntValue goldInternalBuffer;
    public static ForgeConfigSpec.BooleanValue goldAutoEject;
    public static ForgeConfigSpec.ConfigValue<? extends String> goldDimensions;
    public static ForgeConfigSpec.BooleanValue goldIsDimensionsBlacklist;

    public static ForgeConfigSpec.IntValue diamondTicksPerOperation;
    public static ForgeConfigSpec.IntValue diamondItemsPerOperation;
    public static ForgeConfigSpec.IntValue diamondInternalBuffer;
    public static ForgeConfigSpec.BooleanValue diamondAutoEject;
    public static ForgeConfigSpec.ConfigValue<? extends String> diamondDimensions;
    public static ForgeConfigSpec.BooleanValue diamondIsDimensionsBlacklist;

    public static ForgeConfigSpec.IntValue emeraldTicksPerOperation;
    public static ForgeConfigSpec.IntValue emeraldItemsPerOperation;
    public static ForgeConfigSpec.IntValue emeraldInternalBuffer;
    public static ForgeConfigSpec.BooleanValue emeraldAutoEject;
    public static ForgeConfigSpec.ConfigValue<? extends String> emeraldDimensions;
    public static ForgeConfigSpec.BooleanValue emeraldIsDimensionsBlacklist;

    public static ForgeConfigSpec.IntValue netheriteTicksPerOperation;
    public static ForgeConfigSpec.IntValue netheriteItemsPerOperation;
    public static ForgeConfigSpec.IntValue netheriteInternalBuffer;
    public static ForgeConfigSpec.BooleanValue netheriteAutoEject;
    public static ForgeConfigSpec.ConfigValue<? extends String> netheriteDimensions;
    public static ForgeConfigSpec.BooleanValue netheriteIsDimensionsBlacklist;

    // --- JEI CATEGORY ---
    public static final String CATEGORY_JEI = "jei";
    public static ForgeConfigSpec.BooleanValue enableJeiPlugin;
    public static ForgeConfigSpec.BooleanValue enableJeiCatalysts;
    public static ForgeConfigSpec.BooleanValue enableJeiClickArea;

    // --- MISC CATEGORY ---
    public static final String CATEGORY_MISC = "misc";
    public static ForgeConfigSpec.BooleanValue verboseLogging;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Resource Generator Settings").push(CATEGORY_SRGs);
        setupGeneratorsConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("JEI Settings").push(CATEGORY_JEI);
        setupJEIConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Misc").push(CATEGORY_MISC);
        verboseLogging = COMMON_BUILDER
                .comment(" Logs additional information when loading Quantum Singularity config files.")
                .define("verbose_logging", false);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupGeneratorsConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        // -- IRON SRG --

        ironTicksPerOperation = COMMON_BUILDER
                .comment(" The default number of ticks per operation for the iron tier when generating recipes.\n" +
                        " Default: 200 (10 seconds)")
                .defineInRange("srg.iron.ticks", 200, 1, 72000);

        ironItemsPerOperation = COMMON_BUILDER
                .comment(" The default number of items per operation for the iron tier when generating recipes.\n" +
                        " Default: 1")
                .defineInRange("srg.iron.items", 1, 1, 64);

        ironInternalBuffer = COMMON_BUILDER
                .comment(" The internal buffer size for the iron tier.\n" +
                        " Default: 0")
                .defineInRange("srg.iron.buffer", 0, 0, 8192);

        ironAutoEject = COMMON_BUILDER
                .comment(" Indicates whether the SRG should try push items 'upward' into an inventory above.\n" +
                        " Default: true")
                .define("srg.iron.auto_eject", true);

        ironDimensions = COMMON_BUILDER
                .comment(" A semi-colon separated list of dimensions for this SRG tier.\n" +
                        " If blank, then this list is completely ignored, regardless of the 'is_blacklist' setting.\n" +
                        " Default: ''\n" +
                        "\n" +
                        " An example for how to ONLY allow this SRG tier to work in the End would be to:\n" +
                        " - set this setting to 'minecraft:the_end'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to false.\n" +
                        "\n" +
                        " An example for how to disable this SRG tier in the Nether, and the End, but allow it to be used\n" +
                        " in the overworld and any other modded dimensions would be to blacklist those as follows:\n" +
                        " - set this setting to 'minecraft:the_end;minecraft:the_nether'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to true.")
                .define("srg.iron.dimensions", "");

        ironIsDimensionsBlacklist = COMMON_BUILDER
                .comment(" Indicates whether the list of dimensions for this SRG is a blacklist instead of a whitelist.\n" +
                        " Default: false")
                .define("srg.iron.is_blacklist", false);

        // -- GOLD SRG --

        goldTicksPerOperation = COMMON_BUILDER
                .comment(" The default number of ticks per operation for the gold tier when generating recipes.\n" +
                        " Default: 100 (5 seconds)")
                .defineInRange("srg.gold.ticks", 100, 1, 72000);

        goldItemsPerOperation = COMMON_BUILDER
                .comment(" The default number of items per operation for the gold tier when generating recipes.\n" +
                        " Default: 2")
                .defineInRange("srg.gold.items", 2, 1, 64);

        goldInternalBuffer = COMMON_BUILDER
                .comment(" The internal buffer size for the gold tier.\n" +
                        " Default: 320 (5 stacks)")
                .defineInRange("srg.gold.buffer", 320, 0, 8192);

        goldAutoEject = COMMON_BUILDER
                .comment(" Indicates whether the SRG should try push items 'upward' into an inventory above.\n" +
                        " Default: true")
                .define("srg.gold.auto_eject", true);

        goldDimensions = COMMON_BUILDER
                .comment(" A semi-colon separated list of dimensions for this SRG tier.\n" +
                        " If blank, then this list is completely ignored, regardless of the 'is_blacklist' setting.\n" +
                        " Default: ''\n" +
                        "\n" +
                        " An example for how to ONLY allow this SRG tier to work in the End would be to:\n" +
                        " - set this setting to 'minecraft:the_end'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to false.\n" +
                        "\n" +
                        " An example for how to disable this SRG tier in the Nether, and the End, but allow it to be used\n" +
                        " in the overworld and any other modded dimensions would be to blacklist those as follows:\n" +
                        " - set this setting to 'minecraft:the_end;minecraft:the_nether'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to true.")
                .define("srg.gold.dimensions", "");

        goldIsDimensionsBlacklist = COMMON_BUILDER
                .comment(" Indicates whether the list of dimensions for this SRG is a blacklist instead of a whitelist.\n" +
                        " Default: false")
                .define("srg.gold.is_blacklist", false);

        // -- DIAMOND SRG --

        diamondTicksPerOperation = COMMON_BUILDER
                .comment(" The default number of ticks per operation for the diamond tier when generating recipes.\n" +
                        " Default: 50 (2.5 seconds)")
                .defineInRange("srg.diamond.ticks", 50, 1, 72000);

        diamondItemsPerOperation = COMMON_BUILDER
                .comment(" The default number of items per operation for the diamond tier when generating recipes.\n" +
                        " Default: 4")
                .defineInRange("srg.diamond.items", 4, 1, 64);

        diamondInternalBuffer = COMMON_BUILDER
                .comment(" The internal buffer size for the diamond tier.\n" +
                        " Default: 1024 (16 stacks)")
                .defineInRange("srg.diamond.buffer", 1024, 0, 8192);

        diamondAutoEject = COMMON_BUILDER
                .comment(" Indicates whether the SRG should try push items 'upward' into an inventory above.\n" +
                        " Default: true")
                .define("srg.diamond.auto_eject", true);

        diamondDimensions = COMMON_BUILDER
                .comment(" A semi-colon separated list of dimensions for this SRG tier.\n" +
                        " If blank, then this list is completely ignored, regardless of the 'is_blacklist' setting.\n" +
                        " Default: ''\n" +
                        "\n" +
                        " An example for how to ONLY allow this SRG tier to work in the End would be to:\n" +
                        " - set this setting to 'minecraft:the_end'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to false.\n" +
                        "\n" +
                        " An example for how to disable this SRG tier in the Nether, and the End, but allow it to be used\n" +
                        " in the overworld and any other modded dimensions would be to blacklist those as follows:\n" +
                        " - set this setting to 'minecraft:the_end;minecraft:the_nether'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to true.")
                .define("srg.diamond.dimensions", "");

        diamondIsDimensionsBlacklist = COMMON_BUILDER
                .comment(" Indicates whether the list of dimensions for this SRG is a blacklist instead of a whitelist.\n" +
                        " Default: false")
                .define("srg.diamond.is_blacklist", false);

        // -- EMERALD SRG --

        emeraldTicksPerOperation = COMMON_BUILDER
                .comment(" The default number of ticks per operation for the emerald tier when generating recipes.\n" +
                        " Default: 20 (1 second)")
                .defineInRange("srg.emerald.ticks", 20, 1, 72000);

        emeraldItemsPerOperation = COMMON_BUILDER
                .comment(" The default number of items per operation for the emerald tier when generating recipes.\n" +
                        " Default: 8")
                .defineInRange("srg.emerald.items", 8, 1, 64);

        emeraldInternalBuffer = COMMON_BUILDER
                .comment(" The internal buffer size for the emerald tier.\n" +
                        " Default: 2048 (32 stacks)")
                .defineInRange("srg.emerald.buffer", 2048, 0, 8192);

        emeraldAutoEject = COMMON_BUILDER
                .comment(" Indicates whether the SRG should try push items 'upward' into an inventory above.\n" +
                        " Default: true")
                .define("srg.emerald.auto_eject", true);

        emeraldDimensions = COMMON_BUILDER
                .comment(" A semi-colon separated list of dimensions for this SRG tier.\n" +
                        " If blank, then this list is completely ignored, regardless of the 'is_blacklist' setting.\n" +
                        " Default: ''\n" +
                        "\n" +
                        " An example for how to ONLY allow this SRG tier to work in the End would be to:\n" +
                        " - set this setting to 'minecraft:the_end'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to false.\n" +
                        "\n" +
                        " An example for how to disable this SRG tier in the Nether, and the End, but allow it to be used\n" +
                        " in the overworld and any other modded dimensions would be to blacklist those as follows:\n" +
                        " - set this setting to 'minecraft:the_end;minecraft:the_nether'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to true.")
                .define("srg.emerald.dimensions", "");

        emeraldIsDimensionsBlacklist = COMMON_BUILDER
                .comment(" Indicates whether the list of dimensions for this SRG is a blacklist instead of a whitelist.\n" +
                        " Default: false")
                .define("srg.emerald.is_blacklist", false);

        // -- NETHERITE SRG --

        netheriteTicksPerOperation = COMMON_BUILDER
                .comment(" The default number of ticks per operation for the netherite tier when generating recipes.\n" +
                        " Default: 10 (0.5 seconds)")
                .defineInRange("srg.netherite.ticks", 10, 1, 72000);

        netheriteItemsPerOperation = COMMON_BUILDER
                .comment(" The default number of items per operation for the netherite tier when generating recipes.\n" +
                        " Default: 16")
                .defineInRange("srg.netherite.items", 16, 1, 64);

        netheriteInternalBuffer = COMMON_BUILDER
                .comment(" The internal buffer size for the netherite tier.\n" +
                        " Default: 4096 (64 stacks)")
                .defineInRange("srg.netherite.buffer", 4096, 0, 8192);

        netheriteAutoEject = COMMON_BUILDER
                .comment(" Indicates whether the SRG should try push items 'upward' into an inventory above.\n" +
                        " Default: true")
                .define("srg.netherite.auto_eject", true);

        netheriteDimensions = COMMON_BUILDER
                .comment(" A semi-colon separated list of dimensions for this SRG tier.\n" +
                        " If blank, then this list is completely ignored, regardless of the 'is_blacklist' setting.\n" +
                        " Default: ''\n" +
                        "\n" +
                        " An example for how to ONLY allow this SRG tier to work in the End would be to:\n" +
                        " - set this setting to 'minecraft:the_end'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to false.\n" +
                        "\n" +
                        " An example for how to disable this SRG tier in the Nether, and the End, but allow it to be used\n" +
                        " in the overworld and any other modded dimensions would be to blacklist those as follows:\n" +
                        " - set this setting to 'minecraft:the_end;minecraft:the_nether'\n" +
                        " - and ensure that the is_blacklist setting for this tier is set to true.")
                .define("srg.netherite.dimensions", "");

        netheriteIsDimensionsBlacklist = COMMON_BUILDER
                .comment(" Indicates whether the list of dimensions for this SRG is a blacklist instead of a whitelist.\n" +
                        " Default: false")
                .define("srg.netherite.is_blacklist", false);

    }

    private static void setupJEIConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        enableJeiPlugin = COMMON_BUILDER
                .comment(" Enable or disable the JeiPlugin of Cuboid machines.").define("enable_jei", true);

        enableJeiCatalysts = COMMON_BUILDER
                .comment(" Enable or disable the Catalysts in Jei for Cuboid machines.").define("enable_jei_catalysts", true);

        enableJeiClickArea = COMMON_BUILDER
                .comment(" Enable or disable the Click Area inside the GUI in all Cuboid machines.").define("enable_jei_click_area", true);
    }


    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        CuboidResourceGenMod.LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        CuboidResourceGenMod.LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        CuboidResourceGenMod.LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }
}
