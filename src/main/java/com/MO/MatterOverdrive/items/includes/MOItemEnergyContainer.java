package com.MO.MatterOverdrive.items.includes;

import java.text.DecimalFormat;
import java.util.List;

import com.MO.MatterOverdrive.util.MOEnergyHelper;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.ItemEnergyContainer;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;

public class MOItemEnergyContainer extends MOBaseItem implements IEnergyContainerItem
{
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public MOItemEnergyContainer(String name)
	{
		this(name,32000);
	}

	public MOItemEnergyContainer(String name,int capacity) 
	{
		this(name,capacity, capacity, capacity);
	}

	public MOItemEnergyContainer(String name,int capacity, int maxTransfer) {

		this(name,capacity, maxTransfer, maxTransfer);
	}

	public MOItemEnergyContainer(String name,int capacity, int maxReceive, int maxExtract) {
		super(name);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	@Override
	public int getDamage(ItemStack stack)
	 {
		return capacity - this.getEnergyStored(stack) + 1;
	 }
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return capacity+2;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }
	
	@Override
	public int getDisplayDamage(ItemStack stack)
    {
        return this.getDamage(stack);
    }
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	 {
		this.TagCompountCheck(itemstack);
		infos.add(EnumChatFormatting.YELLOW + MOEnergyHelper.formatEnergy(this.getEnergyStored(itemstack), capacity));
	 }

	public MOItemEnergyContainer setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public void setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		this.TagCompountCheck(container);

		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) 
		{
            energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

        if(container.stackTagCompound != null && container.stackTagCompound.hasKey("Energy"))
        {
            int energy = container.stackTagCompound.getInteger("Energy");
            int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

            if (!simulate) {
                energy -= energyExtracted;
                container.stackTagCompound.setInteger("Energy", energy);
                System.out.println("trying to extract " + maxExtract + " from " + this.getUnlocalizedName() + ". New Energy " + (energy - energyExtracted));
            }
            return energyExtracted;
        }
        return 0;
	}
	
	protected void setEnergyStored(ItemStack container,int amount)
	{
		EnergyHelper.setDefaultEnergyTag(container, amount);
	}

	@Override
	public int getEnergyStored(ItemStack container) 
	{
		this.TagCompountCheck(container);
		return container.stackTagCompound.getInteger("Energy");
	}
	
	@Override
	 public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	 {
		this.setEnergyStored(stack, 0);
	 }

	@Override
	public int getMaxEnergyStored(ItemStack container) 
	{
		return capacity;
	}
	
	@Override
	public void InitTagCompount(ItemStack stack)
	{
		this.setEnergyStored(stack, capacity);
	}
}
