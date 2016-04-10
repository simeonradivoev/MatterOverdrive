package matteroverdrive.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Simeon on 1/9/2016.
 */
public class PortableDecomposer extends MOItemEnergyContainer
{
	private int defaultMatter;
	private float defaultMatterRatio;

	public PortableDecomposer(String name, int capacity, int chargeSpeed, int defaultMatter, float defaultMatterRatio)
	{
		super(name, capacity, chargeSpeed, 0);
		this.defaultMatter = defaultMatter;
		this.defaultMatterRatio = defaultMatterRatio;
	}

	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		super.addDetails(itemstack, player, infos);
		infos.add(String.format("%s/%s %s", DecimalFormat.getIntegerInstance().format(getMatter(itemstack)), getMaxMatter(itemstack), MatterHelper.MATTER_UNIT));
		if (itemstack.getTagCompound() != null)
		{
			ItemStack s;
			NBTTagList list = itemstack.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++)
			{
				s = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
				infos.add(ChatFormatting.GRAY + s.getDisplayName());
			}
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.getTileEntity(pos) instanceof IFluidHandler)
		{
			IFluidHandler fluidHandler = (IFluidHandler)worldIn.getTileEntity(pos);
			FluidStack fluidStack = new FluidStack(MatterOverdriveFluids.matterPlasma, getMatter(stack));
			int filled = fluidHandler.fill(facing, fluidStack, true);
			setMatter(stack, Math.max(0, fluidStack.amount - filled));
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	public int getMatter(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() != null)
		{
			return itemStack.getTagCompound().getInteger("Matter");
		}
		return 0;
	}

	public void setMatter(ItemStack itemStack, float matter)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.getTagCompound().setFloat("Matter", matter);
	}

	public float getMaxMatter(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("MaxMatter"))
		{
			return itemStack.getTagCompound().getFloat("MaxMatter");
		}
		return defaultMatter;
	}

	public boolean isStackListed(ItemStack decomposer, ItemStack itemStack)
	{
		if (decomposer.getTagCompound() != null && MatterHelper.containsMatter(itemStack))
		{
			NBTTagList stackList = decomposer.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < stackList.tagCount(); i++)
			{
				ItemStack s = ItemStack.loadItemStackFromNBT(stackList.getCompoundTagAt(i));
				if (s.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(s, itemStack))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void addStackToList(ItemStack decomposer, ItemStack itemStack)
	{
		if (decomposer.getTagCompound() == null)
		{
			decomposer.setTagCompound(new NBTTagCompound());
		}

		NBTTagList list = decomposer.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
		if (MatterHelper.containsMatter(itemStack))
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			itemStack.writeToNBT(tagCompound);
			list.appendTag(tagCompound);
		}
		decomposer.getTagCompound().setTag("Items", list);
	}

	public void decomposeItem(ItemStack decomposer, ItemStack itemStack)
	{
		if (MatterHelper.containsMatter(itemStack) && isStackListed(decomposer, itemStack))
		{
			float matterFromItem = MatterHelper.getMatterAmountFromItem(itemStack) * defaultMatterRatio;
			int energyForItem = MathHelper.ceiling_float_int(matterFromItem / defaultMatterRatio);
			float freeMatter = getMaxMatter(decomposer) - getMatter(decomposer);
			if (freeMatter > 0 && getEnergyStored(decomposer) > energyForItem)
			{
				int canTakeCount = (int)(freeMatter / matterFromItem);
				int itemsTaken = Math.min(canTakeCount, itemStack.stackSize);
				itemsTaken = Math.min(itemsTaken, getEnergyStored(decomposer) / energyForItem);
				setEnergyStored(decomposer, getEnergyStored(decomposer) - (itemsTaken * energyForItem));
				setMatter(decomposer, getMatter(decomposer) + itemsTaken * matterFromItem);
				itemStack.stackSize = Math.max(0, itemStack.stackSize - itemsTaken);
			}
		}
	}
}
