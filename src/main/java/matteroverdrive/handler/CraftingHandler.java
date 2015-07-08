package matteroverdrive.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import matteroverdrive.init.MatterOverdriveItems;

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
