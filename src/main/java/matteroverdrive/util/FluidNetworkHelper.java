package matteroverdrive.util;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.transport.FluidPipeNetwork;

import java.util.Stack;

/**
 * Created by Simeon on 12/28/2015.
 */
public class FluidNetworkHelper
{
    private static final int MAX_POOL_SIZE = 32;
    private static Stack<FluidPipeNetwork> fluidPipeNetworksPool = new Stack<>();

    public static FluidPipeNetwork getFluidPipeNetworkFromPool()
    {
        if (fluidPipeNetworksPool.size() > 0)
        {
            return fluidPipeNetworksPool.pop();
        }else
        {
            return new FluidPipeNetwork();
        }
    }

    public static void addFluidPipeToPool(FluidPipeNetwork pipeNetwork)
    {
        if (fluidPipeNetworksPool.size() > MAX_POOL_SIZE)
        {
            MatterOverdrive.log.warn("Fluid Pipe Network pool reached max size of %s",MAX_POOL_SIZE);
        }
        else
        {
            fluidPipeNetworksPool.push(pipeNetwork);
        }
    }
}
