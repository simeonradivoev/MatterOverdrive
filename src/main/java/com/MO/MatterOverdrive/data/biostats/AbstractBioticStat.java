package com.MO.MatterOverdrive.data.biostats;

import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

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
    int maxLevel;
    boolean showOnHud;

    public AbstractBioticStat(String name, int xp)
    {
        this.name = name;
        this.xp = xp;
        competitors = new ArrayList<IBionicStat>();
        reqiredItems = new ArrayList<ItemStack>();
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
            for (int i = 0;i < competitors.size();i++)
            {
                if (android.isUnlocked(competitors.get(i),0))
                {
                    return false;
                }
            }
        }
        if (reqiredItems.size() > 0)
        {
            for (int i = 0;i < reqiredItems.size();i++)
            {

                if (!hasItem(android, reqiredItems.get(i)))
                {
                    return false;
                }
            }
        }
        if (android.isAndroid())
        {
            if (android.getPlayer().capabilities.isCreativeMode)
            {
                return true;
            }
            else
            {
                return android.getPlayer().experienceLevel >= xp;
            }
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
        for (int i = 0;i < reqiredItems.size();i++)
        {
            ItemStack itemStack = reqiredItems.get(i);
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
        if (maxLevel() > 0 && level > 0)
        {
            name += " x" + level;
        }
        list.add(EnumChatFormatting.WHITE + name);
        String details = getDetails(level);
        String[] detailsSplit = details.split("/n/");
        for (int i = 0;i < detailsSplit.length;i++)
        {
            list.add(EnumChatFormatting.GRAY + detailsSplit[i]);
        }

        if (root != null)
        {
            list.add("Requires: "+EnumChatFormatting.GOLD+"[" + root.getDisplayName(android,0) + "]");
        }

        if (level <= maxLevel())
        {
            list.add((android.getPlayer().experienceLevel < xp ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + "XP: " + xp);
        }
    }

    public void addReqiredItm(ItemStack stack)
    {
        reqiredItems.add(stack);
    }

    public boolean showOnHud(AndroidPlayer android,int level)
    {
        return showOnHud;
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

    public List<ItemStack> getRequiredItems()
    {
        return reqiredItems;
    }
}
