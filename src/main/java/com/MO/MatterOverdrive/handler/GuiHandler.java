package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.container.*;
import com.MO.MatterOverdrive.gui.*;
import com.MO.MatterOverdrive.tile.*;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler
{
    private Map<Class<? extends MOTileEntity>,Class<? extends MOGuiBase>> guis;
    private Map<Class<? extends MOTileEntity>,Class<? extends MOBaseContainer>> containers;

    public static final byte guiIDReplicator = 0;
    public static final byte guiIDDecomposer = 1;
    public static final byte guiIDMatterScanner = 2;
    public static final byte guiNetworkRouter = 3;
    public static final byte guiMatterAnalyzer = 4;
    public static final byte guiPatternStorage = 5;
    public static final byte guiSolarPanel = 6;
    public static final byte guiWeaponStation = 7;
    public static final byte guiPatternMonitor = 8;
    public static final byte getGuiNetworkSwitch = 9;

    public GuiHandler()
    {
        guis = new HashMap<Class<? extends MOTileEntity>, Class<? extends MOGuiBase>>();
        containers = new HashMap<Class<? extends MOTileEntity>, Class<? extends MOBaseContainer>>();
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
                for (int i = 0;i < constructors.length;i++)
                {
                    Class[] parameterTypes = constructors[i].getParameterTypes();
                    if (parameterTypes.length == 2)
                    {
                        if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity))
                        {
                            onContainerOpen(entity,Side.SERVER);
                            return constructors[i].newInstance(player.inventory,entity);
                        }
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
                for (int i = 0;i < constructors.length;i++)
                {
                    Class[] parameterTypes = constructors[i].getParameterTypes();
                    if (parameterTypes.length == 2)
                    {
                        if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity))
                        {
                            onContainerOpen(entity,Side.CLIENT);
                            return constructors[i].newInstance(player.inventory,entity);
                        }
                    }
                }
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
	}
	
}
