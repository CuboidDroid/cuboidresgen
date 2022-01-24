package com.cuboiddroid.cuboidsrg.setup;

import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    static void register() {
    }

    // singularity resource generators
    public static final RegistryObject<ContainerType<IronSrgContainer>> IRON_SRG =
            Registration.CONTAINERS.register("iron_srg", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new IronSrgContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<GoldSrgContainer>> GOLD_SRG =
            Registration.CONTAINERS.register("gold_srg", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new GoldSrgContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<DiamondSrgContainer>> DIAMOND_SRG =
            Registration.CONTAINERS.register("diamond_srg", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new DiamondSrgContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<EmeraldSrgContainer>> EMERALD_SRG =
            Registration.CONTAINERS.register("emerald_srg", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new EmeraldSrgContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<NetheriteSrgContainer>> NETHERITE_SRG =
            Registration.CONTAINERS.register("netherite_srg", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new NetheriteSrgContainer(windowId, world, pos, inv, inv.player);
            }));
}
