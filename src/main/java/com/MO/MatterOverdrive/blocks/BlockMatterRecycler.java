package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import com.MO.MatterOverdrive.client.render.block.MOBlockRenderer;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterRecycler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/15/2015.
 */
public class BlockMatterRecycler extends MOMatterEnergyStorageBlock {

    private IIcon iconTop;

    public BlockMatterRecycler(Material material, String name)
    {
        super(material, name,true,true);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
        setHasGui(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_top");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == BlockHelper.getAboveSide(meta))
        {
            return iconTop;
        }
        return MatterOverdriveIcons.Recycler;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineMatterRecycler();
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }
}
