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

import cofh.api.block.IDismantleable;
import matteroverdrive.tile.pipes.TileEntityNetworkPipe;
import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;

import static matteroverdrive.util.MOBlockHelper.RotationType;

/**
 * Created by Simeon on 3/15/2015.
 */
public class BlockNetworkPipe extends BlockPipe implements IDismantleable
{

    public BlockNetworkPipe(Material material, String name)
    {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(9.0f);
        setRotationType(RotationType.PREVENT);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityNetworkPipe();
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops)
    {
        ArrayList<ItemStack> items = new ArrayList<>();
        IBlockState state = world.getBlockState(pos);

        if (!returnDrops)
        {
            state.getBlock().harvestBlock(world, player, pos,state,null);
            state.getBlock().removedByPlayer(world, pos,player, true);
        }
        else
        {
            state.getBlock().removedByPlayer(world,pos, player, true);
            state.getBlock().breakBlock(world, pos,state);
            for (ItemStack itemStack : getDrops(world, pos,state,0))
                MOInventoryHelper.insertItemStackIntoInventory(player.inventory, itemStack, EnumFacing.DOWN);
        }

        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, BlockPos pos)
    {
        return true;
    }
}
