package matteroverdrive.data.quest.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import matteroverdrive.api.events.MOEventDialogConstruct;
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.exceptions.MORuntimeException;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.dialog.DialogMessage;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/24/2016.
 */
public class QuestLogicConversation extends AbstractQuestLogic
{
    String regex;
    String npcType;
    IDialogOption[] given;
    IDialogOption targetOption;

    public QuestLogicConversation(){}

    public QuestLogicConversation(String npcType,DialogMessage targetOption,DialogMessage[] given)
    {
        this.npcType = npcType;
        this.targetOption = targetOption;
        this.given = given;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        npcType = MOJsonHelper.getString(jsonObject,"npc");
        if (jsonObject.has("given"))
        {
            JsonArray givenArray = jsonObject.getAsJsonArray("given");
            given = new IDialogOption[givenArray.size()];
            for (int i = 0;i < givenArray.size();i++)
            {
                given[i] = MatterOverdrive.dialogAssembler.parseOption(givenArray.get(i),MatterOverdrive.dialogRegistry);
            }
        }
        if (jsonObject.has("target"))
        {
            targetOption = MatterOverdrive.dialogAssembler.parseOption(jsonObject.get("target"),MatterOverdrive.dialogRegistry);
            if (targetOption == null)
            {
                throw new MORuntimeException("Conversation Quest Logic mush have a target dialog option");
            }
        }
        regex = MOJsonHelper.getString(jsonObject,"regex",null);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$target", MOStringHelper.translateToLocal("entity." + npcType + ".name"));
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return hasTalked(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        objective = objective.replace("$target", MOStringHelper.translateToLocal("entity." + npcType + ".name"));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {

    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof MOEventDialogInteract)
        {
            if (isTarget(((MOEventDialogInteract) event).npc) && targetOption.equalsOption(((MOEventDialogInteract) event).dialogOption))
            {
                setTalked(questStack,true);
                if (autoComplete)
                    questStack.markComplited(entityPlayer,false);
                return true;
            }
        }
        else if (event instanceof MOEventDialogConstruct.Post)
        {
            if (given != null && isTarget(((MOEventDialogConstruct) event).npc))
            {
                if (((MOEventDialogConstruct) event).mainMessage instanceof DialogMessage)
                {
                    for (IDialogOption option : given)
                    {
                        ((DialogMessage) ((MOEventDialogConstruct) event).mainMessage).addOption(option);
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

    public boolean isTarget(IDialogNpc npc)
    {
        EntityLiving entity = npc.getEntity();
        if (regex != null && !entity.getDisplayName().getFormattedText().matches(regex))
            return false;

        return npcType.equals(EntityList.getEntityString(entity));
    }

    public boolean hasTalked(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
            return questStack.getTagCompound().getBoolean("talked");
        return false;
    }

    public void setTalked(QuestStack questStack,boolean talked)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());
        questStack.getTagCompound().setBoolean("talked",talked);
    }
}
