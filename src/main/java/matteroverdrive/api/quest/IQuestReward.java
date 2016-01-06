package matteroverdrive.api.quest;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 1/3/2016.
 */
public interface IQuestReward
{
    void giveReward(QuestStack questStack,EntityPlayer entityPlayer);
}
