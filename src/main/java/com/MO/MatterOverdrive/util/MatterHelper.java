package com.MO.MatterOverdrive.util;

import java.text.DecimalFormat;
import java.util.List;

import com.MO.MatterOverdrive.api.matter.*;
import com.MO.MatterOverdrive.handler.IMatterEntry;
import com.MO.MatterOverdrive.handler.MatterRegistry;

import com.MO.MatterOverdrive.items.MatterScanner;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MatterHelper 
{
	public static final String MATTER_UNIT = " kM";
	
	public static boolean containsMatter(ItemStack item)
	{
		if(getMatterAmountFromItem(item) > 0)
		{
			return true;
		}
		
		return false;
	}
	
	public static int getMatterAmountFromItem(ItemStack item)
	{
        if(item != null) {
            IMatterEntry matter = MatterRegistry.getEntry(item);
            if(matter != null)
                return matter.getMatter();
        }
		return 0;
	}

    public static int getEnergyFromMatter(int multiply,ItemStack itemStack)
    {
        int matter = getMatterAmountFromItem(itemStack);
        return multiply * matter;
    }

    public static int getTotalEnergyFromMatter(int multiply,ItemStack itemStack,int time)
    {
        int matter = getMatterAmountFromItem(itemStack);
        return multiply * matter * time;
    }



    public static int Transfer(ForgeDirection toDir,int amount,IMatterProvider from,IMatterReceiver to)
    {
        ForgeDirection oposite = MatterHelper.opposite(toDir);
        int extract = from.extractMatter(toDir,amount,true);
        int recived = to.receiveMatter(oposite, extract, false);
        from.extractMatter(toDir,recived,false);
        return  recived;
    }

    public static ForgeDirection opposite(ForgeDirection dir)
    {
        return ForgeDirection.values()[ForgeDirection.OPPOSITES[dir.ordinal()]];
    }
	
	private static IRecipe GetRecipeOf(ItemStack item)
	{
		List recipes = CraftingManager.getInstance().getRecipeList();
		for(int i = 0;i < recipes.size();i++)
		{
			IRecipe recipe = (IRecipe)recipes.get(i);
			
			if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == item.getItem())
            {
				return recipe;
			}
		}
		
		return null;
	}
	
	public static boolean isRegistered(ItemStack item)
	{
		return MatterRegistry.hasEntry(item);
	}
	
	public static boolean isMatterScanner(ItemStack item)
	{
        if(item != null && item.getItem() != null)
		    return item.getItem() instanceof MatterScanner;
        return false;
	}

    public static boolean isMatterPatternStorage(ItemStack item)
    {
        if(item != null && item.getItem() != null)
            return item.getItem() instanceof IMatterPatternStorage;
        return false;
    }

    public static boolean isUpgrade(ItemStack itemStack)
    {
        return false;
    }
	
	public static boolean CanScan(ItemStack stack)
	{
        if(MatterHelper.getMatterAmountFromItem(stack) <= 0)
            return false;

		Item item = stack.getItem();
		
		if(item instanceof ItemBlock)
		{
			Block block = Block.getBlockFromItem(item);
			
			if(block == Blocks.bedrock || block == Blocks.obsidian)
			{
				return false;
			}
		}
		
		return true;
	}

    public static String formatMatter(int matter)
    {
        return MOStringHelper.formatNUmber(matter) + MATTER_UNIT;
    }

    public static String formatMatter(int matter,int capacity)
    {
        return MOStringHelper.formatNUmber(matter) + " / " + MOStringHelper.formatNUmber(capacity) + MATTER_UNIT;
    }

    public static boolean DropInventory(World world,IInventory inventory,int x,int y,int z)
    {
        if(inventory != null)
        {
            for (int i1 = 0; i1 < inventory.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = inventory.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            return true;
        }

        return false;
    }

    public static void DrawMatterInfoTooltip(ItemStack itemStack,int speed,int energyPerTick,List tooltips)
    {
        int matter = MatterHelper.getMatterAmountFromItem(itemStack);
        if(matter > 0)
        {
            tooltips.add(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.BLUE.toString() + "Matter: " + MatterHelper.formatMatter(matter));
            tooltips.add(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.DARK_RED + "Power: " + MOEnergyHelper.formatEnergy(speed * matter * energyPerTick));
        }
    }
}
