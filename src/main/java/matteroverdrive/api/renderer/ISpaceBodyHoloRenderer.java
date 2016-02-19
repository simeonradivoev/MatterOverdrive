/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.api.renderer;

import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public interface ISpaceBodyHoloRenderer
{
    void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance);
    void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks,float opacity);
    boolean displayOnZoom(int zoom,SpaceBody spaceBody);
    double getHologramHeight(SpaceBody spaceBody);
}
