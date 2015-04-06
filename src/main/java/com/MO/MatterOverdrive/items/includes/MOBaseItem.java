package com.MO.MatterOverdrive.items.includes;

import com.MO.MatterOverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

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

	public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	{
		if(hasDetails(itemstack)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				addDetails(itemstack,player,infos);
			}
			else
			{
				infos.add(MOStringHelper.MORE_INFO);
			}
		}
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{

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

	public boolean hasDetails(ItemStack damage){return false;}
}
