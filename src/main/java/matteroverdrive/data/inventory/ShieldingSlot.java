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

package matteroverdrive.data.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 4/6/2015.
 */
public class ShieldingSlot extends Slot
{
    public ShieldingSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        if(this.getItem() == null || this.getItem().stackSize < 4) {

            if (itemStack != null && itemStack.getItem() != null) {
                return itemStack.getItem() == MatterOverdriveItems.tritanium_plate;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getHoloIcon()
    {
        return ClientProxy.holoIcons.getIcon("shielding");
    }

    @Override
    public int getMaxStackSize(){return 5;}

    @Override
    public boolean keepOnDismatle(){return true;}

    @Override
    public String getUnlocalizedTooltip(){
        return "gui.tooltip.slot.shielding";
    }
}
