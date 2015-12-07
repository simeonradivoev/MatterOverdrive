/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.blocks.includes;

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.tile.MOTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Simeon on 3/24/2015.
 */
public class MOBlock extends Block
{
    private int rotationType;

    public MOBlock(Material material, String name)
    {
        super(material);
        this.setBlockName(name);
        this.setBlockTextureName(Reference.MOD_ID + ":" + name);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive);
        rotationType = BlockHelper.RotationType.FOUR_WAY;
    }

    public void register()
    {
        registerBlock();
    }

    protected void registerBlock()
    {
        GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x, y, z);
        if(tileEntity != null)
            tileEntity.onAdded(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x, y, z);
        if(tileEntity != null)
            tileEntity.onNeighborBlockChange();
    }

    protected void setDefaultRotation(World world, int x, int y, int z)
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
        int meta = BlockHelper.determineXZPlaceFacing(player);
        World.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        if (rotationType >= 0) {
            int meta = worldObj.getBlockMetadata(x, y, z);
            int rotation = meta;

            if (rotationType == BlockHelper.RotationType.FOUR_WAY) {
                rotation = BlockHelper.SIDE_LEFT[meta];
            } else if (rotationType == BlockHelper.RotationType.SIX_WAY) {
                if (meta < 6) {
                    rotation = (meta + 1) % 6;
                }
            }

            worldObj.setBlockMetadataWithNotify(x, y, z, rotation, 3);
            return true;
        }
        return false;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {

    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        if (hasTileEntity(meta) && world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof MOTileEntity)
        {
            ((MOTileEntity) world.getTileEntity(x, y, z)).onDestroyed();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public void setRotationType(int type)
    {
        rotationType = type;
    }
}
