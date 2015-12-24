package matteroverdrive.data.quest;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 12/24/2015.
 */
public class QuestItem
{
    ItemStack itemStack;
    int itemAmount;
    int itemDamage;
    String name;
    String mod;
    NBTTagCompound nbtTagCompound;

    public QuestItem(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public QuestItem(String name,String mod){this(name,mod,1,0,null);}

    public QuestItem(String name,String mod,int itemAmount){this(name,mod,itemAmount,0,null);}

    public QuestItem(String name,String mod,int itemAmount,int itemDamage){this(name,mod,itemAmount,itemDamage,null);}

    public QuestItem(String name,String mod,int itemAmount,int itemDamage,NBTTagCompound tagCompound)
    {
        this.name = name;
        this.mod = mod;
        this.itemAmount = itemAmount;
        this.itemDamage = itemDamage;
        this.nbtTagCompound = tagCompound;
    }

    public boolean isModded()
    {
        return mod != null && !mod.isEmpty();
    }

    public boolean isModPresent()
    {
        return Loader.isModLoaded(mod);
    }

    public boolean canItemExist()
    {
        if (isModded())
        {
            return isModPresent();
        }return true;
    }

    public ItemStack getItemStack()
    {
        if (isModded())
        {
            Item item = (Item) Item.itemRegistry.getObject(name);
            if (item != null)
            {
                ItemStack itemStack = new ItemStack(item,itemAmount,itemDamage);
                itemStack.setTagCompound(nbtTagCompound);
                return itemStack;
            }

        }else
        {
            return itemStack;
        }
        return null;
    }

    public static QuestItem fromItemStack(ItemStack itemStack)
    {
        return new QuestItem(itemStack);
    }
}
