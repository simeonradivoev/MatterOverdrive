package matteroverdrive.tile.pipes;

import matteroverdrive.tile.MOTileEntity;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityPipe extends MOTileEntity implements ITickable
{
    protected boolean needsUpdate = true;
    protected boolean awoken;
    private int connections = 0;

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        connections = pkt.getNbtCompound().getInteger("Connections");
        worldObj.markBlockRangeForRenderUpdate(getPos(),getPos());
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("Connections",connections);
        return new SPacketUpdateTileEntity(getPos(),0,tagCompound);
    }

    @Override
	public void update()
	{
        if(needsUpdate)
        {
            updateSides(true);
            needsUpdate = false;
        }

        if (!awoken)
        {
            onAwake(worldObj.isRemote ? Side.CLIENT : Side.SERVER);
            awoken = true;
        }
	}

    public void updateSides(boolean notify)
	{
        int connections = 0;

        for (EnumFacing direction : EnumFacing.VALUES)
        {
            TileEntity t = this.worldObj.getTileEntity(getPos().offset(direction));

            if(canConnectToPipe(t,direction))
            {
                connections |= 1 << direction.ordinal();
            }
        }

        this.setConnections(connections,notify);
	}

    public int getConnectionsMask()
    {
        return connections;
    }

    public int getConnectionsCount()
    {
        int tot = 0;
        int con = connections;
        while (con > 0) {
            ++tot;
            con &= con - 1;
        }

        return tot;
    }

    public void setConnections(int connections,boolean notify)
    {
        this.connections = connections;
        if (notify)
        {
            // TODO: 3/25/2016 Find how to mark block for update
            //worldObj.markBlockForUpdate(getPos());
        }
    }

    public void setConnection(EnumFacing connection,boolean value)
    {
        this.connections = MOMathHelper.setBoolean(connections,connection.ordinal(),value);
    }

    public boolean isConnectedFromSide(EnumFacing enumFacing)
    {
        return MOMathHelper.getBoolean(connections,enumFacing.ordinal());
    }

    public abstract boolean canConnectToPipe(TileEntity entity, EnumFacing direction);

    public void queueUpdate()
    {
        needsUpdate = true;
    }

    public  boolean isConnectableSide(EnumFacing dir)
    {
        return  MOMathHelper.getBoolean(connections,dir.ordinal());
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos(),getPos().add(1,1,1));
    }
}
