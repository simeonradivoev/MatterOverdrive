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

import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class BlockPatternMonitor extends BlockMonitor
{
	public BlockPatternMonitor(Material material, String name)
	{
		super(material, name);
		setHasGui(true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityMachinePatternMonitor();
	}
}
