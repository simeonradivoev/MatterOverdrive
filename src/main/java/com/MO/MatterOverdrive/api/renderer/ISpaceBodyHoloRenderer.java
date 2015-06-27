package com.MO.MatterOverdrive.api.renderer;

import com.MO.MatterOverdrive.starmap.data.GalacticPosition;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.SpaceBody;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/17/2015.
 */
public interface ISpaceBodyHoloRenderer
{
    void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance);
    void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks,float opacity);
    double getHologramHeight();
}
