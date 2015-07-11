package matteroverdrive.api.renderer;

import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/17/2015.
 */
public interface ISpaceBodyHoloRenderer
{
    void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance);
    void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks,float opacity);
    double getHologramHeight();
}
