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

package matteroverdrive.items.android;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 9/10/2015.
 */
public abstract class BionicPart extends MOBaseItem implements IBionicPart
{
	public BionicPart(String name)
	{
		super(name);
		this.setCreativeTab(MatterOverdrive.tabMatterOverdrive_androidParts);
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		super.addDetails(itemstack, player, infos);
		Multimap<String, AttributeModifier> multimap = getModifiers(MOPlayerCapabilityProvider.GetAndroidCapability(player), itemstack);
		if (multimap != null)
		{
			multimap.values().stream()
					.forEach(modifier -> {
						switch (modifier.getOperation())
						{
							case 0:
								infos.add(ChatFormatting.GREEN + String.format("%s: +%s", modifier.getName(), modifier.getAmount()));
								break;
							case 1:
								infos.add(ChatFormatting.GREEN + String.format("%s: %s", modifier.getName(), (modifier.getAmount() >= 0 ? "+" : "") + DecimalFormat.getPercentInstance().format(modifier.getAmount())));
								break;
							default:
								infos.add(ChatFormatting.GREEN + String.format("%s: %s", modifier.getName(), DecimalFormat.getPercentInstance().format(modifier.getAmount() + 1)));
						}
					});
		}
	}

	public Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack)
	{
		Multimap multimap = HashMultimap.create();
		loadCustomAttributes(itemStack, multimap);
		return multimap;
	}

	private void loadCustomAttributes(ItemStack itemStack, Multimap<String, AttributeModifier> multimap)
	{
		if (itemStack.getTagCompound() != null)
		{
			NBTTagList attributeList = itemStack.getTagCompound().getTagList("CustomAttributes", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < attributeList.tagCount(); i++)
			{
				NBTTagCompound tagCompound = attributeList.getCompoundTagAt(i);
				String attributeName = tagCompound.getString("Name");
				double amount = tagCompound.getDouble("Amount");
				int operation = tagCompound.getByte("Operation");
				multimap.put(attributeName, new AttributeModifier(UUID.fromString(tagCompound.getString("UUID")), MOStringHelper.translateToLocal("attribute.name." + attributeName), amount, operation));
			}
		}
	}

	public boolean hasDetails(ItemStack stack)
	{
		return true;
	}
}
