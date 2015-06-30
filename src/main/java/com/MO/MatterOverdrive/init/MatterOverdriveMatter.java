package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.Reference;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.handler.ConfigurationHandler;
import com.MO.MatterOverdrive.handler.MatterRegistry;

public class MatterOverdriveMatter
{
	public static void init(ConfigurationHandler config)
	{

	}
	
	public static void registerBasic(ConfigurationHandler c)
	{
        registerBasicItems(c);
        registerBasicBlocks(c);
	}

    public static void registerFromConfig(ConfigurationHandler c)
    {
        MatterRegistry.loadNewItemsFromConfig(c);
    }

    public static void registerBlacklistFromConfig(ConfigurationHandler c)
    {
        MatterRegistry.loadBlacklistFromConfig(c);
    }

    public static void registerBasicBlocks(ConfigurationHandler c)
    {
        reg(c,Blocks.dirt, 1,3);
        reg(c,Blocks.wool, 2,16);
        reg(c,Blocks.grass, 5);
        reg(c,"cobblestone", 1);
        reg(c,"logWood", 16);
        reg(c,"sand", 2);
        reg(c,Blocks.gravel, 2);
        reg(c,"sandstone",4);
        reg(c,Blocks.clay,4);
        reg(c,Blocks.cactus,4);
        reg(c,"plankWood",4);
        reg(c, Blocks.end_stone, 6);
        reg(c,"stone",1);
        reg(c,Blocks.soul_sand,4);
        reg(c,Blocks.snow,2);
        reg(c,Blocks.pumpkin,2);
        reg(c,Blocks.obsidian,16);
        reg(c,"treeLeaves",1);
        reg(c,Blocks.mycelium,5);
        reg(c,Blocks.ice,3);
        reg(c,Blocks.packed_ice,4);
        reg(c,"blockGlass",3);
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

        //region Ore
        reg(c,"oreDiamond",512);
        reg(c,"oreEmerald",512);
        reg(c,"oreCoal",16);
        reg(c,"oreRedstone",16);
        reg(c,"oreLapis",4);
        reg(c,"oreIron",64);
        reg(c,"oreGold",84);
        reg(c,"oreQuartz",6);
        //endregion

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
        reg(c, "gemDiamond", 256);
        reg(c, Items.egg, 1);
        reg(c, "gemEmerald", 256);
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
        reg(c, "gemLapis", 4);
        reg(c, "dustGlowstone", 2);
        reg(c, Items.spider_eye, 1);
        reg(c, Items.saddle, 18);
        reg(c, Items.reeds, 1);
        reg(c, "gemQuartz", 3);
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
        reg(c,Items.skull,16,5);
        reg(c,Items.glass_bottle,3);

        //region Ingots
        reg(c, "ingotBrick", 2);
        reg(c, "ingotIron", 32);
        reg(c, "dustIron", 32);
        reg(c, "ingotGold", 42);
        reg(c, "dustGold", 42);
        reg(c,"ingotTin", 28);
        reg(c, "dustTin", 28);
        reg(c, "ingotCopper", 28);
        reg(c,"dustCopper",28);
        reg(c, "ingotAluminum", 26);
        reg(c, "dustAluminum", 26);
        reg(c, "ingotSilver", 30);
        reg(c, "dustSilver", 30);
        reg(c, "ingotLead", 32);
        reg(c, "dustLead", 32);
        reg(c, "ingotNickel", 32);
        reg(c, "dustNickel", 32);
        reg(c, "ingotInvar", 38);
        reg(c, "dustInvar", 38);
        reg(c, "ingotPlatinum", 64);
        reg(c, "ingotBronze", 28);
        //endregion

        //region Plants
        reg(c,Items.reeds,1);
        //endregion

        //region dyes
        reg(c,new ItemStack(Items.dye,1,2),1);
        reg(c,new ItemStack(Items.dye,1,3),1);
        //endregion

        //region rouge android parts
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_HEAD),64 * 5);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_ARMS),64 * 6);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_LEGS),64 * 6);
        reg(c,new ItemStack(MatterOverdriveItems.androidParts,1,Reference.BIONIC_CHEST),64 * 9);
        //endregion
    }

	private static void reg(ConfigurationHandler c,String name,int matter)
	{
		MatterRegistry.register(name,matter);
	}

    private static void reg(ConfigurationHandler c,ItemStack itemStack,int matter)
    {
        MatterRegistry.register(itemStack,matter);
        MatterRegistry.basicEntires++;
    }
    private static void reg(ConfigurationHandler c,Block block,int matter)
    {
        reg(c,block,matter,1);
    }
    private static void reg(ConfigurationHandler c,Block block,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterRegistry.getKey(new ItemStack(Item.getItemFromBlock(block), 1, i));
            if (key != null) {
                MatterRegistry.register(key, matter);
                MatterRegistry.basicEntires++;
            }
        }
    }
    private static void reg(ConfigurationHandler c,Item item,int matter){reg(c,item,matter,1);}
    private static void reg(ConfigurationHandler c,Item item,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterRegistry.getKey(new ItemStack(item, 1, i));
            if (key != null) {
                MatterRegistry.register(key, matter);
                MatterRegistry.basicEntires++;
            }
        }
    }
}
