package matteroverdrive.data.transport;

import matteroverdrive.util.FluidNetworkHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Simeon on 12/28/2015.
 */
public class FluidPipeNetwork extends AbstractPipeNetwork<IFluidPipe>
{
    Set<IFluidPipe> fluidPipes;
    Set<IFluidPipe> fluidHandlers;

    public FluidPipeNetwork()
    {
        fluidHandlers = new HashSet<>();
        fluidPipes = new HashSet<>();
    }

    @Override
    public void invalidateNetwork()
    {
        super.invalidateNetwork();
        FluidNetworkHelper.addFluidPipeToPool(this);
    }

    @Override
    public void addPipe(IFluidPipe pipe)
    {
        super.addPipe(pipe);
        manageFluidHandlersAdding(pipe);
    }

    public void manageFluidHandlersAdding(IFluidPipe fluidPipe)
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = fluidPipe.getTile().getWorldObj().getTileEntity(fluidPipe.getTile().xCoord+direction.offsetX,fluidPipe.getTile().yCoord+direction.offsetY,fluidPipe.getTile().zCoord+direction.offsetZ);
            if (tileEntity instanceof IFluidHandler && !(tileEntity instanceof IFluidPipe) && !fluidHandlers.contains(tileEntity))
            {
                fluidHandlers.add(fluidPipe);
                return;
            }
        }
    }

    @Override
    public void removePipe(IFluidPipe pipe)
    {
        super.removePipe(pipe);
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = pipe.getTile().getWorldObj().getTileEntity(pipe.getTile().xCoord+direction.offsetX,pipe.getTile().yCoord+direction.offsetY,pipe.getTile().zCoord+direction.offsetZ);
            if (tileEntity instanceof IFluidHandler && !(tileEntity instanceof IFluidPipe))
            {
                fluidHandlers.remove(pipe);
            }
        }
    }

    @Override
    public void networkUpdate(IFluidPipe fluidPipe)
    {
        boolean stillAHandler = false;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = fluidPipe.getTile().getWorldObj().getTileEntity(fluidPipe.getTile().xCoord+direction.offsetX,fluidPipe.getTile().yCoord+direction.offsetY,fluidPipe.getTile().zCoord+direction.offsetZ);
            if (tileEntity instanceof IFluidHandler && !(tileEntity instanceof IFluidPipe))
            {
                stillAHandler = true;
                break;
            }
        }
        if (!stillAHandler)
        {
            fluidHandlers.remove(fluidPipe);
        }

        manageFluidHandlersAdding(fluidPipe);
    }

    public Collection<IFluidPipe> getFluidHandlers(){return fluidHandlers;}

    @Override
    public Collection<IFluidPipe> getNetworkPipes()
    {
        return fluidPipes;
    }
}
