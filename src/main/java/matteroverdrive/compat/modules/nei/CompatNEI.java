/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.compat.modules.nei;

import matteroverdrive.compat.Compat;
import net.minecraftforge.fml.common.Optional;

/**
 * @author shadowfacts
 */
@Compat("NotEnoughItems")
@Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
public class CompatNEI //implements INEIGuiHandler
{
	/*@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event)
	{
		API.registerNEIGuiHandler(new CompatNEI());
		API.hideItem(new ItemStack(MatterOverdriveBlocks.boundingBox));
		CraftingHandlerInscriber handlerInscriber = new CraftingHandlerInscriber();
		API.registerRecipeHandler(handlerInscriber);
		API.registerUsageHandler(handlerInscriber);
	}

	@Override
	public VisiblityData modifyVisiblity(GuiContainer guiContainer, VisiblityData visiblityData)
	{
		if (guiContainer instanceof GuiStarMap)
		{
			visiblityData.showNEI = false;
		}
		return visiblityData;
	}

	@Override
	public Iterable<Integer> getItemSpawnSlots(GuiContainer guiContainer, ItemStack itemStack) {
		return new ArrayList<Integer>();
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
	}*/
}
