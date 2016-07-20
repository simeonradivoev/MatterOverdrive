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

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockTransporter extends MOMatterEnergyStorageBlock<TileEntityMachineTransporter>
{

	public BlockTransporter(Material material, String name)
	{
		super(material, name, true, true);
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setHasGui(true);
	}

	/*@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":transporter_side");
		this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":transporter_top");
		this.iconFront = iconRegister.registerIcon(Reference.MOD_ID + ":transporter_front");
	}*/

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	/*@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
    {
    	if(side == 0 || side == 1)
    	{
    		return this.iconTop;
    	}
    	else if(side == metadata)
    	{
    		return this.iconFront;
    	}

    	return this.blockIcon;
    }*/

	@Override
	public Class<TileEntityMachineTransporter> getTileEntityClass()
	{
		return TileEntityMachineTransporter.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityMachineTransporter();
	}

}
