package matteroverdrive.api.transport;

import java.util.Collection;
/**
 * Created by Simeon on 12/28/2015.
 */
public interface IPipeNetwork<T extends IPipe>
{
    Collection<T> getNetworkPipes();
    void addPipe(T pipe);
    void removePipe(T pipe);
    void destroyPipe(T pipe);
    void invalidateNetwork();
}
