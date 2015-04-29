package matter_network;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.IMatterNetworkDispatcher;
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
    protected HashSet<MatterNetworkPathNode> path;

    public MatterNetworkPacket(){path = new HashSet<MatterNetworkPathNode>();}
    public MatterNetworkPacket(BlockPosition senderPos)
    {
        this();
        this.senderPos = senderPos;
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

    public IMatterNetworkDispatcher getSender(World world)
    {
        if (world != null)
        {
            TileEntity tileEntity = senderPos.getTileEntity(world);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkConnectionProxy && ((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection() instanceof IMatterNetworkDispatcher)
                return (IMatterNetworkDispatcher)((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection();
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

    public abstract String getName();
}
