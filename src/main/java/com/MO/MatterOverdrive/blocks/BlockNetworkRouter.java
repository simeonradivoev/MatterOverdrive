package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkRouter;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/11/2015.
 */
public class BlockNetworkRouter extends MOBlockMachine
{
    public BlockNetworkRouter(Material material, String name)
    {
        super(material, name);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
    {
        TileEntity entity = world.getTileEntity(x,y,z);
        if (entity instanceof TileEntityMachineNetworkRouter)
        {
            if (((TileEntityMachineNetworkRouter) entity).isActive())
            {
                return blockIcon;
            }
        }
        return MatterOverdriveIcons.Network_port_square;
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
        return new TileEntityMachineNetworkRouter();
    }
}
