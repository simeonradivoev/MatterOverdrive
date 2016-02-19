package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/24/2016.
 */
public class QuestLogicBlockInteract extends AbstractQuestLogic
{
    private String regex;
    private boolean mustBeInteractable;
    private boolean destoryBlock;
    private QuestBlock block;

    public QuestLogicBlockInteract(){}

    public QuestLogicBlockInteract(String regex, boolean mustBeInteractable, boolean destoryBlock)
    {
        this.regex = regex;
        this.mustBeInteractable = mustBeInteractable;
        this.destoryBlock = destoryBlock;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        if (jsonObject.has("block"))
        {
            block = new QuestBlock(jsonObject);
        }
        regex = MOJsonHelper.getString(jsonObject,"regex",null);
        mustBeInteractable = MOJsonHelper.getBool(jsonObject,"intractable",false);
        destoryBlock = MOJsonHelper.getBool(jsonObject,"destroy",false);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        //info = info.replace("$containerName",containerName);
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return hasInteracted(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        BlockPos pos = MOQuestHelper.getPosition(questStack);
        if (pos != null)
        {
            double distance = Math.sqrt(entityPlayer.getPosition().distanceSq(pos));
            objective = objective.replace("$distance",Integer.toString((int)distance));
        }
        //objective = objective.replace("$containerName",containerName);
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {

    }

    public static void setBlockPosition(QuestStack questStack,BlockPos pos)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setLong("pos",pos.toLong());
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof PlayerInteractEvent)
        {
            PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
            if (interactEvent.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && !hasInteracted(questStack))
            {
                BlockPos pos = MOQuestHelper.getPosition(questStack);
                if (pos != null)
                {
                    if (!pos.equals(((PlayerInteractEvent) event).pos))
                        return false;
                }

                if (mustBeInteractable)
                {
                    TileEntity tileEntity = interactEvent.world.getTileEntity(interactEvent.pos);
                    if (!(tileEntity instanceof IInteractionObject))
                        return false;

                    if (regex != null && ((!((IInteractionObject) tileEntity).hasCustomName()) || !((IInteractionObject) tileEntity).getName().matches(regex)))
                        return false;
                }

                if (destoryBlock && pos != null)
                    ((PlayerInteractEvent) event).world.setBlockToAir(pos);

                setInteracted(questStack,true);

                if (autoComplete)
                    questStack.markComplited(entityPlayer,false);
                return true;
            }
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

    public boolean hasInteracted(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
            return questStack.getTagCompound().getBoolean("interacted");
        return false;
    }

    public void setInteracted(QuestStack questStack,boolean interacted)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());
        questStack.getTagCompound().setBoolean("interacted",interacted);
    }
}
