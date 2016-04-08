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

package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Simeon on 11/5/2015.
 */
public class BlockTritaniumCrate extends MOBlockMachine
{
    public BlockTritaniumCrate(Material material, String name) {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        // TODO: 3/26/2016 Find how to set block bounds
        //setBlockBounds(0, 0, 0, 1, 12 * (1 / 16f), 1);
        this.setHarvestLevel("pickaxe", 2);
        setHasRotation();
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityTritaniumCrate();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity entity = worldIn.getTileEntity(pos);
            if (entity instanceof TileEntityTritaniumCrate) {
                //FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance, GuiHandler.TRITANIUM_CRATE, world, x, y, z);
                worldIn.playSound(null,pos.getX(),pos.getY(),pos.getZ(), MatterOverdriveSounds.blocksCrateOpen, SoundCategory.BLOCKS,0.5f,1);
                playerIn.displayGUIChest(((TileEntityTritaniumCrate) entity).getInventory());
                return true;
            }
        }
        return false;
    }

    @Override
    protected Inventory getInventory(World world, BlockPos pos)
    {
        if (world.getTileEntity(pos) instanceof TileEntityTritaniumCrate) {
            TileEntityTritaniumCrate machine = (TileEntityTritaniumCrate) world.getTileEntity(pos);
            return machine.getInventory();
        }
        return null;
    }
}
