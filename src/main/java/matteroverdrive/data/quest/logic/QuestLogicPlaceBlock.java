package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/3/2016.
 */
public class QuestLogicPlaceBlock extends QuestLogicBlock
{
    private int radius;
    String namePattern;
    int minBlockPlace;
    int maxBlockPlace;

    public QuestLogicPlaceBlock(){super();}

    public QuestLogicPlaceBlock(int radius,QuestItem blockStack)
    {
        this(radius,blockStack,1,1);
    }

    public QuestLogicPlaceBlock(int radius, QuestItem blockStack, int minBlockPlace, int maxBlockPlace)
    {
        super(blockStack);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
    }

    public QuestLogicPlaceBlock(int radius,QuestBlock block)
    {
        this(radius,block,1,1);
    }

    public QuestLogicPlaceBlock(int radius, QuestBlock block, int minBlockPlace, int maxBlockPlace)
    {
        super(block);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        radius = MOJsonHelper.getInt(jsonObject,"radius",0);
        minBlockPlace = MOJsonHelper.getInt(jsonObject,"place_count_min");
        maxBlockPlace = MOJsonHelper.getInt(jsonObject,"place_count_max");
        namePattern = MOJsonHelper.getString(jsonObject,"regex",null);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getBlockPlaced(questStack) >= getMaxBlockPlace(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        objective = replaceBlockNameInText(objective);
        BlockPos pos = MOQuestHelper.getPosition(questStack);
        if (pos != null)
        {
            double distance = new Vec3(Math.floor(entityPlayer.posX),Math.floor(entityPlayer.posY),Math.floor(entityPlayer.posZ)).distanceTo(new Vec3(pos));
            objective = objective.replace("$distance",Integer.toString((int)Math.max(distance - radius,0))+" blocks");
        }else
        {
            objective = objective.replace("$distance","0 blocks");
        }
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        setMaxBlockPlace(questStack,random(random,minBlockPlace,maxBlockPlace));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof BlockEvent.PlaceEvent)
        {
            BlockEvent.PlaceEvent placeEvent = (BlockEvent.PlaceEvent)event;
            boolean isTheSameBlockFlag = false;
            if (blockStack != null && placeEvent.itemInHand != null)
            {
                isTheSameBlockFlag = areBlockStackTheSame(placeEvent.itemInHand);
            }
            else
            {
                if (areBlocksTheSame(placeEvent.placedBlock))
                {
                    if (namePattern != null && placeEvent.itemInHand != null)
                    {
                        isTheSameBlockFlag = placeEvent.itemInHand.getDisplayName().matches(namePattern);
                    }else
                    {
                        isTheSameBlockFlag = true;
                    }
                }
            }

            BlockPos pos = MOQuestHelper.getPosition(questStack);
            if (pos != null && isTheSameBlockFlag)
            {
                if (!(new Vec3(placeEvent.pos).distanceTo(new Vec3(pos)) <= radius))
                {
                    return false;
                }
            }

            if (isTheSameBlockFlag)
            {
                setBlockPlaced(questStack,getBlockPlaced(questStack)+1);
                if (autoComplete)
                    questStack.markComplited(entityPlayer,false);
            }
            return isTheSameBlockFlag;
        }
        return false;
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

    protected void setBlockPlaced(QuestStack questStack,int placed)
    {
        initTag(questStack);
        getTag(questStack).setShort("Placed",(short) placed);
    }

    protected int getBlockPlaced(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getShort("Placed");
        }
        return 0;
    }

    protected void setMaxBlockPlace(QuestStack questStack,int maxBlockPlace)
    {
        initTag(questStack);
        getTag(questStack).setShort("MaxPlaced",(short) maxBlockPlace);
    }

    protected int getMaxBlockPlace(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getShort("MaxPlaced");
        }
        return 0;
    }

    public void setNamePattern(String namePattern)
    {
        this.namePattern = namePattern;
    }
}
