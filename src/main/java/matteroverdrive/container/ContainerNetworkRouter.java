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

package matteroverdrive.container;

import cofh.lib.util.helpers.InventoryHelper;
import matteroverdrive.tile.TileEntityMachineNetworkRouter;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/13/2015.
 */
public class ContainerNetworkRouter extends ContainerMachine<TileEntityMachineNetworkRouter>
{
    public ContainerNetworkRouter(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkRouter entity)
    {
        super(inventoryPlayer,entity);
    }

    @Override
    public  void init(InventoryPlayer inventory)
    {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    protected boolean putInPlayerInventory(ItemStack itemStack)
    {
        return InventoryHelper.mergeItemStack(inventorySlots, itemStack, machine.getSizeInventory() - 1, (inventoryItemStacks.size() - machine.getSizeInventory()), false, true);
    }
}
