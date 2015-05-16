package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * Created by Simeon on 5/15/2015.
 */
public class CraftingHandler
{

    @SubscribeEvent
    public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event)
    {
        if (event.smelting.getItem() == MatterOverdriveItems.matter_dust)
        {
            System.out.println("Matter Smelted");
        }
    }
}
