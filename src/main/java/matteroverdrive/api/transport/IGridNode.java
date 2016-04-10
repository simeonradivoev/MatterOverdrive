package matteroverdrive.api.transport;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/20/2016.
 */
public interface IGridNode<T extends IGridNetwork>
{
	T getNetwork();

	void setNetwork(T network);

	BlockPos getPos();

	World getWorld();

	boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction);

	/**
	 * Can the Matter Connection connect form a given side.
	 *
	 * @param blockState
	 * @param side the side of the tested connection.
	 * @return can the connection be made trough the given side.
	 */
	boolean canConnectFromSide(IBlockState blockState, EnumFacing side);
}
