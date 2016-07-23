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
import matteroverdrive.data.quest.GenericQuest;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.data.quest.logic.QuestLogicBecomeAndroid;
import matteroverdrive.data.quest.logic.QuestLogicCocktailOfAscension;
import matteroverdrive.data.quest.logic.QuestLogicCraft;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.handler.quest.Quests;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/19/2015.
 */
public class MatterOverdriveQuests
{
	public static final List<WeightedRandom.Item> contractGeneration = new ArrayList<>();
	//public static RandomQuestText killAndroids;
	public static GenericQuest cocktailOfAscension;
	//public static GenericQuest sacrifice;
	//public static GenericQuest departmentOfAgriculture;
	//public static GenericQuest weaponsOfWar;
	//public static GenericQuest oneTrueLove;
	public static GenericQuest toThePowerOf;
	public static GenericQuest punyHumans;
	//public static GenericQuest is_it_really_me;
	//public static GenericQuest beast_belly;
	//public static GenericQuest crashLanding;
	//public static GenericQuest weMustKnow;
	//public static GenericMultiQuest gmo;
	//public static GenericMultiQuest trade_route;

	public static void init()
	{
		initMatterOverdriveQuests();
		initModdedQuests();
	}

	private static void initMatterOverdriveQuests()
	{
		cocktailOfAscension = (GenericQuest)new GenericQuest(new QuestLogicCocktailOfAscension(), "cocktail_of_ascension", 512).addQuestRewards(new ItemStackReward(MatterOverdrive.items.androidPill, 1, 0), new ItemStackReward(MatterOverdrive.items.androidPill, 1, 1), new ItemStackReward(MatterOverdrive.items.androidPill, 1, 2));
		//oneTrueLove = (GenericQuest)new GenericQuest(new QuestLogicMine(Blocks.diamond_ore.getDefaultState(),1,1,180).setAutoComplete(true),"one_true_love",0).addQuestRewards(new ItemStackReward(Items.emerald,6));
		punyHumans = (GenericQuest)new GenericQuest(new QuestLogicBecomeAndroid(), "puny_humans", 256).addQuestRewards(new ItemStackReward(MatterOverdrive.items.battery), new ItemStackReward(MatterOverdrive.items.androidPill, 1, 1), new ItemStackReward(MatterOverdrive.items.androidPill, 5, 2));
		//is_it_really_me = (GenericQuest)new GenericQuest(new QuestLogicSingleEvent(MOEventTransport.class).setAutoComplete(true),"is_it_really_me",120).addQuestRewards(new ItemStackReward(MatterOverdrive.items.item_upgrade,2,4));
		//beast_belly = (GenericQuest)new GenericQuest(new QuestLogicSingleEvent(MOEventGravitationalAnomalyConsume.class),"beast_belly",210).addQuestRewards(new ItemStackReward(MatterOverdrive.blocks.gravitational_stabilizer,2));
		//weMustKnow = (GenericQuest)new GenericQuest(new QuestLogicPlaceBlock(4,new QuestItem(new ItemStack(MatterOverdrive.blocks.decorative_coils).setStackDisplayName("Communication Relay")),1,1).setAutoComplete(true),"we_must_know",120).addQuestRewards(new ItemStackReward(Items.emerald,8));
		//crashLanding = (GenericQuest)new GenericQuest(new QuestLogicCraft(new ItemStack(MatterOverdrive.items.security_protocol),0,0,0).setAutoComplete(true),"crash_landing",60).addQuestRewards(new ItemStackReward(new ItemStack(MatterOverdrive.blocks.decorative_coils).setStackDisplayName("Communication Relay")),new QuestStackReward(new QuestStack(weMustKnow)).setCopyNBT("Pos"));
		//gmo = (GenericMultiQuest)new GenericMultiQuest(new IQuestLogic[]{new QuestLogicScanBlock(new QuestBlock(Blocks.carrots.getDefaultState()),12,24,10).setOnlyDestroyable(true),new QuestLogicScanBlock(new QuestBlock(Blocks.potatoes.getDefaultState()),12,24,10).setOnlyDestroyable(true)},"gmo",0).setAutoComplete(true).setSequential(true).addQuestRewards(new ItemStackReward(MatterOverdrive.androidPartsFactory.addAttributeToPart(MatterOverdrive.androidPartsFactory.addAttributeToPart(new ItemStack(MatterOverdrive.items.tritaniumSpine),new AttributeModifier(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), 5, 0)),new AttributeModifier(AndroidAttributes.attributeGlitchTime.getAttributeUnlocalizedName(), -1, 2)).setStackDisplayName("Hardened Tritanium Spine")));
		//trade_route = new GenericMultiQuest(new IQuestLogic[]{new QuestLogicBlockInteract(null,true,false),new QuestLogicItemInteract(new QuestItem(new ItemStack(MatterOverdrive.items.isolinear_circuit).setStackDisplayName("Trade Route Agreement")),true),new QuestLogicConversation("mo.mad_scientist",MatterOverdriveDialogs.tradeRouteQuest,MatterOverdriveDialogs.tradeRouteQuest)},"trade_route",180).setAutoComplete(true).setSequential(true);
	}

	private static void initModdedQuests()
	{
		toThePowerOf = (GenericQuest)new GenericQuest(new QuestLogicCraft(new QuestItem[] {
				new QuestItem("BigReactors:BRReactorPart", "BigReactors"),
				new QuestItem("ExtraUtilities:generator", "ExtraUtilities", 1, 1),
				new QuestItem(new ItemStack(MatterOverdrive.items.battery))
		}, 1, 1, 120).setRandomItem(false).setAutoComplete(true), "to_the_power_of", 0).addQuestRewards(new ItemStackReward(MatterOverdrive.items.tritanium_ingot, 10), new ItemStackReward(MatterOverdrive.items.tritanium_plate, 4));
	}

	public static void register(Quests quests)
	{
		registerMatterOverdriveQuests(quests);
		registerModdedQuests(quests);
	}

	private static void registerMatterOverdriveQuests(Quests quests)
	{
		quests.registerQuest("cocktail_of_ascension", cocktailOfAscension);
		//quests.registerQuest("one_true_love",oneTrueLove);
		quests.registerQuest("puny_humans", punyHumans);
		//quests.registerQuest("is_it_really_me",is_it_really_me);
		//quests.registerQuest("beast_belly",beast_belly);
		//quests.registerQuest("crash_landing",crashLanding);
		//quests.registerQuest("weMustKnow",weMustKnow);
		//quests.registerQuest("gmo",gmo);
		//quests.registerQuest("trade_route",trade_route);

		//contractGeneration.add(new WeightedRandomQuest(oneTrueLove,100));
		//contractGeneration.add(new WeightedRandomQuest(is_it_really_me,80));
		//contractGeneration.add(new WeightedRandomQuest(beast_belly,60));
	}

	private static void registerModdedQuests(Quests quests)
	{
		quests.registerQuest("to_the_power_of", toThePowerOf);
	}
}
