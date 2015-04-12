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
public class ElementSlotsList extends ElementSlot
{
    private static final ResourceLocation MAIN_SLOT_TEXTURE = new ResourceLocation(Reference.PATH_ELEMENTS + "side_slot_active_bg.png");
    List<Slot> inventory;
    int main;

    public ElementSlotsList(GuiBase gui, int posX, int posY, Inventory inventory,int main)
    {
        super(gui, posX, posY,true);
        this.inventory = new ArrayList<Slot>();
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).isMainSlot())
            {
                this.inventory.add(inventory.getSlot(i));
            }
        }
        this.main = main;
    }

    public ElementSlotsList(GuiBase gui, int posX, int posY, List<Slot> inventory,int main)
    {
        super(gui, posX, posY,true);
        this.inventory = inventory;
        this.main = main;
    }

    void setMainSlot(int id)
    {
        this.main = id;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1,1,1);

        for (int i = 0;i < inventory.size();i++)
        {
            int y = this.posY + i * 27;
            if(main == i)
            {
                gui.bindTexture(MAIN_SLOT_TEXTURE);
                gui.drawSizedTexturedModalRect(this.posX,y,0,0,37,22,37,22);

            }
            else
            {
                gui.bindTexture(slot_big);
                gui.drawSizedTexturedModalRect(this.posX, y, 0, 0, 22, 22, 22, 22);
            }

            drawSlotIcon(inventory.get(i),this.posX + 3,y + 3);
        }
    }

    public void AddSlot(Slot slot)
    {
        inventory.add(slot);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
