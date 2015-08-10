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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.container.*;
import matteroverdrive.gui.*;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
        }
        else
        {
            //Gui Registration
            registerGui(TileEntityMachineReplicator.class, GuiReplicator.class);
            registerGui(TileEntityMachineDecomposer.class, GuiDecomposer.class);
            registerGui(TileEntityMachineNetworkRouter.class, GuiNetworkRouter.class);
            registerGui(TileEntityMachineMatterAnalyzer.class, GuiMatterAnalyzer.class);
            registerGui(TileEntityMachinePatternStorage.class, GuiPatternStorage.class);
            registerGuiAndContainer(TileEntityMachineSolarPanel.class, GuiSolarPanel.class,ContainerSolarPanel.class);
            registerGuiAndContainer(TileEntityWeaponStation.class, GuiWeaponStation.class,ContainerWeaponStation.class);
            registerGui(TileEntityMachinePatternMonitor.class, GuiPatternMonitor.class);
            registerGui(TileEntityMachineNetworkSwitch.class, GuiNetworkSwitch.class);
            registerGui(TileEntityMachineTransporter.class,GuiTransporter.class);
            registerGui(TileEntityMachineMatterRecycler.class, GuiRecycler.class);
            registerGuiAndContainer(TileEntityMachineFusionReactorController.class,GuiFusionReactor.class,ContainerFusionReactor.class);
            registerGuiAndContainer(TileEntityAndroidStation.class,GuiAndroidStation.class,ContainerAndroidStation.class);
            registerGuiAndContainer(TileEntityMachineStarMap.class,GuiStarMap.class,ContainerStarMap.class);
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

		TileEntity entity = world.getTileEntity(x, y, z);

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
				MatterOverdrive.log.warn("Could not call TileEntity constructor in server GUI handler", e);
            } catch (InstantiationException e) {
				MatterOverdrive.log.warn("Could not instantiate TileEntity in server GUI handler", e);
            } catch (IllegalAccessException e) {
				MatterOverdrive.log.warn("Could not access TileEntity constructor in server GUI handler", e);
            }
        }else if (entity instanceof MOTileEntityMachine)
        {
            return ContainerFactory.createMachineContainer((MOTileEntityMachine)entity,player.inventory);
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

        if (tileEntityGuiList.containsKey(entity.getClass()))
        {
            try {

                Class<? extends MOGuiBase> containerClass = tileEntityGuiList.get(entity.getClass());
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
				MatterOverdrive.log.warn("Could not call TileEntity constructor in client GUI handler", e);
            } catch (InstantiationException e) {
				MatterOverdrive.log.warn("Could not instantiate the TileEntity in client GUI handler", e);
            } catch (IllegalAccessException e) {
				MatterOverdrive.log.warn("Could not access TileEntity constructor in client GUI handler");
            }
        }
        return null;
	}
	
}
