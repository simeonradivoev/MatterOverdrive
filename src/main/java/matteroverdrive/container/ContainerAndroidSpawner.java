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

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import matteroverdrive.tile.TileEntityAndroidSpawner;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 12/11/2015.
 */
public class ContainerAndroidSpawner extends ContainerMachine<TileEntityAndroidSpawner>
{
    private int spawnedAndroids;

    public ContainerAndroidSpawner(InventoryPlayer playerInventory, TileEntityAndroidSpawner machine)
    {
        super(playerInventory, machine);
    }

    @Override
    protected void init(InventoryPlayer inventory)
    {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }

    @Override
    public void onCraftGuiOpened(ICrafting icrafting)
    {
        super.onCraftGuiOpened(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, spawnedAndroids);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.spawnedAndroids != this.machine.getSpawnedCount()) {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.getMaxSpawnCount());
            }
        }

        this.spawnedAndroids = this.machine.getSpawnedCount();
    }

    @Override
    public boolean enchantItem(EntityPlayer entityPlayer, int action)
    {
        if (action == 0)
        {
            machine.removeAllAndroids();
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue)
    {
        if(slot == 0)
            spawnedAndroids = newValue;
    }

    public int getSpawnedCount()
    {
        return spawnedAndroids;
    }
}
