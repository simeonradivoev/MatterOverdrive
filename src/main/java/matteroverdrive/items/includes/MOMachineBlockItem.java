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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MOMachineBlockItem extends ItemBlock
{
	public MOMachineBlockItem(Block block)
	{
		super(block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List infos, boolean p_77624_4_)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			if (MOStringHelper.hasTranslation(getUnlocalizedName() + ".details"))
			{
				infos.add(TextFormatting.GRAY + MOStringHelper.translateToLocal(getUnlocalizedName() + ".details"));
			}

			if (stack.hasTagCompound())
			{
				if (stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy"))
				{
					infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(stack.getTagCompound().getInteger("Energy"), stack.getTagCompound().getInteger("MaxEnergy")));
					if (stack.getTagCompound().hasKey("PowerSend") && stack.getTagCompound().hasKey("PowerReceive"))
					{
						infos.add("Send/Receive: " + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("PowerSend")) + "/" + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("PowerReceive")) + MOEnergyHelper.ENERGY_UNIT + "/t");
					}
				}
				if (stack.getTagCompound().hasKey("Matter") && stack.getTagCompound().hasKey("MaxMatter"))
				{
					infos.add(TextFormatting.BLUE + MatterHelper.formatMatter(stack.getTagCompound().getInteger("Matter"), stack.getTagCompound().getInteger("MaxMatter")));

					if (stack.getTagCompound().hasKey("MatterSend") && stack.getTagCompound().hasKey("MatterReceive"))
					{
						infos.add(TextFormatting.DARK_BLUE + "Send/Receive: " + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("MatterSend")) + "/" + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("MatterReceive")) + MatterHelper.MATTER_UNIT + "/t");
					}
				}

				showItems(stack, player, infos);
			}
		}
		else
		{
			infos.add(MOStringHelper.MORE_INFO);
		}


	}

	public String getItemStackDisplayName(ItemStack itemStack)
	{
		if (itemStack.hasTagCompound())
		{
			return super.getItemStackDisplayName(itemStack) + String.format(TextFormatting.AQUA + " [%s]" + TextFormatting.RESET, MOStringHelper.translateToLocal("item.info.configured"));
		}
		else
		{
			return super.getItemStackDisplayName(itemStack);
		}
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			return stack.getTagCompound().getInteger("MaxEnergy") - stack.getTagCompound().getInteger("Energy") + 1;
		}
		return 0;
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			return stack.getTagCompound().getInteger("MaxEnergy");
		}
		return 0;
	}

	private void showItems(ItemStack itemStack, EntityPlayer player, List infos)
	{
		NBTTagList stackTagList = itemStack.getTagCompound().getCompoundTag("Machine").getTagList("Items", Constants.NBT.TAG_COMPOUND);

		if (stackTagList.tagCount() > 0)
		{
			infos.add("");
			infos.add(TextFormatting.YELLOW + "Inventory:");
			for (int i = 0; i < stackTagList.tagCount(); i++)
			{
				ItemStack stack = ItemStack.loadItemStackFromNBT(stackTagList.getCompoundTagAt(i));
				if (stack.getItem() instanceof IUpgrade)
				{
					infos.add("   " + TextFormatting.GREEN + stack.getDisplayName());
				}
				else
				{
					infos.add("   " + infos.add(stack.getDisplayName()));
				}
			}
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getDamage(stack) > 0;
	}
}
