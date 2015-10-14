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

package matteroverdrive.api.network;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 7/13/2015.
 * This is used by all Matter Network Machines.
 * This is just for calling the custom tick method for all Matter Network Machines.
 */
public interface IMatterNetworkHandler
{
    /**
     * Called each matter Network tick.
     * This is handled separately from the Tile Entity's update function.
     * It has to phases. A start and an end.
     * The Matter Network ticking is limited based on the number of interaction on the Network for each Minecraft world tick.
     * @param world the world.
     * @param phase the phase of the Matter Network tick.
     *              There are 2 phases:
     *              <ul>
     *                  <li>Start</li>
     *                  <li>End</li>
     *              </ul>
     * @return the number of interaction made this tick.
     * Used to limit the interaction per World tick.
     */
    int onNetworkTick(World world,TickEvent.Phase phase);
}
