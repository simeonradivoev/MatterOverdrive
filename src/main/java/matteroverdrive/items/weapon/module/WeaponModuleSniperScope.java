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

package matteroverdrive.items.weapon.module;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 12/8/2015.
 */
public class WeaponModuleSniperScope extends MOBaseItem implements IWeaponScope
{
    public WeaponModuleSniperScope(String name)
    {
        super(name);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_modules);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    @Override
    public float getZoomAmount(ItemStack scopeStack,ItemStack weapon)
    {
        return 0.85f;
    }

    @Override
    public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed,float originalAccuracy)
    {
        if (zoomed)
        {
            return originalAccuracy * 0.4f;
        }
        return originalAccuracy + 3f;
    }

    @Override
    public int getSlot(ItemStack module) {
        return Reference.MODULE_SIGHTS;
    }

    @Override
    public String getModelPath() {
        return Reference.PATH_MODEL_ITEMS + "sniper_scope.obj";
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return new ResourceLocation(Reference.PATH_ITEM + "sniper_scope_texture.png");
    }

    @Override
    public String getModelName(ItemStack module) {
        return "sniper_scope";
    }

    @Override
    public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon,float originalStat)
    {
        if (statID == Reference.WS_RANGE)
        {
            return originalStat * 1.5f;
        }
        return originalStat;
    }
}
