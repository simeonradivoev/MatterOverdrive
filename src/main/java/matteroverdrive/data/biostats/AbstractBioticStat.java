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

package matteroverdrive.data.biostats;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.api.android.BionicStatGuiInfo;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.client.render.HoloIcons;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/27/2015.
 */
public abstract class AbstractBioticStat implements IBioticStat
{
	protected String name;
	boolean showOnHud;
	boolean showOnWheel;
	@SideOnly(Side.CLIENT)
	HoloIcon icon;
	private int xp;
	private IBioticStat root;
	private BionicStatGuiInfo guiInfo;
	private boolean rootMaxLevel;
	private List<IBioticStat> competitors;
	private List<ItemStack> requiredItems;
	private List<IBioticStat> enabledBlacklist;
	private int maxLevel;

	public AbstractBioticStat(String name, int xp)
	{
		this.name = name;
		this.xp = xp;
		competitors = new ArrayList<>();
		requiredItems = new ArrayList<>();
		enabledBlacklist = new ArrayList<>();
		maxLevel = 1;
	}

	@Override
	public String getUnlocalizedName()
	{
		return name;
	}

	protected String getUnlocalizedDetails()
	{
		return "biotic_stat." + name + ".details";
	}

	@Override
	public String getDisplayName(AndroidPlayer androidPlayer, int level)
	{
		return MOStringHelper.translateToLocal("biotic_stat." + name + ".name");
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		return checkBlacklistActive(android, level);
	}

	public String getDetails(int level)
	{
		return MOStringHelper.translateToLocal("biotic_stat." + name + ".details");
	}

	@Override
	public boolean canBeUnlocked(AndroidPlayer android, int level)
	{
		//if the root is not unlocked then this stat can't be unlocked
		if (root != null && !android.isUnlocked(root, rootMaxLevel ? root.maxLevel() : 1))
		{
			return false;
		}
		if (isLocked(android, level))
		{
			return false;
		}
		if (requiredItems.size() > 0 && !android.getPlayer().capabilities.isCreativeMode)
		{
			for (ItemStack item : requiredItems)
			{

				if (!hasItem(android, item))
				{
					return false;
				}
			}
		}
		return android.isAndroid() && (android.getPlayer().capabilities.isCreativeMode || android.getPlayer().experienceLevel >= xp);
	}

	@Override
	public boolean isLocked(AndroidPlayer androidPlayer, int level)
	{
		return areCompeditrosUnlocked(androidPlayer);
	}

	protected boolean hasItem(AndroidPlayer player, ItemStack stack)
	{
		int amountCount = stack.stackSize;
		for (int i = 0; i < player.getPlayer().inventory.getSizeInventory(); i++)
		{
			ItemStack s = player.getPlayer().inventory.getStackInSlot(i);
			if (s != null && s.isItemEqual(stack))
			{
				amountCount -= s.stackSize;
			}
		}

		return amountCount <= 0;
	}

	@Override
	public void onUnlock(AndroidPlayer android, int level)
	{
		android.getPlayer().removeExperienceLevel(xp);
		consumeItems(android);
	}

	@Override
	public void onUnlearn(AndroidPlayer androidPlayer, int level)
	{

	}

	//consume all the necessary items from the player inventory
	//does not check if the items exist
	protected void consumeItems(AndroidPlayer androidPlayer)
	{
		for (ItemStack itemStack : requiredItems)
		{
			int itemCount = itemStack.stackSize;
			for (int j = 0; j < androidPlayer.getPlayer().inventory.getSizeInventory(); j++)
			{
				ItemStack pStack = androidPlayer.getPlayer().inventory.getStackInSlot(j);
				if (pStack != null && pStack.isItemEqual(itemStack))
				{
					int countShouldTake = Math.min(itemCount, pStack.stackSize);
					androidPlayer.getPlayer().inventory.decrStackSize(j, countShouldTake);
					itemCount -= countShouldTake;
				}

				if (itemCount <= 0)
				{
					return;
				}
			}
		}
	}

