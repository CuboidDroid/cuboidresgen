package com.cuboiddroid.cuboidsrg.modules.resgen.inventory;

import com.cuboiddroid.cuboidsrg.setup.ModBlocks;
import com.cuboiddroid.cuboidsrg.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.cuboiddroid.cuboidqs.util.ContainerHelper.isWithinUsableDistance;

public class NetheriteSrgContainer extends SrgContainerBase {

    public NetheriteSrgContainer(int windowId, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.NETHERITE_SRG.get(), windowId, level, pos, playerInventory, player);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return isWithinUsableDistance(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, ModBlocks.NETHERITE_SRG.get());
    }
}