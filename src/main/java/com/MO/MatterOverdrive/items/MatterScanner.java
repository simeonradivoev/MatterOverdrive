package com.MO.MatterOverdrive.items;

import java.util.ArrayList;
import java.util.List;

import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
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

public class MatterScanner extends MOBaseItem
{
	public static final String SELECTED_TAG_NAME = "lastScanIndex";
	public static final int PROGRESS_PER_ITEM = 10;
	public static final int SCAN_SPEED = 10;

	public MatterScanner(String name)
	{
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
		 IMatterDatabase database = getLink(player.worldObj, itemstack);
		 if(database != null)
		 {
			 NBTTagCompound lastItem = database.getItemAsNBT(getSelectedIndex(itemstack));

			 if(itemstack.hasTagCompound())
			 {
				 int x = itemstack.getTagCompound().getInteger("link_x");
				 int y = itemstack.getTagCompound().getInteger("link_y");
				 int z = itemstack.getTagCompound().getInteger("link_z");

				 infos.add(ChatFormatting.RED + "Linked at: " + x + "," + y + "," + z);
			 }

			 if (lastItem != null)
			 {
				 infos.add("Scan Progress: " + lastItem.getByte(MatterDatabaseHelper.PROGRESS_TAG_NAME) + " / " + 100);
				 infos.add("Current Block: " + MatterDatabaseHelper.GetItemStackFromNBT(lastItem).getDisplayName());
			 }
		 }
		 else
		 {
			 infos.add(ChatFormatting.RED + "Unlinked");
		 }
	 }

	public static IMatterDatabase getLink(World world,ItemStack scanner)
	{
		if(scanner.getItem() instanceof MatterScanner)
		{
			if(scanner.hasTagCompound())
			{
				if(scanner.getTagCompound().getBoolean("isLinked"))
				{
					int x = scanner.getTagCompound().getInteger("link_x");
					int y = scanner.getTagCompound().getInteger("link_y");
					int z = scanner.getTagCompound().getInteger("link_z");

					TileEntity e = world.getTileEntity(x, y, z);
					if (e instanceof IMatterDatabase)
						return (IMatterDatabase) e;
					else
						unLink(world,scanner);
				}
			}
		}
		return null;
	}

	public static void unLink(World world,ItemStack scanner)
	{
		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setBoolean("isLinked",false);
		}
	}

	public static void link(World world, int xCoord, int yCoord, int zCoord,ItemStack scanner)
	{
		if(scanner.getItem() instanceof MatterScanner)
		{
			((MatterScanner)scanner.getItem()).TagCompountCheck(scanner);
		}

		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setBoolean("isLinked",true);
			scanner.getTagCompound().setInteger("link_x", xCoord);
			scanner.getTagCompound().setInteger("link_y", yCoord);
			scanner.getTagCompound().setInteger("link_z", zCoord);
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
			return world.func_147480_a(x, y, z, false);
		}

		return false;
	}

	public static void setSelectedIndex(ItemStack scanner,String index)
	{
		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setString(SELECTED_TAG_NAME, index);
		}
	}

	public static String getSelectedIndex(ItemStack scanner)
	{
		if(scanner.hasTagCompound())
		{
			return scanner.getTagCompound().getString(SELECTED_TAG_NAME);
		}
		return null;
	}

	@Override
	public boolean onItemUse(ItemStack scanner, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        this.TagCompountCheck(scanner);

			IMatterDatabase database = getLink(world, scanner);
			ItemStack worldBlock = MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z);

			if (database != null)
			{
				if (database.hasItem(worldBlock))
				{
					String lastSelectedIndex = getSelectedIndex(scanner);
					String newSelectedIndex = worldBlock.getItem().getUnlocalizedName();
					if (!lastSelectedIndex.equalsIgnoreCase(newSelectedIndex))
					{
						//reset scan progress when targets changed
						resetScanProgress(scanner);
					}
					this.setSelectedIndex(scanner, newSelectedIndex);

					if (getScanProgress(scanner) < SCAN_SPEED)
					{
						//scanning
						System.out.println("Scanning... :" + this.getScanProgress(scanner));
						increaseScanProgress(scanner);
						SoundHandler.PlaySoundAt(world, "scanner_beep", player, 0.4f);
						return true;
					}
					else
					{
						//finished scanning
						resetScanProgress(scanner);

						if (database.increaseProgress(worldBlock, PROGRESS_PER_ITEM)) {
							//scan successful
							SoundHandler.PlaySoundAt(world, "scanner_scanning", player);
							HarvestBlock(scanner, player, world, x, y, z);
							//SoundHandler.PlaySoundAt(world, "scanner_success", player);
							return true;
						} else {
							//scan fail
							SoundHandler.PlaySoundAt(world, "scanner_fail", player);
							return false;
						}
					}
				} else
				{
					return database.addItem(worldBlock);
				}
			}

        return false;
    }

	
	@Override
	public void InitTagCompount(ItemStack stack)
	{
        MatterDatabaseHelper.InitTagCompound(stack);
	}

    public static void DisplayGuiScreen()
    {
        if(Minecraft.getMinecraft().theWorld.isRemote)
        {
            if(MatterHelper.isMatterScanner(Minecraft.getMinecraft().thePlayer.getHeldItem()))
            {
                //Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.getHeldItem(),Minecraft.getMinecraft().thePlayer.getHeldItem().));
               // return;
            }

            for (int i = 0; i < Minecraft.getMinecraft().thePlayer.inventory.getSizeInventory(); i++)
            {
                if (MatterHelper.isMatterScanner(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i)))
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i),i));
                    return;
                }
            }
        }
    }
}
