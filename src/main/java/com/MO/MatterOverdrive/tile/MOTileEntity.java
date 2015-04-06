package com.MO.MatterOverdrive.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 3/21/2015.
 */
public class MOTileEntity extends TileEntity implements IMOTileEntity
{
    protected void updateBlock()
    {
        if(worldObj != null)
        {
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        }
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        readCustomNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        writeCustomNBT(nbt);
    }

    public void writeCustomNBT(NBTTagCompound nbt)
    {

    }

    public void readCustomNBT(NBTTagCompound nbt)
    {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void onNeighborBlockChange()
    {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack)
    {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack)
    {

    }
}
