package com.MO.MatterOverdrive.util;

import com.MO.MatterOverdrive.container.MOBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MOContainerHelper
{
    public static void AddPlayerSlots(InventoryPlayer inventory,MOBaseContainer container,int x,int y)
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0;j < 9;j++)
            {
                container.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for(int i = 0;i < 9;i++)
        {
            container.addSlotToContainer(new Slot(inventory, i, x + i * 18, y + 58));
        }
    }
}
