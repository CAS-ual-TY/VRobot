package de.cas_ual_ty.vrobot.client;

import com.mojang.blaze3d.systems.RenderSystem;

import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.RobotInventoryContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RobotInventoryContainerGui extends ContainerScreen<RobotInventoryContainer>
{
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation WIDGETS_GUI_TEXTURE = new ResourceLocation("textures/gui/widgets.png");
    
    public RobotInventoryContainerGui(RobotInventoryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        
        RobotEntity robot = this.getContainer().robot;
        Slot s = this.getContainer().getSlot(robot.getActiveSlotFromDataManager());
        
        this.minecraft.getTextureManager().bindTexture(RobotInventoryContainerGui.WIDGETS_GUI_TEXTURE);
        int i = 4;
        this.blit(s.xPos - i, s.yPos - i, 0, 22, 24, 24);
        
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        this.minecraft.getTextureManager().bindTexture(RobotInventoryContainerGui.CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, 3 * 18 + 17);
        this.blit(i, j + 3 * 18 + 17, 0, 126, this.xSize, 96);
    }
}
