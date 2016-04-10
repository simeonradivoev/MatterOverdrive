package matteroverdrive.data.quest;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.*;
import matteroverdrive.data.quest.logic.AbstractQuestLogic;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/5/2016.
 */
public class GenericMultiQuest extends GenericQuest
{
	protected final IQuestLogic[] logics;
	boolean sequential;
	boolean autoComplete;

	public GenericMultiQuest(String title, JsonObject jsonObject, IQuestLogic[] questLogics)
	{
		super(title, jsonObject, null);
		this.logics = questLogics;
		configureLogics();
		this.autoComplete = MOJsonHelper.getBool(jsonObject, "auto_complete", false);
		this.sequential = MOJsonHelper.getBool(jsonObject, "sequential", false);
	}

	public GenericMultiQuest(IQuestLogic[] questLogics, String title, int xpReward)
	{
		super(null, title, xpReward);
		this.logics = questLogics;
		configureLogics();
	}

	private void configureLogics()
	{
		for (int i = 0; i < logics.length; i++)
		{
			if (logics[i] instanceof AbstractQuestLogic)
			{
				((AbstractQuestLogic)logics[i]).setAutoComplete(true);
				((AbstractQuestLogic)logics[i]).setId(Integer.toString(i));
			}
		}
	}

	@Override
	public boolean canBeAccepted(QuestStack questStack, EntityPlayer entityPlayer)
	{
		MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(entityPlayer);
		if (extendedProperties != null)
		{
			for (IQuestLogic logic : logics)
			{
				if (!logic.canAccept(questStack, entityPlayer))
				{
					return false;
				}
			}
		}
		return !extendedProperties.hasCompletedQuest(questStack) && !extendedProperties.hasQuest(questStack);
	}

	@Override
	public String getTitle(QuestStack questStack)
	{
		String t = MOStringHelper.translateToLocal("quest." + title + ".title");
		if (sequential)
		{
			t = logics[getCurrentObjective(questStack)].modifyTitle(questStack, t);
		}
		return t;
	}

	@Override
	public String getTitle(QuestStack questStack, EntityPlayer entityPlayer)
	{
		String t = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".title"), entityPlayer);
		if (sequential)
		{
			t = logics[getCurrentObjective(questStack)].modifyTitle(questStack, t);
		}
		return t;
	}

	@Override
	public String getInfo(QuestStack questStack, EntityPlayer entityPlayer)
	{
		if (sequential)
		{
			String i;
			if (MOStringHelper.hasTranslation("quest." + title + ".info." + getCurrentObjective(questStack)))
			{
				i = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info." + getCurrentObjective(questStack)), entityPlayer);
			}
			else
			{
				i = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info"), entityPlayer);
			}

			i = logics[getCurrentObjective(questStack)].modifyInfo(questStack, i);
			return i;
		}
		return replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info"), entityPlayer);
	}

	@Override
	public String getObjective(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
	{
		return logics[objectiveIndex].modifyObjective(questStack, entityPlayer, replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".objective." + objectiveIndex), entityPlayer), objectiveIndex);
	}

	@Override
	public int getObjectivesCount(QuestStack questStack, EntityPlayer entityPlayer)
	{
		if (sequential)
		{
			return getCurrentObjective(questStack) + 1;
		}
		else
		{
			return logics.length;
		}
	}

	@Override
	public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
	{
		return logics[objectiveIndex].isObjectiveCompleted(questStack, entityPlayer, 0);
	}

	@Override
	public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo)
	{
		return questStackOne.getQuest() instanceof GenericMultiQuest && questStackTwo.getQuest() instanceof GenericMultiQuest && questStackOne.getQuest() == questStackTwo.getQuest();
	}

	@Override
	public void initQuestStack(Random random, QuestStack questStack)
	{
		for (IQuestLogic logic : logics)
		{
			logic.initQuestStack(random, questStack);
		}
	}

	@Override
	public QuestState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
	{
		QuestState.Type type = null;
		List<Integer> logicIds = null;
		for (int i = 0; i < logics.length; i++)
		{
			if (sequential)
			{
				if (i <= getCurrentObjective(questStack))
				{
					QuestLogicState logicState = logics[i].onEvent(questStack, event, entityPlayer);
					if (logicState != null && logicState.isShowOnHud())
					{
						if (logicIds == null)
						{
							logicIds = new ArrayList<>();
						}
						logicIds.add(i);
						type = QuestState.Type.UPDATE;
					}
				}
				else if (i >= getObjectivesCount(questStack, entityPlayer) - 1)
				{
					type = QuestState.Type.COMPLETE;
				}
			}
			else
			{
				QuestLogicState logicState = logics[i].onEvent(questStack, event, entityPlayer);
				if (logicState != null && logicState.isShowOnHud())
				{
					if (type == null || type == QuestState.Type.UPDATE)
					{
						type = logicState.getType();
						if (logicIds == null)
						{
							logicIds = new ArrayList<>();
						}
						logicIds.add(i);
					}
				}
			}

		}
		if (type != null && logicIds != null)
		{
			int[] logicIdsArray = new int[logicIds.size()];
			for (int i = 0; i < logicIds.size(); i++)
			{
				logicIdsArray[i] = logicIds.get(i);
			}
			return new QuestState(type, logicIdsArray, true);
		}
		return null;
	}

	@Override
	public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
	{
		for (IQuestLogic logic : logics)
		{
			logic.onQuestCompleted(questStack, entityPlayer);
		}
	}

	@Override
	public int getXpReward(QuestStack questStack, EntityPlayer entityPlayer)
	{
		int xp = xpReward;
		for (IQuestLogic logic : logics)
		{
			xp = logic.modifyXP(questStack, entityPlayer, xp);
		}
		return xp;
	}

	@Override
	public void addToRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
	{
		rewards.addAll(questRewards);
		for (IQuestLogic logic : logics)
		{
			logic.modifyRewards(questStack, entityPlayer, rewards);
		}
	}

	public GenericMultiQuest setSequential(boolean sequential)
	{
		this.sequential = sequential;
		return this;
	}

	@Override
	public void setCompleted(QuestStack questStack, EntityPlayer entityPlayer)
	{
		for (int i = 0; i < logics.length; i++)
		{
			if (!logics[i].isObjectiveCompleted(questStack, entityPlayer, 0))
			{
				return;
			}
			else if (sequential && i >= getCurrentObjective(questStack) && i < logics.length)
			{
				setCurrentObjective(questStack, i + 1);
			}
		}
		if (autoComplete)
		{
			super.setCompleted(questStack, entityPlayer);
		}
	}

	public int getCurrentObjective(QuestStack questStack)
	{
		if (questStack.getTagCompound() != null)
		{
			return MathHelper.clamp_int(questStack.getTagCompound().getByte("CurrentObjective"), 0, logics.length - 1);
		}
		return 0;
	}

	public void setCurrentObjective(QuestStack questStack, int objective)
	{
		if (questStack.getTagCompound() == null)
		{
			questStack.setTagCompound(new NBTTagCompound());
		}

		questStack.getTagCompound().setByte("CurrentObjective", (byte)MathHelper.clamp_int(objective, 0, logics.length - 1));
	}

	public GenericMultiQuest setAutoComplete(boolean autoComplete)
	{
		this.autoComplete = autoComplete;
		return this;
	}
}
