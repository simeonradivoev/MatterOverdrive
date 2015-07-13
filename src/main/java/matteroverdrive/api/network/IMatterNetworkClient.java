package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkPacket;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkClient extends IMatterNetworkConnection,IMatterNetworkHandler
{
    boolean canPreform(MatterNetworkPacket task);
    void queuePacket(MatterNetworkPacket packet,ForgeDirection from);
}
