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

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkTaskReplicatePattern extends MatterNetworkTask
{
    ItemPattern pattern;
    int amount;

    public MatterNetworkTaskReplicatePattern()
    {
        super();
        pattern = new ItemPattern();
    }

    public MatterNetworkTaskReplicatePattern(short itemID,short itemMetadata,byte amount)
    {
        pattern = new ItemPattern(itemID,itemMetadata);
        this.amount = amount;
    }

    public MatterNetworkTaskReplicatePattern(ItemPattern pattern,int amount)
    {
        this.pattern = pattern;
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
            pattern.readFromNBT(compound.getCompoundTag("Pattern"));
            amount = compound.getShort("amount");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("amount",(short) amount);
        if (compound != null)
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            pattern.writeToNBT(tagCompound);
            compound.setTag("Pattern", tagCompound);
        }
    }

    //region Getters and setters
    @Override
    public String getName()
    {
        return String.format("[%s] %s",amount,MOStringHelper.translateToLocal(pattern.getItem().getUnlocalizedName() + ".name"));
    }

   public ItemPattern getPattern(){return pattern;}

    public boolean isValid(World world)
    {
        if (!super.isValid(world))
            return false;

        return MatterHelper.getMatterAmountFromItem(pattern.toItemStack(false)) > 0;
    }

    public int getAmount(){return amount;}

    public void setAmount(int amount){this.amount = amount;}
    //endregion
}
