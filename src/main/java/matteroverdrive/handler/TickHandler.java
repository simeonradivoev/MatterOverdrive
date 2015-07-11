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

package matteroverdrive.handler;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.tile.IMOTickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler networkTick;
    private PlayerEventHandler playerEventHandler;
    private boolean worldStartFired = false;

    public TickHandler(ConfigurationHandler configurationHandler,PlayerEventHandler playerEventHandler)
    {
        networkTick = new MatterNetworkTickHandler();
        this.playerEventHandler = playerEventHandler;

        configurationHandler.subscribe(networkTick);
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {

    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        playerEventHandler.onServerTick(event);
    }

    public void onServerStart(FMLServerStartedEvent event)
    {

    }

    //Called when a new frame is displayed (See fps)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {

    }

    //Called when the world ticks
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!worldStartFired)
        {
            onWorldStart(event.side,event.world);
            worldStartFired = true;
        }
        networkTick.onWorldTick(event);

        if (event.side.isServer()) {
            for (int i = 0;i < event.world.loadedTileEntityList.size();i++) {
                TileEntity tileentity = (TileEntity) event.world.loadedTileEntityList.get(i);

                if (tileentity instanceof IMOTickable && !tileentity.isInvalid() && tileentity.hasWorldObj() && event.world.blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)) {
                    ((IMOTickable) tileentity).onServerTick(event);
                }
            }
        }
    }

    public void onWorldStart(Side side,World world)
    {

    }
}
