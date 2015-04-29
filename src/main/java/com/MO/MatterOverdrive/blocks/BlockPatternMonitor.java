package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.tile.TileEntitiyMachinePatternMonitor;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class BlockPatternMonitor extends MOBlockMachine
{
    public BlockPatternMonitor(Material material, String name)
    {
        super(material, name);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
        if(!world.isRemote)
        {
            FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiPatternMonitor, world, x, y, z);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_)
    {
        return new TileEntitiyMachinePatternMonitor();
    }
}
