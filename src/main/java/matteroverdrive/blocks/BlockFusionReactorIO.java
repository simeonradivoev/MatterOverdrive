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
import matteroverdrive.tile.TileEntityFusionReactorPart;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 10/30/2015.
 */
public class BlockFusionReactorIO extends MOBlockMachine<TileEntityFusionReactorPart>
{
	public BlockFusionReactorIO(Material material, String name)
	{
		super(material, name);
		setHardness(30.0F);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public Class<TileEntityFusionReactorPart> getTileEntityClass()
	{
		return TileEntityFusionReactorPart.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityFusionReactorPart();
	}

    /*@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
    {
        return MatterOverdriveIcons.Network_port_square;
    }*/
}
