package matteroverdrive.data.quest.rewards;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * Created by Simeon on 3/1/2016.
 */
public class SoundReward implements IQuestReward
{
	String soundName;
	float volume;
	float pitch;

	@Override
	public void loadFromJson(JsonObject object)
	{
		soundName = JsonUtils.getString(object, "name");
		volume = JsonUtils.getFloat(object, "volume", 1);
		pitch = JsonUtils.getFloat(object, "pitch", 1);
	}

	@Override
	public void giveReward(QuestStack questStack, EntityPlayer entityPlayer)
	{
		if (!SoundEvent.soundEventRegistry.containsKey(new ResourceLocation(soundName)))
		{
			return;
		}
		entityPlayer.worldObj.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvent.soundEventRegistry.getObject(new ResourceLocation(soundName)), SoundCategory.MUSIC, volume, pitch);
	}

	@Override
	public boolean isVisible(QuestStack questStack)
	{
		return false;
	}
}
