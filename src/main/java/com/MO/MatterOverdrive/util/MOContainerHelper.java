package com.MO.MatterOverdrive.util;

import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotPlayerInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MOContainerHelper
{
    public static void AddPlayerSlots(InventoryPlayer inventory,MOBaseContainer container,int x,int y,boolean main,boolean hotbar)
    {
        if(main) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    container.addSlotToContainer(new SlotPlayerInventory(inventory, j + i * 9 + 9, x + j * 18, y + i * 18, false));
                }
            }
        }

        if(hotbar) {
            for (int i = 0; i < 9; i++) {
                container.addSlotToContainer(new SlotPlayerInventory(inventory, i, x + i * 18, y + 61, true));
            }
        }
    }
}
