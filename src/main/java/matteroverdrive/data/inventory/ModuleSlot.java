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

import cofh.lib.util.helpers.EnergyHelper;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ModuleSlot extends Slot
{
    int type;

    public ModuleSlot(boolean isMainSlot,int type)
    {
        super(isMainSlot);
        this.type = type;
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return EnergyHelper.isEnergyContainerItem(item);
            default:
                if(MatterHelper.isWeaponModule(item))
                {
                    if (((IWeaponModule)item.getItem()).getSlot(item) == type)
                    {
                        return true;
                    }
                }
                return false;
        }
    }

    @Override
    public IIcon getHoloIcon()
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return ClientProxy.holoIcons.getIcon("battery");
            case Reference.MODULE_COLOR:
                return ClientProxy.holoIcons.getIcon("color");
            case Reference.MODULE_BARREL:
                return ClientProxy.holoIcons.getIcon("barrel");
            case Reference.MODULE_SIGHTS:
                return ClientProxy.holoIcons.getIcon("sights");
            default:
                return ClientProxy.holoIcons.getIcon("module");
        }
    }

    @Override
    public int getMaxStackSize(){return 1;}
}
