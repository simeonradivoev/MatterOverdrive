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

package matteroverdrive.api.gravity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 10/23/2015.
 */
public class AnomalySuppressor
{
    private BlockPos pos;
    private int time;
    private float amount;

    public AnomalySuppressor(NBTTagCompound tagCompound)
    {
        readFromNBT(tagCompound);
    }

    public AnomalySuppressor(BlockPos pos, int time, float amount)
    {
        this.pos = pos;
        this.time = time;
        this.amount = amount;
    }

    public boolean update(AnomalySuppressor suppressor)
    {
        if (suppressor.pos.equals(pos)) {
            if (time < suppressor.time) {
                this.time = suppressor.time;
            }
            this.amount = suppressor.amount;
            return true;
        }
        return false;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setLong("block",pos.toLong());
        tagCompound.setByte("time", (byte)time);
        tagCompound.setFloat("amount", amount);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        pos = BlockPos.fromLong(tagCompound.getLong("block"));
        time = tagCompound.getByte("time");
        amount = tagCompound.getFloat("amount");
    }

    public void tick()
    {
        time--;
    }

    public boolean isValid()
    {
        return time > 0;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public BlockPos getPos(){return pos;}

    public void setPos(BlockPos pos){this.pos = pos;}
}
