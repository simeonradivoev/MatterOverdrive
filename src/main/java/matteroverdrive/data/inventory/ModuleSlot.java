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
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ModuleSlot extends Slot
{
    int type;
    WeaponSlot weaponSlot;

    public ModuleSlot(boolean isMainSlot, int type,WeaponSlot weaponSlot)
    {
        super(isMainSlot);
        this.type = type;
        this.weaponSlot = weaponSlot;
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return MOEnergyHelper.isEnergyContainerItem(item) && !WeaponHelper.isWeapon(item);
            default:
                if(WeaponHelper.isWeaponModule(item))
                {
                    if (((IWeaponModule)item.getItem()).getSlot(item) == type)
                    {
                        if (weaponSlot != null && weaponSlot.getItem() != null && weaponSlot.getItem().getItem() instanceof IWeapon)
                        {
                            return ((IWeapon) weaponSlot.getItem().getItem()).supportsModule(weaponSlot.getItem(),item);
                        }
                        return true;
                    }
                }
                return false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon()
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
