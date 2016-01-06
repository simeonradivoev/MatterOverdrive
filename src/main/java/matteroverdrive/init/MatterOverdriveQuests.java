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

import cpw.mods.fml.common.event.FMLInitializationEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.MOEventTransport;
import matteroverdrive.api.events.anomaly.MOEventGravitationalAnomalyConsume;
import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.*;
import matteroverdrive.data.quest.logic.*;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.data.quest.rewards.QuestStackReward;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import matteroverdrive.entity.player.AndroidAttributes;
import matteroverdrive.handler.quest.Quests;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/19/2015.
 */
public class MatterOverdriveQuests
{
    public static List<WeightedRandom.Item> contractGeneration = new ArrayList<>();
    public static RandomQuestText killAndroids;
    public static GenericQuest cocktailOfAscension;
    public static GenericQuest sacrifice;
    public static GenericQuest departmentOfAgriculture;
    public static GenericQuest weaponsOfWar;
    public static GenericQuest oneTrueLove;
    public static GenericQuest toThePowerOf;
    public static GenericQuest punyHumans;
    public static GenericQuest is_it_really_me;
    public static GenericQuest beast_belly;
    public static GenericQuest crashLanding;
    public static GenericQuest weMustKnow;
    public static GenericQuest gmo;

    public static void init(FMLInitializationEvent event)
    {
        initMatterOverdriveQuests();
        initModdedQuests();
    }

