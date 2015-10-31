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

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterConnection;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.MatterSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Random;

public class TileEntityMachineDecomposer extends MOTileEntityMachineMatter implements ISidedInventory, IMatterConnection
{
	public static int MATTER_STORAGE = 1024;
	public static int ENERGY_STORAGE = 512000;
    public static final int MATTER_EXTRACT_SPEED = 32;
    public static final float FAIL_CHANGE = 0.005f;

    public static int DECEOPOSE_SPEED_PER_MATTER = 80;
    public static int DECOMPOSE_ENERGY_PER_MATTER = 8000;

    public int INPUT_SLOT_ID;
    public int OUTPUT_SLOT_ID;

    private TimeTracker time;
    private static Random random = new Random();
	public int decomposeTime;

	public TileEntityMachineDecomposer()
	{
		super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_STORAGE);
        this.energyStorage.setMaxReceive(ENERGY_STORAGE);

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
	public void updateEntity()
	{
		super.updateEntity();
		this.manageDecompose();
        this.manageExtract();
	}

    @Override
    public String getSound() {
        return "machine";
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public float soundVolume() { return 1;}

    @Override
    public void onContainerOpen(Side side) {

    }

    private void  manageExtract()
    {
        if(!worldObj.isRemote)
        {
            if(time.hasDelayPassed(worldObj,MATTER_EXTRACT_SPEED))
            {
                for (int i = 0; i < 6; i++)
                {
                    ForgeDirection dir = ForgeDirection.values()[i];
                    TileEntity e = worldObj.getTileEntity(this.xCoord + dir.offsetX,this.yCoord + dir.offsetY,this.zCoord + dir.offsetZ);
                    if(e instanceof IMatterHandler)
                    {
                        ForgeDirection oposite = dir.getOpposite();
                        int recived = ((IMatterHandler)e).fill(oposite, new FluidStack(MatterOverdriveFluids.matterPlasma,matterStorage.getFluidAmount()), true);
                        if(recived != 0)
                        {
                            matterStorage.setMatterStored(Math.max(0,matterStorage.getMatterStored()-recived));
                            updateClientMatter();
                        }
                    }
                }
            }
        }
    }

	protected void manageDecompose()
	{
        if(!worldObj.isRemote)
        {
            if (this.isDecomposing())
            {
                if(this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick())
                {
                    this.decomposeTime++;
                    extractEnergy(ForgeDirection.DOWN, getEnergyDrainPerTick(), false);

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
                && matter <= this.getMatterCapacity() - this.getMatterStored()
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
        matter*=matter;
        return MathHelper.round(DECEOPOSE_SPEED_PER_MATTER * matter * getUpgradeMultiply(UpgradeTypes.Speed));
    }

    public int getEnergyDrainPerTick()
    {
        int maxEnergy = getEnergyDrainMax();
        int speed = getSpeed();
        if (speed > 0) {
            return maxEnergy / speed;
        }
        return 0;
    }

    public int getEnergyDrainMax()
    {
        int matter = MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(INPUT_SLOT_ID));
        double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.PowerUsage);
        return MathHelper.round((matter * DECOMPOSE_ENERGY_PER_MATTER) * upgradeMultiply);
    }

    private boolean canPutInOutput(int matter)
    {
        ItemStack stack = getStackInSlot(OUTPUT_SLOT_ID);
        if(stack == null)
        {
            return true;
        }
        else
        {
            if(stack.getItem() == MatterOverdriveItems.matter_dust)
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
        int matter =MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));

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

		if(getStackInSlot(INPUT_SLOT_ID) != null && canPutInOutput(matterAmount))
		{
            if(random.nextFloat() < getFailChance())
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
        if (categories.contains(MachineNBTCategory.DATA)) {
            this.decomposeTime = nbt.getShort("DecomposeTime");
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.writeCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setShort("DecomposeTime", (short) this.decomposeTime);
        }
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
        return new int[]{INPUT_SLOT_ID,OUTPUT_SLOT_ID};
	}

	@Override
	public boolean canExtractItem(int i, ItemStack item,
			int j)
	{
		return i != INPUT_SLOT_ID;
	}

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return true;
    }

    @Override
    public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type != UpgradeTypes.Range && type != UpgradeTypes.SecondOutput;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public float getProgress()
    {
        float speed = (float) getSpeed();
        if (speed > 0) {
            return (float) (decomposeTime) / speed;
        }
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return false;
    }
}
