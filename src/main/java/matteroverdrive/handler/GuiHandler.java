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
import matteroverdrive.container.*;
import matteroverdrive.container.matter_network.ContainerPatternMonitor;
import matteroverdrive.gui.*;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.tile.*;
import matteroverdrive.util.MOLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler
{
    private Map<Class<? extends MOTileEntity>,Class<? extends MOGuiBase>> tileEntityGuiList;
    private Map<Class<? extends MOTileEntity>,Class<? extends MOBaseContainer>> tileEntityContainerList;

    public GuiHandler()
    {
        tileEntityGuiList = new HashMap<>();
        tileEntityContainerList = new HashMap<>();
    }

    public void register(Side side)
    {
        if (side == Side.SERVER)
        {
            //Container Registration
            registerContainer(TileEntityMachineSolarPanel.class, ContainerSolarPanel.class);
            registerContainer(TileEntityWeaponStation.class, ContainerWeaponStation.class);
            registerContainer(TileEntityMachineFusionReactorController.class,ContainerFusionReactor.class);
            registerContainer(TileEntityAndroidStation.class,ContainerAndroidStation.class);
            registerContainer(TileEntityMachineStarMap.class,ContainerStarMap.class);
            registerContainer(TileEntityInscriber.class,ContainerInscriber.class);
            registerContainer(TileEntityAndroidSpawner.class,ContainerAndroidSpawner.class);
            registerContainer(TileEntityMachineReplicator.class,ContainerReplicator.class);
            registerContainer(TileEntityMachinePatternMonitor.class,ContainerPatternMonitor.class);
            registerContainer(TileEntityMachineMatterAnalyzer.class,ContainerAnalyzer.class);
            registerContainer(TileEntityMachineDimensionalPylon.class,ContainerDimensionalPylon.class);
        }
        else
        {
            //Gui Registration
            registerGuiAndContainer(TileEntityMachineReplicator.class, GuiReplicator.class,ContainerReplicator.class);
            registerGui(TileEntityMachineDecomposer.class, GuiDecomposer.class);
            registerGui(TileEntityMachineNetworkRouter.class, GuiNetworkRouter.class);
            registerGuiAndContainer(TileEntityMachineMatterAnalyzer.class, GuiMatterAnalyzer.class,ContainerAnalyzer.class);
            registerGui(TileEntityMachinePatternStorage.class, GuiPatternStorage.class);
            registerGuiAndContainer(TileEntityMachineSolarPanel.class, GuiSolarPanel.class, ContainerSolarPanel.class);
            registerGuiAndContainer(TileEntityWeaponStation.class, GuiWeaponStation.class, ContainerWeaponStation.class);
            registerGuiAndContainer(TileEntityMachinePatternMonitor.class, GuiPatternMonitor.class,ContainerPatternMonitor.class);
            registerGui(TileEntityMachineNetworkSwitch.class, GuiNetworkSwitch.class);
            registerGui(TileEntityMachineTransporter.class,GuiTransporter.class);
            registerGui(TileEntityMachineMatterRecycler.class, GuiRecycler.class);
            registerGuiAndContainer(TileEntityMachineFusionReactorController.class, GuiFusionReactor.class, ContainerFusionReactor.class);
            registerGuiAndContainer(TileEntityAndroidStation.class,GuiAndroidStation.class,ContainerAndroidStation.class);
            registerGuiAndContainer(TileEntityMachineStarMap.class,GuiStarMap.class,ContainerStarMap.class);
            registerGui(TileEntityHoloSign.class,GuiHoloSign.class);
            registerGui(TileEntityMachineChargingStation.class,GuiChargingStation.class);
            registerGuiAndContainer(TileEntityInscriber.class,GuiInscriber.class,ContainerInscriber.class);
            registerGui(TileEntityMachineContractMarket.class,GuiContractMarket.class);
            registerGuiAndContainer(TileEntityAndroidSpawner.class,GuiAndroidSpawner.class,ContainerAndroidSpawner.class);
            registerGui(TileEntityMachineSpacetimeAccelerator.class,GuiSpacetimeAccelerator.class);
            registerGuiAndContainer(TileEntityMachineDimensionalPylon.class,GuiDimensionalPylon.class,ContainerDimensionalPylon.class);
        }
    }

    public void registerContainer(Class<? extends MOTileEntity> tileEntity,Class<? extends  MOBaseContainer> container)
    {
        tileEntityContainerList.put(tileEntity, container);
    }

    public void registerGuiAndContainer(Class<? extends MOTileEntity> tileEntity,Class<? extends MOGuiBase> gui,Class<? extends  MOBaseContainer> container)
    {
        tileEntityContainerList.put(tileEntity, container);
        tileEntityGuiList.put(tileEntity, gui);
    }

    public void registerGui(Class<? extends MOTileEntity> tileEntity,Class<? extends MOGuiBase> gui)
    {
        tileEntityGuiList.put(tileEntity, gui);
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

        TileEntity entity = world.getTileEntity(new BlockPos(x,y,z));

        switch (ID)
        {
            default:
                if (entity != null && tileEntityContainerList.containsKey(entity.getClass()))
                {
                    try
                    {
                        Class<? extends MOBaseContainer> containerClass = tileEntityContainerList.get(entity.getClass());
                        Constructor[] constructors = containerClass.getDeclaredConstructors();
                        for (Constructor constructor : constructors)
                        {
                            Class[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == 2)
                            {
                                if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity))
                                {
                                    onContainerOpen(entity,Side.SERVER);
                                    return constructor.newInstance(player.inventory, entity);
                                }
                            }
                        }
                    } catch (InvocationTargetException e) {
                        MOLog.log(Level.WARN,e,"Could not call TileEntity constructor in server GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not call TileEntity constructor in server GUI handler");
                    } catch (InstantiationException e) {
                        MOLog.log(Level.WARN,e,"Could not instantiate TileEntity in server GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not instantiate TileEntity in server GUI handler");
                    } catch (IllegalAccessException e) {
                        MOLog.log(Level.WARN,e,"Could not access TileEntity constructor in server GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not access TileEntity constructor in server GUI handler");
                    }
                }else if (entity instanceof MOTileEntityMachine)
                {
                    return ContainerFactory.createMachineContainer((MOTileEntityMachine)entity,player.inventory);
                }
        }
		return null;
	}

    private void onContainerOpen(TileEntity entity,Side side)
    {
        if (entity instanceof MOTileEntityMachine)
        {
            ((MOTileEntityMachine) entity).onContainerOpen(side);
        }
    }

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

        TileEntity entity = world.getTileEntity(new BlockPos(x,y,z));

        switch (ID) {
            default:
                if (tileEntityGuiList.containsKey(entity.getClass())) {
                    try {

                        Class<? extends MOGuiBase> containerClass = tileEntityGuiList.get(entity.getClass());
                        Constructor[] constructors = containerClass.getDeclaredConstructors();
                        for (Constructor constructor : constructors) {
                            Class[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == 2) {
                                if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity)) {
                                    onContainerOpen(entity, Side.CLIENT);
                                    return constructor.newInstance(player.inventory, entity);
                                }
                            }
                        }
                    } catch (InvocationTargetException e) {
                        MOLog.log(Level.WARN,e,"Could not call TileEntity constructor in client GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not call TileEntity constructor in client GUI handler");
                    } catch (InstantiationException e) {
                        MOLog.log(Level.WARN,e,"Could not instantiate the TileEntity in client GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not instantiate the TileEntity in client GUI handler");
                    } catch (IllegalAccessException e) {
                        MOLog.log(Level.WARN,e,"Could not access TileEntity constructor in client GUI handler");
                        MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Could not access TileEntity constructor in client GUI handler");
                    }
                }else if (entity instanceof MOTileEntityMachine)
                {
                    return new MOGuiMachine(ContainerFactory.createMachineContainer((MOTileEntityMachine)entity,player.inventory),(MOTileEntityMachine)entity);
                }
        }
        return null;
	}
}
