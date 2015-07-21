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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ContainerMatterAnalyzer extends ContainerMachine<TileEntityMachineMatterAnalyzer>
{
    private int lastAnalyzeTime;

    public ContainerMatterAnalyzer(InventoryPlayer inventory,TileEntityMachineMatterAnalyzer analyzer)
    {
        super(inventory, analyzer);
    }

    @Override
    public  void init(InventoryPlayer inventory)
    {
        this.addSlotToContainer(new SlotInventory(machine,machine.getInventoryContainer().getSlot(machine.input_slot),8,55));
        this.addSlotToContainer(new SlotInventory(machine,machine.getInventoryContainer().getSlot(machine.database_slot),8,82));
        this.addSlotToContainer(new SlotEnergy(machine,machine.getEnergySlotID(),8,109));

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.machine.analyzeTime);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(Object icrafting : this.crafters)
        {
            if(this.lastAnalyzeTime != this.machine.analyzeTime)
            {
                ((ICrafting)icrafting).sendProgressBarUpdate(this, 0, this.machine.analyzeTime);
            }

            this.lastAnalyzeTime = this.machine.analyzeTime;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.machine.analyzeTime = newValue;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
