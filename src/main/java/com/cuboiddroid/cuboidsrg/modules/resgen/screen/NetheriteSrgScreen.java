package com.cuboiddroid.cuboidsrg.modules.resgen.screen;

import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.NetheriteSrgContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetheriteSrgScreen extends SrgScreenBase<NetheriteSrgContainer> {

    public NetheriteSrgScreen(NetheriteSrgContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}