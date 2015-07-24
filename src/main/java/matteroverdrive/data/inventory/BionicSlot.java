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
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/26/2015.
 */
public class BionicSlot extends Slot
{
    int type;
    public static String[] names = {"head","arms","legs","chest","other","battery"};

    public ResourceLocation[] icons = new ResourceLocation[]{,};

    public BionicSlot(boolean isMainSlot,int type)
    {
        super(isMainSlot);
        this.type = type;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IBionicPart && ((IBionicPart) item.getItem()).getType(item) == type;
    }

    @Override
    public IIcon getHoloIcon()
    {
        if (type < names.length) {
            return ClientProxy.holoIcons.getIcon("android_slot_" + names[type]);
        }
        return null;
    }

    @Override
    public String getUnlocalizedTooltip(){
        if (type < names.length)
            return String.format("gui.tooltip.slot.bionic.%s",names[type]);
        return null;
    }
}
