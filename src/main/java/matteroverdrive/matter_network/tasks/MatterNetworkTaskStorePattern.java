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

package matteroverdrive.matter_network.tasks;

import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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

    public boolean isValid(World world)
    {
        if (!super.isValid(world))
            return false;

        return MatterHelper.getMatterAmountFromItem(itemStack) > 0;
    }
    //endregion
}
