package matteroverdrive.api.transport;

import matteroverdrive.data.TransportLocation;

import java.util.List;

/**
 * Created by Simeon on 5/5/2015.
 */
public interface ITransportList {
    List<TransportLocation> getPositions();
}
