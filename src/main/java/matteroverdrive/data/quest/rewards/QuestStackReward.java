package matteroverdrive.data.quest.rewards;

import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 1/3/2016.
 */
public class QuestStackReward implements IQuestReward
{
    QuestStack questStack;
    String[] copyNBT;

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
    public void giveReward(QuestStack completedQuest,EntityPlayer entityPlayer)
    {
        if (this.questStack != null && this.questStack.canAccept(entityPlayer,this.questStack))
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
            if (extendedProperties != null)
            {
                QuestStack questStack = this.questStack.copy();
                questStack.getQuest().initQuestStack(entityPlayer.getRNG(),questStack);
                if (copyNBT != null && copyNBT.length > 0 && completedQuest.getTagCompound() != null)
                {
                    if (questStack.getTagCompound() == null)
                        questStack.setTagCompound(new NBTTagCompound());

                    for (int i = 0;i < copyNBT.length;i++)
                    {
                        NBTBase nbtBase = completedQuest.getTagCompound().getTag(copyNBT[i]);
                        if (nbtBase != null)
                        {

                            questStack.getTagCompound().setTag(copyNBT[i],nbtBase.copy());
                        }
                    }
                }
                extendedProperties.addQuest(questStack);
            }
        }
    }
}
