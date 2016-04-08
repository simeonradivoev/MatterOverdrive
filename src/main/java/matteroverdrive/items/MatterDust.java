package matteroverdrive.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.api.matter.IMatterItem;
import matteroverdrive.api.matter.IRecyclable;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MatterDust extends MOBaseItem implements IRecyclable, IMatterItem
{
    final boolean isRefined;

	public MatterDust(String name,boolean refined)
	{
		super(name);
		isRefined = refined;
	}

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack,player,infos);
        if (!isRefined) {
            infos.add(ChatFormatting.BLUE + "Potential Matter: " + MatterHelper.formatMatter(itemstack.getItemDamage()));
        }
    }

	public int getDamage(ItemStack stack)
	{
		TagCompountCheck(stack);
		return stack.getTagCompound().getInteger("Matter");
	}

    public void setMatter(ItemStack itemStack,int matter)
    {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setInteger("Matter",matter);
    }

	@Override
	public boolean hasDetails(ItemStack itemStack)
	{
		return !isRefined;
	}

    @Override
    public ItemStack getOutput(ItemStack from)
    {
        ItemStack newItemStack = new ItemStack(MatterOverdriveItems.matter_dust_refined);
        MatterOverdriveItems.matter_dust_refined.setMatter(newItemStack,from.getItemDamage());
        return newItemStack;
    }

    @Override
    public int getRecycleMatter(ItemStack stack)
    {
        return stack.getItemDamage();
    }

    @Override
    public boolean canRecycle(ItemStack stack)
    {
        if (stack.getItem() instanceof MatterDust)
        {
            return !((MatterDust) stack.getItem()).isRefined;
        }
        return false;
    }

    @Override
    public int getMatter(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof MatterDust && ((MatterDust) itemStack.getItem()).isRefined)
        {
            return itemStack.getItemDamage();
        }
        return 0;
    }
}
