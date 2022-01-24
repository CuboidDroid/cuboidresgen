package com.cuboiddroid.cuboidsrg.modules.resgen.block;

import com.cuboiddroid.cuboidsrg.modules.resgen.tile.DiamondSrgTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class DiamondSrgBlock extends SrgBlockBase {

    public DiamondSrgBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DiamondSrgTileEntity();
    }
}