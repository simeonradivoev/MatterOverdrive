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
import matteroverdrive.Reference;
import matteroverdrive.data.matter.DamageAwareStackHandler;
import matteroverdrive.data.matter.MatterEntryItem;
import matteroverdrive.data.matter.OreHandler;
import matteroverdrive.handler.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatterOverdriveMatter
{

	public static void registerBasic(ConfigurationHandler c)
	{
		registerBasicItems(c);
		registerBasicBlocks(c);

		registerBasicCompoundItems(c);
	}

	public static void registerBlacklistFromConfig(ConfigurationHandler c)
	{
		MatterOverdrive.matterRegistry.loadModBlacklistFromConfig(c);
	}

	public static void registerBasicBlocks(ConfigurationHandler c)
	{
		reg(c, Blocks.DIRT, 1, 3);
		reg(c, Blocks.GRASS, 1, 3);
		reg(c, "blockWool", 2);
		reg(c, "blockCloth", 2);
		reg(c, "blockGlass", 5);
		reg(c, "cobblestone", 1);
		reg(c, "logWood", 16);
		reg(c, "sand", 2);
		reg(c, Blocks.GRAVEL, 2);
		reg(c, "sandstone", 4);
		reg(c, Blocks.CLAY, 4);
		reg(c, Blocks.CACTUS, 4);
		reg(c, "plankWood", 4);
		reg(c, Blocks.END_STONE, 6);
		reg(c, "stone", 1);
		reg(c, Blocks.SOUL_SAND, 4);
		reg(c, Blocks.SNOW, 2);
		reg(c, Blocks.PUMPKIN, 2);
		reg(c, Blocks.OBSIDIAN, 16);
		reg(c, "treeLeaves", 1);
		reg(c, Blocks.MYCELIUM, 5);
		reg(c, Blocks.ICE, 3);
		reg(c, Blocks.PACKED_ICE, 4);
		reg(c, "blockGlass", 3);
		reg(c, "paneGlass", 1);
		reg(c, Blocks.BEDROCK, 1024);
		reg(c, Blocks.SPONGE, 8);
		reg(c, Blocks.VINE, 1);
		reg(c, Blocks.TALLGRASS, 1);
		reg(c, Blocks.MOSSY_COBBLESTONE, 2);
		reg(c, Blocks.NETHERRACK, 1);
		reg(c, Blocks.CLAY, 3, 16);
		reg(c, Blocks.HARDENED_CLAY, 3);
		reg(c, Blocks.STONEBRICK, 2, 4);
		reg(c, Blocks.COBBLESTONE_WALL, 1);
		reg(c, Blocks.WEB, 1);

		//region flowers
		reg(c, Blocks.RED_FLOWER, 1, 9);
		reg(c, Blocks.YELLOW_FLOWER, 1);
		reg(c, Blocks.BROWN_MUSHROOM, 1);
		reg(c, Blocks.BROWN_MUSHROOM_BLOCK, 1);
		reg(c, Blocks.RED_MUSHROOM, 1);
		reg(c, Blocks.RED_MUSHROOM_BLOCK, 1);
		reg(c, Blocks.DEADBUSH, 1);
		reg(c, Blocks.WATERLILY, 1);
		reg(c, "treeSapling", 2);
		reg(c, Blocks.DOUBLE_PLANT, 1, 6);
		//endregion
	}


	public static void registerBasicItems(ConfigurationHandler c)
	{
		reg(c, new ItemStack(Items.APPLE), 1);
		reg(c, Items.ARROW, 1);
		reg(c, Items.BAKED_POTATO, 1);
		reg(c, Items.BEEF, 2);
		reg(c, Items.BLAZE_ROD, 4);
		reg(c, Items.BONE, 2);
		reg(c, "cropCarrot", 1);
		reg(c, Items.CLAY_BALL, 1);
		reg(c, Items.COAL, 8);
		reg(c, new ItemStack(Items.COAL, 1, 1), 5);
		reg(c, Items.EGG, 1);
		reg(c, new ItemStack(Items.DYE, 1, 3), 1);
		reg(c, new ItemStack(Items.DYE, 1, 0), 1);
		reg(c, Items.ENDER_PEARL, 8);
		reg(c, Items.FEATHER, 1);
		reg(c, Items.FERMENTED_SPIDER_EYE, 1);
		reg(c, Items.FLINT, 1);
		reg(c, Items.FISH, 1, 4);
		reg(c, Items.GHAST_TEAR, 8);
		reg(c, "nuggetGold", 4);
		reg(c, Items.GUNPOWDER, 2);
		reg(c, Items.MELON, 1);
		reg(c, "cropWheat", 1);
		reg(c, Items.WHEAT_SEEDS, 1);
		reg(c, Items.SUGAR, 1);
		reg(c, Items.STRING, 1);
		reg(c, "stickWood", 1);
		reg(c, "dustRedstone", 4);
		reg(c, "dustGlowstone", 2);
		reg(c, Items.SPIDER_EYE, 1);
		reg(c, Items.SADDLE, 18);
		reg(c, Items.REEDS, 1);
		reg(c, "cropPotato", 1);
		reg(c, Items.LEATHER, 3);
		reg(c, Items.PUMPKIN_SEEDS, 1);
		reg(c, Items.PORKCHOP, 2);
		reg(c, Items.COOKED_PORKCHOP, 4);
		reg(c, Items.PAPER, 1);
		reg(c, Items.LAVA_BUCKET, 24 + 96);
		reg(c, Items.WATER_BUCKET, 12 + 96);
		reg(c, Items.MILK_BUCKET, 12 + 96);
		reg(c, "ingotBrickNether", 1);
		reg(c, Items.NETHER_WART, 3);
		reg(c, Items.NETHER_STAR, 1024);
		reg(c, Items.IRON_HORSE_ARMOR, 32 * 5);
		reg(c, Items.GOLDEN_HORSE_ARMOR, 42 * 5);
		reg(c, Items.DIAMOND_HORSE_ARMOR, 256 * 5);
		reg(c, Items.EXPERIENCE_BOTTLE, 32);
		reg(c, "slimeball", 2);
		reg(c, "record", 4);
		reg(c, Items.CHICKEN, 2);
		reg(c, Items.RABBIT, 2);
		reg(c, Items.MUTTON, 2);
		reg(c, Items.COOKED_CHICKEN, 3);
		reg(c, Items.ROTTEN_FLESH, 1);
		reg(c, "dustSaltpeter", 2);
		reg(c, "dustSulfur", 2);
		reg(c, Items.NAME_TAG, 32);
		reg(c, new ItemStack(Items.SKULL, 1, 0), 16); //skeleton
		reg(c, new ItemStack(Items.SKULL, 1, 1), 64); //wither
		reg(c, new ItemStack(Items.SKULL, 1, 2), 12); //zombie
		reg(c, new ItemStack(Items.SKULL, 1, 4), 19); //creeper
		reg(c, Items.GLASS_BOTTLE, 3);
		reg(c, "silicon", 2);

		//region Gems
		reg(c, "gemDiamond", 256);
		reg(c, "gemQuartz", 3);
		reg(c, "gemLapis", 4);
		reg(c, "gemEmerald", 256);
		reg(c, "gemRuby", 64);
		reg(c, "gemRupee", 64);
		reg(c, "gemSapphire", 64);
		//endregion

		//region Ingots
		reg(c, "ingotBrick", 2);
		reg(c, "ingotIron", 32);
		reg(c, "ingotGold", 42);
		reg(c, "ingotTin", 28);
		reg(c, "ingotCopper", 28);
		reg(c, "ingotAluminum", 26);
		reg(c, "ingotSilver", 30);
		reg(c, "ingotLead", 32);
		reg(c, "ingotNickel", 32);
		reg(c, "ingotInvar", 38);
		reg(c, "ingotPlatinum", 64);
		reg(c, "ingotBronze", 28);
		reg(c, "ingotRedAlloy", 24);
		reg(c, "ingotUranium", 64);
		reg(c, "ingotZinc", 30);
		reg(c, "ingotQuartz", 24);
		reg(c, "ingotSteel", 38);
		//endregion

		//region Plants
		reg(c, Items.REEDS, 1);
		//endregion

		//region dyes
		reg(c, new ItemStack(Items.DYE, 1, 2), 1);
		reg(c, new ItemStack(Items.DYE, 1, 3), 1);
		//endregion

		//region rouge android parts
		reg(c, new ItemStack(MatterOverdriveItems.androidParts, 1, Reference.BIONIC_HEAD), 64 * 5);
		reg(c, new ItemStack(MatterOverdriveItems.androidParts, 1, Reference.BIONIC_ARMS), 64 * 6);
		reg(c, new ItemStack(MatterOverdriveItems.androidParts, 1, Reference.BIONIC_LEGS), 64 * 6);
		reg(c, new ItemStack(MatterOverdriveItems.androidParts, 1, Reference.BIONIC_CHEST), 64 * 9);
		//endregion

		//region Matter Overdrive Basic Items
		reg(c, MatterOverdriveItems.emergency_ration, 3);
		reg(c, MatterOverdriveItems.earl_gray_tea, 2);
		reg(c, MatterOverdriveItems.romulan_ale, 2);
		reg(c, new ItemStack(MatterOverdriveItems.androidPill, 1, 1), 64);
		reg(c, new ItemStack(MatterOverdriveItems.androidPill, 1, 2), 32);
		//endregion
	}

	public static void registerBasicCompoundItems(ConfigurationHandler c)
	{
		reg(c, "dustObsidian", 0, Blocks.OBSIDIAN);
		reg(c, "dustCharcoal", 0, new ItemStack(Items.COAL, 1, 1));
		reg(c, "dustCoal", 0, Items.COAL);
		reg(c, "dustDiamond", 0, "gemDiamond");
		reg(c, "dustFlour", 0, "cropWheat");
		reg(c, "dustNetherQuartz", 0, "oreQuartz");
		reg(c, "gemGreenSapphire", 0, "gemEmerald");
		reg(c, "dustEmerald", 0, "gemEmerald");


		//region dusts
		reg(c, "dustIron", 0, "ingotIron");
		reg(c, "dustGold", 0, "ingotGold");
		reg(c, "dustTin", 0, "ingotTin");
		reg(c, "dustCopper", 0, "ingotCopper");
		reg(c, "dustAluminum", 0, "ingotAluminum");
		reg(c, "dustSilver", 0, "ingotSilver");
		reg(c, "dustLead", 0, "ingotLead");
		reg(c, "dustNickel", 0, "ingotNickel");
		reg(c, "dustInvar", 0, "ingotInvar");
		reg(c, "dustPlatinum", 0, "ingotPlatinum");
		reg(c, "dustBronze", 0, "ingotBronze");
		//endregion

		//region Ore
		regOre(c, "oreDiamond", 2, "gemDiamond");
		regOre(c, "oreEmerald", 2, "gemEmerald");
		regOre(c, "oreCoal", 2, Items.COAL);
		regOre(c, "oreRedstone", 4, "dustRedstone");
		regOre(c, "oreLapis", 4, "gemLapis");
		regOre(c, "oreIron", 2, "ingotIron");
		regOre(c, "oreGold", 2, "ingotGold");
		regOre(c, "oreQuartz", 2, "gemQuartz");
		regOre(c, "oreTin", 2, "ingotTin");
		regOre(c, "oreSilver", 2, "ingotSilver");
		regOre(c, "oreLead", 2, "ingorLead");
		regOre(c, "oreCopper", 2, "ingotCopper");
		regOre(c, "oreNikel", 2, "ingotNikel");
		regOre(c, "oreAluminum", 2, "ingotAluminum");
		regOre(c, "oreUranium", 2, "ingotUranium");
		regOre(c, "oreRuby", 2, "gemRuby");
		regOre(c, "oreZinc", 2, "ingotZinc");
		regOre(c, "oreQuartz", 2, "ingotQuartz");
		//endregion

		//region ender io
		reg(c, "enderio.electricalSteel", 0, "ingotIron", "silicon", "dustCoal");
		reg(c, "enderio.energeticAlloy", 0, "dustRedstone", "ingotGold", "dustGlowstone");
		reg(c, "enderio.phasedGold", 0, "enderio.energeticAlloy", Items.ENDER_PEARL);
		reg(c, "enderio.redstoneAlloy", 0, "silicon", "dustRedstone");
		reg(c, "enderio.conductiveIron", 0, "ingotIron", "dustRedstone");
		reg(c, "enderio.phasedIron", 0, Items.ENDER_PEARL, "ingotIron");
		reg(c, "enderio.darkSteel", 0, "ingotIron", "dustCoal", Blocks.OBSIDIAN);
		reg(c, "enderio.soularium", 0, Blocks.SOUL_SAND, "ingotGold");
		reg(c, "enderio.silicon", 0, "silicon");
		reg(c, "enderio.conduitBinder", 1);
		//endregion
	}

	private static void reg(ConfigurationHandler c, String name, int matter)
	{
		MatterOverdrive.matterRegistry.registerOre(name, new OreHandler(matter));
	}

	private static void regOre(ConfigurationHandler c, String name, int multiply, String ingot)
	{
		int matter = MatterOverdrive.matterRegistry.getMatterOre(ingot);
		if (matter > 0)
		{
			MatterOverdrive.matterRegistry.registerOre(name, new OreHandler(matter * multiply));
		}
	}

	private static void regOre(ConfigurationHandler c, String name, int multiply, Item ingot)
	{
		int matter = MatterOverdrive.matterRegistry.getMatter(new ItemStack(ingot));
		if (matter > 0)
		{
			MatterOverdrive.matterRegistry.registerOre(name, new OreHandler(matter * multiply));
		}
	}

	private static void reg(ConfigurationHandler c, String name, int matter, Object... items)
	{
		for (Object item : items)
		{
			MatterEntryItem entry = null;

			if (item instanceof String)
			{
				matter += MatterOverdrive.matterRegistry.getMatterOre((String)item);
			}
			else if (item instanceof Item)
			{
				matter += MatterOverdrive.matterRegistry.getMatter(new ItemStack((Item)item));
			}
			else if (item instanceof Block)
			{
				matter += MatterOverdrive.matterRegistry.getMatter(new ItemStack(Item.getItemFromBlock((Block)item)));
			}
			else if (item instanceof ItemStack)
			{
				matter += MatterOverdrive.matterRegistry.getMatter((ItemStack)item);
			}
		}

		if (matter > 0)
		{
			reg(c, name, matter);
		}
	}

	private static void reg(ConfigurationHandler c, ItemStack itemStack, int matter)
	{
		MatterOverdrive.matterRegistry.register(itemStack.getItem(), new DamageAwareStackHandler(itemStack.getItemDamage(), matter));
		MatterOverdrive.matterRegistry.basicEntries++;
	}

	private static void reg(ConfigurationHandler c, Block block, int matter)
	{
		reg(c, block, matter, 1);
	}

	private static void reg(ConfigurationHandler c, Block block, int matter, int subItems)
	{
		for (int i = 0; i < subItems; i++)
		{
			MatterOverdrive.matterRegistry.register(Item.getItemFromBlock(block), new DamageAwareStackHandler(i, matter));
			MatterOverdrive.matterRegistry.basicEntries++;
		}
	}

	private static void reg(ConfigurationHandler c, Item item, int matter)
	{
		reg(c, item, matter, 1);
	}

	private static void reg(ConfigurationHandler c, Item item, int matter, int subItems)
	{
		for (int i = 0; i < subItems; i++)
		{
			MatterOverdrive.matterRegistry.register(item, new DamageAwareStackHandler(i, matter));
			MatterOverdrive.matterRegistry.basicEntries++;
		}
	}
}
