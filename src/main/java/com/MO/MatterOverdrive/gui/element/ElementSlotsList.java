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
    ElementSlot mainSlot;
    int margin = 0;

    public ElementSlotsList(GuiBase gui, int posX, int posY, int width, int height, Inventory inventory, int main)
    {
        super(gui, posX, posY, width, height);
        int index = 0;
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).isMainSlot())
            {
                if (index == main)
                {
                    mainSlot = new ElementSlot(gui, 0, 0, 37, 22, "big_main", inventory.getSlot(i).getTexture());
                    mainSlot.setItemOffset(3, 3);
                    addElement(mainSlot);
                }
                else {

                    addElement(new ElementSlot(gui, 0, 0, 22, 22, "big", inventory.getSlot(i).getTexture()));
                }

                index++;
            }
        }
    }

    public ElementSlotsList(GuiBase gui, int posX, int posY,int width,int height, List<Slot> inventory,int main)
    {
        super(gui, posX, posY, width, height);
        for (int i = 0;i < inventory.size();i++)
        {
            if (i == main) {
                mainSlot = new ElementSlot(gui, 0, 0, 37, 22, "big_main", inventory.get(i).getTexture());
                mainSlot.setItemOffset(5,5);
                addElement(mainSlot);

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

    public ElementSlot getMainSlot()
    {
        return mainSlot;
    }
}
