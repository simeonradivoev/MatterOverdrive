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
 * @<code>NBTTagCompound data = new NBTTagCompound();
NBTTagCompound itemNBT = new NBTTagCompound();
data.setTag("Main",new ItemStack(Items.carrot).writeToNBT(itemNBT));
itemNBT = new NBTTagCompound();
data.setTag("Sec",new ItemStack(Items.gold_nugget).writeToNBT(itemNBT));
itemNBT = new NBTTagCompound();
data.setTag("Output",new ItemStack(Items.golden_carrot).writeToNBT(itemNBT));
data.setInteger("Energy",16000);
data.setInteger("Time",200);
FMLInterModComms.sendMessage("mo",IMC.INSCRIBER_RECIPE,data);

data = new NBTTagCompound();
itemNBT = new NBTTagCompound();
data.setTag("Item",new ItemStack(Items.carrot).writeToNBT(itemNBT));
data.setInteger("Matter",666);
FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTER,data);

FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTRY_BLACKLIST,new ItemStack(Items.carrot));

FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTRY_BLACKLIST_MOD,"modeID");</code>
 * @author shadowfacts
 */
public interface IMC {

	/**
	 * Adds the specified ItemStack to the Matter Registry blacklist
	 */
	String MATTER_REGISTRY_BLACKLIST = "registry:blacklist:add";

	String MATTER_REGISTRY_BLACKLIST_MOD = "registry:blacklist:mod:add";

	String MATTER_REGISTER = "registry:matter:add";

	String INSCRIBER_RECIPE = "registry:inscriber:recipe:add";
}
