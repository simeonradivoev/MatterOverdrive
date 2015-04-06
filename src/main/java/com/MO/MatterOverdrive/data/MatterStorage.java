package com.MO.MatterOverdrive.data;

import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.api.matter.IMatterStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class MatterStorage implements IMatterStorage
{
	protected int matter;
	protected int capacity;
    protected int maxExtract;
    protected int maxReceive;
	
	public MatterStorage()
	{
		this(32000);
	}
	
	public MatterStorage(int capacity)
	{
        this(capacity,capacity,capacity);
	}

    public MatterStorage(int capacity,int maxExtract)
    {
        this(capacity,maxExtract,maxExtract);
    }

    public MatterStorage(int capacity,int maxExtract,int maxReceive)
    {
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.capacity = capacity;
    }
	
	@Override
	public int getMatterStored() {
		return matter;
	}

	@Override
	public void setMatterStored(int amount) {
		matter = amount;
	}

	@Override
	public int extractMatter(ForgeDirection direction,int amount,boolean simulate) 
	{
		int maxDrain = MathHelper.clampI(Math.min(amount, maxExtract),0,this.matter);
		
		if(!simulate)
		{
			this.matter -= maxDrain;
		}
		
		return maxDrain;
	}

	@Override
	public int receiveMatter(ForgeDirection side,int amount,boolean simulate) 
	{
		int maxFill = MathHelper.clampI(Math.min(amount, maxReceive),0,this.capacity - this.matter);
		
		if(!simulate)
		{
			this.matter += maxFill;
		}
		
		return maxFill;
	}


	@Override
	public int getCapacity() 
	{
		return this.capacity;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(this.matter < 0)
		{
			this.matter = 0;
		}
		nbt.setInteger("Matter", this.matter);
	}
	
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.matter = nbt.getInteger("Matter");
	}

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }
}
