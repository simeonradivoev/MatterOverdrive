package matteroverdrive.container.matter_network;

import matteroverdrive.api.matter.IMatterDatabase;

import java.util.List;

/**
 * Created by Simeon on 1/30/2016.
 */
public interface IMatterDatabaseMonitor
{
	List<IMatterDatabase> getConnectedDatabases();
}
