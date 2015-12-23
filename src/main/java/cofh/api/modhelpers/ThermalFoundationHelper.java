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

package cofh.api.modhelpers;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * The purpose of this class is to show how to use and provide an interface for Thermal Foundation's IMC Lexicon Blacklist manipulation.
 *
 * This is really the only safe way to do this. Please do not attempt any direct Lexicon manipulation.
 *
 * @author King Lemming
 *
 */
public class ThermalFoundationHelper {

	private ThermalFoundationHelper() {

	}

	/* Lexicon */
	public static void addBlacklistEntry(ItemStack entry) {

		if (entry == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setTag("entry", new NBTTagCompound());

		entry.writeToNBT(toSend.getCompoundTag("entry"));
		FMLInterModComms.sendMessage("ThermalFoundation", "AddLexiconBlacklistEntry", toSend);
	}

	public static void removeBlacklistEntry(ItemStack entry) {

		if (entry == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setTag("entry", new NBTTagCompound());

		entry.writeToNBT(toSend.getCompoundTag("entry"));
		FMLInterModComms.sendMessage("ThermalFoundation", "RemoveLexiconBlacklistEntry", toSend);
	}

}
