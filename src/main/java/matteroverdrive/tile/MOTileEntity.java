package matteroverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/21/2015.
 */
public abstract class MOTileEntity extends TileEntity implements IMOTileEntity
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

    public abstract void writeCustomNBT(NBTTagCompound nbt);

    public abstract void readCustomNBT(NBTTagCompound nbt);

    protected abstract void onAwake(Side side);
}
