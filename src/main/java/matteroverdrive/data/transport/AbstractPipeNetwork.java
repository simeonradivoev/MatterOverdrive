package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IPipe;
import matteroverdrive.api.transport.IPipeNetwork;

import java.util.Collection;

/**
 * Created by Simeon on 12/28/2015.
 */
public abstract class AbstractPipeNetwork<T extends IPipe> implements IPipeNetwork<T>
{
    public void invalidateNetwork()
    {
        for (IPipe pipe : getNetworkPipes())
        {
            pipe.setNetwork(null);
        }

        getNetworkPipes().clear();
    }

    public void merge(IPipeNetwork<T> network)
    {
        for (T pipe : network.getNetworkPipes())
        {
            pipe.setNetwork(this);
            getNetworkPipes().add(pipe);
        }
    }

    @Override
    public void addPipe(T pipe)
    {
        getNetworkPipes().add(pipe);
        if (pipe.getNetwork() != null && pipe.getNetwork() != this)
        {
            pipe.getNetwork().removePipe(pipe);
        }

        pipe.setNetwork(this);
    }

    @Override
    public void removePipe(T pipe)
    {
        getNetworkPipes().remove(pipe);
    }

    @Override
    public void destroyPipe(T pipe)
    {
        removePipe(pipe);
        if (getNetworkPipes().size() == 0)
        {
            invalidateNetwork();
        }
        Collection<T> connections = pipe.getConnections();
        if (connections.size() > 1)
        {
            for(T connection : connections)
            {
                if (connection.getNetwork() != null && connection.getNetwork() == this)
                    connection.getNetwork().invalidateNetwork();
            }
        }
    }

    public abstract void networkUpdate(T pipe);
}
