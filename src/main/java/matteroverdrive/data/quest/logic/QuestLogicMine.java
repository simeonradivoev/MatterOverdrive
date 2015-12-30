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

package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.data.quest.QuestStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/9/2015.
 */
public class QuestLogicMine extends AbstractQuestLogic
{
    QuestBlock[] blocks;
    boolean hasMetadata;
    int metadata;
    int minMineCount;
    int maxMineCount;
    int xpPerMine;
    boolean randomBlock;

    public QuestLogicMine(Block block,int minMineCount,int maxMineCount,int xpPerMine)
    {
        this.blocks = new QuestBlock[]{QuestBlock.fromBlock(block)};
        this.minMineCount = minMineCount;
        this.maxMineCount = maxMineCount;
        this.xpPerMine = xpPerMine;
        this.randomBlock = true;
    }

    public QuestLogicMine(Block block,int minMineCount,int maxMineCount,int xpPerMine,int metadata)
    {
        this(block,minMineCount,maxMineCount,xpPerMine);
        this.metadata = metadata;
        this.hasMetadata = true;
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$maxMineAmount",Integer.toString(getMaxMineCount(questStack)));
        Block block = getBlock(questStack);
        info = info.replace("$mineBlock",block != null ? block.getLocalizedName() : "Unknown Block");
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getMineCount(questStack) >= getMaxMineCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        objective = objective.replace("$mineAmount",Integer.toString(getMineCount(questStack)));
        objective = objective.replace("$maxMineAmount",Integer.toString(getMaxMineCount(questStack)));
        Block block = getBlock(questStack);
        objective = objective.replace("$mineBlock",block != null ? block.getLocalizedName() : "Unknown Block");
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        initBlockType(random,questStack);
        questStack.getTagCompound().setInteger("MaxMineCount",random(random,minMineCount,maxMineCount));
    }

    private void initBlockType(Random random,QuestStack questStack)
    {
        if (randomBlock)
        {
            List<Integer> avalibleBlocks = new ArrayList<>();
            for (int i = 0;i < blocks.length;i++)
            {
                Block block = blocks[i].getBlock();
                if (block != null)
                {
                    avalibleBlocks.add(i);
                }
            }
            if (avalibleBlocks.size() > 0)
            {
                setBlockType(questStack,avalibleBlocks.get(random.nextInt(avalibleBlocks.size())));
            }
        }else
        {
            for (int i = 0;i < blocks.length;i++)
            {
                Block block = blocks[i].getBlock();
                if (block != null)
                {
                    setBlockType(questStack,i);
                }
            }
        }
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof BlockEvent.HarvestDropsEvent)
        {
            BlockEvent.HarvestDropsEvent harvestEvent = (BlockEvent.HarvestDropsEvent)event;
            Block block = getBlock(questStack);
            if (block != null && harvestEvent.block == block && (!hasMetadata || harvestEvent.blockMetadata == metadata))
            {
                if (getMineCount(questStack) < getMaxMineCount(questStack))
                {
                    setMineCount(questStack, getMineCount(questStack) + 1);
                    if (isObjectiveCompleted(questStack,entityPlayer,0) && autoComplete)
                    {
                        questStack.setCompleted(true);
                    } else
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onTaken(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp) {
        return originalXp + getMaxMineCount(questStack) * xpPerMine;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<ItemStack> rewards) {

    }

    public int getMineCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("MineCount");
        }
        return 0;
    }

    public void setMineCount(QuestStack questStack,int mineCount)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setInteger("MineCount",mineCount);
    }

    public int getMaxMineCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("MaxMineCount");
        }
        return 0;
    }

    public int getBlockType(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getByte("BlockType");
        }return 0;
    }

    public void setBlockType(QuestStack questStack,int blockType)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setByte("BlockType",(byte) blockType);
    }

    public Block getBlock(QuestStack questStack)
    {
        int blockType = getBlockType(questStack);
        if (blockType < blocks.length)
        {
            return blocks[blockType].getBlock();
        }
        return null;
    }

    public QuestLogicMine setRandomBlock(boolean randomBlock)
    {
        this.randomBlock = randomBlock;
        return this;
    }
}
