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

package matteroverdrive.items.food;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by Simeon on 7/12/2015.
 */
public class AndroidPill extends ItemFood
{
    IIcon bluePillIcon;

    public AndroidPill(String name)
    {
        super(0, 0, false);
        setUnlocalizedName(name);
        setTextureName(Reference.MOD_ID + ":" + name);
        setAlwaysEdible();
        hasSubtypes = true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            infos.add(EnumChatFormatting.GRAY + MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details"));
        }
        else
        {
            infos.add(MOStringHelper.MORE_INFO);
        }
    }

    public void addToDunguns()
    {
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1),1,1,1));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() == 1)
        {
            return getUnlocalizedName() + "_blue";
        }
        return getUnlocalizedName() + "_red";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "red_pill");
        bluePillIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "blue_pill");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        if (damage == 1)
        {
            return bluePillIcon;
        }
        return this.itemIcon;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(player);
        if (itemStack.getItemDamage() == 1)
        {
            if (!androidPlayer.isTurning() && androidPlayer.isAndroid())
            {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            }
        }else
        {
            if (!androidPlayer.isAndroid() && !androidPlayer.isTurning())
            {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            }
        }
        return itemStack;
    }

    public void register()
    {
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
        GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(player);
        if (itemStack.getItemDamage() == 0)
        {
            androidPlayer.startConversion();
        }else
        {
            androidPlayer.setAndroid(false);
        }
    }
}
