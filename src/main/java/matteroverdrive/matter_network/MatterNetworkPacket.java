package matteroverdrive.matter_network;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkConnectionProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

/**
 * Created by Simeon on 4/26/2015.
 */
public abstract class MatterNetworkPacket
{
    protected BlockPosition senderPos;
    protected BlockPosition receiverPos;
    protected HashSet<MatterNetworkPathNode> path;


    public MatterNetworkPacket(){path = new HashSet<MatterNetworkPathNode>();}
    public MatterNetworkPacket(BlockPosition senderPos,ForgeDirection port)
    {
        this();
        this.senderPos = senderPos;
        this.senderPos.setOrientation(port);
    }
    public MatterNetworkPacket(BlockPosition senderPos,ForgeDirection port,BlockPosition receiverPos)
    {
        this();
        this.senderPos = senderPos;
        this.senderPos.setOrientation(port);
        this.receiverPos = receiverPos;
    }

    public MatterNetworkPacket addToPath(IMatterNetworkConnection connection,ForgeDirection recivedFrom)
    {
        MatterNetworkPathNode node = new MatterNetworkPathNode(connection.getPosition(),recivedFrom.getOpposite());
        if (!path.contains(node))
        {
            path.add(node);
        }
        return this;
    }

    public boolean hasPassedTrough(IMatterNetworkConnection connection)
    {
        return path.contains(new MatterNetworkPathNode(connection.getPosition()));
    }

    public IMatterNetworkConnection getSender(World world)
    {
        if (world != null)
        {
            TileEntity tileEntity = senderPos.getTileEntity(world);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkConnectionProxy)
                return ((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection();
        }
        return null;
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null)
        {
            senderPos = new BlockPosition(tagCompound);
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null && senderPos != null)
        {
            senderPos.writeToNBT(tagCompound);
        }
    }

    public abstract boolean isValid(World world);

    public boolean isGuided(){return receiverPos != null;}

    public BlockPosition getReceiverPos(){return receiverPos;}

    public abstract String getName();

    public ForgeDirection getSenderPort()
    {
        return senderPos.orientation;
    }
}
