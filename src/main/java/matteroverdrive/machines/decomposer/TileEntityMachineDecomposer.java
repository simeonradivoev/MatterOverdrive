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

package matteroverdrive.machines.decomposer;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.MatterSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;

import java.util.EnumSet;
import java.util.Random;

public class TileEntityMachineDecomposer extends MOTileEntityMachineMatter implements ISidedInventory
{
	public static final int MATTER_EXTRACT_SPEED = 32;
	public static final float FAIL_CHANGE = 0.005f;
	private static final Random random = new Random();
	public static int MATTER_STORAGE = 1024;
	public static int ENERGY_STORAGE = 512000;
	public static int DECEOPOSE_SPEED_PER_MATTER = 80;
	public static int DECOMPOSE_ENERGY_PER_MATTER = 6000;
	private static EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.Fail, UpgradeTypes.MatterStorage, UpgradeTypes.MatterTransfer, UpgradeTypes.PowerStorage, UpgradeTypes.PowerUsage, UpgradeTypes.Speed);
	private final TimeTracker time;
	public int INPUT_SLOT_ID;
	public int OUTPUT_SLOT_ID;
	public int decomposeTime;

	public TileEntityMachineDecomposer()
	{
		super(4);
		this.energyStorage.setCapacity(ENERGY_STORAGE);
		this.energyStorage.setOutputRate(ENERGY_STORAGE);
		this.energyStorage.setInputRate(ENERGY_STORAGE);

		this.matterStorage.setCapacity(MATTER_STORAGE);
		this.matterStorage.setMaxReceive(0);
		this.matterStorage.setMaxExtract(MATTER_STORAGE);
		time = new TimeTracker();
		playerSlotsMain = true;
		playerSlotsHotbar = true;
	}

	@Override
	protected void RegisterSlots(Inventory inventory)
	{
		INPUT_SLOT_ID = inventory.AddSlot(new MatterSlot(true));
		OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false));
		super.RegisterSlots(inventory);
	}

	@Override
	public void update()
	{
		super.update();
		this.manageDecompose();
		this.manageExtract();
	}

	@Override
	public SoundEvent getSound()
	{
		return MatterOverdriveSounds.decomposer;
	}

	@Override
	public boolean hasSound()
	{
		return true;
	}

	@Override
	public float soundVolume()
	{
		return 0.3f;
	}

	private void manageExtract()
	{
		if (!worldObj.isRemote)
		{
			if (time.hasDelayPassed(worldObj, MATTER_EXTRACT_SPEED))
			{
				for (int i = 0; i < 6; i++)
				{
					EnumFacing dir = EnumFacing.VALUES[i];
					TileEntity e = worldObj.getTileEntity(getPos().offset(dir));
					EnumFacing opposite = dir.getOpposite();
					if (e != null && e.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, opposite))
					{
						int received = e.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, opposite).receiveMatter(matterStorage.getFluidAmount(), true);
						if (received != 0)
						{
							matterStorage.setMatterStored(Math.max(0, matterStorage.getMatterStored() - received));
							updateClientMatter();
						}
					}
				}
			}
		}
	}

	protected void manageDecompose()
	{
		if (!worldObj.isRemote)
		{
			if (this.isDecomposing())
			{
				if (this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick())
				{
					this.decomposeTime++;
					extractEnergy(EnumFacing.DOWN, getEnergyDrainPerTick(), false);

					if (this.decomposeTime >= getSpeed())
					{
						this.decomposeTime = 0;
						this.decomposeItem();
					}
				}
			}
		}

		if (!this.isDecomposing())
		{
			this.decomposeTime = 0;
		}
	}

	public boolean isDecomposing()
	{
		int matter = MatterHelper.getMatterAmountFromItem(this.getStackInSlot(INPUT_SLOT_ID));
		return getRedstoneActive()
				&& this.getStackInSlot(INPUT_SLOT_ID) != null
				&& MatterHelper.containsMatter(this.getStackInSlot(INPUT_SLOT_ID))
				&& isItemValidForSlot(INPUT_SLOT_ID, getStackInSlot(INPUT_SLOT_ID))
				&& matter <= this.matterStorage.getCapacity() - this.matterStorage.getMatterStored()
				&& canPutInOutput(matter);
	}

	@Override
	public boolean getServerActive()
	{
		return isDecomposing() && this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick();
	}

	public double getFailChance()
	{
		double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.Fail);
		//this does not nagate all fail chance if item is not fully scanned
		return FAIL_CHANGE * upgradeMultiply * upgradeMultiply;
	}

	public int getSpeed()
	{
		double matter = Math.log1p(MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(INPUT_SLOT_ID)));
		matter *= matter;
		return (int)Math.round((matter + 6) * DECEOPOSE_SPEED_PER_MATTER * getUpgradeMultiply(UpgradeTypes.Speed));
	}

	public int getEnergyDrainPerTick()
	{
		int speed = getSpeed();
		return getEnergyDrainMax() / speed;
	}

	public int getEnergyDrainMax()
	{
		int matter = MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(INPUT_SLOT_ID));
		double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.PowerUsage);
		return (int)Math.round(Math.log1p(matter * 0.01) * 15 * DECOMPOSE_ENERGY_PER_MATTER * upgradeMultiply);
	}

	private boolean canPutInOutput(int matter)
	{
		ItemStack stack = getStackInSlot(OUTPUT_SLOT_ID);
		if (stack == null)
		{
			return true;
		}
		else
		{
			if (stack.getItem() == MatterOverdriveItems.matter_dust)
			{
				if (stack.getItemDamage() == matter && stack.stackSize < stack.getMaxStackSize())
				{
					return true;
				}
			}
		}

		return false;
	}

	private void failDecompose()
	{
		ItemStack stack = getStackInSlot(OUTPUT_SLOT_ID);
		int matter = MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));

		if (stack != null)
		{
			if (stack.getItem() == MatterOverdriveItems.matter_dust && stack.getItemDamage() == matter && stack.stackSize < stack.getMaxStackSize())
			{
				stack.stackSize++;
			}
		}
		else
		{
			stack = new ItemStack(MatterOverdriveItems.matter_dust);
			MatterOverdriveItems.matter_dust.setMatter(stack, matter);
			setInventorySlotContents(OUTPUT_SLOT_ID, stack);
		}
	}

	private void decomposeItem()
	{
		int matterAmount = MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));

		if (getStackInSlot(INPUT_SLOT_ID) != null && canPutInOutput(matterAmount))
		{
			if (random.nextFloat() < getFailChance())
			{
				failDecompose();
			}
			else
			{
				int matter = this.matterStorage.getMatterStored();
				this.matterStorage.setMatterStored(matterAmount + matter);
				updateClientMatter();
			}

			this.decrStackSize(INPUT_SLOT_ID, 1);
			forceSync();
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA))
		{
			this.decomposeTime = nbt.getShort("DecomposeTime");
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA))
		{
			nbt.setShort("DecomposeTime", (short)this.decomposeTime);
		}
	}

	@Override
	protected void onMachineEvent(MachineEvent event)
	{

	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {INPUT_SLOT_ID, OUTPUT_SLOT_ID};
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == INPUT_SLOT_ID;
	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type)
	{
		return upgradeTypes.contains(type);
	}

	@Override
	public float getProgress()
	{
		float speed = (float)getSpeed();
		if (speed > 0)
		{
			return (float)(decomposeTime) / speed;
		}
		return 0;
	}
}
