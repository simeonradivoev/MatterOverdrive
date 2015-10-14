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

package matteroverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * A wrapper for the {@link IFluidHandler}.
 */
public interface IMatterHandler extends IMatterProvider,IMatterReceiver,IFluidHandler
{
	/**
	 * Gets the matter stored
	 * @return the matter stored
	 */
	@Override
	int getMatterStored();

	/**
	 * Gets the matter capacity.
	 * @return the matter capacity.
	 */
	@Override
	int getMatterCapacity();

	/**
	 * Used to receive matter.
	 * @param side side from which matter is received.
	 * @param amount amount of received matter.
	 * @param simulate is this a simulation ?
	 * @return amount of actually received matter.
	 */
	@Override
	int receiveMatter(ForgeDirection side,int amount, boolean simulate);

	/**
	 * Used to extract matter.
	 * @param direction side from which matter is extracted.
	 * @param amount amount of extracted matter.
	 * @param simulate is this a simulation ?
	 * @return amount of actually extracted matter.
	 */
	@Override
	int extractMatter(ForgeDirection direction,int amount,boolean simulate);
}
