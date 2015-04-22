package com.MO.MatterOverdrive.data.network;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 4/20/2015.
 */
public class MatterNetworkTaskStorePattern extends MatterNetworkTask
{
    ItemStack itemStack;
    int progress;

    public MatterNetworkTaskStorePattern()
    {
        super();

    }

    public MatterNetworkTaskStorePattern(IMatterNetworkConnection sender,ItemStack itemStack,int progress)
    {
        super(sender);
        this.itemStack = itemStack;
        this.progress = progress;
    }

    @Override
    protected void init()
    {
        setUnlocalizedName("store_pattern");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound != null)
        {
            itemStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Item"));
            progress = compound.getInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (compound != null)
        {
            NBTTagCompound itemComp = new NBTTagCompound();
            if (itemStack != null)
                itemStack.writeToNBT(itemComp);
            compound.setTag("Item",itemComp);
            compound.setInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME,progress);
        }
    }


    //region Getters and Setters
    @Override
    public String getName()
    {
        return itemStack.getDisplayName();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    //endregion
}
