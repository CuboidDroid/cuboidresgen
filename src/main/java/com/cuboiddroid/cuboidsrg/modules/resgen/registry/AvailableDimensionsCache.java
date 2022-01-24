package com.cuboiddroid.cuboidsrg.modules.resgen.registry;

import com.cuboiddroid.cuboidsrg.Config;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class AvailableDimensionsCache {
    // a map of dimensions to a list of SRG tiers that should be enabled
    private static final Map<ResourceLocation, List<String>> _map = new LinkedHashMap<>();

    public static boolean isTierEnabledInDimension(MinecraftServer server, String tier, ResourceLocation dimensionId)
    {
        if (_map.isEmpty())
            initialise(server);

        if (!_map.containsKey(dimensionId))
            return true;

        List<String> list = _map.getOrDefault(dimensionId, null);
        return (list != null && list.stream().anyMatch((t) -> t.equalsIgnoreCase(tier)));
    }

    public static List<String> getValidDimensionsForTier(String tier)
    {
        List<String> result = new LinkedList<>();

        _map.forEach((d, l) -> {
            if (l.stream().anyMatch((t) -> t.equalsIgnoreCase(tier)))
                result.add(d.toString());
        });

        return result;
    }

    public static void initialise(MinecraftServer server)
    {
        if (server == null || !_map.isEmpty())
            return;

        boolean ironIsBlacklist = Config.ironIsDimensionsBlacklist.get();
        String dimsString = Config.ironDimensions.get();
        String[] dims = dimsString.split(";");
        String[] ironDims = Arrays.stream(dims).map(String::trim).toArray(String[]::new);

        boolean goldIsBlacklist = Config.goldIsDimensionsBlacklist.get();
        dimsString = Config.goldDimensions.get();
        dims = dimsString.split(";");
        String[] goldDims = Arrays.stream(dims).map(String::trim).toArray(String[]::new);

        boolean diamondIsBlacklist = Config.diamondIsDimensionsBlacklist.get();
        dims = Config.diamondDimensions.get().split(";");
        String[] diamondDims = Arrays.stream(dims).map(String::trim).toArray(String[]::new);

        boolean emeraldIsBlacklist = Config.emeraldIsDimensionsBlacklist.get();
        dims = Config.emeraldDimensions.get().split(";");
        String[] emeraldDims = Arrays.stream(dims).map(String::trim).toArray(String[]::new);

        boolean netheriteIsBlacklist = Config.netheriteIsDimensionsBlacklist.get();
        dims = Config.netheriteDimensions.get().split(";");
        String[] netheriteDims = Arrays.stream(dims).map(String::trim).toArray(String[]::new);

        server.getAllLevels().forEach(serverWorld -> {
            ResourceLocation dimId = serverWorld.dimension().location();

            addIfWhitelistedDimensionsForTier(dimId, "iron", ironDims, ironIsBlacklist);
            addIfWhitelistedDimensionsForTier(dimId, "gold", goldDims, goldIsBlacklist);
            addIfWhitelistedDimensionsForTier(dimId, "diamond", diamondDims, diamondIsBlacklist);
            addIfWhitelistedDimensionsForTier(dimId, "emerald", emeraldDims, emeraldIsBlacklist);
            addIfWhitelistedDimensionsForTier(dimId, "netherite", netheriteDims, netheriteIsBlacklist);
        });
    }

    private static void addIfWhitelistedDimensionsForTier(
            ResourceLocation dimId,
            String tierId,
            String[] dimensionList,
            boolean isBlacklist)
    {
        if (dimensionList.length == 0 || (dimensionList.length == 1 && dimensionList[0].equalsIgnoreCase(""))) {
            // if dimension list is empty, then everything is whitelisted for this tier (ignore blacklist)
            addDimensionTier(dimId, tierId);
            return;
        }

        String dim = dimId.toString();
        boolean inList = Arrays.stream(dimensionList).anyMatch((d) -> d.equalsIgnoreCase(dim));
        if ((isBlacklist && !inList) || (!isBlacklist && inList))
            addDimensionTier(dimId, tierId);
    }

    private static void addDimensionTier(ResourceLocation dimId, String tier)
    {
        List<String> list = _map.getOrDefault(dimId, new LinkedList<>());
        list.add(tier);
        _map.put(dimId, list);
    }
}
