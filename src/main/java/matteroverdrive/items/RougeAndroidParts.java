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

package matteroverdrive.items;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by Simeon on 5/28/2015.
 */
public class RougeAndroidParts extends MOBaseItem implements IBionicPart
{
    String[] names = new String[]{"head","arms","legs","chest"};
    IIcon[] icons = new IIcon[names.length];

    public RougeAndroidParts(String name)
    {
        super(name);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0;i < names.length;i++)
        {
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + "rouge_android_" + names[i]);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, 3);
        return super.getUnlocalizedName() + "." + names[cofh.lib.util.helpers.MathHelper.clampI(i,0,names.length-1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0;i < names.length;i++)
        {
            list.add(new ItemStack(this,1,i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp_int(damage, 0, names.length-1);
        return this.icons[j];
    }

    @Override
    public int getType(ItemStack itemStack) {
        return itemStack.getItemDamage();
    }

    @Override
    public boolean affectAndroid(AndroidPlayer player, ItemStack itemStack) {
        return false;
    }

    @Override
    public Multimap getModifiers(AndroidPlayer player, ItemStack itemStack) {
        return null;
    }
}
