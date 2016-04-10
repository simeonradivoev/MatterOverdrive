package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IGridNetwork;
import matteroverdrive.handler.matter_network.FluidNetworkHandler;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Simeon on 12/28/2015.
 */
public class FluidPipeNetwork extends AbstractGridNetwork<IFluidPipe>
{
	private Fluid fluidType;
	private int fluidReqiest;

	public FluidPipeNetwork(FluidNetworkHandler networkHandler)
	{
		super(networkHandler, IFluidPipe.class);
	}

	@Override
	public boolean canMerge(IGridNetwork network)
	{
		return true;
	}

	@Override
	protected void onNodeAdded(IFluidPipe node)
	{

	}

	@Override
	protected void onNodeRemoved(IFluidPipe node)
	{

	}
}
