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

package matteroverdrive.items.includes;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MOBaseItem extends Item
{
	public MOBaseItem(String name)
	{
		this.init(name);
	}
	
	protected void init(String name)
	{
		this.setUnlocalizedName(name);
		this.setCreativeTab(MatterOverdrive.tabMatterOverdrive);
		this.setTextureName(Reference.MOD_ID + ":" + name);
	}

	public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	{
		if(hasDetails(itemstack))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				addDetails(itemstack,player,infos);
			}
			else
			{
				infos.add(MOStringHelper.MORE_INFO);
			}
		}
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		if (MOStringHelper.hasTranslation(getUnlocalizedName(itemstack) + ".details"))
		{
			String[] infoList = MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details").split("\n");
			for (String info : infoList)
			{
				infos.add(EnumChatFormatting.GRAY + info);
			}
		}
	}
	
	public void register(String name)
	{
		GameRegistry.registerItem(this, name);
	}
	
	public void register()
	{
		this.register(this.getUnlocalizedName().substring(5));
	}
	
	public void InitTagCompount(ItemStack stack)
	{
		stack.setTagCompound(new NBTTagCompound());
	}
	
	public void TagCompountCheck(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			InitTagCompount(stack);
		}
	}

	public boolean hasDetails(ItemStack stack){return false;}
}
