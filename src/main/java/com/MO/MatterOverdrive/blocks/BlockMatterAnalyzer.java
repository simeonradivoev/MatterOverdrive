package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterAnalyzer;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/16/2015.
 */
public class BlockMatterAnalyzer extends MOBlockContainer
{

    private IIcon iconTop;
    private IIcon iconFront;

    public BlockMatterAnalyzer(Material material, String name)
    {
        super(material, name);

    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMachineMatterAnalyzer();
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        super.onBlockActivated(world,x,y,z,player,side,hitX,hitY,hitZ);
        if(!world.isRemote)
        {
            FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiMatterAnalyzer, world, x, y, z);
        }

        return true;
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        this.blockIcon = MatterOverdriveIcons.Base;
        this.iconFront = iconRegister.registerIcon(Reference.MOD_ID + ":" + "analyzer_front");
        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "analyzer_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if(side == 1)
        {
            return this.iconTop;
        }
        else if(side == metadata)
        {
            return this.iconFront;
        }
        else if(side == BlockHelper.getOppositeSide(metadata))
        {
            return MatterOverdriveIcons.Vent;
        }

        return this.blockIcon;
    }
}
