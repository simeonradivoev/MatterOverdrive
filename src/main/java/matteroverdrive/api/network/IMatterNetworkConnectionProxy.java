package matteroverdrive.api.network;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/19/2015.
 * Used by tile entities
 */
public interface IMatterNetworkConnectionProxy
{
    IMatterNetworkConnection getMatterNetworkConnection();
    int onNetworkTick(World world,TickEvent.Phase phase);
}
