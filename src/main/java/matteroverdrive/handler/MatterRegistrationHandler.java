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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.thread.RegisterItemsFromRecipes;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import matteroverdrive.util.MOLog;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.concurrent.Future;

/**
 * Created by Simeon on 8/22/2015.
 */
public class MatterRegistrationHandler
{
    private Future matterCalculationThread;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load load)
    {
        if (!load.getWorld().isRemote && load.getWorld().provider.getDimension() == 0)
        {
            File matterRegistryFile = getMatterRegistryFile(load.getWorld());
            File customHandlersFile = getCustomHandlersFile(load.getWorld());

            try
            {
                MatterOverdrive.matterRegistry.loadCustomHandlers(customHandlersFile);
            }catch (Exception e)
            {
                MOLog.log(Level.ERROR,e,"There was a problem while loading custom matter handlers");
            }

            try
            {
                if (MatterOverdrive.matterRegistry.needsCalculation(matterRegistryFile) && MatterOverdrive.matterRegistry.AUTOMATIC_CALCULATION)
                {
                    try
                    {
                        runCalculationThread(load.getWorld());
                    } catch (Exception e)
                    {
                        MOLog.log(Level.ERROR, e, "There was a problem calculating Matter from Recipes or Furnaces");
                    }
                } else
                {
                    try
                    {
                        MatterOverdrive.matterRegistry.loadFromFile(matterRegistryFile);
                    } catch (Exception e)
                    {
                        MOLog.log(Level.ERROR, e, "There was a problem loading the Matter Registry file.");
                        if (MatterOverdrive.matterRegistry.AUTOMATIC_CALCULATION)
                        {
                            MOLog.log(Level.INFO, e, "Starting automatic matter calculation thread.");
                            runCalculationThread(load.getWorld());
                        } else
                        {
                            MOLog.log(Level.INFO, e, "Automatic matter calculation disabled. To enable go to Matter Overdrive configs");
                        }
                    }
                }
            } catch (Exception e)
            {
                MOLog.log(Level.ERROR, e, "There was a problem while trying to load Matter Registry or trying to Calculate it");
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload unload)
    {
        if (!unload.getWorld().isRemote && unload.getWorld().provider.getDimension() == 0)
        {
            MatterOverdrive.matterRegistry.unload();
        }
    }

    public void runCalculationThread(World world)
    {
        File matterRegistryFile = getMatterRegistryFile(world);
        if (matterCalculationThread != null)
        {
            MOLog.log(Level.INFO, "Old calculation thread is running. Stopping old calculation thread");
            matterCalculationThread.cancel(true);
            matterCalculationThread = null;
        }
        matterCalculationThread = MatterOverdrive.threadPool.submit(new RegisterItemsFromRecipes(matterRegistryFile));
    }

    public void onRegistrationComplete()
    {
        PacketUpdateMatterRegistry updateMatterRegistry = new PacketUpdateMatterRegistry(MatterOverdrive.matterRegistry);
        // TODO: 3/26/2016 Find how to get Minecraft server
        //MinecraftServer.getServer().getEntityWorld().playerEntities.stream().filter(playerMP -> playerMP instanceof EntityPlayerMP).forEach(playerMP -> MatterOverdrive.packetPipeline.sendTo(updateMatterRegistry, (EntityPlayerMP) playerMP));
    }

    private File getMatterRegistryFile(World world)
    {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        return new File(worldDirectory.getPath() + "/matter_registry.dat");
    }

    private File getCustomHandlersFile(World world)
    {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        return new File(worldDirectory.getPath() + "/custom_matter.json");
    }
}
