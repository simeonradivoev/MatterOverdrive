package com.MO.MatterOverdrive.tile.pipes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/7/2015.
 */
public class MatterTransfer
{
    public static final String LIFE_KEY = "transfer_life";
    public static final String DIRECTION_KEY = "transfer_direction";
    public static final String MATTER_AMOUNT_KEY = "transfer_matter";

    public ForgeDirection dir = ForgeDirection.values()[0];
    public int amount = 0;
    public int life = 0;

    private MatterTransfer()
    {

    }

    public MatterTransfer(ForgeDirection dir,int amount,int life)
    {
        this.dir = dir;
        this.amount = amount;
        this.life = life;
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        if(tag != null)
        {
            tag.setInteger(MATTER_AMOUNT_KEY,amount);
            tag.setByte(DIRECTION_KEY, (byte) dir.ordinal());
            tag.setInteger(LIFE_KEY,life);
        }
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        if(tag != null)
        {
            if(tag.hasKey(MATTER_AMOUNT_KEY,3))
            {
                this.amount = tag.getInteger(MATTER_AMOUNT_KEY);
            }
            if(tag.hasKey(DIRECTION_KEY,1))
            {
                int dirId = tag.getByte(DIRECTION_KEY);
                if(dirId < ForgeDirection.values().length)
                {
                    this.dir = ForgeDirection.values()[dirId];
                }
            }
            if(tag.hasKey(LIFE_KEY,3))
            {
                this.life = tag.getInteger(LIFE_KEY);
            }
        }
    }

    public static MatterTransfer loadMatterTransferFromNBT(NBTTagCompound comp)
    {
        if(comp != null) {
            MatterTransfer t = new MatterTransfer();
            t.readFromNBT(comp);
        }
        return  null;
    }
}
