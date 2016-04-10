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

package matteroverdrive.imc;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.IMC;
import matteroverdrive.data.matter.DamageAwareStackHandler;
import matteroverdrive.data.matter.ItemHandler;
import matteroverdrive.data.matter.OreHandler;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.handler.recipes.InscriberRecipes;
import matteroverdrive.util.MOLog;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;

/**
 * @author shadowfacts
 */
public class MOIMCHandler
{

	public static void imcCallback(FMLInterModComms.IMCEvent event)
	{
		event.getMessages().forEach(MOIMCHandler::handleMessage);
	}

	public static void handleMessage(FMLInterModComms.IMCMessage msg)
	{
		switch (msg.key)
		{
			case IMC.MATTER_REGISTRY_BLACKLIST:
				handleItemBlacklistRegistration(msg);
				break;
			case IMC.MATTER_REGISTRY_BLACKLIST_MOD:
				MatterOverdrive.matterRegistry.addModToBlacklist(msg.getStringValue());
				break;
			case IMC.INSCRIBER_RECIPE:
				handleInscriberRecipeRegistration(msg);
				break;
			case IMC.MATTER_REGISTER:
				handleMatterRegistration(msg);
				break;
		}
	}

	private static void handleMatterRegistration(FMLInterModComms.IMCMessage msg)
	{
		if (!msg.isNBTMessage())
		{
			MOLog.warn("Invalid message type for Matter Registration. Message needs to be of type NBT");
			return;
		}
		try
		{
			NBTTagCompound data = msg.getNBTValue();
			if (containsAllTags(data, "Matter"))
			{
				int matter = data.getInteger("Matter");
				boolean isFinalHandler = data.getBoolean("FinalMatter");
				if (data.hasKey("Item", Constants.NBT.TAG_COMPOUND))
				{
					ItemStack itemStack = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Item"));

					if (data.getBoolean("MetaAware"))
					{
						MatterOverdrive.matterRegistry.register(itemStack.getItem(), new DamageAwareStackHandler(itemStack.getItemDamage(), matter, isFinalHandler));
					}
					else
					{
						MatterOverdrive.matterRegistry.register(itemStack.getItem(), new ItemHandler(matter, isFinalHandler));
					}
				}
				else if (data.hasKey("Ore", Constants.NBT.TAG_STRING))
				{
					String oreName = data.getString("Ore");
					MatterOverdrive.matterRegistry.registerOre(oreName, new OreHandler(matter, isFinalHandler));
				}
			}
		}
		catch (Exception e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while trying to register an Item in the Matter Registry from: %s", msg.getSender());
		}
	}

	private static void handleItemBlacklistRegistration(FMLInterModComms.IMCMessage msg)
	{
		try
		{


			if (msg.isItemStackMessage())
			{
				ItemStack itemStack = msg.getItemStackValue();
				if (itemStack != null)
				{
					MatterOverdrive.matterRegistry.register(itemStack.getItem(), new DamageAwareStackHandler(itemStack.getItemDamage(), 0, true));
				}
			}
			else if (msg.isNBTMessage())
			{
				NBTTagCompound data = msg.getNBTValue();
				ItemStack itemStack = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Item"));
				if (data.hasKey("Item", Constants.NBT.TAG_COMPOUND))
				{
					if (data.getBoolean("MetaAware"))
					{
						MatterOverdrive.matterRegistry.register(itemStack.getItem(), new DamageAwareStackHandler(itemStack.getItemDamage(), 0, true));
					}
					else
					{
						MatterOverdrive.matterRegistry.register(itemStack.getItem(), new ItemHandler(0, true));
					}
				}
				else if (data.hasKey("Ore", Constants.NBT.TAG_STRING))
				{
					String oreName = data.getString("Ore");
					MatterOverdrive.matterRegistry.registerOre(oreName, new OreHandler(0, true));
				}
			}
			else
			{
				MOLog.warn("Invalid message type for Matter Blacklisting. Message needs to be of type NBT or ItemStack");
			}
		}
		catch (Exception e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while trying to blacklist an Item in the Matter Registry from: %s", msg.getSender());
		}
	}

	private static void handleInscriberRecipeRegistration(FMLInterModComms.IMCMessage msg)
	{
		if (!msg.isNBTMessage())
		{
			MOLog.error("Invalid message format for Inscriber Recipe registration. Message needs to be of type NBT");
			return;
		}
		try
		{
			NBTTagCompound data = msg.getNBTValue();
			if (containsAllTags(data, "Main", "Sec", "Output", "Energy", "Time"))
			{
				ItemStack mainStack = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Main"));
				ItemStack secStack = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Sec"));
				ItemStack output = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Output"));
				int energy = data.getInteger("Energy");
				int time = data.getInteger("Time");
				if (mainStack != null && secStack != null && output != null)
				{
					InscriberRecipe recipe = new InscriberRecipe(mainStack, secStack, output, energy, time);
					InscriberRecipes.registerRecipe(recipe);
				}
			}
		}
		catch (Exception e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while trying to register an Inscriber Recipe from: %s", msg.getSender());
		}
	}

	private static boolean containsAllTags(NBTTagCompound tagCompound, String... tags)
	{
		for (String tag : tags)
		{
			if (!tagCompound.hasKey(tag))
			{
				return false;
			}
		}
		return true;
	}

}
