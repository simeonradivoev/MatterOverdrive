package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.client.render.block.MOBlockRenderer;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterAnalyzer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/16/2015.
 */
public class BlockMatterAnalyzer extends MOBlockMachine
{
    public static float MACHINE_VOLUME;
    private IIcon iconTop;
    private IIcon iconFront;

    public BlockMatterAnalyzer(Material material, String name)
    {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineMatterAnalyzer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
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
            return MatterOverdriveIcons.Network_port_square;
        }
        else if (side == BlockHelper.getLeftSide(metadata) || side == BlockHelper.getRightSide(metadata))
        {
            return MatterOverdriveIcons.Vent2;
        }

        return MatterOverdriveIcons.Base;
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }
}
