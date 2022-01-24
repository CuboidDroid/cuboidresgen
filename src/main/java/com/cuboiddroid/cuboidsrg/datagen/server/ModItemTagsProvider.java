package com.cuboiddroid.cuboidsrg.datagen.server;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.setup.ModItems;
import com.cuboiddroid.cuboidsrg.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagsProvider, CuboidResourceGenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
