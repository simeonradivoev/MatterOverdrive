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
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import matteroverdrive.data.quest.GenericQuest;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.data.quest.RandomQuestText;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.data.quest.logic.*;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import matteroverdrive.handler.quest.Quests;
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

    public static void init(FMLPreInitializationEvent event)
    {
        initMatterOverdriveQuests();
        initModdedQuests();
    }

    private static void initMatterOverdriveQuests()
    {
        killAndroids = new RandomQuestText(new QuestLogicKillCreature(EntityRougeAndroidMob.class,3,6,40).setAutoComplete(true),"kill_androids",1,0);
        cocktailOfAscension = (GenericQuest)new GenericQuest(new QuestLogicCocktailOfAscension(),"cocktail_of_ascension",512).addItemReward(new ItemStack(MatterOverdriveItems.androidPill,2));
        sacrifice = new GenericQuest(new QuestLogicKillCreature(new Class[]{EntityChicken.class, EntityCow.class,EntityCow.class},8,15,10).setOnlyChildren(true).setAutoComplete(true),"sacrifice",0);
        departmentOfAgriculture = new GenericQuest(new QuestLogicCollectItem(new Item[]{Items.wheat,Items.carrot,Items.potato},31,63,3),"department_of_agriculture",0);
        weaponsOfWar = new GenericQuest(new QuestLogicCraft(new ItemStack(Blocks.anvil),1,3,60).setAutoComplete(true),"weapons_of_war",0);
        oneTrueLove = (GenericQuest)new GenericQuest(new QuestLogicMine(Blocks.diamond_ore,1,1,180).setAutoComplete(true),"one_true_love",0).addItemReward(new ItemStack(Items.emerald,6));
        punyHumans = (GenericQuest)new GenericQuest(new QuestLogicBecomeAndroid(),"puny_humans",256).addItemReward(new ItemStack(MatterOverdriveItems.battery));
    }

    private static void initModdedQuests()
    {
        toThePowerOf = (GenericQuest)new GenericQuest(new QuestLogicCraft(new QuestItem[]{
                new QuestItem("BigReactors:BRReactorPart","BigReactors"),
                new QuestItem("ExtraUtilities:generator","ExtraUtilities",1,1),
                new QuestItem(new ItemStack(MatterOverdriveItems.battery))
        },0,0,120).setRandomItem(false).setAutoComplete(true),"to_the_power_of",0).addItemRewards(new ItemStack(MatterOverdriveItems.tritanium_ingot,10),new ItemStack(MatterOverdriveItems.tritanium_plate,4));
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

        contractGeneration.add(new WeightedRandomQuest(killAndroids,100));
        contractGeneration.add(new WeightedRandomQuest(sacrifice,100));
        contractGeneration.add(new WeightedRandomQuest(departmentOfAgriculture,100));
        contractGeneration.add(new WeightedRandomQuest(weaponsOfWar,80));
        contractGeneration.add(new WeightedRandomQuest(oneTrueLove,100));
    }

    private static void registerModdedQuests(Quests quests)
    {
        quests.registerQuest("to_the_power_of",toThePowerOf);
    }
}
