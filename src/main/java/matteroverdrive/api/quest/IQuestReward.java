package matteroverdrive.api.quest;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 1/3/2016.
 */
public interface IQuestReward
{
	void loadFromJson(JsonObject object);

	void giveReward(QuestStack questStack, EntityPlayer entityPlayer);

	boolean isVisible(QuestStack questStack);
}
