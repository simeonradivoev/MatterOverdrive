package matteroverdrive.api.transport;

import matteroverdrive.data.transport.AbstractGridNetwork;
import matteroverdrive.data.transport.AbstractPipeNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 12/28/2015.
 */
public interface IPipe<T extends AbstractGridNetwork> extends IGridNode<T>
{

}
