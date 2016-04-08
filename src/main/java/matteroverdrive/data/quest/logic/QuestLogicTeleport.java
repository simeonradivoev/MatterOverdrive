package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.events.MOEventTransport;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/30/2015.
 */
public class QuestLogicTeleport extends AbstractQuestLogic
{
    int minDistance;
    int maxDistance;

    public QuestLogicTeleport()
    {
    }

    public QuestLogicTeleport(BlockPos pos)
    {
        this(pos,0,0);
    }

    public QuestLogicTeleport(BlockPos pos,int minDistance,int maxDistance)
    {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        minDistance = MOJsonHelper.getInt(jsonObject,"distance_min",0);
        maxDistance = MOJsonHelper.getInt(jsonObject,"distance_max",0);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return hasTeleported(questStack);
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
        if (event instanceof MOEventTransport)
        {
            BlockPos pos = MOQuestHelper.getPosition(questStack);

            if (pos != null)
            {
                int distance = ((MOEventTransport) event).destination.getDistance(pos);
                if (distance < maxDistance && distance > minDistance)
                {
                    markComplete(questStack,entityPlayer);
                    setTeleported(questStack);
                    return new QuestLogicState(QuestState.Type.COMPLETE,true);
                }
            }else
            {
                markComplete(questStack,entityPlayer);
                setTeleported(questStack);
                return new QuestLogicState(QuestState.Type.COMPLETE,true);
            }

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

    public void setTeleported(QuestStack questStack)
    {
        initTag(questStack);
        getTag(questStack).setBoolean("Teleported",true);
    }

    public boolean hasTeleported(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getBoolean("Teleported");
        }
        return false;
    }
}
