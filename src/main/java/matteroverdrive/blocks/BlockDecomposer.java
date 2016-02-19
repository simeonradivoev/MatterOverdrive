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
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDecomposer extends MOMatterEnergyStorageBlock
{
	public BlockDecomposer(Material material, String name)
	{
		super(material, name, true, true);
        setHasRotation();
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
        setHasGui(true);
	}

    @Override
    public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos)
	 {
		 return true;
	 }

    @Override
	 public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	 {
		 return true;
	 }

	 @Override
		public TileEntity createNewTileEntity(World world, int meta)
		{
			return new TileEntityMachineDecomposer();
		}

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        super.onConfigChanged(config);
        config.initMachineCategory(getUnlocalizedName());
        TileEntityMachineDecomposer.MATTER_STORAGE = config.getMachineInt(getUnlocalizedName(), "storage.matter" ,1024, String.format("How much matter can the %s hold", getLocalizedName()));
        TileEntityMachineDecomposer.ENERGY_STORAGE = config.getMachineInt(getUnlocalizedName(),"storage.energy", 512000, String.format("How much energy can the %s hold", getLocalizedName()));
        TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER = config.getMachineInt(getUnlocalizedName(), "speed.decompose", 80, "The speed in ticks, of decomposing. (per matter)");
        TileEntityMachineDecomposer.DECOMPOSE_ENERGY_PER_MATTER = config.getMachineInt(getUnlocalizedName(), "cost.decompose", 6000, "Decomposing cost per matter");

    }

}
