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
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkTaskReplicatePattern extends MatterNetworkTask
{
    NBTTagCompound pattern;

    public MatterNetworkTaskReplicatePattern()
    {
        super();

    }

    public MatterNetworkTaskReplicatePattern(IMatterNetworkConnection sender,short itemID,short itemMetadata,byte amount)
    {
        super(sender);
        pattern = new NBTTagCompound();
        pattern.setShort("id", itemID);
        pattern.setShort("Damage", itemMetadata);
        pattern.setByte("Count", amount);
    }

    public MatterNetworkTaskReplicatePattern(IMatterNetworkConnection sender,NBTTagCompound pattern)
    {
        super(sender);
        this.pattern = pattern;
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
            pattern = compound.getCompoundTag("Pattern");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (compound != null)
        {
            compound.setTag("Pattern", pattern);
        }
    }

    //region Getters and setters
    @Override
    public String getName()
    {
        return pattern.getByte("Count") + " " + MOStringHelper.translateToLocal(Item.getItemById(pattern.getShort("id")).getUnlocalizedName() + ".name");
    }

    public int getAmount() {
        return pattern.getByte("Count");
    }

    public void setAmount(int amount) {
        pattern.setByte("Count",(byte)amount);
    }

    public int getItemMetadata() {
        return pattern.getShort("Damage");
    }

    public int getItemID() {
        return pattern.getShort("id");
    }

    public boolean isValid(World world)
    {
        if (!super.isValid(world))
            return false;

        ItemStack stack = MatterDatabaseHelper.GetItemStackFromNBT(pattern);
        return MatterHelper.getMatterAmountFromItem(stack) > 0;
    }
    //endregion
}
