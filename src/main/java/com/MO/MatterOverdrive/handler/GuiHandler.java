package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.container.ContainerDecomposer;
import com.MO.MatterOverdrive.container.ContainerMatterAnalyzer;
import com.MO.MatterOverdrive.container.ContainerNetworkController;
import com.MO.MatterOverdrive.container.ContainerReplicator;
import com.MO.MatterOverdrive.gui.*;
import com.MO.MatterOverdrive.tile.TileEntityMachineDecomposer;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterAnalyzer;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

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
        System.out.println("Trying to get container");


		if(entity != null)
		{
			switch(ID)
			{
			case MatterOverdrive.guiIDReplicator:
				if(entity instanceof TileEntityMachineReplicator)
				{
					return new ContainerReplicator(player.inventory,(TileEntityMachineReplicator) entity);
				}
                break;
			case MatterOverdrive.guiIDDecomposer:
				if(entity instanceof TileEntityMachineDecomposer)
				{
					return new ContainerDecomposer(player.inventory,(TileEntityMachineDecomposer) entity);
				}
                break;
            case MatterOverdrive.guiNetworkController:
                if(entity instanceof TileEntityMachineNetworkController)
                {
                    return new ContainerNetworkController(player.inventory,(TileEntityMachineNetworkController)entity);
                }
                break;
            case MatterOverdrive.guiMatterAnalyzer:
                if(entity instanceof TileEntityMachineMatterAnalyzer)
                {
                    return new ContainerMatterAnalyzer(player.inventory,(TileEntityMachineMatterAnalyzer)entity);
                }
                break;
			}
		}
		return null;
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
                    if(entity instanceof TileEntityMachineNetworkController)
                    {
                        return new GuiNetworkController(player.inventory,(TileEntityMachineNetworkController)entity);
                    }
                    break;
                case MatterOverdrive.guiMatterAnalyzer:
                    if (entity instanceof TileEntityMachineMatterAnalyzer)
                    {
                        return new GuiMatterAnalyzer(player.inventory,(TileEntityMachineMatterAnalyzer)entity);
                    }
			}
		}
		return null;
	}
	
}
