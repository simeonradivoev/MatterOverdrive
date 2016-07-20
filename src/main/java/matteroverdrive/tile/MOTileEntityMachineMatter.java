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
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.MachineMatterStorage;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.client.PacketMatterUpdate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class MOTileEntityMachineMatter extends MOTileEntityMachineEnergy
{
	protected MachineMatterStorage matterStorage;

	public MOTileEntityMachineMatter(int upgradesCount)
	{
		super(upgradesCount);
		matterStorage = new MachineMatterStorage<>(this, 32768);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA) && matterStorage != null)
		{
			matterStorage.writeToNBT(nbt);
		}

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA) && matterStorage != null)
		{
			matterStorage.readFromNBT(nbt);
		}

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

		if (itemStack != null && matterStorage != null)
		{
			if (itemStack.hasTagCompound())
			{
				matterStorage.readFromNBT(itemStack.getTagCompound());
			}
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		super.writeToDropItem(itemStack);

		if (itemStack != null && matterStorage != null)
		{
			if (matterStorage.getMatterStored() > 0)
			{
				if (!itemStack.hasTagCompound())
				{
					itemStack.setTagCompound(new NBTTagCompound());
				}

				matterStorage.writeToNBT(itemStack.getTagCompound());
				itemStack.getTagCompound().setInteger("MaxMatter", matterStorage.getCapacity());
				itemStack.getTagCompound().setInteger("MatterSend", matterStorage.getMaxExtract());
				itemStack.getTagCompound().setInteger("MatterReceive", matterStorage.getMaxReceive());
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == MatterOverdriveCapabilities.MATTER_HANDLER ||
				capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == MatterOverdriveCapabilities.MATTER_HANDLER ||
				capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)matterStorage;
		}
		return super.getCapability(capability, facing);
	}

}
