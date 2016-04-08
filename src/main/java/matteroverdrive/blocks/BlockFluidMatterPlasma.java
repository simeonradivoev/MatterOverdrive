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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Simeon on 8/20/2015.
 */
public class BlockFluidMatterPlasma extends BlockFluidClassic
{
    public BlockFluidMatterPlasma(Fluid fluid, Material material) {
        super(fluid, material);
    }

    /*@Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? this.getFluid().getStillIcon() : this.getFluid().getFlowingIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register)
    {

    }*/

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return !state.getBlock().getMaterial(state).isLiquid() && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return !state.getBlock().getMaterial(state).isLiquid() && super.displaceIfPossible(world, pos);
    }
}
