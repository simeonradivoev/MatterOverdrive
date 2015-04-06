package com.MO.MatterOverdrive.items.includes;

import java.util.List;

import com.MO.MatterOverdrive.util.MOEnergyHelper;

import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

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
		if(stack.hasTagCompound())
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				if (stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy")) {
					infos.add(EnumChatFormatting.YELLOW + MOEnergyHelper.formatEnergy(stack.getTagCompound().getInteger("Energy"), stack.getTagCompound().getInteger("MaxEnergy")));
					if (stack.getTagCompound().hasKey("PowerSend") && stack.getTagCompound().hasKey("PowerReceive")) {
						infos.add("Send/Receive: " + MOStringHelper.formatNUmber(stack.getTagCompound().getInteger("PowerSend")) + "/" + MOStringHelper.formatNUmber(stack.getTagCompound().getInteger("PowerReceive")) + MOEnergyHelper.ENERGY_UNIT + "/t");
					}
				}
				if (stack.getTagCompound().hasKey("Matter") && stack.getTagCompound().hasKey("MaxMatter")) {
					infos.add(EnumChatFormatting.BLUE + MatterHelper.formatMatter(stack.getTagCompound().getInteger("Matter"), stack.getTagCompound().getInteger("MaxMatter")));

					if (stack.getTagCompound().hasKey("MatterSend") && stack.getTagCompound().hasKey("MatterReceive")) {
						infos.add(EnumChatFormatting.DARK_BLUE + "Send/Receive: " + MOStringHelper.formatNUmber(stack.getTagCompound().getInteger("MatterSend")) + "/" + MOStringHelper.formatNUmber(stack.getTagCompound().getInteger("MatterReceive")) + MatterHelper.MATTER_UNIT + "/t");
					}
				}
			}else
			{
				infos.add(MOStringHelper.MORE_INFO);
			}
		}
		
	}
	
	@Override
	public int getDamage(ItemStack stack)
	 {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy"))
		{
			return stack.getTagCompound().getInteger("MaxEnergy") - stack.getTagCompound().getInteger("Energy") + 1;
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
