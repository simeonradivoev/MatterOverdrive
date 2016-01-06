package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.api.events.MOEventScan;
import matteroverdrive.api.inventory.IBlockScanner;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/5/2016.
 */
public class QuestLogicScanBlock extends QuestLogicBlock
{
    private int minBlockScan;
    private int maxBlockScan;
    private int xpPerBlock;
    private boolean onlyDestoryable;

    public QuestLogicScanBlock(Block block, int blockMetadata,int minBlockScan,int maxBlockScan,int xpPerBlock)
    {
        super(block, blockMetadata);
        this.minBlockScan = minBlockScan;
        this.maxBlockScan = maxBlockScan;
        this.xpPerBlock = xpPerBlock;
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        return replaceBlockNameInText(info);
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getBlockScan(questStack) >= getMaxBlockScan(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        objective = replaceBlockNameInText(objective);
        objective = objective.replace("$scanAmount",Integer.toString(getBlockScan(questStack)));
        objective = objective.replace("$maxScanAmount",Integer.toString(getMaxBlockScan(questStack)));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        setMaxBlockScan(questStack,random(random,minBlockScan,maxBlockScan));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof MOEventScan)
        {
            MOEventScan eventScan = (MOEventScan)event;
            if (eventScan.position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                if (onlyDestoryable)
                {
                    if (eventScan.scannerStack.getItem() instanceof IBlockScanner && !((IBlockScanner) eventScan.scannerStack.getItem()).destroysBlocks(eventScan.scannerStack))
                    {
                        return false;
                    }
                }

                Block block = entityPlayer.worldObj.getBlock(eventScan.position.blockX,eventScan.position.blockY,eventScan.position.blockZ);
                int meta = entityPlayer.worldObj.getBlockMetadata(eventScan.position.blockX,eventScan.position.blockY,eventScan.position.blockZ);
                if (block != null && areBlocksTheSame(block,meta))
                {
                    if (getBlockScan(questStack) < getMaxBlockScan(questStack))
                    {
                        setBlocScan(questStack,getBlockScan(questStack)+1);

                        if (getBlockScan(questStack) >= getMaxBlockScan(questStack))
                        {
                            if (autoComplete)
                            {
                                questStack.markComplited(entityPlayer,false);
                            }
                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp)
    {
        return originalXp + getMaxBlockScan(questStack) * xpPerBlock;
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

    protected void setMaxBlockScan(QuestStack questStack,int maxBlockScan)
    {
        initTag(questStack);
        getTag(questStack).setShort("MaxBlockScan",(short) maxBlockScan);
    }

    protected int getMaxBlockScan(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getShort("MaxBlockScan");
        }
        return 0;
    }

    protected int getBlockScan(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getShort("BlockScan");
        }
        return 0;
    }

    protected void setBlocScan(QuestStack questStack,int blockScan)
    {
        initTag(questStack);
        getTag(questStack).setShort("BlockScan",(short) blockScan);
    }

    public QuestLogicScanBlock setOnlyDestoryable(boolean onlyDestoryable)
    {
        this.onlyDestoryable = true;
        return this;
    }
}
