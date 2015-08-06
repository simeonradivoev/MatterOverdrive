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
 * Created by Simeon on 8/5/2015.
 */
public class TeleportFlashDriveSlot extends Slot
{
    public TeleportFlashDriveSlot(boolean isMainSlot)
    {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return item != null && item.getItem() == MatterOverdriveItems.transportFlashDrive;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getHoloIcon()
    {
        return ClientProxy.holoIcons.getIcon("flash_drive");
    }

    @Override
    public String getUnlocalizedTooltip(){
        return "item.transport_flash_drive.name";
    }
}
