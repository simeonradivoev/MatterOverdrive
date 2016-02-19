package matteroverdrive.compat.modules;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import matteroverdrive.compat.Compat;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author shadowfacts
 */
@Compat("MineFactoryReloaded")
public class CompatMFR {

	@Compat.Init
	public void init(FMLInitializationEvent event) {
		NBTTagCompound dilithiumTag = new NBTTagCompound();
		new ItemStack(MatterOverdriveBlocks.dilithium_ore).writeToNBT(dilithiumTag);
		dilithiumTag.setInteger("value", 3);
		FMLInterModComms.sendMessage("MineFactoryReloaded", "addLaserPreferredOre", dilithiumTag);

		NBTTagCompound tritaniumTag = new NBTTagCompound();
		new ItemStack(MatterOverdriveBlocks.tritaniumOre).writeToNBT(tritaniumTag);
		tritaniumTag.setInteger("value", 3);
		FMLInterModComms.sendMessage("MineFactoryReloaded", "addLaserPreferredOre", tritaniumTag);
	}

}
