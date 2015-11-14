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

package matteroverdrive.client.render.item;

import matteroverdrive.Reference;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

/**
 * Created by Simeon on 11/8/2015.
 */
public abstract class WeaponItemRenderer implements IItemRenderer
{
    protected ResourceLocation weaponTexture;
    protected WavefrontObject weaponModel;

    public WeaponItemRenderer(WavefrontObject weaponModel,ResourceLocation weaponTexture)
    {
        this.weaponModel = weaponModel;
        this.weaponTexture = weaponTexture;
    }

    protected void renderBarrel(ItemStack weaponStack)
    {
        bindTexture(weaponTexture);
        ItemStack barrelStack = WeaponHelper.getModuleAtSlot(Reference.MODULE_BARREL,weaponStack);
        if (barrelStack != null)
        {
            GroupObject object = getModelPart(barrelStack.getUnlocalizedName().substring(5).replace('.','_'));
            if (object != null) {
                object.render();
                return;
            }
        }

        renderDefaultBarrel(weaponStack);
    }

    protected void renderDefaultBarrel(ItemStack weaponStack)
    {
        weaponModel.renderPart("weapon_module_barrel_none");
    }

    protected void bindTexture(ResourceLocation texture)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }

    protected GroupObject getModelPart(String part)
    {
        for (GroupObject object : weaponModel.groupObjects)
        {
            if (object.name.equalsIgnoreCase(part))
            {
                return object;
            }
        }
        return null;
    }
}
