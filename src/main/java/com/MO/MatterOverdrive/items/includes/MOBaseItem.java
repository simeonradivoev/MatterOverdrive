package com.MO.MatterOverdrive.items.includes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;

import cpw.mods.fml.common.registry.GameRegistry;

public class MOBaseItem extends Item
{
	private String description;

	public MOBaseItem(String name)
	{
		this.init(name);
	}
	
	protected void init(String name)
	{
		this.setUnlocalizedName(name);
		this.setCreativeTab(MatterOverdrive.tabMatterOverdrive);
		this.setTextureName(Reference.MOD_ID + ":" + name);
	}
	
	public void Register(String name)
	{
		GameRegistry.registerItem(this, name);
	}
	
	public void Register()
	{
		this.Register(this.getUnlocalizedName().substring(5));
	}
	
	public void InitTagCompount(ItemStack stack)
	{
		stack.setTagCompound(new NBTTagCompound());
	}
	
	public void TagCompountCheck(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			InitTagCompount(stack);
		}
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
}
