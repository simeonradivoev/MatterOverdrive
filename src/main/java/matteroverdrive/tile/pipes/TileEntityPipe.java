package matteroverdrive.tile.pipes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.tile.MOTileEntity;
import matteroverdrive.util.math.MOMathHelper;
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

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        connections = pkt.func_148857_g().getInteger("Connections");
        worldObj.markBlockRangeForRenderUpdate(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("Connections",connections);
        return new S35PacketUpdateTileEntity(xCoord,yCoord,zCoord,0,tagCompound);
    }

    @Override
	public void updateEntity()
	{
        if(needsUpdate)
        {
            updateSides(true);
            needsUpdate = false;
        }
	}

    public void updateSides(boolean notify)
	{
        int connections = 0;

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity t = this.worldObj.getTileEntity(direction.offsetX + this.xCoord,direction.offsetY + this.yCoord,direction.offsetZ + this.zCoord);

            if(canConnectTo(t,direction))
            {
                connections |= direction.flag;
            }
        }

        this.setConnections(connections,notify);
	}

    public int getConnectionsMask()
    {
        return connections;
    }

    public void setConnections(int connections,boolean notify)
    {
        this.connections = connections;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
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
