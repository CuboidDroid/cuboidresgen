package com.cuboiddroid.cuboidsrg.modules.resgen.screen;

import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.EmeraldSrgContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmeraldSrgScreen extends SrgScreenBase<EmeraldSrgContainer> {

    public EmeraldSrgScreen(EmeraldSrgContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}