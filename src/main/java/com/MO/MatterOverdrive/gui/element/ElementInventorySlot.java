package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Simeon on 4/8/2015.
 */
public class ElementInventorySlot extends ElementSlot
{
    MOSlot slot;

    public ElementInventorySlot(GuiBase gui,MOSlot slot, int posX, int posY,int width,int height, String type,ResourceLocation icon)
    {
        super(gui, posX, posY,width,height, type,icon);
        this.slot = slot;
    }

    public ElementInventorySlot(GuiBase gui,MOSlot slot, int posX, int posY,int width,int height, String type)
    {
        this(gui,slot, posX, posY,width,height, type,null);
    }

    public ElementInventorySlot(GuiBase gui,MOSlot slot,int width,int height, String type,ResourceLocation icon) {
        this(gui,slot, slot.xDisplayPosition, slot.yDisplayPosition, width,height,type,icon);
    }

    public ElementInventorySlot(GuiBase gui,MOSlot slot,int width,int height, String type) {
        this(gui,slot, slot.xDisplayPosition, slot.yDisplayPosition, width,height,type,null);
    }

    public void addTooltip(List<String> list)
    {
        if (!info.isEmpty() && !slot.getHasStack())
        {
            list.add(StringHelper.localize(info));
        }
    }

    public void updateInfo()
    {
        boolean isVisible = isVisible() && (parent == null || parent.isVisible());

        if (!isVisible)
        {
            slot.xDisplayPosition = Integer.MIN_VALUE;
            slot.yDisplayPosition = Integer.MIN_VALUE;
        }
        else {
            slot.xDisplayPosition = posX;
            slot.yDisplayPosition = posY;
        }

        slot.setVisible(isVisible);
    }

    public MOSlot getSlot() {
        return slot;
    }

    public void setSlot(MOSlot slot) {
        this.slot = slot;
    }
}
