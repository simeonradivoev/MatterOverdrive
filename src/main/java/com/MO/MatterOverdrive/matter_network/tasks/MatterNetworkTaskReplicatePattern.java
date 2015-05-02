package com.MO.MatterOverdrive.matter_network.tasks;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.util.MOStringHelper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkTaskReplicatePattern extends MatterNetworkTask
{
    int itemID;
    int itemMetadata;
    int amount;

    public MatterNetworkTaskReplicatePattern()
    {
        super();

    }

    public MatterNetworkTaskReplicatePattern(IMatterNetworkConnection sender,int itemID,int itemMetadata,int amount)
    {
        super(sender);
        this.itemID = itemID;
        this.itemMetadata = itemMetadata;
        this.amount = amount;
    }

    @Override
    protected void init()
    {
        setUnlocalizedName("replicate_pattern");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound != null)
        {
            itemID = compound.getInteger("itemID");
            itemMetadata = compound.getInteger("itemMetadata");
            amount = compound.getInteger("amount");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (compound != null)
        {
            compound.setInteger("itemID",itemID);
            compound.setInteger("itemMetadata",itemMetadata);
            compound.setInteger("amount",amount);
        }
    }

    //region Getters and setters
    @Override
    public String getName()
    {
        return amount + " " + MOStringHelper.translateToLocal(Item.getItemById(itemID).getUnlocalizedName() + ".name");
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getItemMetadata() {
        return itemMetadata;
    }

    public int getItemID() {
        return itemID;
    }
    //endregion
}
