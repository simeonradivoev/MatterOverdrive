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

package matteroverdrive.api.events;

import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

/**
 * Created by Simeon on 11/19/2015.
 */
public class MOEventQuest extends PlayerEvent
{
	public final QuestStack questStack;

	public MOEventQuest(QuestStack questStack, EntityPlayer entityPlayer)
	{
		super(entityPlayer);
		this.questStack = questStack;
	}

	public static class Completed extends MOEventQuest
	{
		public int xp;
		public List<IQuestReward> rewards;

		public Completed(QuestStack questStack, EntityPlayer entityPlayer, int xp, List<IQuestReward> rewards)
		{
			super(questStack, entityPlayer);
			this.xp = xp;
			this.rewards = rewards;
		}

		public boolean isCancelable()
		{
			return true;
		}
	}

	public static class Added extends MOEventQuest
	{
		public Added(QuestStack questStack, EntityPlayer entityPlayer)
		{
			super(questStack, entityPlayer);
		}

		public boolean isCancelable()
		{
			return true;
		}
	}
}
