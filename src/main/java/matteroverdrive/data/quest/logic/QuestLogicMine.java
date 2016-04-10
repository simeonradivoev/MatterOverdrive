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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/9/2015.
 */
public class QuestLogicMine extends AbstractQuestLogic
{
	QuestBlock[] blocks;
	int minMineCount;
	int maxMineCount;
	int xpPerMine;
	boolean randomBlock;
	boolean destryDrops;

	public QuestLogicMine()
	{
	}

	public QuestLogicMine(IBlockState block, int minMineCount, int maxMineCount, int xpPerMine)
	{
		this.blocks = new QuestBlock[] {QuestBlock.fromBlock(block)};
		this.minMineCount = minMineCount;
		this.maxMineCount = maxMineCount;
		this.xpPerMine = xpPerMine;
		this.randomBlock = true;
	}

	@Override
	public void loadFromJson(JsonObject jsonObject)
	{
		super.loadFromJson(jsonObject);
		JsonArray blocksElement = jsonObject.getAsJsonArray("blocks");
		blocks = new QuestBlock[blocksElement.size()];
		for (int i = 0; i < blocks.length; i++)
		{
			blocks[i] = new QuestBlock(blocksElement.get(i).getAsJsonObject());
		}
		minMineCount = MOJsonHelper.getInt(jsonObject, "mine_count_min");
		maxMineCount = MOJsonHelper.getInt(jsonObject, "mine_count_max");
		xpPerMine = MOJsonHelper.getInt(jsonObject, "xp", 0);
		randomBlock = MOJsonHelper.getBool(jsonObject, "random", false);
		destryDrops = MOJsonHelper.getBool(jsonObject, "destroy_drops", false);
	}

	@Override
	public String modifyInfo(QuestStack questStack, String info)
	{
		info = info.replace("$maxMineAmount", Integer.toString(getMaxMineCount(questStack)));
		IBlockState state = getBlock(questStack);
		info = info.replace("$mineBlock", state != null ? state.getBlock().getLocalizedName() : "Unknown Block");
		return info;
	}

	@Override
	public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
	{
		return getMineCount(questStack) >= getMaxMineCount(questStack);
	}

	@Override
	public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
	{
		objective = objective.replace("$mineAmount", Integer.toString(getMineCount(questStack)));
		objective = objective.replace("$maxMineAmount", Integer.toString(getMaxMineCount(questStack)));
		IBlockState state = getBlock(questStack);
		objective = objective.replace("$mineBlock", state.getBlock() != null ? state.getBlock().getLocalizedName() : "Unknown Block");
		return objective;
	}

	@Override
	public void initQuestStack(Random random, QuestStack questStack)
	{
		initTag(questStack);
		initBlockType(random, questStack);
		getTag(questStack).setInteger("MaxMineCount", random(random, minMineCount, maxMineCount));
	}

	private void initBlockType(Random random, QuestStack questStack)
	{
		if (randomBlock)
		{
			List<Integer> avalibleBlocks = new ArrayList<>();
			for (int i = 0; i < blocks.length; i++)
			{
				IBlockState block = blocks[i].getBlockState();
				if (block != null)
				{
					avalibleBlocks.add(i);
				}
			}
			if (avalibleBlocks.size() > 0)
			{
				setBlockType(questStack, avalibleBlocks.get(random.nextInt(avalibleBlocks.size())));
			}
		}
		else
		{
			for (int i = 0; i < blocks.length; i++)
			{
				IBlockState block = blocks[i].getBlockState();
				if (block != null)
				{
					setBlockType(questStack, i);
				}
			}
		}
	}

	@Override
	public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
	{
		if (event instanceof BlockEvent.HarvestDropsEvent)
		{
			BlockEvent.HarvestDropsEvent harvestEvent = (BlockEvent.HarvestDropsEvent)event;
			IBlockState state = getBlock(questStack);
			if (state != null && harvestEvent.getState().equals(state))
			{
				if (getMineCount(questStack) < getMaxMineCount(questStack))
				{
					if (destryDrops)
					{
						harvestEvent.getDrops().clear();
					}

					setMineCount(questStack, getMineCount(questStack) + 1);
					if (isObjectiveCompleted(questStack, entityPlayer, 0))
					{
						markComplete(questStack, entityPlayer);
						return new QuestLogicState(QuestState.Type.COMPLETE, true);
					}
					else
					{
						return new QuestLogicState(QuestState.Type.UPDATE, true);
					}
				}
			}
		}
		return null;
	}

	@Override
	public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer)
	{

	}

	@Override
	public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp)
	{
		return originalXp + getMaxMineCount(questStack) * xpPerMine;
	}

	@Override
	public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer)
	{

	}

	@Override
	public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
	{

	}

	public int getMineCount(QuestStack questStack)
	{
		if (hasTag(questStack))
		{
			return getTag(questStack).getInteger("MineCount");
		}
		return 0;
	}

	public void setMineCount(QuestStack questStack, int mineCount)
	{
		initTag(questStack);
		getTag(questStack).setInteger("MineCount", mineCount);
	}

	public int getMaxMineCount(QuestStack questStack)
	{
		if (hasTag(questStack))
		{
			return getTag(questStack).getInteger("MaxMineCount");
		}
		return 0;
	}

	public int getBlockType(QuestStack questStack)
	{
		if (hasTag(questStack))
		{
			return getTag(questStack).getByte("BlockType");
		}
		return 0;
	}

	public void setBlockType(QuestStack questStack, int blockType)
	{
		initTag(questStack);
		getTag(questStack).setByte("BlockType", (byte)blockType);
	}

	public IBlockState getBlock(QuestStack questStack)
	{
		int blockType = getBlockType(questStack);
		if (blockType < blocks.length)
		{
			return blocks[blockType].getBlockState();
		}
		return null;
	}

	public QuestLogicMine setRandomBlock(boolean randomBlock)
	{
		this.randomBlock = randomBlock;
		return this;
	}

	public QuestLogicMine setDestroyDrops(boolean destryDrops)
	{
		this.destryDrops = destryDrops;
		return this;
	}
}
