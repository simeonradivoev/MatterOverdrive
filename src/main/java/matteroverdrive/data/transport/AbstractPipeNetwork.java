package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IGridNetwork;
import matteroverdrive.api.transport.IPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Simeon on 12/28/2015.
 */
public abstract class AbstractPipeNetwork<T extends IPipe> implements IGridNetwork<T>
{

}
