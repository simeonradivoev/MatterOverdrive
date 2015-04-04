package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/11/2015.
 */
public class BlockNetworkController extends MOBlockContainer
{

    public BlockNetworkController(Material material, String name)
    {
        super(material, name);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        if(!world.isRemote)
        {
            FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiNetworkController, world, x, y, z);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineNetworkController();
    }
}
