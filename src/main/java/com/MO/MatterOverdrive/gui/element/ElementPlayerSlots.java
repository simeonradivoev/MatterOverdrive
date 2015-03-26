package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ElementPlayerSlots extends ElementBase
{
    public ElementPlayerSlots(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        this.setTexture(Reference.PATH_ELEMENTS + "slot_bg.png",18,18);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        gui.bindTexture(texture);

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0;j < 9;j++)
            {
                drawTexturedModalRect(posX + j * 18,posY + i * 18,0,0,18,18);
            }
        }

        for(int i = 0;i < 9;i++)
        {
            drawTexturedModalRect(posX + i * 18,posY + 58,0,0,18,18);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
