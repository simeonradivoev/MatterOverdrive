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

package matteroverdrive.api;

/**
 * IMC message names for Matter Overdrive.
 * <b>Any changes other than additions are strictly forbidden.</b>
 *
 * @author shadowfacts
 */
public class IMC {

	/**
	 * Adds the specified ItemStack to the Matter Registry blacklist
	 */
	public static final String MATTER_REGISTRY_BLACKLIST = "registry:blacklist:add";

	public static final String MATTER_REGISTRY_BLACKLIST_MOD = "registry:blacklist:mod:add";

	public static final String MATTER_REGISTRY_REGISTER = "registry:register";

}
