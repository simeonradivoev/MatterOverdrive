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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.thread.RegisterItemsFromRecipes;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Simeon on 8/22/2015.
 */
public class MatterRegistrationHandler
{
    public final String registryPath;
    private Future matterCalculationThread;

    public MatterRegistrationHandler(String registryPath)
    {
        this.registryPath = registryPath;
    }

    public void serverStart(FMLServerStartedEvent event)
    {
        try {
            if (MatterOverdrive.matterRegistry.needsCalculation(registryPath))
            {
                try {
                    runCalculationThread();
                }catch (Exception e) {
                    MatterOverdrive.log.log(Level.ERROR,e,"There was a problem calculating Matter from Recipes or Furnaces");
                }
            }else
            {
                try {
                    MatterOverdrive.matterRegistry.loadFromFile(registryPath);
                }catch (Exception e) {
                    MatterOverdrive.log.log(Level.ERROR, e, "There was a problem loading the Matter Registry file. Running Matter Calculation");
                    {
                        runCalculationThread();
                    }
                }
            }
        } catch (Exception e)
        {
            MatterOverdrive.log.log(Level.ERROR,e,"There was a problem while trying to load Matter Registry or trying to Calculate it");
        }
    }

    public void runCalculationThread()
    {
        if (matterCalculationThread != null)
        {
            matterCalculationThread.cancel(true);
            matterCalculationThread = null;
        }
        matterCalculationThread = MatterOverdrive.threadPool.submit(new RegisterItemsFromRecipes(registryPath));
    }

    public void onRegistrationComplete()
    {
        PacketUpdateMatterRegistry updateMatterRegistry = new PacketUpdateMatterRegistry(MatterOverdrive.matterRegistry.getEntries());
        for (EntityPlayerMP playerMP : (List<EntityPlayerMP>) MinecraftServer.getServer().getEntityWorld().playerEntities)
        {
            MatterOverdrive.packetPipeline.sendTo(updateMatterRegistry,playerMP);
        }
    }
}
