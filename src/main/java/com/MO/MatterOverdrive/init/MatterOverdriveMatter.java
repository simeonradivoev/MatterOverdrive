package com.MO.MatterOverdrive.init;

import java.util.Map;

import com.MO.MatterOverdrive.MatterOverdrive;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Property;

import com.MO.MatterOverdrive.handler.IMatterEntry;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.handler.MatterRegistry;

public class MatterOverdriveMatter
{
	public static void init(MOConfigurationHandler config)
	{

	}
	
	public static void registerBasic(MOConfigurationHandler c)
	{
        MatterOverdrive.configHandler.load();
        registerBasicItems(c);
        registerBasicBlocks(c);
	}

    public static void registerFromConfig(MOConfigurationHandler c)
    {
        MatterRegistry.loadNewItemsFromConfig(c);
    }

    public static void registerBlacklistFromConfig(MOConfigurationHandler c)
    {
        MatterRegistry.loadBlacklistFromConfig(c);
    }

    public static void registerBasicBlocks(MOConfigurationHandler c)
    {
        reg(c,Blocks.dirt, 4);
        reg(c,Blocks.wool, 4,16);
        reg(c,Blocks.grass, 5);
        reg(c,"cobblestone", 4);
        reg(c,"logWood", 16);
        reg(c,"sand", 6);
        reg(c,Blocks.gravel, 8);
        reg(c,"sandstone",8);
        reg(c,Blocks.clay,10);
        reg(c,Blocks.cactus,10);
        reg(c,"plankWood",4);
        reg(c, Blocks.end_stone, 8);
        reg(c,"stone",4);
        reg(c,Blocks.soul_sand,8);
        reg(c,Blocks.snow,2);
        reg(c,Blocks.pumpkin,8);
        reg(c,Blocks.obsidian,64);
        reg(c,"treeLeaves",1);
        reg(c,Blocks.mycelium,5);
        reg(c,Blocks.ice,3);
        reg(c,Blocks.packed_ice,4);
        reg(c,"blockGlass",6);
        reg(c,"paneGlass",1);
        reg(c,Blocks.bedrock,512);
        reg(c,Blocks.sponge,4);
        reg(c,Blocks.rail,1);
        reg(c,Blocks.vine,1);
        reg(c,Blocks.tallgrass,1);
        reg(c,Blocks.mossy_cobblestone,5);
        reg(c,Blocks.netherrack,3);
        reg(c,Blocks.furnace,8);
        reg(c,Blocks.stained_hardened_clay,11,16);
        reg(c, Blocks.hardened_clay, 11);
        reg(c,Blocks.stonebrick,4,4);
        reg(c,Blocks.skull,8,5);

        //region Ore
        reg(c,"oreDiamond",128);
        reg(c,"oreEmerald",128);
        reg(c,"oreCoal",16);
        reg(c,"oreRedstone",16);
        reg(c,"oreLapis",4);
        reg(c,"oreIron",18);
        reg(c,"oreGold",18);
        reg(c,"oreQuartz",16);
        //endregion

        //region flowers
        reg(c,Blocks.red_flower,1);
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


    public static void registerBasicItems(MOConfigurationHandler c)
    {
        reg(c, Items.apple, 1);
        reg(c, Items.arrow, 1);
        reg(c, Items.baked_potato, 2);
        reg(c, Items.beef, 3);
        reg(c, Items.blaze_rod, 2);
        reg(c, Items.bone, 2);
        reg(c, "cropCarrot", 1);
        reg(c, Items.clay_ball, 3);
        reg(c, Items.coal, 8);
        reg(c,new ItemStack(Items.coal,1,1),5);
        reg(c, Items.cooked_beef, 3);
        reg(c, "gemDiamond", 64);
        reg(c, Items.egg, 1);
        reg(c, "gemEmerald", 64);
        reg(c, "dye", 1);
        reg(c, Items.ender_pearl, 8);
        reg(c, Items.feather, 1);
        reg(c, Items.fermented_spider_eye, 1);
        reg(c, Items.flint, 2);
        reg(c, Items.fish, 2);
        reg(c, Items.cooked_fished, 2);
        reg(c, Items.ghast_tear, 8);
        reg(c, "nuggetGold", 1);
        reg(c, Items.gunpowder, 2);
        reg(c, Items.melon, 1);
        reg(c, "cropWheat", 1);
        reg(c, Items.wheat_seeds, 1);
        reg(c, Items.sugar, 1);
        reg(c, Items.string, 1);
        reg(c, "stickWood", 1);
        reg(c, "dustRedstone", 4);
        reg(c, "gemLapis", 4);
        reg(c, "dustGlowstone", 4);
        reg(c, Items.spider_eye, 1);
        reg(c, Items.saddle, 4);
        reg(c, Items.reeds, 1);
        reg(c, "gemQuartz", 4);
        reg(c, "cropPotato", 2);
        reg(c, Items.leather, 3);
        reg(c, Items.pumpkin_seeds, 1);
        reg(c, Items.porkchop, 3);
        reg(c, Items.cooked_porkchop, 4);
        reg(c, Items.paper, 1);
        reg(c, Items.lead, 9);
        reg(c, Items.lava_bucket, 30);
        reg(c, Items.water_bucket, 28);
        reg(c, Items.milk_bucket, 28);
        reg(c, "ingotBrickNether", 3);
        reg(c, Items.nether_wart, 3);
        reg(c, Items.nether_star, 256);
        reg(c,Items.iron_horse_armor,18);
        reg(c, "slimeball", 2);
        reg(c, "record", 4);
        reg(c, Items.chicken, 3);
        reg(c, Items.cooked_chicken, 3);
        reg(c, Items.rotten_flesh, 2);
        reg(c, "dustSaltpeter", 2);
        reg(c, "dustSulfur", 2);
        reg(c,Items.name_tag,8);

        //region Ingots
        reg(c, "ingotBrick", 3);
        reg(c, "ingotIron", 9);
        reg(c, "dustIron", 9);
        reg(c, "ingotGold", 9);
        reg(c, "dustGold", 9);
        reg(c,"ingotTin", 7);
        reg(c, "dustTin", 7);
        reg(c, "ingotCopper", 7);
        reg(c,"dustCopper",7);
        reg(c, "ingotAluminum", 7);
        reg(c, "dustAluminum", 7);
        reg(c, "ingotSilver", 8);
        reg(c, "dustSilver", 8);
        reg(c, "ingotLead", 9);
        reg(c, "dustLead", 9);
        reg(c, "ingotNickel", 9);
        reg(c, "dustNickel", 9);
        reg(c, "ingotInvar", 9);
        reg(c, "dustInvar", 9);
        reg(c, "ingotPlatinum", 16);
        reg(c, "ingotBronze", 10);
        //endregion

        //region Plants
        reg(c,Items.reeds,3);
        //endregion

        //region dyes
        reg(c,new ItemStack(Items.dye,1,2),1);
        reg(c,new ItemStack(Items.dye,1,3),1);
        //endregion

        MatterRegistry.register(MatterOverdriveItems.matter_dust_refined, 1);
    }

	private static void reg(MOConfigurationHandler c,String name,int matter)
	{
		MatterRegistry.register(name,matter);
	}

    private static void reg(MOConfigurationHandler c,ItemStack itemStack,int matter)
    {
        MatterRegistry.register(itemStack,matter);
    }
    private static void reg(MOConfigurationHandler c,Block block,int matter)
    {
        reg(c,block,matter,1);
    }
    private static void reg(MOConfigurationHandler c,Block block,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterRegistry.getKey(new ItemStack(Item.getItemFromBlock(block), 1, i));
            if (key != null)
                MatterRegistry.register(key,matter);
        }
    }
    private static void reg(MOConfigurationHandler c,Item item,int matter){reg(c,item,matter,1);}
    private static void reg(MOConfigurationHandler c,Item item,int matter,int subItems)
    {
        for (int i = 0;i < subItems;i++) {
            String key = MatterRegistry.getKey(new ItemStack(item, 1, i));
            if (key != null)
                MatterRegistry.register(key,matter);
        }
    }
}
