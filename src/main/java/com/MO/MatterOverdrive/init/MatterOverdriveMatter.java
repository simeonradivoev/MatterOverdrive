package com.MO.MatterOverdrive.init;

import java.util.Map;

import com.MO.MatterOverdrive.MatterOverdrive;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Property;

import com.MO.MatterOverdrive.handler.IMatterEntry;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.handler.MatterRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MatterOverdriveMatter 
{
	public static void init(MOConfigurationHandler config)
	{
		Map<String,Property> map = config.config.getCategory(MOConfigurationHandler.MATTER_LIST_CATEGORY).getValues();
		
		for (Property entry : map.values())
		{
            Item i = (Item)Item.itemRegistry.getObject(entry.getName());

			if(i != null)
			{
				IMatterEntry e = MatterRegistry.register(i, entry.getInt());
                System.out.println("Registered - " + e.getName());
                continue;
            }

            Block b = (Block)Block.blockRegistry.getObject(entry.getName());
            if(b != null && b != Blocks.air)
			{
				IMatterEntry e = MatterRegistry.register(b,entry.getInt());
                System.out.println("Registered - " + b.getUnlocalizedName());
                continue;
			}

            String s = entry.getName();
            if(s != null)
            {
                IMatterEntry e = MatterRegistry.register(s,entry.getInt());
                System.out.println("Registered - " + s);
            }
		}
	}
	
	public static void registerBasic(MOConfigurationHandler c)
	{
        MOConfigurationHandler.load();
        registerBasicItems(c);
        registerBasicBlocks(c);
        MOConfigurationHandler.save();
	}

    public static void registerComplex(MOConfigurationHandler c)
    {
        registerRecipeItems(c);
        registerRecipeBlocks(c);
    }

    public static void registerBasicBlocks(MOConfigurationHandler c)
    {
        reg(c,Blocks.dirt, 4);
        reg(c,Blocks.wool, 4);
        reg(c,Blocks.grass, 5);
        reg(c,"cobblestone", 4);
        reg(c,"logWood", 16);
        reg(c,"sand", 6);
        reg(c,Blocks.gravel, 8);
        reg(c,Blocks.clay,12);
        reg(c,Blocks.cactus,10);
        reg(c,"plankWood",4);
        reg(c, Blocks.end_stone, 8);
        reg(c,"stone",4);
        reg(c,Blocks.soul_sand,8);
        reg(c,Blocks.snow,2);
        reg(c,Blocks.pumpkin,8);
        reg(c,Blocks.obsidian,64);
        reg(c,Blocks.hardened_clay,16);
        reg(c,"treeLeaves",1);
        reg(c,Blocks.mycelium,5);
        reg(c,Blocks.ice,4);
        reg(c,"blockGlass",6);
        reg(c,"paneGlass",1);
        reg(c,Blocks.bedrock,512);

        reg(c,Blocks.vine,1);
        reg(c,Blocks.tallgrass,1);
        reg(c,Blocks.reeds,3);
        reg(c,Blocks.mossy_cobblestone,5);
        reg(c,Blocks.netherrack,3);
        reg(c,Blocks.furnace,8);

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
    }

    public static void registerRecipeBlocks(MOConfigurationHandler c)
    {
        MatterRegistry.registerFromRecipe(Blocks.quartz_block);
        MatterRegistry.registerFromRecipe(Blocks.torch);
        MatterRegistry.registerFromRecipe(Blocks.redstone_torch);
        MatterRegistry.registerFromRecipe(Blocks.nether_brick);
        MatterRegistry.registerFromRecipe(Blocks.brick_block);
        MatterRegistry.registerFromRecipe(Blocks.coal_block);
        MatterRegistry.registerFromRecipe(Blocks.diamond_block);
        MatterRegistry.registerFromRecipe(Blocks.emerald_block);
        MatterRegistry.registerFromRecipe(Blocks.redstone_block);
        MatterRegistry.registerFromRecipe(Blocks.tnt);
        MatterRegistry.registerFromRecipe(Blocks.gold_block);
        MatterRegistry.registerFromRecipe(Blocks.iron_block);
        MatterRegistry.registerFromRecipe(Blocks.hay_block);
        MatterRegistry.registerFromRecipe(Blocks.lapis_block);
        MatterRegistry.registerFromRecipe(Blocks.melon_block);
        MatterRegistry.registerFromRecipe(Blocks.quartz_block);
        MatterRegistry.registerFromRecipe(Blocks.anvil);
        MatterRegistry.registerFromRecipe(Blocks.bed);
        MatterRegistry.registerFromRecipe(Blocks.bookshelf);
        MatterRegistry.registerFromRecipe(Blocks.cake);
        MatterRegistry.registerFromRecipe(Blocks.carpet);
        MatterRegistry.registerFromRecipe(Blocks.cauldron);
        MatterRegistry.registerFromRecipe(Blocks.chest);
        MatterRegistry.registerFromRecipe(Blocks.cobblestone_wall);
        MatterRegistry.registerFromRecipe(Blocks.crafting_table);
        MatterRegistry.registerFromRecipe(Blocks.dispenser);
        MatterRegistry.registerFromRecipe(Blocks.dropper);
        MatterRegistry.registerFromRecipe(Blocks.enchanting_table);
        MatterRegistry.registerFromRecipe(Blocks.ender_chest);
        MatterRegistry.registerFromRecipe(Blocks.dark_oak_stairs);
        MatterRegistry.registerFromRecipe(Blocks.brick_stairs);
        MatterRegistry.registerFromRecipe(Blocks.oak_stairs);
        MatterRegistry.registerFromRecipe(Blocks.acacia_stairs);
        MatterRegistry.registerFromRecipe(Blocks.birch_stairs);
        MatterRegistry.registerFromRecipe(Blocks.nether_brick_stairs);
        MatterRegistry.registerFromRecipe(Blocks.beacon);
        MatterRegistry.registerFromRecipe(Blocks.fence);
        MatterRegistry.registerFromRecipe(Blocks.fence_gate);
        MatterRegistry.registerFromRecipe(Blocks.nether_brick_fence);
        MatterRegistry.registerFromRecipe(Blocks.flower_pot);
        MatterRegistry.registerFromRecipe(Blocks.piston);
        MatterRegistry.registerFromRecipe(Blocks.noteblock);
        MatterRegistry.registerFromRecipe(Blocks.rail);
        MatterRegistry.registerFromRecipe(Blocks.ladder);
        MatterRegistry.registerFromRecipe(Blocks.stonebrick);
        MatterRegistry.registerFromRecipe(Blocks.redstone_lamp);
        MatterRegistry.registerFromRecipe(Blocks.glass_pane);
        MatterRegistry.registerFromRecipe(Blocks.quartz_stairs);
        MatterRegistry.registerFromRecipe(Blocks.wooden_slab);
        MatterRegistry.registerFromRecipe(Blocks.stone_slab);
    }

    public static void registerBasicItems(MOConfigurationHandler c)
    {
        reg(c,Items.apple,1);
        reg(c,Items.arrow,1);
        reg(c,Items.baked_potato,2);
        reg(c,Items.beef,3);
        reg(c,Items.blaze_rod,2);
        reg(c,Items.bone,2);
        reg(c,Items.carrot, 1);
        reg(c,Items.clay_ball, 3);
        reg(c,Items.coal, 8);
        reg(c,Items.cooked_beef, 3);
        reg(c,"gemDiamond", 64);
        reg(c,Items.egg,1);
        reg(c,"gemEmerald", 64);
        reg(c,"dye", 1);
        reg(c,Items.ender_pearl, 8);
        reg(c,Items.feather, 1);
        reg(c,Items.fermented_spider_eye, 1);
        reg(c,Items.flint, 2);
        reg(c,Items.fish, 2);
        reg(c,Items.cooked_fished, 2);
        reg(c,Items.ghast_tear, 8);
        reg(c,"ingotGold", 9);
        reg(c,"nuggetGold", 1);
        reg(c,Items.gunpowder, 2);
        reg(c,Items.melon, 1);
        reg(c,"ingotIron", 9);
        reg(c,Items.wheat, 1);
        reg(c,Items.wheat_seeds,1);
        reg(c,Items.sugar, 1);
        reg(c,Items.string, 1);
        reg(c,"stickWood", 1);
        reg(c,"dustRedstone", 4);
        reg(c,"gemLapis",4);
        reg(c,"dustGlowstone", 4);
        reg(c,Items.spider_eye, 1);
        reg(c,Items.saddle, 4);
        reg(c,Items.reeds, 1);
        reg(c,"gemQuartz", 4);
        reg(c,Items.potato, 2);
        reg(c,Items.leather, 3);
        reg(c,Items.pumpkin_seeds, 1);
        reg(c,Items.porkchop, 3);
        reg(c,Items.paper, 1);
        reg(c,Items.lead, 9);
        reg(c,Items.lava_bucket, 30);
        reg(c,Items.water_bucket, 28);
        reg(c,Items.milk_bucket, 28);
        reg(c,"ingotBrick", 3);
        reg(c,"ingotBrickNether",3);
        reg(c,Items.nether_wart,3);
        reg(c,Items.nether_star,256);
        reg(c,Items.iron_horse_armor,18);
        reg(c,"slimeball",2);
        reg(c,"record",4);
        reg(c,Items.chicken,3);
        reg(c,Items.cooked_chicken,3);
        reg(c,Items.rotten_flesh,2);

        MatterRegistry.register(MatterOverdriveItems.matter_dust_refined,1);
    }

    public static void registerRecipeItems(MOConfigurationHandler c)
    {
        MatterRegistry.registerFromRecipe(Items.minecart);
        MatterRegistry.registerFromRecipe(Items.book);
        MatterRegistry.registerFromRecipe(Items.fishing_rod);
        MatterRegistry.registerFromRecipe(Items.item_frame);
        MatterRegistry.registerFromRecipe(Items.blaze_powder);
        MatterRegistry.registerFromRecipe(Items.compass);
        MatterRegistry.registerFromRecipe(Items.boat);
        MatterRegistry.registerFromRecipe(Items.bow);
        MatterRegistry.registerFromRecipe(Items.bowl);
        MatterRegistry.registerFromRecipe(Items.cauldron);
        MatterRegistry.registerFromRecipe(Items.cake);
        MatterRegistry.registerFromRecipe(Items.clock);
        MatterRegistry.registerFromRecipe(Items.cookie);
        MatterRegistry.registerFromRecipe(Items.ender_eye);
        MatterRegistry.registerFromRecipe(Items.carrot_on_a_stick);
        MatterRegistry.registerFromRecipe(Items.item_frame);
        MatterRegistry.registerFromRecipe(Items.comparator);
        MatterRegistry.registerFromRecipe(Items.flint_and_steel);
        MatterRegistry.registerFromRecipe(Items.flower_pot);
        MatterRegistry.registerFromRecipe(Items.furnace_minecart);
        MatterRegistry.registerFromRecipe(Items.bucket);
        MatterRegistry.registerFromRecipe(Items.golden_apple);
        MatterRegistry.registerFromRecipe(Items.glass_bottle);
        MatterRegistry.registerFromRecipe(Items.wooden_sword);
        MatterRegistry.registerFromRecipe(Items.wooden_axe);
        MatterRegistry.registerFromRecipe(Items.wooden_door);
        MatterRegistry.registerFromRecipe(Items.wooden_hoe);
        MatterRegistry.registerFromRecipe(Items.wooden_pickaxe);
        MatterRegistry.registerFromRecipe(Items.wooden_shovel);
        MatterRegistry.registerFromRecipe(Items.iron_axe);
        MatterRegistry.registerFromRecipe(Items.iron_boots);
        MatterRegistry.registerFromRecipe(Items.iron_chestplate);
        MatterRegistry.registerFromRecipe(Items.iron_door);
        MatterRegistry.registerFromRecipe(Items.iron_helmet);
        MatterRegistry.registerFromRecipe(Items.iron_hoe);
        MatterRegistry.registerFromRecipe(Items.iron_leggings);
        MatterRegistry.registerFromRecipe(Items.iron_pickaxe);
        MatterRegistry.registerFromRecipe(Items.iron_shovel);
        MatterRegistry.registerFromRecipe(Items.iron_sword);
    }

    public static void regWithRecipe(MOConfigurationHandler c,Block block)
    {
        int matter = MatterRegistry.getMatterFromRecipe(block);

        if(matter > 0)
        {
            reg(c,block,matter);
        }
    }

	private static void reg(MOConfigurationHandler c,String name,int matter)
	{
		c.config.get(MOConfigurationHandler.MATTER_LIST_CATEGORY, name, matter);
	}

    private static void reg(MOConfigurationHandler c,Block block,int matter)
    {
        c.config.get(MOConfigurationHandler.MATTER_LIST_CATEGORY, Block.blockRegistry.getNameForObject(block), matter);
    }

    private static void reg(MOConfigurationHandler c,Item item,int matter)
    {
        c.config.get(MOConfigurationHandler.MATTER_LIST_CATEGORY, Item.itemRegistry.getNameForObject(item), matter);
    }
}
