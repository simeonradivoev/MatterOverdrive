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

package matteroverdrive.data;

import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.Serializable;

/**
 * Created by Simeon on 12/22/2015.
 */
public class BlockPos implements Comparable<BlockPos>, Serializable
{
    public int x;
    public int y;
    public int z;
    public ForgeDirection orientation;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public BlockPos(int x, int y, int z, ForgeDirection var4) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = var4;
    }

    public BlockPos(BlockPos blockPos) {
        this.x = blockPos.x;
        this.y = blockPos.y;
        this.z = blockPos.z;
        this.orientation = blockPos.orientation;
    }

    public BlockPos(NBTTagCompound tagCompound) {
        this.x = tagCompound.getInteger("bp_i");
        this.y = tagCompound.getInteger("bp_j");
        this.z = tagCompound.getInteger("bp_k");
        if(!tagCompound.hasKey("bp_dir")) {
            this.orientation = ForgeDirection.UNKNOWN;
        } else {
            this.orientation = ForgeDirection.getOrientation(tagCompound.getByte("bp_dir"));
        }

    }

    public BlockPos(TileEntity tileEntity)
    {
        this.x = tileEntity.xCoord;
        this.y = tileEntity.yCoord;
        this.z = tileEntity.zCoord;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public BlockPos copy() {
        return new BlockPos(this.x, this.y, this.z, this.orientation);
    }

    public BlockPos copy(ForgeDirection direction) {
        return new BlockPos(this.x, this.y, this.z, direction);
    }

    public BlockPos setOrientation(ForgeDirection var1) {
        this.orientation = var1;
        return this;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("bp_i", this.x);
        tagCompound.setInteger("bp_j", this.y);
        tagCompound.setInteger("bp_k", this.z);
        tagCompound.setByte("bp_dir", (byte)this.orientation.ordinal());
    }

    public String toString() {
        return this.orientation == null?"{" + this.x + ", " + this.y + ", " + this.z + "}":"{" + this.x + ", " + this.y + ", " + this.z + ";" + this.orientation.toString() + "}";
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof BlockPos)) {
            return false;
        } else {
            BlockPos blockPos = (BlockPos)obj;
            return blockPos.x == this.x & blockPos.y == this.y & blockPos.z == this.z & blockPos.orientation == this.orientation;
        }
    }

    public boolean equals(BlockPos blockPos) {
        return blockPos != null && blockPos.x == this.x & blockPos.y == this.y & blockPos.z == this.z & blockPos.orientation == this.orientation;
    }

    public int hashCode() {
        return this.x & 4095 | this.y & '\uff00' | this.z & 16773120;
    }

    public BlockPos step(int dir) {
        int[] var2 = MOBlockHelper.SIDE_COORD_MOD[dir];
        this.x += var2[0];
        this.y += var2[1];
        this.z += var2[2];
        return this;
    }

    public BlockPos step(int dir, int amount) {
        int[] var3 = MOBlockHelper.SIDE_COORD_MOD[dir];
        this.x += var3[0] * amount;
        this.y += var3[1] * amount;
        this.z += var3[2] * amount;
        return this;
    }

    public BlockPos step(ForgeDirection direction) {
        this.x += direction.offsetX;
        this.y += direction.offsetY;
        this.z += direction.offsetZ;
        return this;
    }

    public BlockPos step(ForgeDirection direction, int amount) {
        this.x += direction.offsetX * amount;
        this.y += direction.offsetY * amount;
        this.z += direction.offsetZ * amount;
        return this;
    }

    public boolean blockExists(World world) {
        return world.blockExists(this.x, this.y, this.z);
    }

    public TileEntity getTileEntity(World world) {
        return world.getTileEntity(this.x, this.y, this.z);
    }

    public <T extends TileEntity> T getTileEntity(World world, Class<T> tClass) {
        TileEntity var3 = world.getTileEntity(this.x, this.y, this.z);
        return tClass.isInstance(var3)?(T)var3:null;
    }

    public Block getBlock(World world) {
        return world.getBlock(this.x, this.y, this.z);
    }

    public int compareTo(BlockPos blockPos) {
        return this.x == blockPos.x?(this.y == blockPos.y?this.z - blockPos.z:this.y - blockPos.y):this.x - blockPos.x;
    }
}
