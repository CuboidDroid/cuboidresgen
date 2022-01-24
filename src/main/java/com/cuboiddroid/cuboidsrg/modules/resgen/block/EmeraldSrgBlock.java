package com.cuboiddroid.cuboidsrg.modules.resgen.block;

import com.cuboiddroid.cuboidsrg.modules.resgen.tile.EmeraldSrgTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class EmeraldSrgBlock extends SrgBlockBase {

    public EmeraldSrgBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EmeraldSrgTileEntity();
    }
}