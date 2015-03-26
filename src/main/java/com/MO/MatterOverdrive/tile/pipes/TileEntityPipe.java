package com.MO.MatterOverdrive.tile.pipes;

import com.MO.MatterOverdrive.sound.MachineSound;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import com.MO.MatterOverdrive.tile.MOTileEntity;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPipe<T> extends MOTileEntity implements IMOTileEntity
{
    Class<T> connectionType;
    private boolean needsUpdate = true;
    public ForgeDirection[] connections = new ForgeDirection[6];

    public TileEntityPipe(Class<T> cType)
    {
        connectionType = cType;
    }

    @Override
	public void updateEntity()
	{
        if(needsUpdate)
        {
            updateSides();
            needsUpdate = false;
        }
	}

    public void updateSides()
	{
        for(int i = 0;i < 6;i++)
        {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord,ForgeDirection.values()[i].offsetY + this.yCoord,ForgeDirection.values()[i].offsetZ + this.zCoord);

            if(connectionType.isInstance(t))
            {
                connections[i] = ForgeDirection.values()[i];
            }else
            {
                connections[i] = null;
            }
        }
	}

    public void queueUpdate()
    {
        needsUpdate = true;
    }

    public  boolean isConnectableSide(ForgeDirection dir)
    {
        if(connections != null && connections[dir.ordinal()] != null)
        {
            return  true;
        }
        return  false;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        Block type = getBlockType();
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord+1,yCoord+1,zCoord+1);
    }

    @Override
    public void onNeighborBlockChange()
    {
        needsUpdate = true;
    }
}
