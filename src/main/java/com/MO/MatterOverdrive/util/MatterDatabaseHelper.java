package com.MO.MatterOverdrive.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cofh.lib.util.helpers.MathHelper;
import scala.actors.threadpool.Arrays;
import scala.util.parsing.json.JSONArray;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.world.World;

public class MatterDatabaseHelper 
{
	public static final int MAX_ITEM_PROGRESS = 100;
	public static final String PROGRESS_TAG_NAME = "scan_progress";
	public static final String SELECTED_TAG_NAME = "lastScanIndex";
	public static final String ITEMS_TAG_NAME = "items";
	
	
	public void onDatabseChange()
	{
		
	}
	
	public static boolean Register(ItemStack scanner,ItemStack item,int progress)
	{
		if(!HasItemOrCantScan(scanner,item))
		{
			writeToNBT(scanner,item,progress);
			return true;
		}
		
		return false;
	}

    public static void InitTagCompound(ItemStack scanner)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        scanner.setTagCompound(tagCompound);
        InitItemListTagCompound(scanner);

    }
	
	public static void InitItemListTagCompound(ItemStack scanner)
	{
		NBTTagList items = new NBTTagList();
		scanner.setTagInfo(ITEMS_TAG_NAME, items);
	}
	
	public static boolean Register(ItemStack scanner,World world,int x,int y,int z,int progress)
	{
		ItemStack item = GetItemStackFromWorld(world,x,y,z);
		return Register(scanner,item,progress);
	}
	
	public static boolean Register(ItemStack scanner,Block block,int progress)
	{
		ItemStack item = new ItemStack(block);
		return Register(scanner,item,progress);
	}
	
	/** This is used to get the index of the item in the TagList, not the ID of the item
	 * @param database the database item to get the selected item index from
	 * @return the index of the seleced item, in the items tag list
	 *  */
	public static int GetSelectedIndex(ItemStack database)
	{
		if(database != null && database.hasTagCompound() && database.getTagCompound().hasKey(SELECTED_TAG_NAME,2))
			return database.getTagCompound().getShort(SELECTED_TAG_NAME);
		else
			return -1;
	}

    /** This is used to get the selected item
     * @param database the database item to get the selected item from
     * @return the selected item. Returns null if no item is selected
     *  */
    public static ItemStack GetSelectedItem(ItemStack database)
    {
        int index = MatterDatabaseHelper.GetSelectedIndex(database);
        if(index >= 0)
        {
            NBTTagCompound tag = MatterDatabaseHelper.GetItemAsNBTAt(database, index);
            return MatterDatabaseHelper.GetItemStackFromNBT(tag);
        }
        return null;
    }

    /** This is used to get the matter for the selected item in the database
     * return 0 if no items is selected
     * @param database the database item to get the selected item index from
     * @return the amount of matter for the selected item
     *  */
    public static int GetMatterFromSelected(ItemStack database)
    {
        return MatterHelper.getMatterAmountFromItem(GetSelectedItem(database));
    }
	
	/** This is used to set the index of the item in the TagList, not the ID of the item 
	 * @param database the database item to set the selected item index to
	 * */
	public static void SetSelectedIndex(ItemStack database,int id)
	{
		database.getTagCompound().setShort(SELECTED_TAG_NAME,(short)id);
	}

    /** This is used to check if item is in tagList and if so set it as the selected index
     * @param database the database item to set the selected item index to
     * */
    public static void SetSelectedItem(ItemStack database,ItemStack itemStack)
    {
        if(database.hasTagCompound())
        {
            NBTTagList itemList = database.getTagCompound().getTagList(ITEMS_TAG_NAME, 10);

            for(int i = 0;i < itemList.tagCount();i++)
            {
                if(areEqual(GetItemStackFromNBT(itemList.getCompoundTagAt(i)), itemStack))
                {
                    database.getTagCompound().setShort(SELECTED_TAG_NAME,(short)i);
                    return;
                }
            }
        }
    }
	
	public static Item GetItem(World world,int x,int y,int z)
	{
		return world.getBlock(x, y, z).getItem(world, x, y, z);
	}
	
	private static NBTTagCompound newNBTTagCompoundDatabase()
	{
		NBTTagCompound comp = new NBTTagCompound();
		return comp;
	}
	
	public static boolean HasItem(ItemStack scanner,ItemStack item)
	{
		return HasItem(scanner.getTagCompound(),Item.getIdFromItem(item.getItem()));
	}
	public static boolean HasItem(NBTTagCompound tagcompound, ItemStack item)
	{
		if(tagcompound != null)
		{
			NBTTagCompound items = tagcompound;
			
			if(items != null)
			{
				NBTTagList itemList = items.getTagList(ITEMS_TAG_NAME, 10);
				
				for(int i = 0;i < itemList.tagCount();i++)
				{
					if(areEqual(GetItemStackFromNBT(itemList.getCompoundTagAt(i)), item))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	public static boolean HasItem(ItemStack database, int id) 
	{
		return HasItem(database.getTagCompound(),id);
	}
	public static boolean HasItem(NBTTagCompound tagcompound, int id)
	{
		if(tagcompound != null)
		{
			NBTTagCompound items = tagcompound;
			
			if(items != null)
			{
				NBTTagList itemList = items.getTagList(ITEMS_TAG_NAME, 10);
				
				for(int i = 0;i < itemList.tagCount();i++)
				{
					if(itemList.getCompoundTagAt(i).getShort("id") == id)
						return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean HasItemOrCantScan(ItemStack scanner, ItemStack item)
	{
		int itemProgress = GetItemProgress(scanner,item);
		
		if(!MatterHelper.CanScan(item) || !MatterHelper.isRegistered(item))
			return true;
		
		if(itemProgress >= 0)
		{
			if(itemProgress < 100)
			{
				return false;
			}
			return true;
		}
		
		return false;
	}
	
	public static int IncreaseProgress(NBTTagCompound itemNBT,int amount)
	{
		int lastProgress = itemNBT.getByte(PROGRESS_TAG_NAME);
		int newProgress = MathHelper.clampI(lastProgress + amount, 0, 100);
		itemNBT.setByte(PROGRESS_TAG_NAME, (byte)newProgress);
		return newProgress;
	}
	
	public static int GetProgressFromNBT(NBTTagCompound itemNBT)
	{
		return itemNBT.getByte(PROGRESS_TAG_NAME);
	}
	
	public static int GetItemProgress(ItemStack scanner, ItemStack item)
	{
		NBTTagCompound tag = GetItemAsNBT(scanner,item);
		
		if(tag != null && tag.hasKey(PROGRESS_TAG_NAME))
		{
			return tag.getByte(PROGRESS_TAG_NAME);
		}
		
		return -1;
	}
	
	public static void writeToNBT(ItemStack scanner,ItemStack item,int progress) {
        if (!scanner.hasTagCompound()) {
            InitItemListTagCompound(scanner);
        }
        NBTTagCompound itemNBT = GetItemAsNBT(scanner, item);

        if (itemNBT != null) {
            int lastProgress = itemNBT.getByte(PROGRESS_TAG_NAME);
            int newProgress = MathHelper.clampI(lastProgress + progress, 0, 100);
            itemNBT.setByte(PROGRESS_TAG_NAME, (byte) newProgress);
            System.out.println("Item in list");
        } else {
            NBTTagList itemList = GetItemsTagList(scanner);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte(PROGRESS_TAG_NAME, (byte) progress);
            writeStackToNBT(item, nbttagcompound1);
            itemList.appendTag(nbttagcompound1);
        }
    }

    public static void writeStackToNBT(ItemStack itemStack,NBTTagCompound tagCompound)
    {
        if(itemStack != null && tagCompound != null)
        {
            itemStack.writeToNBT(tagCompound);
            tagCompound.setByte("Count",(byte)1);
        }
    }
	
	public static ItemStack GetItemWithInfo(ItemStack scanner, int id)
	{
		NBTTagCompound itemCompound = GetItemAsNBT(scanner,id);
		ItemStack item = GetItem(scanner,id);
		if(item != null && itemCompound != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByte(PROGRESS_TAG_NAME, itemCompound.getByte(PROGRESS_TAG_NAME));
			item.setTagCompound(tag);
			return item;
		}
		return null;
	}
	public static ItemStack GetItem(ItemStack scanner, ItemStack item)
	{
		return GetItemStackFromNBT(GetItemAsNBT(scanner,item));
	}
	public static ItemStack GetItem(ItemStack scanner, int id)
	{
		return GetItemStackFromNBT(GetItemAsNBT(scanner,id));
	}
	public static ItemStack GetItemStackFromNBT(NBTTagCompound comp)
	{
		if(comp != null)
			return ItemStack.loadItemStackFromNBT(comp);
		else 
			return null;
	}
	public static ItemStack GetItemAt(ItemStack scanner, int index) 
	{
		return GetItemStackFromNBT(GetItemAsNBTAt(scanner,index));
	}
	
	public static NBTTagCompound GetItemAsNBT(ItemStack scanner, ItemStack item)
	{
		if(scanner.hasTagCompound())
		{
				NBTTagList itemList = GetItemsTagList(scanner);
				
				for(int i = 0;i < itemList.tagCount();i++)
				{
					if(areEqual(item, GetItemStackFromNBT(itemList.getCompoundTagAt(i))))
						return GetItemAsNBTAt(scanner,i);
				}
		}
		
		return null;
	}
	public static NBTTagCompound GetItemAsNBT(ItemStack scanner, int id)
	{
		if(scanner.hasTagCompound())
		{
				NBTTagList itemList = GetItemsTagList(scanner);
				
				for(int i = 0;i < itemList.tagCount();i++)
				{
					if(itemList.getCompoundTagAt(i).getShort("id") == id)
						return GetItemAsNBTAt(scanner,i);
				}
		}
		
		return null;
	}
	
	public static NBTTagCompound GetItemAsNBTAt(ItemStack scanner, int index)
	{
		if(scanner.hasTagCompound())
		{
			NBTTagList itemList = GetItemsTagList(scanner);
			if(itemList.tagCount() > index)
				return itemList.getCompoundTagAt(index);
		}
		return null;
	}
	
	public static int GetIndexOfItem(ItemStack scanner, Block block)
	{
		ItemStack item = new ItemStack(block);
		return GetIndexOfItem(scanner,item);
	}
	
	public static int GetIndexOfItem(ItemStack scanner, World world,int x,int y,int z)
	{
		ItemStack item = GetItemStackFromWorld(world,x,y,z);
		return GetIndexOfItem(scanner,item);
	}
	
	public static int GetIndexOfItem(ItemStack scanner, ItemStack item)
	{
		if(scanner.hasTagCompound())
		{
				NBTTagList itemList = GetItemsTagList(scanner);
				System.out.println(itemList.tagCount());
				
				for(int i = 0;i < itemList.tagCount();i++)
				{
					if(areEqual(item, GetItemStackFromNBT(itemList.getCompoundTagAt(i))))
						return i;
				}
		}
		
		return -1;
	}
	
	private static boolean areEqual(ItemStack one,ItemStack two)
	{
        if(one != null && two != null)
        {
            if(one.getItem() == two.getItem())
            {
                if(one.getHasSubtypes() && two.getHasSubtypes())
                {
                    if(one.getItemDamage() == two.getItemDamage())
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
        }
		return false;
	}
	
	public static NBTTagList GetItemsTagList(ItemStack scanner)
	{
		if(scanner.hasTagCompound())
			return scanner.getTagCompound().getTagList(ITEMS_TAG_NAME, 10);
		return null;
	}
	
	public static ItemStack GetItemStackFromWorld(World world,int x, int y,int z)
	{
		Block b = world.getBlock(x, y, z);
		
		if(Item.getItemFromBlock(b).getHasSubtypes())
		{
			//Item bi = Item.getItemFromBlock(b);
			//List subBlocks = new ArrayList();
			//b.getSubBlocks(bi, null, subBlocks);
			int meta = world.getBlockMetadata(x, y, z);
			return new ItemStack(b,1,b.damageDropped(meta));
			
		}
		
		return new ItemStack(b);
	}
}