package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternMonitor;
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
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/26/2015.
 */
public class BlockPatternMonitor extends MOBlockMachine
{
    private IIcon front_icon;

    public BlockPatternMonitor(Material material, String name)
    {
        super(material, name);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setBlockBounds(0,0,0,1,1,5f * (1f/16f));
        lightValue = 10;
        setHasGui(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        front_icon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "pattern_monitor_front");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            return front_icon;
        }
        else if (side == BlockHelper.getOppositeSide(meta))
        {
            return MatterOverdriveIcons.Network_port_square;
        }
        return MatterOverdriveIcons.Base;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
        float pixel = 1f / 16f;

        if (direction == ForgeDirection.EAST)
        {
           setBlockBounds(0,0,0,5 * pixel,1,1);
        }
        else if (direction == ForgeDirection.WEST)
        {
            setBlockBounds(1 - 5 * pixel,0,0,1,1,1);
        }
        else if (direction == ForgeDirection.SOUTH)
        {
            setBlockBounds(0,0,0,1,1,5 * pixel);
        }
        else if (direction == ForgeDirection.NORTH)
        {
            setBlockBounds(0,0,1 - 5 * pixel,1,1,1);
        }
        else
        {
            setBlockBounds(0,0,0,1,1,5 * pixel);
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachinePatternMonitor();
    }

    @Override
    public int getRenderType()
    {
        return BlockRendererReplicator.renderID;
    }
}
