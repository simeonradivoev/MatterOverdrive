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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 11/22/2015.
 */
public abstract class BlockMonitor extends MOBlockMachine
{
    public BlockMonitor(Material material, String name)
    {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        lightValue = 10;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            MatterOverdriveIcons.Monitor_back.setType(0);
            return MatterOverdriveIcons.Monitor_back;
        }
        else if (side == MOBlockHelper.getOppositeSide(meta))
        {
            return MatterOverdriveIcons.Network_port_square;
        }
        return MatterOverdriveIcons.Base;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection direction = ForgeDirection.getOrientation(meta);
        float pixel = 1f / 16f;

        if (direction == ForgeDirection.EAST)
        {
            this.setBlockBounds(0, 0, 0, 5 * pixel, 1, 1);
        }
        else if (direction == ForgeDirection.WEST)
        {
            this.setBlockBounds(1 - 5 * pixel, 0, 0, 1, 1, 1);
        }
        else if (direction == ForgeDirection.SOUTH)
        {
            this.setBlockBounds(0, 0, 0, 1, 1, 5 * pixel);
        }
        else if (direction == ForgeDirection.NORTH)
        {
            this.setBlockBounds(0, 0, 1 - 5 * pixel, 1, 1, 1);
        }
        else
        {
            this.setBlockBounds(0, 0, 0, 1, 1, 5 * pixel);
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0, 0, 0, 1, 1, 5 * (1f / 16f));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vector1, Vec3 vector2)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, vector1, vector2);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }
}
