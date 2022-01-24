package com.cuboiddroid.cuboidsrg.datagen.client;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CuboidResourceGenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerBlockItemModels();
    }

    private void registerBlockItemModels() {
        withExistingParent("iron_srg", modLoc("block/iron_srg"));
        withExistingParent("gold_srg", modLoc("block/gold_srg"));
        withExistingParent("diamond_srg", modLoc("block/diamond_srg"));
        withExistingParent("emerald_srg", modLoc("block/emerald_srg"));
        withExistingParent("netherite_srg", modLoc("block/netherite_srg"));
    }
}
