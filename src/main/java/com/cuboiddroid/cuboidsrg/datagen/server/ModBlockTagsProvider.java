package com.cuboiddroid.cuboidsrg.datagen.server;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, CuboidResourceGenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
