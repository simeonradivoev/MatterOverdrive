package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.container.*;
import com.MO.MatterOverdrive.gui.*;
import com.MO.MatterOverdrive.tile.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity entity = world.getTileEntity(x, y, z);
        //System.out.println("Trying to get container");

		if(entity != null)
		{
			switch(ID)
			{
			case MatterOverdrive.guiIDReplicator:
				if(entity instanceof TileEntityMachineReplicator)
				{
                    onContainerOpen(entity);
					return new ContainerReplicator(player.inventory,(TileEntityMachineReplicator) entity);
				}
                break;
			case MatterOverdrive.guiIDDecomposer:
				if(entity instanceof TileEntityMachineDecomposer)
				{
                    onContainerOpen(entity);
					return new ContainerDecomposer(player.inventory,(TileEntityMachineDecomposer) entity);
				}
                break;
            case MatterOverdrive.guiNetworkController:
                if(entity instanceof TileEntityMachineNetworkRouter)
                {
                    onContainerOpen(entity);
                    return new ContainerNetworkController(player.inventory,(TileEntityMachineNetworkRouter)entity);
                }
                break;
            case MatterOverdrive.guiMatterAnalyzer:
                if(entity instanceof TileEntityMachineMatterAnalyzer)
                {
                    onContainerOpen(entity);
                    return new ContainerMatterAnalyzer(player.inventory,(TileEntityMachineMatterAnalyzer)entity);
                }
                break;
            case MatterOverdrive.guiPatternStorage:
                if(entity instanceof TileEntityMachinePatternStorage)
                {
                    onContainerOpen(entity);
                    return new ContainerPatternStorage(player.inventory,(TileEntityMachinePatternStorage)entity);
                }
                break;
            case MatterOverdrive.guiSolarPanel:
                if (entity instanceof TileEntityMachineSolarPanel)
                {
                    onContainerOpen(entity);
                    return new ContainerSolarPanel(player.inventory,(TileEntityMachineSolarPanel)entity);
                }
                break;
            case MatterOverdrive.guiWeaponStation:
                if (entity instanceof TileEntityWeaponStation)
                {
                    onContainerOpen(entity);
                    return new ContainerWeaponStation(player.inventory,(TileEntityWeaponStation)entity);
                }
                break;
            case MatterOverdrive.guiPatternMonitor:
                if (entity instanceof TileEntitiyMachinePatternMonitor)
                {
                    onContainerOpen(entity);
                    return new ContainerPatternMonitor(player.inventory,(TileEntitiyMachinePatternMonitor)entity);
                }
                break;
			}
		}
		return null;
	}

    private void onContainerOpen(TileEntity entity)
    {
        if (entity instanceof MOTileEntityMachine)
        {
            ((MOTileEntityMachine) entity).onContainerOpen();
        }
    }

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null)
		{
			switch(ID)
			{
                case MatterOverdrive.guiIDReplicator:
                    if(entity instanceof TileEntityMachineReplicator)
                    {
                        return new GuiReplicator(player.inventory,(TileEntityMachineReplicator) entity);
                    }
                    break;
                case MatterOverdrive.guiIDDecomposer:
                    if(entity instanceof TileEntityMachineDecomposer)
                    {
                        return new GuiDecomposer(player.inventory,(TileEntityMachineDecomposer) entity);
                    }
                    break;
                case  MatterOverdrive.guiNetworkController:
                    if(entity instanceof TileEntityMachineNetworkRouter)
                    {
                        return new GuiNetworkRouter(player.inventory,(TileEntityMachineNetworkRouter)entity);
                    }
                    break;
                case MatterOverdrive.guiMatterAnalyzer:
                    if (entity instanceof TileEntityMachineMatterAnalyzer)
                    {
                        return new GuiMatterAnalyzer(player.inventory,(TileEntityMachineMatterAnalyzer)entity);
                    }
                case MatterOverdrive.guiPatternStorage:
                    if(entity instanceof TileEntityMachinePatternStorage)
                    {
                        return new GuiPatternStorage(player.inventory,(TileEntityMachinePatternStorage)entity);
                    }
                    break;
                case MatterOverdrive.guiSolarPanel:
                    if (entity instanceof TileEntityMachineSolarPanel)
                    {
                        return new GuiSolarPanel(player.inventory,(TileEntityMachineSolarPanel)entity);
                    }
                    break;
                case MatterOverdrive.guiWeaponStation:
                    if (entity instanceof TileEntityWeaponStation)
                    {
                        return new GuiWeaponStation(player.inventory,(TileEntityWeaponStation)entity);
                    }
                    break;
                case MatterOverdrive.guiPatternMonitor:
                    if (entity instanceof TileEntitiyMachinePatternMonitor)
                    {
                        return new GuiPatternMonitor(player.inventory,(TileEntitiyMachinePatternMonitor)entity);
                    }
                    break;
			}
		}
		return null;
	}
	
}
