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

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.container.*;
import matteroverdrive.gui.*;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.tile.*;
import matteroverdrive.util.MOLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler
{
    private Map<Class<? extends MOTileEntity>,Class<? extends MOGuiBase>> guis;
    private Map<Class<? extends MOTileEntity>,Class<? extends MOBaseContainer>> containers;

    public GuiHandler()
    {
        guis = new HashMap<>();
        containers = new HashMap<>();
    }

    public void register(Side side)
    {
        if (side == Side.SERVER)
        {
            registerContainer(TileEntityMachineReplicator.class, ContainerReplicator.class);
            registerContainer(TileEntityMachineDecomposer.class, ContainerDecomposer.class);
            registerContainer(TileEntityMachineNetworkRouter.class, ContainerNetworkRouter.class);
            registerContainer(TileEntityMachineMatterAnalyzer.class, ContainerMatterAnalyzer.class);
            registerContainer(TileEntityMachinePatternStorage.class, ContainerPatternStorage.class);
            registerContainer(TileEntityMachineSolarPanel.class, ContainerSolarPanel.class);
            registerContainer(TileEntityWeaponStation.class, ContainerWeaponStation.class);
            registerContainer(TileEntityMachinePatternMonitor.class, ContainerPatternMonitor.class);
            registerContainer(TileEntityMachineNetworkSwitch.class, ContainerNetworkSwitch.class);
            registerContainer(TileEntityMachineTransporter.class, ContainerTransporter.class);
            registerContainer(TileEntityMachineMatterRecycler.class,ContainerRecycler.class);
            registerContainer(TileEntityMachineFusionReactorController.class,ContainerFusionReactor.class);
            registerContainer(TileEntityAndroidStation.class,ContainerAndroidStation.class);
            registerContainer(TileEntityMachineStarMap.class,ContainerStarMap.class);
        }
        else
        {
            registerGuiAndContainer(TileEntityMachineReplicator.class, GuiReplicator.class,ContainerReplicator.class);
            registerGuiAndContainer(TileEntityMachineDecomposer.class, GuiDecomposer.class,ContainerDecomposer.class);
            registerGuiAndContainer(TileEntityMachineNetworkRouter.class, GuiNetworkRouter.class,ContainerNetworkRouter.class);
            registerGuiAndContainer(TileEntityMachineMatterAnalyzer.class, GuiMatterAnalyzer.class,ContainerMatterAnalyzer.class);
            registerGuiAndContainer(TileEntityMachinePatternStorage.class, GuiPatternStorage.class,ContainerPatternStorage.class);
            registerGuiAndContainer(TileEntityMachineSolarPanel.class, GuiSolarPanel.class,ContainerSolarPanel.class);
            registerGuiAndContainer(TileEntityWeaponStation.class, GuiWeaponStation.class,ContainerWeaponStation.class);
            registerGuiAndContainer(TileEntityMachinePatternMonitor.class, GuiPatternMonitor.class,ContainerPatternMonitor.class);
            registerGuiAndContainer(TileEntityMachineNetworkSwitch.class, GuiNetworkSwitch.class,ContainerNetworkSwitch.class);
            registerGuiAndContainer(TileEntityMachineTransporter.class, GuiTransporter.class,ContainerTransporter.class);
            registerGuiAndContainer(TileEntityMachineMatterRecycler.class, GuiRecycler.class, ContainerRecycler.class);
            registerGuiAndContainer(TileEntityMachineFusionReactorController.class,GuiFusionReactor.class,ContainerFusionReactor.class);
            registerGuiAndContainer(TileEntityAndroidStation.class,GuiAndroidStation.class,ContainerAndroidStation.class);
            registerGuiAndContainer(TileEntityMachineStarMap.class,GuiStarMap.class,ContainerStarMap.class);
        }
    }

    public void registerContainer(Class<? extends MOTileEntity> tileEntity,Class<? extends  MOBaseContainer> container)
    {
        containers.put(tileEntity, container);
    }

    public void registerGuiAndContainer(Class<? extends MOTileEntity> tileEntity,Class<? extends MOGuiBase> gui,Class<? extends  MOBaseContainer> container)
    {
        containers.put(tileEntity, container);
        guis.put(tileEntity,gui);
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity entity = world.getTileEntity(x, y, z);
        //System.out.println("Trying to get container");

        if (entity != null && containers.containsKey(entity.getClass()))
        {
            try
            {
                Class<? extends MOBaseContainer> containerClass = containers.get(entity.getClass());
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
                MOLog.log(Level.WARN, e, "Could call Tile Entity Constructor in Server GUI handling");
            } catch (InstantiationException e) {
                MOLog.log(Level.WARN, e, "Could not instantiate Tile Entity in Server GUI handling");
            } catch (IllegalAccessException e) {
                MOLog.log(Level.WARN, e, "No Rights to access Tile Entity Constructor in Server GUI handling");
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
		TileEntity entity = world.getTileEntity(x, y, z);

        if (guis.containsKey(entity.getClass()))
        {
            try {

                Class<? extends MOGuiBase> containerClass = guis.get(entity.getClass());
                Constructor[] constructors = containerClass.getDeclaredConstructors();
                for (Constructor constructor : constructors)
                {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 2)
                    {
                        if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity))
                        {
                            onContainerOpen(entity,Side.CLIENT);
                            return constructor.newInstance(player.inventory, entity);
                        }
                    }
                }
            } catch (InvocationTargetException e) {
                MOLog.log(Level.WARN, e, "Could call Tile Entity Constructor in Client GUI handling");
            } catch (InstantiationException e) {
                MOLog.log(Level.WARN, e, "Could not instantiate Tile Entity in Client GUI handling");
            } catch (IllegalAccessException e) {
                MOLog.log(Level.WARN, e, "No Rights to access Tile Entity Constructor in Client GUI handling");
            }
        }
        return null;
	}
	
}
