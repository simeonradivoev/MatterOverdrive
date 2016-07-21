package matteroverdrive.init;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.data.MatterStorage;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.shadowfacts.shadowmc.capability.Storage;

/**
 * @author shadowfacts
 */
public class MatterOverdriveCapabilities
{

//	Tesla
	@CapabilityInject(ITeslaHolder.class)
	public static Capability<ITeslaHolder> TESLA_HOLDER;

	@CapabilityInject(ITeslaConsumer.class)
	public static Capability<ITeslaConsumer> TESLA_CONSUMER;

	@CapabilityInject(ITeslaProducer.class)
	public static Capability<ITeslaProducer> TESLA_PRODUCER;

//	MO
	@CapabilityInject(IMatterHandler.class)
	public static Capability<IMatterHandler> MATTER_HANDLER;

	public static void init()
	{
		CapabilityManager.INSTANCE.register(IMatterHandler.class, new Storage<>(), () -> new MatterStorage(2000));
	}

}
