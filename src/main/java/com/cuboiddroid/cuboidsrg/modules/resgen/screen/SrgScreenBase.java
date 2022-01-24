package com.cuboiddroid.cuboidsrg.modules.resgen.screen;

import com.cuboiddroid.cuboidsrg.CuboidResourceGenMod;
import com.cuboiddroid.cuboidsrg.modules.resgen.inventory.SrgContainerBase;
import com.cuboiddroid.cuboidsrg.modules.resgen.registry.AvailableDimensionsCache;
import com.cuboiddroid.cuboidsrg.modules.resgen.tile.SrgTileEntityBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SrgScreenBase<T extends SrgContainerBase> extends ContainerScreen<T> {

    public static ResourceLocation GUI = CuboidResourceGenMod.getModId("textures/gui/singularity_resource_generator.png");
    PlayerInventory playerInv;
    ITextComponent name;
    SrgTileEntityBase tile;

    public SrgScreenBase(T container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    protected void init() {
        super.init();

        this.tile = this.getTileEntity();
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        String[] words = this.name.getString().split("\\s+");
        String firstLine = words[0] + ((words.length > 1) ? " " + words[1] : "");
        String secondLine = "";
        for (int i = 2; i < words.length; i++)
            secondLine += words[i] + (i < words.length-1 ? " " : "");

        this.minecraft.font.draw(matrix, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752);

        this.minecraft.font.draw(matrix, "?", this.imageWidth - 12, 4, 4210752);

        ITextComponent first = new StringTextComponent(firstLine);
        ITextComponent second = new StringTextComponent(secondLine);

        this.minecraft.font.draw(matrix, first, (this.imageWidth - this.minecraft.font.width(firstLine)) / 2, 6, 4210752);
        this.minecraft.font.draw(matrix, second, (this.imageWidth - this.minecraft.font.width(secondLine)) / 2, 18, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        // render the main container background
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderTooltip(MatrixStack matrix, int mouseX, int mouseY) {
        super.renderTooltip(matrix, mouseX, mouseY);

        // tooltip to show energy stored & capacity
        if (mouseX >= this.leftPos + this.imageWidth - 12 && mouseX <= this.leftPos + this.imageWidth - 4 && mouseY >= this.topPos + 4 && mouseY <= this.topPos + 12) {
            List<ITextComponent> tooltip = new ArrayList<>();

            tooltip.add(new StringTextComponent("Valid dimensions:"));

            List<String> dims = AvailableDimensionsCache.getValidDimensionsForTier(tile.getTier());

            dims.forEach((d) -> {
                tooltip.add(new StringTextComponent(d));
            });

            this.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
        }
    }

    private SrgTileEntityBase getTileEntity() {
        ClientWorld world = this.getMinecraft().level;

        if (world != null) {
            TileEntity tile = world.getBlockEntity(this.getMenu().getPos());

            if (tile instanceof SrgTileEntityBase) {
                return (SrgTileEntityBase) tile;
            }
        }

        return null;
    }

}