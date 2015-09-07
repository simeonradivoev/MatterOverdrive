package matteroverdrive.compat.modules;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.API;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import matteroverdrive.compat.Compat;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author shadowfacts
 */
@Compat("NotEnoughItems")
@Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
public class CompatNEI implements INEIGuiHandler {

	@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event) {
		API.registerNEIGuiHandler(new CompatNEI());
		API.hideItem(new ItemStack(MatterOverdriveBlocks.boundingBox));
	}

	@Override
	public VisiblityData modifyVisiblity(GuiContainer guiContainer, VisiblityData visiblityData) {
		if (guiContainer instanceof GuiStarMap) {
			visiblityData.showNEI = false;
		}
		return visiblityData;
	}

	@Override
	public Iterable<Integer> getItemSpawnSlots(GuiContainer guiContainer, ItemStack itemStack) {
		return null;
	}

	@Override
	public List<TaggedInventoryArea> getInventoryAreas(GuiContainer guiContainer) {
		return null;
	}

	@Override
	public boolean handleDragNDrop(GuiContainer guiContainer, int i, int i1, ItemStack itemStack, int i2) {
		return false;
	}

	@Override
	public boolean hideItemPanelSlot(GuiContainer guiContainer, int i, int i1, int i2, int i3) {
		return false;
	}
}
