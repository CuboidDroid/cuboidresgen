package com.cuboiddroid.cuboidsrg.modules.resgen.events;

import com.cuboiddroid.cuboidsrg.Config;
import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.AvailableDimensionsCache;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CuboidResourceGenMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        ServerPlayerEntity serverPlayer	= (ServerPlayerEntity) player;
        MinecraftServer server = serverPlayer.getServer();
        if (server != null) {
            AvailableDimensionsCache.initialise(server);
        }
    }
}