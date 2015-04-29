package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ElementSlotsList extends ElementBaseGroup
{

    int margin = 0;

    public ElementSlotsList(GuiBase gui, int posX, int posY,int width,int height, Inventory inventory,int main,boolean isDark)
    {
        super(gui, posX, posY, width, height);
        int index = 0;
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).isMainSlot())
            {
                if (index == main)
                {
                    ElementSlot elementSlot = new ElementSlot(gui, 0, 0, 37, 22, isDark ? "big_main_dark" : "big_main", inventory.getSlot(i).getTexture());
                    elementSlot.setItemOffset(3, 3);
                    addElement(elementSlot);
                }
                else
                    addElement(new ElementSlot(gui,0,0,22,22,"big",inventory.getSlot(i).getTexture()));

                index++;
            }
        }
    }

    public ElementSlotsList(GuiBase gui, int posX, int posY,int width,int height, List<Slot> inventory,int main,boolean isDark)
    {
        super(gui, posX, posY, width, height);
        for (int i = 0;i < inventory.size();i++)
        {
            if (i == main) {
                ElementSlot elementSlot = new ElementSlot(gui, 0, 0, 37, 22, isDark ? "big_main_dark" : "big_main", inventory.get(i).getTexture());
                elementSlot.setItemOffset(5,5);
                addElement(elementSlot);

            }
            else
                addElement(new ElementSlot(gui,0,0,22,22,"big",inventory.get(i).getTexture()));
        }
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);

        int height = 0;
        for (int i = 0;i < elements.size();i++)
        {
            elements.get(i).setPosition(0,height);
            height += elements.get(i).getHeight() + margin;
        }
    }

    public void setMargin(int margin)
    {
        this.margin = margin;
    }
}
