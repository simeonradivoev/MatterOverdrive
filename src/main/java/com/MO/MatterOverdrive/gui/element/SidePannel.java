package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/13/2015.
 */
public class SidePannel extends ElementButton
{
    int originalX;
    int originalY;
    ResourceLocation panel_bg;
    boolean isOpen;

    public SidePannel(GuiBase gui, int posX, int posY, String name)
    {
        super(gui, posX, posY, name, 0, 0, 16, 0, 16, 143,"");
        originalX = this.posX;
        originalY = this.posY;
        this.setTexture(Reference.PATH_ELEMENTS + "right_side_bar.png", 32, 143);
        panel_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_panel_bg.png");
    }

    @Override
    public boolean onMousePressed(int x, int y, int mouseButton)
    {
        isOpen = !isOpen;
        return super.onMousePressed(x, y, mouseButton);
    }

    @Override
    public void drawBackground(int x, int y, float ticks)
    {
        if(isOpen)
        {
            RenderHelper.bindTexture(panel_bg);
            this.gui.drawSizedTexturedModalRect(this.originalX, this.originalY, 0, 0, 37, 143, 37, 143);

            this.posX = originalX + 32;
        }
        else
        {
            this.posX = originalX;
        }

        super.drawBackground(x,y,ticks);
    }
}
