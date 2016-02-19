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
import matteroverdrive.tile.TileEntityMachineNetworkRouter;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static matteroverdrive.util.MOBlockHelper.RotationType;

/**
 * Created by Simeon on 3/11/2015.
 */
public class BlockNetworkRouter extends MOBlockMachine
{

    public BlockNetworkRouter(Material material, String name)
    {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
        setRotationType(RotationType.PREVENT);
    }

  /*  @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
    {
        TileEntity entity = world.getTileEntity(x,y,z);
        if (entity instanceof TileEntityMachineNetworkRouter)
        {
            if (((TileEntityMachineNetworkRouter) entity).isActive())
            {
                return activeIcon;
            }
        }
        return blockIcon;
    }*/

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineNetworkRouter();
    }
}
