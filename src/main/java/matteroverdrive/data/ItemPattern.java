package matteroverdrive.data;

import io.netty.buffer.ByteBuf;
import matteroverdrive.util.MatterDatabaseHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 12/27/2015.
 */
public class ItemPattern
{
    private int itemID;
    private int damage;
    private int count;
    private int progress;

    public ItemPattern()
    {

    }
    public ItemPattern(ItemStack itemStack)
    {
        this(itemStack,0);
    }
    public ItemPattern(ItemStack itemStack,int progress)
    {
        this(Item.getIdFromItem(itemStack.getItem()),itemStack.getItemDamage(),progress);
    }
    public ItemPattern(int itemID)
    {
        this(itemID,0,0);
    }
    public ItemPattern(int itemID,int damage)
    {
        this(itemID,damage,0);
    }
    public ItemPattern(int itemID,int damage,int progress)
    {
        this.itemID = itemID;
        this.damage = damage;
        this.progress = progress;
    }

    public ItemPattern(NBTTagCompound tagCompound)
    {
        readFromNBT(tagCompound);
    }

    public ItemPattern(ByteBuf byteBuf)
    {
        readFromBuffer(byteBuf);
    }

    public ItemStack toItemStack(boolean withInfo)
    {
        ItemStack itemStack = new ItemStack(Item.getItemById(itemID));
        itemStack.setItemDamage(damage);
        if (withInfo)
        {
            itemStack.setTagCompound(new NBTTagCompound());
            itemStack.getTagCompound().setByte(MatterDatabaseHelper.PROGRESS_TAG_NAME,(byte) progress);
        }
        return itemStack;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setShort("id",(short)itemID);
        nbtTagCompound.setByte(MatterDatabaseHelper.PROGRESS_TAG_NAME,(byte) progress);
        nbtTagCompound.setShort("Damage",(short)damage);
        nbtTagCompound.setByte("Count",(byte) count);
    }

    public void writeToBuffer(ByteBuf byteBuf)
    {
        byteBuf.writeShort(itemID);
        byteBuf.writeByte(progress);
        byteBuf.writeShort(damage);
        byteBuf.writeByte(count);
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        itemID = nbtTagCompound.getShort("id");
        progress = nbtTagCompound.getByte(MatterDatabaseHelper.PROGRESS_TAG_NAME);
        damage = nbtTagCompound.getShort("Damage");
        damage = nbtTagCompound.getByte("Count");
    }

    public void readFromBuffer(ByteBuf byteBuf)
    {
        itemID = byteBuf.readShort();
        progress = byteBuf.readByte();
        damage = byteBuf.readShort();
        count = byteBuf.readByte();
    }

    public int getItemID(){return itemID;}
    public int getProgress(){return progress;}
    public float getProgressF(){return (float)progress/(float)MatterDatabaseHelper.MAX_ITEM_PROGRESS;}
    public int getCount(){return count;}
    public int getDamage(){return damage;}
    public void setDamage(int damage){this.damage = damage;}
    public void setCount(int count){this.count = count;}
    public Item getItem(){return Item.getItemById(getItemID());}
    public boolean equals(ItemPattern pattern)
    {
        return this.getItemID() == pattern.getItemID() && this.getDamage() == pattern.getDamage();
    }
    @Override
    public boolean equals(Object object)
    {
        if (super.equals(object))
            return true;
        if (object instanceof ItemPattern)
        {
            return equals((ItemPattern)object);
        }
        return false;
    }
    public String getDisplayName()
    {
        return toItemStack(false).getDisplayName();
    }
    public ItemPattern copy()
    {
        ItemPattern pattern = new ItemPattern(itemID,damage,progress);
        pattern.setCount(count);
        return pattern;
    }
}
