package matteroverdrive.api.transport;

import java.util.List;

/**
 * Created by Simeon on 12/28/2015.
 */
public interface IPipe<T extends IPipeNetwork>
{
    T getNetwork();
    void setNetwork(T network);
    void onNeighborBlockChange();
    List<IPipe<T>> getConnections();
}
