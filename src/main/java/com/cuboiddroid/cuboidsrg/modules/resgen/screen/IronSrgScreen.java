package com.cuboiddroid.cuboidsrg.modules.resgen.screen;

import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.IronSrgContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IronSrgScreen
        extends SrgScreenBase<IronSrgContainer> {

    public IronSrgScreen(IronSrgContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}