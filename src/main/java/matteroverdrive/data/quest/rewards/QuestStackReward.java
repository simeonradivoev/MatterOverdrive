package matteroverdrive.data.quest.rewards;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.Quest;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.handler.quest.Quests;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;

/**
 * Created by Simeon on 1/3/2016.
 */
public class QuestStackReward implements IQuestReward
{
    QuestStack questStack;
    String questName;
    NBTTagCompound questNbt;
    String[] copyNBT;
    boolean visible;

    public QuestStackReward(){}

    public QuestStackReward(QuestStack questStack)
    {
        this.questStack = questStack;
    }

    public QuestStackReward setCopyNBT(String... copyNBT)
    {
        this.copyNBT = copyNBT;
        return this;
    }

    @Override
    public void loadFromJson(JsonObject object)
    {
        questName = MOJsonHelper.getString(object,"id");
        questNbt = MOJsonHelper.getNbt(object,"nbt",null);
        if (object.has("copy_nbt") && object.get("copy_nbt").isJsonArray())
        {
            JsonArray array = object.get("copy_nbt").getAsJsonArray();
            String[] elements = new String[array.size()];
           for (int i = 0;i < elements.length;i++)
           {
               elements[i] = array.get(i).getAsString();
           }
            copyNBT = elements;
        }
        this.visible = MOJsonHelper.getBool(object,"visible",true);
    }

    @Override
    public void giveReward(QuestStack completedQuest,EntityPlayer entityPlayer)
    {
        QuestStack questStack = getQuestStack();

        if (questStack != null && questStack.canAccept(entityPlayer,questStack))
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
            if (extendedProperties != null)
            {
                QuestStack questStackCopy = questStack.copy();
                questStackCopy.getQuest().initQuestStack(entityPlayer.getRNG(),questStackCopy);
                if (copyNBT != null && copyNBT.length > 0 && completedQuest.getTagCompound() != null)
                {
                    if (questStackCopy.getTagCompound() == null)
                        questStackCopy.setTagCompound(new NBTTagCompound());

                    for (String aCopyNBT : copyNBT)
                    {
                        NBTBase nbtBase = completedQuest.getTagCompound().getTag(aCopyNBT);
                        if (nbtBase != null)
                        {

                            questStackCopy.getTagCompound().setTag(aCopyNBT, nbtBase.copy());
                        }
                    }
                }
                extendedProperties.addQuest(questStackCopy);
            }
        }
    }

    @Override
    public boolean isVisible(QuestStack questStack)
    {
        return visible;
    }

    public QuestStack getQuestStack()
    {
        if (questStack == null)
        {
            IQuest quest = MatterOverdrive.quests.getQuestByName(questName);
            if (quest != null)
            {
                QuestStack questStack = new QuestStack(quest);
                if (questNbt != null)
                {
                    questStack.setTagCompound(questNbt);
                }
                return questStack;
            }
        }else
        {
            return questStack;
        }
        return null;
    }
}
