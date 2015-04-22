package com.MO.MatterOverdrive.api.network;

import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkCable extends IMatterNetworkConnection
{
    boolean isValid();
    void broadcast(MatterNetworkTaskPacket task);
}
