package com.MO.MatterOverdrive.api;

import com.MO.MatterOverdrive.tile.TileEntityGravitationalAnomaly;

/**
 * Created by Simeon on 5/13/2015.
 */
public interface IAnomalyAffector
{
    void affect(TileEntityGravitationalAnomaly anomaly);
    boolean isValid(TileEntityGravitationalAnomaly anomaly);
}
