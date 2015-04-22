package com.MO.MatterOverdrive.tile.pipes;

import com.MO.MatterOverdrive.tile.MOTileEntity;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityPipe<T extends TileEntity> extends MOTileEntity
{
    private boolean needsUpdate = true;
    private int connections = 0;

    public TileEntityPipe()
    {

    }

    @Override
    public void onAdded()
    {

    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        connections = pkt.func_148857_g().getInteger("Connections");
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("Connections",connections);
        return new S35PacketUpdateTileEntity(xCoord,yCoord,zCoord,2,tagCompound);
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
        connections = 0;

        for(int i = 0;i < 6;i++)
        {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord,ForgeDirection.values()[i].offsetY + this.yCoord,ForgeDirection.values()[i].offsetZ + this.zCoord);

            if(canConnectTo(t,ForgeDirection.values()[i]))
            {
                connections |= ForgeDirection.values()[i].flag;
            }
        }

        this.setConnections(connections,2);
	}

    public int getConnections()
    {
        return connections;
    }

    public void setConnections(int connections,int notify)
    {
        this.connections = connections;
        if (worldObj != null) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, connections, notify);
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        }
    }

    public abstract boolean canConnectTo(TileEntity entity,ForgeDirection direction);

    public void queueUpdate()
    {
        needsUpdate = true;
    }

    public  boolean isConnectableSide(ForgeDirection dir)
    {
        return  MOMathHelper.getBoolean(connections,dir.ordinal());
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord+1,yCoord+1,zCoord+1);
    }

    @Override
    public void onNeighborBlockChange()
    {
        queueUpdate();
    }
}
