package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/24/2016.
 */
public class QuestLogicItemInteract extends QuestLogicRandomItem
{
    boolean consumeItem;

    public QuestLogicItemInteract(){}

    public QuestLogicItemInteract(QuestItem item,boolean consumeItem)
    {
        this.consumeItem = consumeItem;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        consumeItem = MOJsonHelper.getBool(jsonObject,"consume",false);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$itemName", getItemName(questStack));
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
        objective = objective.replace("$itemName", getItemName(questStack));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {

    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof PlayerInteractEvent)
        {
            PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
            if (interactEvent.useItem.equals(Event.Result.DEFAULT) && interactEvent.useBlock.equals(Event.Result.DENY));
            {
                boolean hasItem = interactEvent.entityPlayer.getHeldItem() != null;
                if (hasItem)
                {
                    boolean isSameItem = matches(questStack,interactEvent.entityPlayer.getHeldItem());
                    if (isSameItem)
                    {
                        setInteracted(questStack, true);
                        if (consumeItem)
                        {
                            entityPlayer.getHeldItem().stackSize--;
                        }
                        if (autoComplete)
                            questStack.markComplited(entityPlayer, false);
                        return true;
                    }
                }
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
            return questStack.getTagCompound().getBoolean("used");
        return false;
    }

    public void setInteracted(QuestStack questStack, boolean readBook)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setBoolean("used",readBook);
    }
}