	@Override
	public void onTooltip(AndroidPlayer android, int level, List<String> list, int mouseX, int mouseY)
	{
		String name = ChatFormatting.BOLD + getDisplayName(android, level);
		if (maxLevel() > 1)
		{
			name += ChatFormatting.RESET + String.format(" [%s/%s]", level, maxLevel());
		}
		list.add(ChatFormatting.WHITE + name);
		String details = getDetails(level);
		String[] detailsSplit = details.split("/n/");
		for (String detail : detailsSplit)
		{
			list.add(ChatFormatting.GRAY + detail);
		}

		if (root != null)
		{
			String rootLevel = "";
			if (root.maxLevel() > 1)
			{
				if (rootMaxLevel)
				{
					rootLevel = " " + root.maxLevel();
				}
			}
			list.add(ChatFormatting.DARK_AQUA + MOStringHelper.translateToLocal("gui.tooltip.parent") + ": " + ChatFormatting.GOLD + String.format("[%s%s]", root.getDisplayName(android, 0), rootLevel));
		}

		String requires = "";
		if (requiredItems.size() > 0)
		{
			for (ItemStack itemStack : requiredItems)
			{
				if (!requires.isEmpty())
				{
					requires += ChatFormatting.GRAY + ", ";
				}
				if (itemStack.stackSize > 1)
				{
					requires += ChatFormatting.DARK_GREEN.toString() + itemStack.stackSize + "x";
				}

				requires += ChatFormatting.DARK_GREEN + "[" + itemStack.getDisplayName() + "]";
			}
		}

		if (!requires.isEmpty())
		{
			list.add(ChatFormatting.DARK_AQUA + MOStringHelper.translateToLocal("gui.tooltip.requires") + ": " + requires);
		}

		if (competitors.size() > 0)
		{
			String locks = ChatFormatting.RED + MOStringHelper.translateToLocal("gui.tooltip.locks") + ": ";
			for (IBioticStat compeditor : competitors)
			{
				locks += String.format("[%s] ", compeditor.getDisplayName(android, 0));
			}
			list.add(locks);
		}

		if (level < maxLevel())
		{
			list.add((android.getPlayer().experienceLevel < xp ? ChatFormatting.RED : ChatFormatting.GREEN) + "XP: " + xp);
		}
	}

	public boolean checkBlacklistActive(AndroidPlayer androidPlayer, int level)
	{
		for (IBioticStat stat : enabledBlacklist)
		{
			if (stat.isActive(androidPlayer, level))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public void registerIcons(TextureMap textureMap, HoloIcons holoIcons)
	{
		icon = holoIcons.registerIcon(textureMap, "biotic_stat_" + name, 18);
	}

	public void addReqiredItm(ItemStack stack)
	{
		requiredItems.add(stack);
	}

	@Override
	public boolean showOnHud(AndroidPlayer android, int level)
	{
		return showOnHud;
	}

	@Override
	public boolean showOnWheel(AndroidPlayer androidPlayer, int level)
	{
		return showOnWheel;
	}

	@Override
	public int maxLevel()
	{
		return maxLevel;
	}

	public IBioticStat getRoot()
	{
		return root;
	}

	public void setRoot(IBioticStat stat, boolean rootMaxLevel)
	{
		this.root = stat;
		this.rootMaxLevel = rootMaxLevel;
	}

	public void addCompetitor(IBioticStat stat)
	{
		this.competitors.add(stat);
	}

	public void removeCompetitor(IBioticStat competitor)
	{
		this.competitors.remove(competitor);
	}

	public List<IBioticStat> getCompetitors()
	{
		return competitors;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}

	public void setShowOnHud(boolean showOnHud)
	{
		this.showOnHud = showOnHud;
	}

	public void setShowOnWheel(boolean showOnWheel)
	{
		this.showOnWheel = showOnWheel;
	}

	public void setGuiInfo(BionicStatGuiInfo guiInfo)
	{
		this.guiInfo = guiInfo;
	}

	@Override
	public BionicStatGuiInfo getGuiInfo(AndroidPlayer androidPlayer, int level)
	{
		return guiInfo;
	}

	public List<ItemStack> getRequiredItems()
	{
		return requiredItems;
	}

	public List<IBioticStat> getEnabledBlacklist()
	{
		return enabledBlacklist;
	}

	public void addToEnabledBlacklist(IBioticStat stat)
	{
		enabledBlacklist.add(stat);
	}

	@Override
	public HoloIcon getIcon(int level)
	{
		return icon;
	}

	@Override
	public int getXP(AndroidPlayer androidPlayer, int level)
	{
		return xp;
	}

	public boolean areCompeditrosUnlocked(AndroidPlayer androidPlayer)
	{
		if (competitors.size() > 0)
		{
			for (IBioticStat competitor : competitors)
			{
				if (androidPlayer.isUnlocked(competitor, 0))
				{
					return true;
				}
			}
		}
		return false;
	}
}
