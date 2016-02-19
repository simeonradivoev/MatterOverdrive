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

import matteroverdrive.util.MOLog;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.IMOTickable;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private final MatterNetworkTickHandler matterNetworkTickHandler;
    private final PlayerEventHandler playerEventHandler;
    private boolean worldStartFired = false;
    private long lastTickTime;
    private int lastTickLength;

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

        if (ClientProxy.instance().getClientWeaponHandler() != null)
            ClientProxy.instance().getClientWeaponHandler().onClientTick(event);

        if (!Minecraft.getMinecraft().isGamePaused() && event.phase.equals(TickEvent.Phase.START)) {
            ClientProxy.questHud.onTick();
        }
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
        ClientProxy.instance().getClientWeaponHandler().onTick(event);
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

        if (event.side.isServer()) {

            matterNetworkTickHandler.onWorldTickPre(event.phase, event.world);
            int tileEntityListSize = event.world.loadedTileEntityList.size();

            for (int i = 0;i < tileEntityListSize;i++)
            {
                try {
                    TileEntity tileEntity = event.world.loadedTileEntityList.get(i);
                    if (tileEntity instanceof IMOTickable) {
                        if (tileEntity instanceof IMatterNetworkHandler) {
                            matterNetworkTickHandler.updateHandler((IMatterNetworkHandler) tileEntity, event.phase, event.world);
                        } else {
                            ((IMOTickable) tileEntity).onServerTick(event.phase, event.world);
                        }

                    }
                }catch (Throwable e)
                {
                    MOLog.log(Level.ERROR,e,"There was an Error while updating Matter Overdrive Tile Entities.");
                    return;
                }
            }

            matterNetworkTickHandler.onWorldTickPost(event.phase, event.world);
        }

        MatterOverdrive.moWorld.onWorldTick(event);
    }

    public void onWorldStart(Side side,World world)
    {

    }

    public int getLastTickLength()
    {
        return lastTickLength;
    }
}
