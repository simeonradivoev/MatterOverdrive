package com.MO.MatterOverdrive.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.EnergySlot;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/18/2015.
 */
public abstract class MOTileEntityMachineEnergy extends MOTileEntityMachine implements IEnergyHandler
{

    protected EnergyStorage energyStorage;
    protected int energySlotID;


    public MOTileEntityMachineEnergy(int slotCount)
    {
        this.energyStorage = new EnergyStorage(512);
        inventory = new Inventory(this,"",slotCount);
        RegisterSlots(inventory);
        energySlotID = inventory.AddSlot(new EnergySlot());
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        energyStorage.writeToNBT(nbt);
        inventory.writeToNBT(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        energyStorage.readFromNBT(nbt);
        inventory.readFromNBT(nbt);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        manageCharging();
    }

    protected void manageCharging()
    {
        if(isCharging())
        {
            if(!this.worldObj.isRemote)
            {
                int emptyEnergySpace = getFreeEnergySpace(ForgeDirection.DOWN);
                int maxEnergyCanSpare = EnergyHelper.extractEnergyFromContainer(this.inventory.getStackInSlot(energySlotID), emptyEnergySpace, true);

                if(emptyEnergySpace > 0 && maxEnergyCanSpare > 0)
                {
                    this.receiveEnergy(ForgeDirection.DOWN, EnergyHelper.extractEnergyFromContainer(this.inventory.getStackInSlot(energySlotID), emptyEnergySpace, false), false);
                }
            }
        }
    }

    public boolean isCharging()
    {
        return this.inventory.getStackInSlot(energySlotID) != null
                && EnergyHelper.isEnergyContainerItem(this.inventory.getStackInSlot(energySlotID))
                && ((IEnergyContainerItem)this.inventory.getStackInSlot(energySlotID).getItem()).extractEnergy(this.inventory.getStackInSlot(energySlotID), getFreeEnergySpace(ForgeDirection.DOWN), true) > 0;
    }

    public int getEnergySlotID()
    {
        return this.energySlotID;
    }

    protected void RegisterSlots(Inventory inventory)
    {

    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive,
                             boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract,
                             boolean simulate)
    {

        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }

    public IEnergyStorage getEnergyStorage()
    {
        return this.energyStorage;
    }

    public int GetEnergyStoredScaled(int i)
    {
        return MathHelper.ceil(((float) this.getEnergyStored(ForgeDirection.DOWN) / (float) this.energyStorage.getMaxEnergyStored()) * i);
    }

    public int getFreeEnergySpace(ForgeDirection dir)
    {
        return this.getMaxEnergyStored(dir) - this.getEnergyStored(dir);
    }

    public void setEnergyStored(int storage)
    {
        this.energyStorage.setEnergyStored(storage);

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack)
    {
        super.readFromPlaceItem(itemStack);

        if(itemStack != null)
        {
            if(itemStack.hasTagCompound())
            {
                energyStorage.readFromNBT(itemStack.getTagCompound());
            }
        }
    }

    @Override
    public void writeToDropItem(ItemStack itemStack)
    {
        super.writeToDropItem(itemStack);

        if(itemStack != null)
        {
            if(energyStorage.getEnergyStored() > 0) {
                if (!itemStack.hasTagCompound())
                    itemStack.setTagCompound(new NBTTagCompound());

                energyStorage.writeToNBT(itemStack.getTagCompound());
                itemStack.getTagCompound().setInteger("MaxEnergy",energyStorage.getMaxEnergyStored());
            }
        }
    }
}
