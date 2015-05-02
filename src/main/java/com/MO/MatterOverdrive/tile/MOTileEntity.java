package com.MO.MatterOverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/21/2015.
 */
public class MOTileEntity extends TileEntity implements IMOTileEntity
{
    private boolean isAwake = false;

    public MOTileEntity(){super();}

    public MOTileEntity(World world,int meta)
    {
        super();
    }

    protected void updateBlock()
    {
        if(worldObj != null)
        {
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        }
    }

    @Override
    public void updateEntity()
    {
        if (!isAwake)
        {
            onAwake(worldObj.isRemote ? Side.CLIENT : Side.SERVER);
            isAwake = true;
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

    protected void onAwake(Side side) {

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
