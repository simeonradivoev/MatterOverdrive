package com.MO.MatterOverdrive.items.includes;

import java.util.List;

import com.MO.MatterOverdrive.util.MOEnergyHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class MOEnergyMatterBlockItem extends ItemBlock
{

	public MOEnergyMatterBlockItem(Block p_i45328_1_) 
	{
		super(p_i45328_1_);
		// TODO Auto-generated constructor stub
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List infos, boolean p_77624_4_) 
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			infos.add(EnumChatFormatting.YELLOW + MOEnergyHelper.formatEnergy(stack.getTagCompound().getInteger("Energy"), stack.getTagCompound().getInteger("MaxEnergy")));
		}
		
	}
	
	@Override
	public int getDamage(ItemStack stack)
	 {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			return stack.getTagCompound().getInteger("MaxEnergy") - stack.getTagCompound().getInteger("Energy");
		}
		return 0;
	 }
	
	@Override
	public int getDisplayDamage(ItemStack stack)
    {
        return this.getDamage(stack);
    }
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			return stack.getTagCompound().getInteger("MaxEnergy");
		}
		return 0;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return getDamage(stack) > 0;
    }
}
