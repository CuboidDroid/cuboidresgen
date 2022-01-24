package com.cuboiddroid.cuboidsrg.modules.resgen.registry;

import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.network.message.SyncSrgRecipeConfigsMessage;
import com.cuboiddroid.cuboidsrg.util.ResourceGenPaths;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class SrgRecipeConfigsRegistry {
    private static final SrgRecipeConfigsRegistry INSTANCE = new SrgRecipeConfigsRegistry();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final Map<ResourceLocation, SrgRecipeConfig> configs = new LinkedHashMap<>();

    public void registerSrgRecipeConfig(SrgRecipeConfig config)
    {
        configs.put(config.getId(), config);
    }

    public List<SrgRecipeConfig> getSrgRecipeConfigs() {
        return Lists.newArrayList(this.configs.values());
    }

    public SrgRecipeConfig getSrgRecipeConfigById(ResourceLocation id) {
        SrgRecipeConfig qs = this.configs.get(id);
        if (qs != null)
            return qs;

        // not found - check defaults
        for (SrgRecipeConfig q: defaults()) {
            if (q.getId().toString().equalsIgnoreCase(id.toString())) {
                // found in defaults, use it, and add to in-memory & warn
                CuboidResourceGenMod.LOGGER.warn("JSON config file missing for recipe: " + id.getPath());
                this.configs.put(q.getId(), q);
                return q;
            }
        }

        return null;
    }

    public static SrgRecipeConfigsRegistry getInstance() {
        return INSTANCE;
    }

    public void loadSrgRecipeConfigs() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File dir = ResourceGenPaths.GENERATORS_CONFIG.toFile();

        if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("Loading resource generator configs");

        if (!dir.exists() && dir.mkdirs()) {
            if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("Creating default resource generator configs");
            for (SrgRecipeConfig config : defaults()) {
                JsonObject json = SrgRecipeConfigUtils.writeToJson(config);
                FileWriter writer = null;

                try {
                    String fileName = config.getInput().replace(":", "_") + ".json";
                    if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("SRG config: " + fileName);

                    File file = new File(dir, fileName);
                    writer = new FileWriter(file);
                    GSON.toJson(json, writer);
                    writer.close();
                } catch (Exception e) {
                    CuboidResourceGenMod.LOGGER.error("An error occurred while generating default resource generator configs", e);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        }

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir);
        }

        stopwatch.stop();
        CuboidResourceGenMod.LOGGER.info("Loaded {} Resource Generator Config(s) in {} ms", this.configs.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void loadFiles(File dir) {
        File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        for (File file : files) {
            JsonObject json;
            FileReader reader = null;
            SrgRecipeConfig config = null;

            try {
                JsonParser parser = new JsonParser();
                reader = new FileReader(file);
                json = parser.parse(reader).getAsJsonObject();
                String name = file.getName().replace(".json", "");
                if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("Loading Resource Generator Config file: " + name);
                config = SrgRecipeConfigUtils.loadFromJson(json);

                reader.close();
            } catch (Exception e) {
                CuboidResourceGenMod.LOGGER.error("An error occurred while loading Resource Generator Configs", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (config != null && config.getEnabled()) {
                registerSrgRecipeConfig(config);
            }
        }
    }

    public void writeToBuffer(PacketBuffer buffer) {
        buffer.writeVarInt(this.configs.size());

        this.configs.forEach((id, config) -> {
            config.write(buffer);
        });
    }

    public List<SrgRecipeConfig> readFromBuffer(PacketBuffer buffer) {
        List<SrgRecipeConfig> configs = new ArrayList<>();

        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            SrgRecipeConfig config = SrgRecipeConfig.read(buffer);

            configs.add(config);
        }

        return configs;
    }

    public void loadSrgRecipeConfigs(SyncSrgRecipeConfigsMessage message) {
        Map<ResourceLocation, SrgRecipeConfig> configs = message.getSrgRecipeConfigs()
                .stream()
                .collect(Collectors.toMap(SrgRecipeConfig::getId, s -> s));

        this.configs.clear();
        this.configs.putAll(configs);

        CuboidResourceGenMod.LOGGER.info("Loaded {} Resource Generator Configs from the server", configs.size());
    }

    private static List<SrgRecipeConfig> defaults() {
        List<QuantumSingularity> singularities = QuantumSingularityRegistry.getInstance().getSingularities();

        if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("--> Singularities found: " + singularities.stream().count());

        List<SrgRecipeConfig> defaultRecipes = new LinkedList<>();
        singularities.forEach((quantumSingularity -> {
            if (Config.verboseLogging.get()) CuboidResourceGenMod.LOGGER.info("----> Singularity: " + quantumSingularity.getName());
            defaultRecipes.add(new SrgRecipeConfig(
                    new ResourceLocation(CuboidResourceGenMod.MOD_ID, "resource_generating_" + quantumSingularity.getId().toString().replace(":", "_")),
                    quantumSingularity.getEnabled(),
                    quantumSingularity.getId().toString(),
                    quantumSingularity.getInput(),
                    1.0F,
                    1.0F
            ));
        }));

        return defaultRecipes;
    }
}