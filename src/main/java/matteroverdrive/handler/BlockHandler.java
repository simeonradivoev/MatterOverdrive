package matteroverdrive.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Simeon on 12/24/2015.
 */
public class BlockHandler
{
    @SubscribeEvent
    public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event)
    {
        if (event.harvester != null)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(event.harvester);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }
}
