package matteroverdrive.items.includes;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

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
	public int getMaxDamage(ItemStack stack)
	{
		return getMaxEnergyStored(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

	public int getDisplayDamage(ItemStack stack)
	{
		return getMaxEnergyStored(stack) - getEnergyStored(stack);
	}

	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	 {
		this.TagCompountCheck(itemstack);
		infos.add(EnumChatFormatting.YELLOW + MOEnergyHelper.formatEnergy(getEnergyStored(itemstack), getMaxEnergyStored(itemstack)));
	 }

	@Override
	public boolean hasDetails(ItemStack itemStack)
	{
		return true;
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
	public int getMaxEnergyStored(ItemStack container)
	{
		return capacity;
	}
}
