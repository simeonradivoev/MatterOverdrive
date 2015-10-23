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
import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.IMOTickable;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler matterNetworkTickHandler;
    private PlayerEventHandler playerEventHandler;
    private boolean worldStartFired = false;
    private long lastTickTime;
    private int lastTickLength;
    private boolean phaseTracker;

    public TickHandler(ConfigurationHandler configurationHandler,PlayerEventHandler playerEventHandler)
    {
        this.playerEventHandler = playerEventHandler;
        this.matterNetworkTickHandler = new MatterNetworkTickHandler();
        configurationHandler.subscribe(matterNetworkTickHandler);
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
            return;

        if (ClientProxy.weaponHandler != null)
            ClientProxy.weaponHandler.onClientTick(event);
    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        playerEventHandler.onServerTick(event);

        lastTickLength = (int)(System.nanoTime() - lastTickTime);
        lastTickTime = System.nanoTime();
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

        if (event.side.isServer() && event.phase.equals(TickEvent.Phase.START)) {

            TickEvent.Phase phase = phaseTracker ? TickEvent.Phase.END : TickEvent.Phase.START;
            matterNetworkTickHandler.onWorldTickPre(phase, event.world);

            Iterator<TileEntity> iterator = event.world.loadedTileEntityList.iterator();

            while (iterator.hasNext())
            {
                try {
                    TileEntity tileEntity = iterator.next();
                    if (tileEntity instanceof IMOTickable) {
                        if (tileEntity instanceof IMatterNetworkHandler) {
                            matterNetworkTickHandler.updateHandler((IMatterNetworkHandler) tileEntity, phase, event.world);
                        } else {
                            ((IMOTickable) tileEntity).onServerTick(phase, event.world);
                        }

                    }
                }catch (ConcurrentModificationException e)
                {
                    //MatterOverdrive.log.log(Level.ERROR,e,"There was an Error while updating Matter Overdrive Tile Entities.");
                    return;
                }
            }

            matterNetworkTickHandler.onWorldTickPost(phase, event.world);
            phaseTracker = !phaseTracker;
        }

    }

    public void onWorldStart(Side side,World world)
    {

    }

    public int getLastTickLength()
    {
        return lastTickLength;
    }
}
