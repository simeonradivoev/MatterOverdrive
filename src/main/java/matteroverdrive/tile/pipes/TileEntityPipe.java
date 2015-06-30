package matteroverdrive.tile.pipes;

import matteroverdrive.tile.MOTileEntity;
import matteroverdrive.util.math.MOMathHelper;
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
            updateSides(true);
            needsUpdate = false;
        }
	}

    public void updateSides(boolean notify)
	{
        int connections = 0;

        for(int i = 0;i < 6;i++)
        {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord,ForgeDirection.values()[i].offsetY + this.yCoord,ForgeDirection.values()[i].offsetZ + this.zCoord);

            if(canConnectTo(t,ForgeDirection.values()[i]))
            {
                connections |= ForgeDirection.values()[i].flag;
            }
        }

        this.setConnections(connections,notify);
	}

    public int getConnections()
    {
        return connections;
    }

    public void setConnections(int connections,boolean notify)
    {
        if (worldObj != null && this.connections != connections) {

            if (notify) {
                for (int i = 0; i < 6; i++) {
                    TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord, ForgeDirection.values()[i].offsetY + this.yCoord, ForgeDirection.values()[i].offsetZ + this.zCoord);
                    if (t instanceof TileEntityPipe) {
                        ((TileEntityPipe) t).updateSides(false);
                    }
                }
            }

            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, connections, notify ? 3 : 0);
            //worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            //worldObj.markBlockRangeForRenderUpdate(xCoord - 4,yCoord - 4,zCoord - 4,xCoord + 4,yCoord + 4,zCoord + 4);
            //worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, blockType);
        }
        this.connections = connections;
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
