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

import matteroverdrive.Reference;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/27/2015.
 */
public abstract class AbstractBioticStat implements IBionicStat
{
    int xp;
    String name;
    IBionicStat root;
    List<IBionicStat> competitors;
    List<ItemStack> reqiredItems;
    List<IBionicStat> enabledBlacklist;
    int maxLevel;
    boolean showOnHud;
    boolean showOnWheel;
    IIcon icon;

    public AbstractBioticStat(String name, int xp)
    {
        this.name = name;
        this.xp = xp;
        competitors = new ArrayList<>();
        reqiredItems = new ArrayList<>();
        enabledBlacklist = new ArrayList<>();
        maxLevel = 1;
    }

    @Override
    public String getUnlocalizedName() {
        return name;
    }

    @Override
    public String getDisplayName(AndroidPlayer androidPlayer,int level)
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
    public boolean canBeUnlocked(AndroidPlayer android,int level)
    {
        //if the root is not unlocked then this stat can't be unlocked
        if (root != null && !android.isUnlocked(root,root.maxLevel()))
        {
            return false;
        }
        //if any of the competitors are unlocked then this stat can't be unlocked
        if (competitors.size() > 0)
        {
            for (IBionicStat compeditor : competitors)
            {
                if (android.isUnlocked(compeditor,0))
                {
                    return false;
                }
            }
        }
        if (reqiredItems.size() > 0)
        {
            for (ItemStack item : reqiredItems)
            {

                if (!hasItem(android, item))
                {
                    return false;
                }
            }
        }
        if (android.isAndroid())
        {
            return android.getPlayer().capabilities.isCreativeMode || android.getPlayer().experienceLevel >= xp;
        }
        return false;
    }

    protected boolean hasItem(AndroidPlayer player,ItemStack stack)
    {
        int amountCount = stack.stackSize;
        for (int i = 0;i < player.getPlayer().inventory.getSizeInventory();i++)
        {
            ItemStack s = player.getPlayer().inventory.getStackInSlot(i);
            if (s != null && s.isItemEqual(stack))
            {
                amountCount-=s.stackSize;
            }
        }

        return amountCount <= 0;
    }

    @Override
    public void onUnlock(AndroidPlayer android, int level)
    {
        android.getPlayer().addExperienceLevel(-xp);
        consumeItems(android);
    }

    //consume all the necessary items from the player inventory
    //does not check if the items exist
    protected void consumeItems(AndroidPlayer androidPlayer)
    {
        for (ItemStack itemStack : reqiredItems)
        {
            int itemCount = itemStack.stackSize;
            for (int j = 0;j < androidPlayer.getPlayer().inventory.getSizeInventory();j++)
            {
                ItemStack pStack = androidPlayer.getPlayer().inventory.getStackInSlot(j);
                if (pStack != null && pStack.isItemEqual(itemStack))
                {
                    int countShouldTake = Math.min(itemCount,pStack.stackSize);
                    androidPlayer.getPlayer().inventory.decrStackSize(j,countShouldTake);
                    itemCount -= countShouldTake;
                }

                if (itemCount <= 0)
                    return;
            }
        }
    }

    @Override
    public void onTooltip(AndroidPlayer android, int level, List<String> list, int mouseX, int mouseY)
    {
        String name = getDisplayName(android, level);
        if (maxLevel() > 1)
        {
            name += String.format(" [%s/%s]",level,maxLevel());
        }
        list.add(EnumChatFormatting.WHITE + name);
        String details = getDetails(level);
        String[] detailsSplit = details.split("/n/");
        for (String detail : detailsSplit)
        {
            list.add(EnumChatFormatting.GRAY + detail);
        }

        String reqires = "";

        if (root != null)
        {
            reqires += EnumChatFormatting.GOLD+"[" + root.getDisplayName(android,0) + "]";
        }

        if (reqiredItems.size() > 0)
        {
            for (ItemStack itemStack : reqiredItems)
            {
                if (!reqires.isEmpty())
                {
                    reqires += EnumChatFormatting.GRAY + ", ";
                }
                if (itemStack.stackSize > 1)
                {
                    reqires += EnumChatFormatting.WHITE.toString() + itemStack.stackSize + "x";
                }

                reqires += EnumChatFormatting.WHITE + "[" + itemStack.getDisplayName() + "]";
            }
        }

        if (!reqires.isEmpty())
        {
           list.add(MOStringHelper.translateToLocal("gui.tooltip.requires") + ": " + reqires);
        }

        if (level <= maxLevel())
        {
            list.add((android.getPlayer().experienceLevel < xp ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + "XP: " + xp);
        }
    }

    public boolean checkBlacklistActive(AndroidPlayer androidPlayer,int level)
    {
        for (IBionicStat stat : enabledBlacklist)
        {
            if(stat.isActive(androidPlayer,level))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void registerIcons(TextureMap holoIcons)
    {
        icon = holoIcons.registerIcon(Reference.MOD_ID + ":" + "biotic_stat_" + name);
    }

    public void addReqiredItm(ItemStack stack)
    {
        reqiredItems.add(stack);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android,int level)
    {
        return showOnHud;
    }

    @Override
    public boolean showOnWheel(AndroidPlayer androidPlayer,int level)
    {
        return showOnWheel;
    }

    @Override
    public int maxLevel() {
        return maxLevel;
    }

    public IBionicStat getRoot()
    {
        return root;
    }

    public void setRoot(IBionicStat stat)
    {
        this.root = stat;
    }

    public void addCompetitor(IBionicStat stat)
    {
        this.competitors.add(stat);
    }

    public void removeCompetitor(IBionicStat competitor)
    {
        this.competitors.remove(competitor);
    }

    public List<IBionicStat> getCompetitors()
    {
        return competitors;
    }

    public void setMaxLevel(int maxLevel)
    {
        this.maxLevel = maxLevel;
    }

    public int getMaxLevel()
    {
        return maxLevel;
    }

    public void setShowOnHud(boolean showOnHud)
    {
        this.showOnHud = showOnHud;
    }

    public void setShowOnWheel(boolean showOnWheel){this.showOnWheel = showOnWheel;}


    public List<ItemStack> getRequiredItems()
    {
        return reqiredItems;
    }

    public List<IBionicStat> getEnabledBlacklist()
    {
        return enabledBlacklist;
    }

    public void addToEnabledBlacklist(IBionicStat stat)
    {
        enabledBlacklist.add(stat);
    }

    @Override
    public IIcon getIcon(int level)
    {
        return icon;
    }
}
