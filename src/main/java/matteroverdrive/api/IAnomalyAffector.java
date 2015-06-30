package matteroverdrive.api;

import matteroverdrive.tile.TileEntityGravitationalAnomaly;

/**
 * Created by Simeon on 5/13/2015.
 */
public interface IAnomalyAffector
{
    void affect(TileEntityGravitationalAnomaly anomaly);
    boolean isValid(TileEntityGravitationalAnomaly anomaly);
}
