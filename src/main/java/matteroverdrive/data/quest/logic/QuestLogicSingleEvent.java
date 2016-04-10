package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.exceptions.MOQuestParseException;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/31/2015.
 */
public class QuestLogicSingleEvent extends AbstractQuestLogic
{
	Class<? extends Event> event;

	public QuestLogicSingleEvent()
	{
	}

	public QuestLogicSingleEvent(Class<? extends Event> event)
	{
		this.event = event;
	}

	@Override
	public void loadFromJson(JsonObject jsonObject)
	{
		String eventName = MOJsonHelper.getString(jsonObject, "event");
		try
		{
			event = (Class<? extends Event>)Class.forName(eventName);
		}
		catch (ClassNotFoundException e)
		{
			throw new MOQuestParseException(String.format("Could not find event class from type: %s", eventName), e);
		}
		catch (ClassCastException e)
		{
			throw new MOQuestParseException(String.format("Class must be derived form Forge Event Super class"), e);
		}
	}

	@Override
	public String modifyInfo(QuestStack questStack, String info)
	{
		return info;
	}

	@Override
	public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
	{
		return hasEventFired(questStack);
	}

	@Override
	public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
	{
		return objective;
	}

	@Override
	public void initQuestStack(Random random, QuestStack questStack)
	{

	}

	@Override
	public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
	{
		if (!hasEventFired(questStack) && this.event.isInstance(event))
		{
			markComplete(questStack, entityPlayer);
			setEventFired(questStack);
			return new QuestLogicState(QuestState.Type.COMPLETE, true);
		}
		return null;
	}

	@Override
	public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer)
	{

	}

	@Override
	public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer)
	{

	}

	@Override
	public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
	{

	}

	public boolean hasEventFired(QuestStack questStack)
	{
		if (hasTag(questStack))
		{
			return getTag(questStack).getBoolean("e");
		}
		return false;
	}

	public void setEventFired(QuestStack questStack)
	{
		initTag(questStack);
		getTag(questStack).setBoolean("e", true);
	}
}
