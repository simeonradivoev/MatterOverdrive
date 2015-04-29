package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
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
 * Created by Simeon on 4/13/2015.
 */
public class BlockWeaponStation extends MOBlockContainer
{
    private IIcon topIcon;
    private IIcon bottomIcon;

    public BlockWeaponStation(Material material, String name)
    {
        super(material, name);
        setBlockBounds(0,0,0,1,9 * (1/16f),1);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        topIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_top");
        bottomIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_bottom");
        blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            return topIcon;
        }
        else if (side == 0)
        {
            return bottomIcon;
        }
        else
        {
            return blockIcon;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityWeaponStation();
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
        if(!world.isRemote)
        {
            FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiWeaponStation, world, x, y, z);
        }

        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){return false;}

    @Override
    public int getRenderType()
    {
        return BlockRendererReplicator.renderID;
    }
}
