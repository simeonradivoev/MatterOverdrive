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

package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.fluids.FluidMoltenTritanium;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Simeon on 8/20/2015.
 */
public class MatterOverdriveFluids
{
	public static FluidMatterPlasma matterPlasma;
	public static FluidMoltenTritanium moltenTritanium;

	public static void init(FMLPreInitializationEvent event)
	{
		matterPlasma = new FluidMatterPlasma("matter_plasma");
		FluidRegistry.registerFluid(matterPlasma);

		moltenTritanium = new FluidMoltenTritanium("molten_tritanium");
		FluidRegistry.registerFluid(moltenTritanium);
		FluidRegistry.addBucketForFluid(moltenTritanium);

		registerFluidContainers();
	}

	@SuppressWarnings("deprecation")
	private static void registerFluidContainers()
	{
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(matterPlasma, 32), new ItemStack(MatterOverdrive.items.matterContainerFull), new ItemStack(MatterOverdrive.items.matterContainer));
	}
}
