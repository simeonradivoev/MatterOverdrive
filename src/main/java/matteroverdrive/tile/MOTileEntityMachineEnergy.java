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

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.MachineEnergyStorage;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.client.PacketPowerUpdate;
import matteroverdrive.util.MOEnergyHelper;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

/**
 * Created by Simeon on 3/18/2015.
 */
public abstract class MOTileEntityMachineEnergy extends MOTileEntityMachine implements IEnergyReceiver, IEnergyProvider
{
	public static final int ENERGY_CLIENT_SYNC_RANGE = 16;
	protected MachineEnergyStorage energyStorage;
	protected int energySlotID;

	public MOTileEntityMachineEnergy(int upgradeCount)
	{
		super(upgradeCount);
		this.energyStorage = new MachineEnergyStorage<>(512, 512, 512, this);
	}

	@Override
	protected void RegisterSlots(Inventory inventory)
	{
		energySlotID = inventory.AddSlot(new EnergySlot(true));
		super.RegisterSlots(inventory);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA))
		{
			NBTTagCompound energy = energyStorage.serializeNBT();
			nbt.setTag("Energy", energy);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA))
		{
			energyStorage.deserializeNBT(nbt.getCompoundTag("Energy"));
		}
	}

	public void update()
	{
		super.update();
		manageCharging();
	}

	protected void manageCharging()
	{
		if (isCharging())
		{
			if (!this.worldObj.isRemote)
			{
				int emptyEnergySpace = getFreeEnergySpace(EnumFacing.DOWN);
				int maxEnergyCanSpare = MOEnergyHelper.extractEnergyFromContainer(this.inventory.getStackInSlot(energySlotID), emptyEnergySpace, true);

				if (emptyEnergySpace > 0 && maxEnergyCanSpare > 0)
				{
					this.receiveEnergy(EnumFacing.DOWN, MOEnergyHelper.extractEnergyFromContainer(this.inventory.getStackInSlot(energySlotID), emptyEnergySpace, false), false);
				}
			}
		}
	}

	public boolean isCharging()
	{
		return this.inventory.getStackInSlot(energySlotID) != null
				&& MOEnergyHelper.isEnergyContainerItem(this.inventory.getStackInSlot(energySlotID))
				&& ((IEnergyContainerItem)this.inventory.getStackInSlot(energySlotID).getItem()).extractEnergy(this.inventory.getStackInSlot(energySlotID), getFreeEnergySpace(EnumFacing.DOWN), true) > 0;
	}

	public int getEnergySlotID()
	{
		return this.energySlotID;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,
							 boolean simulate)
	{
		int lastEnergy = energyStorage.getEnergyStored();
		int received = energyStorage.receiveEnergy(maxReceive, simulate);
		if (lastEnergy != energyStorage.getEnergyStored() && !simulate)
		{
			UpdateClientPower();
		}
		return received;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract,
							 boolean simulate)
	{
		int lastEnergy = energyStorage.getEnergyStored();
		int extracted = energyStorage.extractEnergy(maxExtract, simulate);
		if (lastEnergy != energyStorage.getEnergyStored() && !simulate)
		{
			UpdateClientPower();
		}
		return extracted;
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return energyStorage.getMaxEnergyStored();
	}

	public MachineEnergyStorage getEnergyStorage()
	{
		return this.energyStorage;
	}

	public int GetEnergyStoredScaled(int i)
	{
		return MathHelper.ceiling_float_int(((float)this.getEnergyStored(EnumFacing.DOWN) / (float)this.energyStorage.getMaxEnergyStored()) * i);
	}

	public int getFreeEnergySpace(EnumFacing dir)
	{
		return this.getMaxEnergyStored(dir) - this.getEnergyStored(dir);
	}

	public void setEnergyStored(int stored)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("TeslaPower", stored);
		this.energyStorage.deserializeNBT(tag);

	}

	public void UpdateClientPower()
	{
		MatterOverdrive.packetPipeline.sendToAllAround(new PacketPowerUpdate(this), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), ENERGY_CLIENT_SYNC_RANGE));
	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		super.readFromPlaceItem(itemStack);

		if (itemStack != null)
		{
			if (itemStack.hasTagCompound())
			{
				energyStorage.deserializeNBT(itemStack.getTagCompound().getCompoundTag("Energy"));
			}
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		super.writeToDropItem(itemStack);

		if (itemStack != null)
		{
			if (energyStorage.getEnergyStored() > 0)
			{
				if (!itemStack.hasTagCompound())
				{
					itemStack.setTagCompound(new NBTTagCompound());
				}

				NBTTagCompound energy = energyStorage.serializeNBT();
				itemStack.getTagCompound().setTag("Energy", energy);
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_CONSUMER)
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
		if (capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_CONSUMER)
		{
			return (T)energyStorage;
		}
		return super.getCapability(capability, facing);
	}

}
