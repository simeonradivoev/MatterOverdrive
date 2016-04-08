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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.client.data.Color;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Battery extends MOItemEnergyContainer
{
	private final Color color;

	public Battery(String name, int capacity,Color color,int input,int output)
	{
		super(name, capacity,input,output);
		this.color = color;
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		ItemStack unpowered = new ItemStack(item);
		ItemStack powered = new ItemStack(item);
		setEnergyStored(powered, getMaxEnergyStored(powered));
		list.add(unpowered);
		list.add(powered);
	}

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack, player, infos);
        infos.add(ChatFormatting.GRAY + MOStringHelper.translateToLocal("gui.tooltip.energy.io") + ": " + maxReceive + "/" + maxExtract + MOEnergyHelper.ENERGY_UNIT + "/t");
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_)
	{
		this.itemIcon = p_94581_1_.registerIcon(Reference.MOD_ID + ":" + "battery");
		overlay = p_94581_1_.registerIcon(Reference.MOD_ID + ":" + "battery_overlay");
	}

	@Override
	public int getRenderPasses(int metadata)
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int damage, int pass)
	{
		if (pass == 1)
		{
			return overlay;
		}else
		{
			return itemIcon;
		}
	}*/

	// TODO: 3/26/2016 Find how to get color for Stack
	/*@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int pass)
	{
		if (pass == 1)
		{
			return color.getColor();
		}
		return super.getColorFromItemStack(itemStack,pass);
	}*/

	@Override
	public void setEnergyStored(ItemStack container,int amount)
	{
		super.setEnergyStored(container, amount);
	}
}
