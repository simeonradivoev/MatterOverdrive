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
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.MatterEntry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatterOverdriveMatter
{
	public static void init(ConfigurationHandler config)
	{

	}
	
	public static void registerBasic(ConfigurationHandler c)
	{
        registerBasicItems(c);
        registerBasicBlocks(c);

        registerBasicCompoundItems(c);
	}

    public static void registerFromConfig(ConfigurationHandler c)
    {
        MatterOverdrive.matterRegistry.loadNewItemsFromConfig(c);
    }

    public static void registerBlacklistFromConfig(ConfigurationHandler c)
    {
        MatterOverdrive.matterRegistry.loadBlacklistFromConfig(c);
        MatterOverdrive.matterRegistry.loadModBlacklistFromConfig(c);
    }

    public static void registerBasicBlocks(ConfigurationHandler c)
    {
        reg(c,Blocks.dirt, 1,3);
        reg(c,Blocks.wool, 2,16);
        reg(c,Blocks.grass, 5);
        reg(c,"cobblestone", 1);
        reg(c,Blocks.cobblestone,1);
        reg(c,"logWood", 16);
        reg(c,"sand", 2);
        reg(c,Blocks.gravel, 2);
        reg(c,"sandstone",4);
        reg(c,Blocks.clay,4);
        reg(c,Blocks.cactus,4);
        reg(c,"plankWood",4);
        reg(c,Blocks.planks,4,6);
        reg(c, Blocks.end_stone, 6);
        reg(c,"stone",1);
        reg(c,Blocks.stone,1);
        reg(c,Blocks.soul_sand,4);
        reg(c,Blocks.snow,2);
        reg(c,Blocks.pumpkin,2);
        reg(c,Blocks.obsidian,16);
        reg(c,"treeLeaves",1);
        reg(c,Blocks.mycelium,5);
        reg(c,Blocks.ice,3);
        reg(c,Blocks.packed_ice,4);
        reg(c,"blockGlass",3);
        reg(c,"glass",3);
        reg(c,"paneGlass",1);
        reg(c,Blocks.bedrock,1024);
        reg(c,Blocks.sponge,8);
        reg(c,Blocks.vine,1);
        reg(c,Blocks.tallgrass,1);
        reg(c,Blocks.mossy_cobblestone,2);
        reg(c,Blocks.netherrack,1);
        reg(c,Blocks.stained_hardened_clay,3,16);
        reg(c, Blocks.hardened_clay, 3);
        reg(c,Blocks.stonebrick,2,4);
        reg(c,Blocks.skull,8,5);
        reg(c,Blocks.cobblestone_wall,1);

        //region flowers
        reg(c,Blocks.red_flower,1,9);
        reg(c,Blocks.yellow_flower,1);
        reg(c,Blocks.brown_mushroom,1);
        reg(c,Blocks.brown_mushroom_block,1);
        reg(c,Blocks.red_mushroom,1);
        reg(c,Blocks.red_mushroom_block,1);
        reg(c,Blocks.deadbush,1);
        reg(c,Blocks.waterlily,1);
        reg(c,Blocks.sapling,2,6);
        reg(c,Blocks.double_plant,1,6);
        //endregion
    }


    public static void registerBasicItems(ConfigurationHandler c)
    {
        reg(c, Items.apple, 1);
        reg(c, Items.arrow, 1);
        reg(c, Items.baked_potato, 1);
        reg(c, Items.beef, 2);
        reg(c, Items.blaze_rod, 4);
        reg(c, Items.bone, 2);
        reg(c, "cropCarrot", 1);
        reg(c, Items.clay_ball, 1);
        reg(c, Items.coal, 8);
        reg(c,new ItemStack(Items.coal,1,1),5);
        reg(c, Items.cooked_beef, 3);
        reg(c, Items.egg, 1);
        reg(c, "dye", 1);
        reg(c, Items.ender_pearl, 8);
        reg(c, Items.feather, 1);
        reg(c, Items.fermented_spider_eye, 1);
        reg(c, Items.flint, 1);
        reg(c, Items.fish, 1,4);
        reg(c, Items.cooked_fished, 1);
        reg(c, Items.ghast_tear, 8);
        reg(c, "nuggetGold", 4);
        reg(c, Items.gunpowder, 2);
        reg(c, Items.melon, 1);
        reg(c, "cropWheat", 1);
        reg(c, Items.wheat_seeds, 1);
        reg(c, Items.sugar, 1);
        reg(c, Items.string, 1);
        reg(c, "stickWood", 1);
        reg(c, "dustRedstone", 4);
        reg(c, "dustGlowstone", 2);
        reg(c, Items.spider_eye, 1);
        reg(c, Items.saddle, 18);
        reg(c, Items.reeds, 1);
        reg(c, "cropPotato", 1);
        reg(c, Items.leather, 3);
        reg(c, Items.pumpkin_seeds, 1);
        reg(c, Items.porkchop, 2);
        reg(c, Items.cooked_porkchop, 4);
        reg(c, Items.paper, 1);
        reg(c, Items.lava_bucket, 24 + 96);
        reg(c, Items.water_bucket, 12 + 96);
        reg(c, Items.milk_bucket, 12 + 96);
        reg(c, "ingotBrickNether", 1);
        reg(c, Items.nether_wart, 3);
        reg(c, Items.nether_star, 1024);
        reg(c,Items.iron_horse_armor,32 * 5);
        reg(c,Items.golden_horse_armor,42 * 5);
        reg(c,Items.diamond_horse_armor,256 * 5);
        reg(c,Items.experience_bottle,32);
        reg(c, "slimeball", 2);
        reg(c, "record", 4);
        reg(c, Items.chicken, 2);
        reg(c, Items.cooked_chicken, 3);
        reg(c, Items.rotten_flesh, 1);
        reg(c, "dustSaltpeter", 2);
        reg(c, "dustSulfur", 2);
        reg(c,Items.name_tag,32);
        reg(c,"itemSkull",16);
        reg(c,Items.glass_bottle,3);
        reg(c,"silicon",2);

        //region Gems
        reg(c, "gemDiamond", 256);
        reg(c, "gemQuartz", 3);
        reg(c, "gemLapis", 4);
        reg(c, "gemEmerald", 256);
        reg(c,"gemRuby",64);
        reg(c,"gemRupee",64);
        reg(c,"gemSapphire",64);
        //endregion

        //region Ingots
        reg(c, "ingotBrick", 2);
        reg(c, "ingotIron", 32);
        reg(c, "ingotGold", 42);
        reg(c,"ingotTin", 28);
        reg(c, "ingotCopper", 28);
        reg(c, "ingotAluminum", 26);
        reg(c, "ingotSilver", 30);
        reg(c, "ingotLead", 32);
        reg(c, "ingotNickel", 32);
        reg(c, "ingotInvar", 38);
        reg(c, "ingotPlatinum", 64);
        reg(c, "ingotBronze", 28);
        reg(c,"ingotRedAlloy",24);
        reg(c,"ingotUranium",64);
        reg(c,"ingotZinc",30);
        reg(c,"ingotQuartz",24);
        reg(c,"ingotSteel",38);
        //endregion

        //region Plants
        reg(c,Items.reeds,1);
        //endregion

        //region dyes
        reg(c,new ItemStack(Items.dye,1,2),1);
        reg(c,new ItemStack(Items.dye,1,3),1);
        //endregion

        //region rouge android parts
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1, Reference.BIONIC_HEAD),64 * 5);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_ARMS),64 * 6);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_LEGS),64 * 6);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_CHEST),64 * 9);
        //endregion

        //region Matter Overdrive Basic Items
        reg(c, MatterOverdriveItems.emergency_ration, 3);
        reg(c, MatterOverdriveItems.earl_gray_tea, 2);
        reg(c, MatterOverdriveItems.romulan_ale, 2);
        reg(c, new ItemStack(MatterOverdriveItems.androidPill, 1, 1), 64);
        reg(c,new ItemStack(MatterOverdriveItems.androidPill,1,2),32);
        //endregion
    }

    public static void registerBasicCompoundItems(ConfigurationHandler c)
    {
        reg(c,"dustObsidian",0,Blocks.obsidian);
        reg(c,"dustCharcoal",0,new ItemStack(Items.coal,1,1));
        reg(c,"dustCoal",0,Items.coal);
        reg(c,"dustDiamond",0,"gemDiamond");
        reg(c,"dustFlour",0,"cropWheat");
        reg(c,"dustNetherQuartz",0,"oreQuartz");
        reg(c,"gemGreenSapphire",0,"gemEmerald");
        reg(c,"dustEmerald",0,"gemEmerald");


        //region dusts
        reg(c,"dustIron",0,"ingotIron");
        reg(c, "dustGold", 0,"ingotGold");
        reg(c, "dustTin", 0,"ingotTin");
        reg(c,"dustCopper",0,"ingotCopper");
        reg(c, "dustAluminum", 0,"ingotAluminum");
        reg(c, "dustSilver", 0,"ingotSilver");
        reg(c, "dustLead", 0,"ingotLead");
        reg(c, "dustNickel", 0,"ingotNickel");
        reg(c, "dustInvar", 0,"ingotInvar");
        reg(c, "dustPlatinum", 0,"ingotPlatinum");
        reg(c,"dustBronze",0,"ingotBronze");
        //endregion

        //region Ore
        regOre(c, "oreDiamond", 2, "gemDiamond");
        regOre(c, "oreEmerald", 2, "gemEmerald");
        regOre(c,"oreCoal",2,Items.coal);
        regOre(c, "oreRedstone", 4, "dustRedstone");
        regOre(c, "oreLapis", 4, "gemLapis");
        regOre(c, "oreIron", 2, "ingotIron");
        regOre(c, "oreGold", 2, "ingotGold");
        regOre(c, "oreQuartz", 2, "gemQuartz");
        regOre(c, "oreTin", 2, "ingotTin");
        regOre(c,"oreSilver",2,"ingotSilver");
        regOre(c,"oreLead",2,"ingorLead");
        regOre(c,"oreCopper",2,"ingotCopper");
        regOre(c,"oreNikel",2,"ingotNikel");
        regOre(c,"oreAluminum",2,"ingotAluminum");
        regOre(c,"oreUranium",2,"ingotUranium");
        regOre(c,"oreRuby",2,"gemRuby");
        regOre(c,"oreZinc",2,"ingotZinc");
        regOre(c,"oreQuartz",2,"ingotQuartz");
        //endregion

        //region ender io
        reg(c,"enderio.electricalSteel",0,"ingotIron","silicon","dustCoal");
        reg(c,"enderio.energeticAlloy",0,"dustRedstone","ingotGold","dustGlowstone");
        reg(c,"enderio.phasedGold",0,"enderio.energeticAlloy",Items.ender_pearl);
        reg(c,"enderio.redstoneAlloy",0,"silicon","dustRedstone");
        reg(c,"enderio.conductiveIron",0,"ingotIron","dustRedstone");
        reg(c,"enderio.phasedIron",0,Items.ender_pearl,"ingotIron");
        reg(c,"enderio.darkSteel",0,"ingotIron","dustCoal",Blocks.obsidian);
        reg(c,"enderio.soularium",0,Blocks.soul_sand,"ingotGold");
        reg(c,"enderio.silicon",0,"silicon");
        reg(c,"enderio.conduitBinder",1);
        //endregion
    }

	private static void reg(ConfigurationHandler c,String name,int matter)
	{
        MatterOverdrive.matterRegistry.register(name,matter);
	}
    private static void regOre(ConfigurationHandler c,String name,int multiply,String ingot)
    {
        MatterEntry entry = MatterOverdrive.matterRegistry.getEntry(ingot);
        if (entry != null)
        {
            MatterOverdrive.matterRegistry.register(name,entry.getMatter()*multiply);
        }
    }
    private static void regOre(ConfigurationHandler c,String name,int multiply,Item ingot)
    {
        MatterEntry entry = MatterOverdrive.matterRegistry.getEntry(ingot);
        if (entry != null)
        {
            MatterOverdrive.matterRegistry.register(name,entry.getMatter()*multiply);
        }
    }
    private static void reg(ConfigurationHandler c,String name,int matter,Object... items)
    {
        for (int i = 0;i < items.length;i++)
        {
            MatterEntry entry = null;

            if (items[i] instanceof String)
            {
                entry = MatterOverdrive.matterRegistry.getEntry((String)items[i]);
            }else if (items[i] instanceof Item)
            {
                entry = MatterOverdrive.matterRegistry.getEntry((Item)items[i]);
            }else if (items[i] instanceof Block)
            {
                entry = MatterOverdrive.matterRegistry.getEntry((Block)items[i]);
            }else if (items[i] instanceof ItemStack)
            {
                entry = MatterOverdrive.matterRegistry.getEntry((ItemStack)items[i]);
            }

            if (entry != null)
            {
                matter+= entry.getMatter();
            }
        }

        if (matter > 0)
        {
            reg(c,name,matter);
        }
    }

    private static void reg(ConfigurationHandler c,ItemStack itemStack,int matter)
    {
        MatterOverdrive.matterRegistry.register(itemStack,matter);
        MatterOverdrive.matterRegistry.basicEntries++;
    }
    private static void reg(ConfigurationHandler c,Block block,int matter)
    {
        reg(c,block,matter,1);
    }
    private static void reg(ConfigurationHandler c,Block block,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterOverdrive.matterRegistry.getKey(new ItemStack(Item.getItemFromBlock(block), 1, i));
            if (key != null) {
                MatterOverdrive.matterRegistry.register(key, matter);
                MatterOverdrive.matterRegistry.basicEntries++;
            }
        }
    }
    private static void reg(ConfigurationHandler c,Item item,int matter){reg(c,item,matter,1);}
    private static void reg(ConfigurationHandler c,Item item,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterOverdrive.matterRegistry.getKey(new ItemStack(item, 1, i));
            if (key != null) {
                MatterOverdrive.matterRegistry.register(key, matter);
                MatterOverdrive.matterRegistry.basicEntries++;
            }
        }
    }
}
