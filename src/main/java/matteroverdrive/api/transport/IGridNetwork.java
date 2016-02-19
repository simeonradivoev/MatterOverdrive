package matteroverdrive.api.transport;

import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Simeon on 1/20/2016.
 */
public interface IGridNetwork<T extends IGridNode>
{
    void onNodeDestroy(final IBlockState blockState, T node);
    void addNode(T node);
    void removeNode(T node);
    Collection<T> getNodes();
    Iterator<T> getNodesIterator();
    boolean canMerge(IGridNetwork network);
    void recycle();
}
