package matteroverdrive.compat.modules.top;

import com.google.common.base.Function;
import matteroverdrive.compat.Compat;
import matteroverdrive.compat.modules.top.provider.*;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * @author shadowfacts
 */
@Compat(CompatTOP.ID)
public class CompatTOP
{

	public static final String ID = "theoneprobe";

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
		FMLInterModComms.sendFunctionMessage(ID, "getTheOneProbe", "matteroverdrive.compat.modules.top.CompatTOP$Handler");
	}

	public static class Handler implements Function<ITheOneProbe, Void>
	{

		@Override
		public Void apply(ITheOneProbe top)
		{
			top.registerProvider(new Matter());
			top.registerProvider(new Replicator());
			top.registerProvider(new StarMap());
			top.registerProvider(new Transporter());
			top.registerProvider(new WeaponStation());

			return null;
		}

	}

}
