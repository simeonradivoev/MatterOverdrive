package matteroverdrive.container.matter_network;

import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.data.matter_network.MatterDatabaseEvent;

/**
 * Created by Simeon on 1/30/2016.
 */
public interface IMatterDatabaseWatcher extends IMachineWatcher
{
	void onConnectToNetwork(IMatterDatabaseMonitor monitor);

	void onDisconnectFromNetwork(IMatterDatabaseMonitor monitor);

	void onDatabaseEvent(MatterDatabaseEvent changeInfo);
}
