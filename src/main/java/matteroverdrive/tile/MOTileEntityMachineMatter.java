/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.tile;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.MachineMatterStorage;
import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.client.PacketMatterUpdate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.EnumSet;

public abstract class MOTileEntityMachineMatter extends MOTileEntityMachineEnergy implements IMatterHandler, IWailaBodyProvider, IFluidHandler
{
	protected MachineMatterStorage matterStorage;

	public MOTileEntityMachineMatter(int upgradesCount)
	{
		super(upgradesCount);
		matterStorage = new MachineMatterStorage(this, 32768);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA) && getMatterStorage() != null)
		{
			getMatterStorage().writeToNBT(nbt);
		}

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA) && getMatterStorage() != null)
		{
			getMatterStorage().readFromNBT(nbt);
		}

	}

	@Override
	public int getMatterStored()
	{
		if (getMatterStorage() != null)
		{
			return this.getMatterStorage().getMatterStored();
		}
		return 0;
	}

	public void setMatterStored(int matter)
	{
		if (getMatterStorage() != null)
		{
			getMatterStorage().setMatterStored(matter);
		}
	}

	@Override
	public int getMatterCapacity()
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().getCapacity();
		}
		return 0;
	}

	@Override
	public int receiveMatter(EnumFacing side, int amount, boolean simulate)
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().receiveMatter(side, amount, simulate);
		}
		return 0;
	}

	@Override
	public int extractMatter(EnumFacing direction, int amount, boolean simulate)
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().extractMatter(direction, amount, simulate);
		}
		return 0;
	}

	protected int modifyEnergyStored(int amount)
	{
		int energyModified = energyStorage.modifyEnergyStored(amount);
		if (energyModified != 0)
		{
			UpdateClientPower();
		}
		return energyModified;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().drain(resource.amount, doDrain);
		}
		else
		{
			return null;
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		if (getMatterStorage() != null)
		{
			return getMatterStorage().drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return fluid instanceof FluidMatterPlasma;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid)
	{
		return fluid instanceof FluidMatterPlasma;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		if (getMatterStorage() != null)
		{
			return new FluidTankInfo[] {getMatterStorage().getInfo()};
		}
		return new FluidTankInfo[0];
	}

	public MachineMatterStorage getMatterStorage()
	{
		return this.matterStorage;
	}

	public void updateClientMatter()
	{
		if (worldObj != null)
		{
			MatterOverdrive.packetPipeline.sendToAllAround(new PacketMatterUpdate(this), this, 64);
		}
	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		super.readFromPlaceItem(itemStack);

		if (itemStack != null && getMatterStorage() != null)
		{
			if (itemStack.hasTagCompound())
			{
				getMatterStorage().readFromNBT(itemStack.getTagCompound());
			}
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		super.writeToDropItem(itemStack);

		if (itemStack != null && getMatterStorage() != null)
		{
			if (getMatterStorage().getMatterStored() > 0)
			{
				if (!itemStack.hasTagCompound())
				{
					itemStack.setTagCompound(new NBTTagCompound());
				}

				getMatterStorage().writeToNBT(itemStack.getTagCompound());
				itemStack.getTagCompound().setInteger("MaxMatter", matterStorage.getCapacity());
				itemStack.getTagCompound().setInteger("MatterSend", matterStorage.getMaxExtract());
				itemStack.getTagCompound().setInteger("MatterReceive", matterStorage.getMaxReceive());
			}
		}
	}

//	WAILA
	/*@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof MOTileEntityMachineMatter) {
			MOTileEntityMachineMatter machine = (MOTileEntityMachineMatter)te;
			currenttip.add(EnumChatFormatting.AQUA + String.format("%s / %s %s",machine.getMatterStored(),machine.getMatterCapacity(), MatterHelper.MATTER_UNIT));

		} else {
			throw new RuntimeException("MOTileEntityMachineMatter WAILA provider is being used for something that is not a MOTileEntityMachineMatter: " + te.getClass());
		}

		return currenttip;
	}*/
}
