package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IGridNetwork;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.handler.matter_network.GridNetworkHandler;
import matteroverdrive.util.MOLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.*;

/**
 * Created by Simeon on 1/20/2016.
 */
public abstract class AbstractGridNetwork<T extends IGridNode> implements IGridNetwork<T>
{
	private final Class<T> nodeTypes;
	private final Set<T> nodes;
	private GridNetworkHandler gridNetworkHandler;

	public AbstractGridNetwork(GridNetworkHandler gridNetworkHandler, final Class<T> nodeTypes)
	{
		this.nodeTypes = nodeTypes;
		this.nodes = new HashSet<>();
		this.gridNetworkHandler = gridNetworkHandler;
	}

	@Override
	public void onNodeDestroy(final IBlockState blockState, final T node)
	{
		removeNode(node);

		if (nodes.size() > 0)
		{
			ConnectionPathfind<T> pathfind = new ConnectionPathfind(node, nodeTypes);
			List<T> connections = new ArrayList<>();

			for (EnumFacing dir : EnumFacing.VALUES)
			{
				if (node.canConnectFromSide(blockState, dir))
				{
					BlockPos otherNodePos = node.getNodePos().offset(dir);
					if (node.getNodeWorld().isBlockLoaded(otherNodePos))
					{
						TileEntity otherNodeTile = node.getNodeWorld().getTileEntity(otherNodePos);
						IBlockState otherNodeBlockState = node.getNodeWorld().getBlockState(otherNodePos);
						if (nodeTypes.isInstance(otherNodeTile))
						{
							T otherNode = nodeTypes.cast(otherNodeTile);
							if (node.canConnectToNetworkNode(blockState, otherNode, dir) && otherNode.canConnectToNetworkNode(otherNodeBlockState, node, dir.getOpposite()))
							{
								connections.add(otherNode);
							}
						}
					}
				}
			}

			if (connections.size() > 1)
			{
				for (T c : connections)
				{
					if (!pathfind.init(c))
					{
						for (T n : pathfind.getBurnedNodes())
						{
							removeNode(n);
						}
					}
				}
			}
		}

		if (nodes.size() <= 0)
		{
			recycle();
			gridNetworkHandler.recycleNetwork(this);
		}
	}

	@Override
	public void addNode(T node)
	{
		if (node.getNetwork() != null)
		{
			if (node.getNetwork() != this)
			{
				if (node.getNetwork().canMerge(this))
				{
					mergeNetworks(node.getNetwork());
				}
			}
			else
			{
				MOLog.info("Adding Node %s to it's own network %s", node, this);
			}
		}
		else
		{
			nodes.add(node);
			node.setNetwork(this);
			onNodeAdded(node);
		}
	}

	protected void mergeNetworks(IGridNetwork<T> network)
	{
		Iterator<T> iterator = network.getNodesIterator();
		while (iterator.hasNext())
		{
			T node = iterator.next();
			iterator.remove();
			node.setNetwork(null);
			addNode(node);
		}
		network.recycle();
		gridNetworkHandler.recycleNetwork(network);
	}

	//removed the node from the network and removes the network reference in the node
	@Override
	public void removeNode(T node)
	{
		if (node.getNetwork() == null || node.getNetwork() == this)
		{
			node.setNetwork(null);
			nodes.remove(node);
			onNodeRemoved(node);
		}
		else if (node.getNetwork() != null)
		{
			MOLog.info("Trying to remove node from a different network than it's assigned one");
		}
	}

	@Override
	public void recycle()
	{
		nodes.clear();
	}

	@Override
	public Collection<T> getNodes()
	{
		return nodes;
	}

	@Override
	public Iterator<T> getNodesIterator()
	{
		return nodes.iterator();
	}

	protected abstract void onNodeAdded(T node);

	protected abstract void onNodeRemoved(T node);
}
