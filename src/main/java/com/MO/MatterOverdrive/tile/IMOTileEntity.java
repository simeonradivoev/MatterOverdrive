package com.MO.MatterOverdrive.tile;

/**
 * Created by Simeon on 3/6/2015.
 */
public interface IMOTileEntity
{
    void onAdded();
    void onDestroyed();
    void onNeighborBlockChange();
}
