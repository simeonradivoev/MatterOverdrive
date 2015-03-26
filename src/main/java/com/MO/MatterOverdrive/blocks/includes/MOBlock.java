package com.MO.MatterOverdrive.blocks.includes;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Simeon on 3/24/2015.
 */
public class MOBlock extends Block
{

    public MOBlock(Material material, String name)
    {
        super(material);
        this.setBlockName(name);
        this.setBlockTextureName(Reference.MOD_ID + ":" + name);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive);
    }

    public void Register()
    {
        RegisterBlock();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        MatterOverdriveIcons.init(iconRegister);
        super.registerBlockIcons(iconRegister);
    }

    protected void RegisterBlock()
    {
        GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        this.SetDefaultRotation(world, x, y, z);

        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x,y,z);
        if(tileEntity != null)
            tileEntity.onAdded();
    }

    private void SetDefaultRotation(World world, int x, int y, int z)
    {
        if (!world.isRemote)
        {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World World, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {
        World.setBlockMetadataWithNotify(x, y, z, BlockHelper.determineXZPlaceFacing(player), 2);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {

    }
}
