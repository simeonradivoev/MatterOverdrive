package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 4/8/2015.
 */
public class ElementInventorySlot extends ElementSlot
{
    MOSlot slot;

    public ElementInventorySlot(GuiBase gui,MOSlot slot, int posX, int posY, boolean big) {
        super(gui, posX, posY, big);
        this.slot = slot;
    }

    public ElementInventorySlot(GuiBase gui,MOSlot slot, boolean big) {
        super(gui, slot.xDisplayPosition, slot.yDisplayPosition, big);
        this.slot = slot;
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
}
