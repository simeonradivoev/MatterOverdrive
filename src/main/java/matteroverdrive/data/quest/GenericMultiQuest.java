package matteroverdrive.data.quest;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.logic.AbstractQuestLogic;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/5/2016.
 */
public class GenericMultiQuest extends GenericQuest
{
    protected IQuestLogic[] logics;
    boolean sequential;
    boolean autoComplete;

    public GenericMultiQuest(IQuestLogic[] questLogics, String title, int xpReward)
    {
        super(null, title, xpReward);
        this.logics = questLogics;
        for (int i = 0;i < logics.length;i++)
        {
            if (logics[i] instanceof AbstractQuestLogic)
            {
                ((AbstractQuestLogic) logics[i]).setAutoComplete(true);
                ((AbstractQuestLogic) logics[i]).setId(Integer.toString(i));
            }
        }
    }

    @Override
    public boolean canBeAccepted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
        if (extendedProperties != null)
        {
            for (IQuestLogic logic : logics)
            {
                if (!logic.canAccept(questStack,entityPlayer))
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
            t = logics[getCurrentObjective(questStack)].modifyTitle(questStack,t);
        }
        return t;
    }

    @Override
    public String getTitle(QuestStack questStack,EntityPlayer entityPlayer)
    {
        String t = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".title"),entityPlayer);
        if (sequential)
        {
            t = logics[getCurrentObjective(questStack)].modifyTitle(questStack,t);
        }
        return t;
    }

    @Override
    public String getInfo(QuestStack questStack, EntityPlayer entityPlayer)
    {
        if (sequential)
        {
            String i;
            if(MOStringHelper.hasTranslation("quest." + title + ".info."+getCurrentObjective(questStack)))
            {
                i = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info."+getCurrentObjective(questStack)),entityPlayer);
            }else
            {
                i = replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info"),entityPlayer);
            }

            i = logics[getCurrentObjective(questStack)].modifyInfo(questStack,i);
            return i;
        }
        return replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info"),entityPlayer);
    }

    @Override
    public String getObjective(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return logics[objectiveIndex].modifyObjective(questStack,entityPlayer,replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".objective."+objectiveIndex),entityPlayer),objectiveIndex);
    }

    @Override
    public int getObjectivesCount(QuestStack questStack, EntityPlayer entityPlayer)
    {
        if (sequential)
        {
            return getCurrentObjective(questStack)+1;
        }else
        {
            return logics.length;
        }
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return logics[objectiveIndex].isObjectiveCompleted(questStack,entityPlayer,0);
    }

    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo)
    {
        if (questStackOne.getQuest() instanceof GenericMultiQuest && questStackTwo.getQuest() instanceof GenericMultiQuest)
        {
            return questStackOne.getQuest() == questStackTwo.getQuest();
        }
        return false;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        for (IQuestLogic logic : logics)
        {
            logic.initQuestStack(random,questStack);
        }
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        boolean hasChangedFlag = false;
        for (int i = 0;i < logics.length;i++)
        {
            if (sequential)
            {
                if (i <= getCurrentObjective(questStack))
                {
                    hasChangedFlag |= logics[i].onEvent(questStack,event,entityPlayer);
                }
            }else
            {
                if (logics[i].onEvent(questStack,event,entityPlayer))
                {
                    hasChangedFlag = true;
                }
            }

        }
        return hasChangedFlag;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        for (IQuestLogic logic : logics)
        {
            logic.onCompleted(questStack,entityPlayer);
        }
    }

    @Override
    public int getXpReward(QuestStack questStack, EntityPlayer entityPlayer)
    {
        int xp = xpReward;
        for (IQuestLogic logic : logics)
        {
            xp = logic.modifyXP(questStack,entityPlayer,xp);
        }
        return xp;
    }

    @Override
    public void addToRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
    {
        rewards.addAll(questRewards);
        for (IQuestLogic logic : logics)
        {
            logic.modifyRewards(questStack,entityPlayer,rewards);
        }
    }

    public GenericMultiQuest setSequential(boolean sequential)
    {
        this.sequential = sequential;
        return this;
    }

    @Override
    public void setCompleted(QuestStack questStack,EntityPlayer entityPlayer)
    {
        for (int i = 0;i < logics.length;i++)
        {
            if (!logics[i].isObjectiveCompleted(questStack,entityPlayer,0))
            {
                return;
            }else if (sequential && i >= getCurrentObjective(questStack) && i < logics.length)
            {
                setCurrentObjective(questStack,i+1);
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
            return MathHelper.clamp_int(questStack.getTagCompound().getByte("CurrentObjective"),0,logics.length);
        }return 0;
    }

    public void setCurrentObjective(QuestStack questStack,int objective)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setByte("CurrentObjective",(byte) MathHelper.clamp_int(objective,0,logics.length));
    }

    public GenericMultiQuest setAutoComplete(boolean autoComplete)
    {
        this.autoComplete = autoComplete;
        return this;
    }
}
