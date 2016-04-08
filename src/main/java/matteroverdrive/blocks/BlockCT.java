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

import matteroverdrive.blocks.includes.MOBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Simeon on 8/19/2015.
 */
public abstract class BlockCT extends MOBlock
{

    public BlockCT(Material material, String name) {
        super(material, name);

    }

/*    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        this.iconConnectedTexture = new IconConnectedTexture(this.blockIcon);
    }*/

/*    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        iconConnectedTexture.setType(0);
        return iconConnectedTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int sideId)
    {
        int type = 0;
        if (isSideCT(world, x, y, z, sideId)) {
            EnumFacing side = EnumFacing.getOrientation(sideId);
            if (side != EnumFacing.UP && side != EnumFacing.DOWN) {
                EnumFacing direction = EnumFacing.getOrientation(MOBlockHelper.getAboveSide(sideId));
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 0, true);
                direction = EnumFacing.getOrientation(MOBlockHelper.getBelowSide(sideId));
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 1, true);
                direction = EnumFacing.getOrientation(MOBlockHelper.getLeftSide(sideId));
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 2, true);
                direction = EnumFacing.getOrientation(MOBlockHelper.getRightSide(sideId));
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 3, true);
            }else
            {
                EnumFacing direction = EnumFacing.NORTH;
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 0, true);
                direction = EnumFacing.SOUTH;
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 1, true);
                direction = EnumFacing.WEST;
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 2, true);
                direction = EnumFacing.EAST;
                if (canConnect(world, world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ), x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                    type = MOMathHelper.setBoolean(type, 3, true);
            }
        }

        IconConnectedTexture icon = getIconConnectedTexture(world.getBlockMetadata(x, y, z), sideId);
        if (icon != null)
            icon.setType(type);
        return icon;
    }*/

    public abstract boolean canConnect(IBlockState state,IBlockAccess world, BlockPos pos, IBlockState blockState);

    public abstract boolean isSideCT(IBlockState state,IBlockAccess world, BlockPos pos, EnumFacing side);

}
