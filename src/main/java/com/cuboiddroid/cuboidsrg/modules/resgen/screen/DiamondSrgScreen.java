package com.cuboiddroid.cuboidsrg.modules.resgen.screen;

import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.DiamondSrgContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DiamondSrgScreen extends SrgScreenBase<DiamondSrgContainer> {

    public DiamondSrgScreen(DiamondSrgContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}