    private static void initMatterOverdriveQuests()
    {
        killAndroids = (RandomQuestText)new RandomQuestText(new QuestLogicKillCreature(EntityRougeAndroidMob.class,12,28,40).setAutoComplete(true),"kill_androids",1,0).addQuestRewards(new ItemStackReward(MatterOverdriveItems.androidParts));
        cocktailOfAscension = (GenericQuest)new GenericQuest(new QuestLogicCocktailOfAscension(),"cocktail_of_ascension",512).addQuestRewards(new ItemStackReward(MatterOverdriveItems.androidPill,1,0),new ItemStackReward(MatterOverdriveItems.androidPill,1,1),new ItemStackReward(MatterOverdriveItems.androidPill,1,2));
        sacrifice = (GenericQuest)new GenericQuest(new QuestLogicKillCreature(new Class[]{EntityChicken.class, EntityCow.class,EntityCow.class},8,15,10).setOnlyChildren(true).setAutoComplete(true),"sacrifice",0).addQuestRewards(new ItemStackReward(Items.saddle),new ItemStackReward(Items.name_tag));
        departmentOfAgriculture = (GenericQuest)new GenericQuest(new QuestLogicCollectItem(new Item[]{Items.wheat,Items.carrot,Items.potato},31,63,3),"department_of_agriculture",0).addQuestRewards(new ItemStackReward(Items.emerald,4),new ItemStackReward(Items.diamond_hoe));
        weaponsOfWar = (GenericQuest)new GenericQuest(new QuestLogicCraft(new ItemStack(Blocks.anvil),1,3,60).setAutoComplete(true),"weapons_of_war",0).addQuestRewards(new ItemStackReward(MatterOverdriveItems.tritaniumSword),new ItemStackReward(MatterOverdriveItems.tritaniumChestplate));
        oneTrueLove = (GenericQuest)new GenericQuest(new QuestLogicMine(Blocks.diamond_ore,1,1,180).setAutoComplete(true),"one_true_love",0).addQuestRewards(new ItemStackReward(Items.emerald,6));
        punyHumans = (GenericQuest)new GenericQuest(new QuestLogicBecomeAndroid(),"puny_humans",256).addQuestRewards(new ItemStackReward(MatterOverdriveItems.battery),new ItemStackReward(MatterOverdriveItems.androidPill,1));
        is_it_really_me = (GenericQuest)new GenericQuest(new QuestLogicSingleEvent(MOEventTransport.class).setAutoComplete(true),"is_it_really_me",120).addQuestRewards(new ItemStackReward(MatterOverdriveItems.item_upgrade,2,4));
        beast_belly = (GenericQuest)new GenericQuest(new QuestLogicSingleEvent(MOEventGravitationalAnomalyConsume.class),"beast_belly",210).addQuestRewards(new ItemStackReward(MatterOverdriveBlocks.gravitational_stabilizer,2));
        weMustKnow = (GenericQuest)new GenericQuest(new QuestLogicPlaceBlock(4,new ItemStack(MatterOverdriveBlocks.decorative_coils).setStackDisplayName("Communication Relay"),1,1).setAutoComplete(true),"we_must_know",120).addQuestRewards(new ItemStackReward(Items.emerald,8));
        crashLanding = (GenericQuest)new GenericQuest(new QuestLogicCraft(new ItemStack(MatterOverdriveItems.security_protocol),0,0,0).setAutoComplete(true),"crash_landing",60).addQuestRewards(new ItemStackReward(new ItemStack(MatterOverdriveBlocks.decorative_coils).setStackDisplayName("Communication Relay")),new QuestStackReward(new QuestStack(weMustKnow)).setCopyNBT("Pos"));
        gmo = (GenericQuest)new GenericMultiQuest(new IQuestLogic[]{new QuestLogicScanBlock(Blocks.carrots,-1,12,24,10).setOnlyDestoryable(true),new QuestLogicScanBlock(Blocks.potatoes,-1,12,24,10).setOnlyDestoryable(true)},"gmo",0).setAutoComplete(true).setSequential(true).addQuestRewards(new ItemStackReward(MatterOverdrive.androidPartsFactory.addAttributeToPart(MatterOverdrive.androidPartsFactory.addAttributeToPart(new ItemStack(MatterOverdriveItems.tritaniumSpine),new AttributeModifier(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), 5, 0)),new AttributeModifier(AndroidAttributes.attributeGlitchTime.getAttributeUnlocalizedName(), -1, 2)).setStackDisplayName("Hardened Tritanium Spine")));
    }

    private static void initModdedQuests()
    {
        toThePowerOf = (GenericQuest)new GenericQuest(new QuestLogicCraft(new QuestItem[]{
                new QuestItem("BigReactors:BRReactorPart","BigReactors"),
                new QuestItem("ExtraUtilities:generator","ExtraUtilities",1,1),
                new QuestItem(new ItemStack(MatterOverdriveItems.battery))
        },0,0,120).setRandomItem(false).setAutoComplete(true),"to_the_power_of",0).addQuestRewards(new ItemStackReward(MatterOverdriveItems.tritanium_ingot,10),new ItemStackReward(MatterOverdriveItems.tritanium_plate,4));
    }

    public static void register(FMLInitializationEvent event, Quests quests)
    {
        registerMatterOverdriveQuests(quests);
        registerModdedQuests(quests);
    }

    private static void registerMatterOverdriveQuests(Quests quests)
    {
        quests.registerQuest("kill_androids",killAndroids);
        quests.registerQuest("cocktail_of_ascension",cocktailOfAscension);
        quests.registerQuest("sacrifice",sacrifice);
        quests.registerQuest("department_of_agriculture",departmentOfAgriculture);
        quests.registerQuest("weapons_of_war",weaponsOfWar);
        quests.registerQuest("one_true_love",oneTrueLove);
        quests.registerQuest("puny_humans",punyHumans);
        quests.registerQuest("is_it_really_me",is_it_really_me);
        quests.registerQuest("beast_belly",beast_belly);
        quests.registerQuest("crash_landing",crashLanding);
        quests.registerQuest("weMustKnow",weMustKnow);
        quests.registerQuest("gmo",gmo);

        contractGeneration.add(new WeightedRandomQuest(killAndroids,100));
        contractGeneration.add(new WeightedRandomQuest(sacrifice,100));
        contractGeneration.add(new WeightedRandomQuest(departmentOfAgriculture,100));
        contractGeneration.add(new WeightedRandomQuest(weaponsOfWar,80));
        contractGeneration.add(new WeightedRandomQuest(oneTrueLove,100));
        contractGeneration.add(new WeightedRandomQuest(is_it_really_me,80));
        contractGeneration.add(new WeightedRandomQuest(beast_belly,60));
    }

    private static void registerModdedQuests(Quests quests)
    {
        quests.registerQuest("to_the_power_of",toThePowerOf);
    }
}
