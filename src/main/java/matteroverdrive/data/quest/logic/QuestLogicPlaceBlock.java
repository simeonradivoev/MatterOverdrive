package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.BlockEvent;

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

    public QuestLogicPlaceBlock(int radius,ItemStack blockStack)
    {
        this(radius,blockStack,1,1);
    }

    public QuestLogicPlaceBlock(int radius,ItemStack blockStack,int minBlockPlace,int maxBlockPlace)
    {
        super(blockStack);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
    }

    public QuestLogicPlaceBlock(int radius,Block block)
    {
        this(radius,block,-1,1,1);
    }

    public QuestLogicPlaceBlock(int radius,Block block,int blockMetadata)
    {
        this(radius,block,blockMetadata,1,1);
    }

    public QuestLogicPlaceBlock(int radius,Block block,int blockMetadata,int minBlockPlace,int maxBlockPlace)
    {
        super(block,blockMetadata);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
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
        BlockPos pos = getPos(questStack);
        if (pos != null)
        {
            double distance = Vec3.createVectorHelper(Math.floor(entityPlayer.posX),Math.floor(entityPlayer.posY),Math.floor(entityPlayer.posZ)).distanceTo(Vec3.createVectorHelper(pos.x,pos.y,pos.z));
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
            boolean isNearTargetFlag = false;
            if (blockStack != null && placeEvent.itemInHand != null)
            {
                isTheSameBlockFlag = areBlockStackTheSame(placeEvent.itemInHand);
            }
            else
            {
                if (areBlocksTheSame(placeEvent.block,placeEvent.blockMetadata))
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

            BlockPos pos = getPos(questStack);
            if (pos != null && isTheSameBlockFlag)
            {
                if (!(Vec3.createVectorHelper(placeEvent.x,placeEvent.y,placeEvent.z).distanceTo(Vec3.createVectorHelper(pos.x,pos.y,pos.z)) <= radius))
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

    protected BlockPos getPos(QuestStack questStack)
    {
        if (hasTag(questStack) && getTag(questStack).hasKey("Pos", Constants.NBT.TAG_INT_ARRAY))
        {
            int[] pos = getTag(questStack).getIntArray("Pos");
            return new BlockPos(pos[0],pos[1],pos[2]);
        }
        return null;
    }

    public void setNamePattern(String namePattern)
    {
        this.namePattern = namePattern;
    }
}
