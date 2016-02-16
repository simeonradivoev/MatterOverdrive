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

package matteroverdrive.multiblock;

import matteroverdrive.machines.MOTileEntityMachine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Simeon on 10/30/2015.
 */
public class MultiBlockTileStructureMachine extends MultiBlockTileStructureAbstract
{
    private HashSet<IMultiBlockTile> tiles = new HashSet<>();
    private MOTileEntityMachine machine;

    public MultiBlockTileStructureMachine(MOTileEntityMachine machine)
    {
        this.machine = machine;
    }

    @Override
    public boolean addMultiBlockTile(IMultiBlockTile tile)
    {
        if (tile != null && !containsMultiBlockTile(tile) && tile.canJoinMultiBlockStructure(this)) {
            tile.setMultiBlockTileStructure(this);
            return tiles.add(tile);
        }
        return false;
    }

    public void update()
    {
        Iterator<IMultiBlockTile> iterator = tiles.iterator();
        while (iterator.hasNext())
        {
            IMultiBlockTile tile = iterator.next();
            if (tile.isMultiblockInvalid())
            {
                tile.setMultiBlockTileStructure(null);
                iterator.remove();
            }
        }
    }

    public void invalidate()
    {
        Iterator<IMultiBlockTile> iterator = tiles.iterator();
        while (iterator.hasNext())
        {
            iterator.next().setMultiBlockTileStructure(null);
            iterator.remove();
        }
        machine = null;
    }

    @Override
    public void removeMultiBlockTile(IMultiBlockTile tile)
    {
        tiles.remove(tile);
        tile.setMultiBlockTileStructure(null);
    }

    @Override
    public boolean containsMultiBlockTile(IMultiBlockTile tile)
    {
        return tiles.contains(tile);
    }

    public MOTileEntityMachine getMachine()
    {
        return machine;
    }

    public Collection<IMultiBlockTile> getTiles()
    {
        return tiles;
    }

    public boolean isInvalid()
    {
        return machine == null || machine.isInvalid();
    }
}
