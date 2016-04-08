package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.util.MOLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Simeon on 1/20/2016.
 */
public class ConnectionPathfind<T extends IGridNode>
{
    private Class<T> nodeTypes;
    private final Set<T> burned;
    private TileEntity neighborTileTmp;
    private T neighborTmp;
    private IBlockState neighborTmpState;
    private BlockPos neighborPosTmp;
    private final T target;

    public ConnectionPathfind(final T target,final Class<T> nodeTypes)
    {
        this.burned = new HashSet<>();
        this.target = target;
        this.nodeTypes = nodeTypes;
    }

    public boolean init(final T startNode)
    {
        burned.clear();
        burned.add(startNode);

        for (EnumFacing d : EnumFacing.VALUES)
        {
            if (startNode.canConnectFromSide(startNode.getWorld().getBlockState(startNode.getPos()),d))
            {
                BlockPos neighborPos = startNode.getPos().offset(d);
                TileEntity neighborTile = startNode.getWorld().getTileEntity(neighborPos);
                if (neighborTile instanceof IGridNode && neighborTile != target)
                {
                    if (isConnectedToSourceRecursive((T) neighborTile))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isConnectedToSourceRecursive(final T node)
    {
        this.burned.add(node);
        for (EnumFacing dir : EnumFacing.VALUES)
        {
            if (node.canConnectFromSide(node.getWorld().getBlockState(node.getPos()),dir))
            {
                neighborPosTmp = node.getPos().offset(dir);
                if (node.getWorld().isBlockLoaded(neighborPosTmp))
                {
                    neighborTileTmp = node.getWorld().getTileEntity(neighborPosTmp);
                    if (nodeTypes.isInstance(neighborTileTmp))
                    {
                        neighborTmp = nodeTypes.cast(neighborTileTmp);
                        neighborTmpState = neighborTmp.getWorld().getBlockState(neighborPosTmp);
                        if (neighborTmp == target || !this.burned.contains(neighborTmp))
                        {
                            if (node.canConnectToNetworkNode(node.getWorld().getBlockState(node.getPos()), neighborTmp, dir) && neighborTmp.canConnectToNetworkNode(neighborTmpState, node, dir.getOpposite()))
                            {
                                if (neighborTmp == target)
                                    return true;

                                if (isConnectedToSourceRecursive(neighborTmp))
                                {
                                    return true;
                                }
                            } else
                            {
                                MOLog.info("Matter Network Pathfind: cannot connect to node");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Collection<T> getBurnedNodes()
    {
        return burned;
    }
}
