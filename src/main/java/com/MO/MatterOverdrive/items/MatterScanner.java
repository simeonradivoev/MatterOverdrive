package com.MO.MatterOverdrive.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.gui.GuiMatterScanner;
import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MatterScanner extends MOBaseItem implements IMatterDatabase
{
	public static final int PROGRESS_PER_ITEM = 10;
	public static final int SCAN_SPEED = 10;

	public MatterScanner(String name) {
		super(name);
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	 public int getDamage(ItemStack stack)
	 {
		 return getScanProgress(stack);
	 }

	 public int getMaxDamage()
	 {
		 return SCAN_SPEED+1;
	 }

	 public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	 {
		 NBTTagCompound lastItem = GetLastItemAsNBT(itemstack);

		 if(lastItem != null)
		 {
			 infos.add("Scan Progress: " + lastItem.getByte(MatterDatabaseHelper.PROGRESS_TAG_NAME) + " / " + 100);
			 infos.add("Current Block: " + MatterDatabaseHelper.GetItemStackFromNBT(lastItem).getDisplayName());
		 }
	 }


	public int getItemStackLimit(ItemStack item)
	{
		return 1;
	}


    public int getMaxItemUseDuration(ItemStack item)
    {
        return 72000;
    }

    public NBTTagCompound GetLastItemAsNBT(ItemStack item)
	{
		if(item.hasTagCompound())
		{
			int lastItemIndex = MatterDatabaseHelper.GetSelectedIndex(item);

			if(lastItemIndex >= 0)
				return MatterDatabaseHelper.GetItemAsNBTAt(item, lastItemIndex);
		}

		return null;
	}

	public ItemStack GetLastItem(ItemStack item)
	{
		if(item.hasTagCompound())
		{
            return MatterDatabaseHelper.GetItemAt(item, MatterDatabaseHelper.GetSelectedIndex(item));
		}

		return null;
	}

	public void SetLastItemIndex(ItemStack item,int id)
	{
		if(item.hasTagCompound())
		{
			MatterDatabaseHelper.SetSelectedIndex(item, id);
		}
	}

	private void increaseScanProgress(ItemStack item)
	{
		if(item.hasTagCompound())
		{
			int lastScanProgress = item.getTagCompound().getInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME);
			item.getTagCompound().setInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME, MathHelper.clampI(lastScanProgress + 1, 0, SCAN_SPEED));
		}
	}

	private int getScanProgress(ItemStack item)
	{
		if(item.hasTagCompound())
		{
			return item.getTagCompound().getInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME);
		}

		return 0;
	}

	private void resetScanProgress(ItemStack item)
	{
		if(item.hasTagCompound())
		{
			item.getTagCompound().setInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME,0);
		}
	}

	private boolean HarvestBlock(ItemStack scanner,EntityPlayer player,World world,int x,int y,int z)
	{
		if(!world.isRemote)
		{
			ItemStack item = MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z);
			if(!MatterDatabaseHelper.HasItemOrCantScan(scanner, item))
			{
				return world.func_147480_a(x, y, z, false);
			}
		}

		return false;
	}

	@Override
	public boolean onItemUse(ItemStack scanner, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        this.TagCompountCheck(scanner);
        System.out.println("Selected index is: " + MatterDatabaseHelper.GetSelectedIndex(scanner));

        NBTTagCompound itemAsNBT = GetLastItemAsNBT(scanner);
        ItemStack lastItem = GetLastItem(scanner);
        ItemStack worldBlock = MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z);

        if (lastItem != null && itemAsNBT != null)
        {
            if (!ItemStack.areItemStacksEqual(lastItem, worldBlock))
            {
                resetScanProgress(scanner);
                ChangeScanTarget(scanner, world, player, x, y, z);
            } else {
                if (MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) < MatterDatabaseHelper.MAX_ITEM_PROGRESS) {
                    if (getScanProgress(scanner) < SCAN_SPEED)
                    {
                        //scanner is scanning
                        System.out.println("Scanning... :" + this.getScanProgress(scanner));
                        increaseScanProgress(scanner);
                        SoundHandler.PlaySoundAt(world, "scanner_beep", player, 0.4f);
                    } else {
                        //scanner has finished scanning
                        resetScanProgress(scanner);
                        if (this.HarvestBlock(scanner, player, world, x, y, z)) {
                            if (MatterDatabaseHelper.IncreaseProgress(itemAsNBT, PROGRESS_PER_ITEM) <= 100 - PROGRESS_PER_ITEM) {
                                SoundHandler.PlaySoundAt(world, "scanner_scanning", player);
                            } else {
                                SoundHandler.PlaySoundAt(world, "scanner_success", player);
                            }
                        }
                    }

                    return true;
                } else {
                    //the item is fully reserched
                    return false;
                }
            }
        } else {
            ChangeScanTarget(scanner, world, player, x, y, z);
        }
        return false;
    }

	
	@Override
	public void InitTagCompount(ItemStack stack)
	{
        MatterDatabaseHelper.InitTagCompound(stack);
	}
	
	void ChangeScanTarget(ItemStack item, World world,EntityPlayer player, int x,int y, int z)
	{
		if(MatterDatabaseHelper.Register(item, world,x,y,z, 0))
		{
			//register new selected item
			System.out.println("new item registered. Item id is:" + Block.getIdFromBlock(world.getBlock(x, y, z)) + ":" + world.getBlockMetadata(x, y, z));
			SetLastItemIndex(item,MatterDatabaseHelper.GetIndexOfItem(item, world,x,y,z));
		}
		else
		{
			//failed to register new item
			//SoundHandler.PlaySoundAt(world, "scanner_fail", player);
			System.out.println("Could not register "+MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z).getUnlocalizedName()+". Item is unscannable");
		}
	}

    public static void DisplayGuiScreen()
    {
        if(Minecraft.getMinecraft().theWorld.isRemote)
        {
            if(MatterHelper.isDatabaseItem(Minecraft.getMinecraft().thePlayer.getHeldItem()))
            {
                //Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.getHeldItem(),Minecraft.getMinecraft().thePlayer.getHeldItem().));
               // return;
            }

            for (int i = 0; i < Minecraft.getMinecraft().thePlayer.inventory.getSizeInventory(); i++)
            {
                if (MatterHelper.isDatabaseItem(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i)))
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i),i));
                    return;
                }
            }
        }
    }

	@Override
	public ItemStack[] getItems(ItemStack database) 
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		NBTTagList itmemTagCompounds = getItemNBTCompounds(database);
		for(int i = 0;i < itmemTagCompounds.tagCount();i++)
		{
			ItemStack item = ItemStack.loadItemStackFromNBT(itmemTagCompounds.getCompoundTagAt(i));
			items.add(item);
		}
		
		return items.toArray(new ItemStack[items.size()]);
	}

	@Override
	public NBTTagList getItemNBTCompounds(ItemStack database) 
	{
		return MatterDatabaseHelper.GetItemsTagList(database);
	}

	@Override
	public boolean hasItem(ItemStack database,int id)
	{
		return MatterDatabaseHelper.HasItem(database, id);
	}

	@Override
	public ItemStack getItem(ItemStack database,int id)
	{
		return MatterDatabaseHelper.GetItem(database, id);
	}

	@Override
	public NBTTagCompound getItemAsNBT(ItemStack database, int id) {
		return MatterDatabaseHelper.GetItemAsNBT(database, id);
	}

	@Override
	public boolean hasItem(ItemStack database, ItemStack item) 
	{
		return MatterDatabaseHelper.HasItem(database, item);
	}

	@Override
	public ItemStack getItem(ItemStack database, ItemStack item) 
	{
		return MatterDatabaseHelper.GetItem(database, item);
	}
}
