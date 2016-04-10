package matteroverdrive.handler.matter_network;

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.data.transport.MatterNetwork;

/**
 * Created by Simeon on 1/28/2016.
 */
public class MatterNetworkHandler extends GridNetworkHandler<IMatterNetworkConnection, MatterNetwork>
{
	@Override
	public MatterNetwork createNewNetwork(IMatterNetworkConnection node)
	{
		return new MatterNetwork(this);
	}
}
