package com.cuboiddroid.cuboidsrg.network.message;

import com.cuboiddroid.cuboidqs.network.NetworkHandler;
import com.cuboiddroid.cuboidqs.network.message.AcknowledgeMessage;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.SrgRecipeConfig;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.SrgRecipeConfigsRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class SyncSrgRecipeConfigsMessage implements IntSupplier {
    private List<SrgRecipeConfig> configs = new ArrayList<>();
    private int loginIndex;

    public SyncSrgRecipeConfigsMessage() { }

    @Override
    public int getAsInt() {
        return this.loginIndex;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public List<SrgRecipeConfig> getSrgRecipeConfigs() {
        return this.configs;
    }

    public static SyncSrgRecipeConfigsMessage read(PacketBuffer buffer) {
        SyncSrgRecipeConfigsMessage message = new SyncSrgRecipeConfigsMessage();

        message.configs = SrgRecipeConfigsRegistry.getInstance().readFromBuffer(buffer);

        return message;
    }

    public static void write(SyncSrgRecipeConfigsMessage message, PacketBuffer buffer) {
        SrgRecipeConfigsRegistry.getInstance().writeToBuffer(buffer);
    }

    public static void onMessage(SyncSrgRecipeConfigsMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            SrgRecipeConfigsRegistry.getInstance().loadSrgRecipeConfigs(message);

            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        });

        context.get().setPacketHandled(true);
    }
}