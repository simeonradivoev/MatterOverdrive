package matteroverdrive.items.includes;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MOBaseItem extends Item
{
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
		if(hasDetails(itemstack))
		{
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
		if (MOStringHelper.hasTranslation(getUnlocalizedName(itemstack) + ".details"))
		{
			infos.add(EnumChatFormatting.GRAY + MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details"));
		}
	}
	
	public void register(String name)
	{
		GameRegistry.registerItem(this, name);
	}
	
	public void register()
	{
		this.register(this.getUnlocalizedName().substring(5));
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

	public boolean hasDetails(ItemStack stack){return false;}
}
