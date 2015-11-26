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

package matteroverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.data.inventory.SlotContract;
import matteroverdrive.data.quest.Quest;
import matteroverdrive.data.quest.QuestStack;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.handler.quest.Quests;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Created by Simeon on 11/22/2015.
 */
public class TileEntityMachineContractMarket extends MOTileEntityMachine
{
    public static final int QUEST_GENERATE_DELAY_MIN = 20*60*10;
    public static final int QUEST_GENERATE_DELAY_PER_SLOT = 20*60*3;
    public static final int CONTRACT_SLOTS = 18;
    private long lastGenerationTime;
    public TileEntityMachineContractMarket() {
        super(0);
        playerSlotsMain = true;
        playerSlotsHotbar = true;
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        super.RegisterSlots(inventory);
        inventory.AddSlot(new Slot(true));
        for (int i = 0;i < CONTRACT_SLOTS;i++)
        {
            inventory.AddSlot(new SlotContract(false));
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            manageContractGeneration();
        }
    }

    protected void manageContractGeneration()
    {
        if (getRedstoneActive() && getTimeUntilNextQuest() <= 0)
        {
            generateContract();
        }
    }

    private void generateContract()
    {
        Quest quest = ((WeightedRandomQuest)WeightedRandom.getRandomItem(random,MatterOverdriveQuests.contractGeneration)).getQuest();
        QuestStack questStack = MatterOverdrive.questFactory.generateQuestStack(random,quest);
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).getItem() != null)
            {
                ItemStack itemStack = inventory.getSlot(i).getItem();
                if (itemStack.getTagCompound() != null)
                {
                    QuestStack qs = QuestStack.loadFromNBT(itemStack.getTagCompound());
                    if (questStack.getQuest().areQuestStacksEqual(questStack,qs))
                    {
                        return;
                    }
                }
            }
        }

        inventory.addItem(questStack.getContract());
        int freeSlots = getFreeSlots();
        lastGenerationTime = worldObj.getTotalWorldTime() + QUEST_GENERATE_DELAY_MIN + (inventory.getSizeInventory() - freeSlots) * QUEST_GENERATE_DELAY_PER_SLOT;
        forceSync();
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt,categories);
        if (categories.contains(MachineNBTCategory.DATA))
        {
            lastGenerationTime = nbt.getLong("LastGenerationTime");
        }
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt,EnumSet<MachineNBTCategory> categories)
    {
        super.writeCustomNBT(nbt,categories);
        if (categories.contains(MachineNBTCategory.DATA))
        {
            nbt.setLong("LastGenerationTime",lastGenerationTime);
        }
    }

    public int getFreeSlots()
    {
        int freeSlots = 0;
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).getItem() == null)
            {
                freeSlots++;
            }
        }
        return freeSlots;
    }

    public int getTimeUntilNextQuest()
    {
        return Math.max(0,(int)(lastGenerationTime - worldObj.getTotalWorldTime()));
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }
}
