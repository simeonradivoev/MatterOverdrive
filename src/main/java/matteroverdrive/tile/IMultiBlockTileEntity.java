package matteroverdrive.tile;

import cofh.repack.codechicken.lib.vec.BlockCoord;

import java.util.List;

/**
 * Used by
 *
 * @author shadowfacts
 */
public interface IMultiBlockTileEntity {

	List<BlockCoord> getBoundingBlocks();

}
