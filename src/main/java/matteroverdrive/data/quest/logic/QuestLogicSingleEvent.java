package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/31/2015.
 */
public class QuestLogicSingleEvent extends AbstractQuestLogic
{
    Class<? extends Event> event;

    public QuestLogicSingleEvent(Class<? extends Event> event)
    {
        this.event = event;
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
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (!hasEventFired(questStack) && this.event.isInstance(event))
        {
            if (autoComplete)
            {
                questStack.markComplited(entityPlayer,false);
            }
            setEventFired(questStack);
            return true;
        }
        return false;
    }

    @Override
    public void onTaken(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
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
        getTag(questStack).setBoolean("e",true);
    }
}
