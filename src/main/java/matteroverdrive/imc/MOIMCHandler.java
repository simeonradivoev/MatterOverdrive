package matteroverdrive.imc;

import cpw.mods.fml.common.event.FMLInterModComms;
import matteroverdrive.api.IMC;
import matteroverdrive.handler.MatterRegistry;

/**
 * @author shadowfacts
 */
public class MOIMCHandler {

	public static void imcCallback(FMLInterModComms.IMCEvent event) {
		event.getMessages().forEach(MOIMCHandler::handleMessage);
	}

	public static void handleMessage(FMLInterModComms.IMCMessage msg) {
		switch (msg.key) {
			case IMC.MATTER_REGISTRY_BLACKLIST:
				MatterRegistry.addToBlacklist(msg.getItemStackValue());
		}
	}

}
