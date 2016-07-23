package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/24/2015.
 */
public class QuestLogicBecomeAndroid extends AbstractQuestLogic
{
	boolean talkToComplete;

	@Override
	public void loadFromJson(JsonObject jsonObject)
	{

	}

	@Override
	public String modifyInfo(QuestStack questStack, String info)
	{
		return info;
	}

	@Override
	public int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count)
	{
		if (isObjectiveCompleted(questStack, entityPlayer, 0))
		{
			return 2;
		}
		return 1;
	}

	@Override
	public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
	{
		if (objectiveIndex == 0)
		{
			boolean[] hasParts = new boolean[4];
			int[] slots = new int[4];

			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				if (entityPlayer.inventory.getStackInSlot(i) != null && entityPlayer.inventory.getStackInSlot(i).getItem() == MatterOverdrive.items.androidParts)
				{
					int damage = entityPlayer.inventory.getStackInSlot(i).getItemDamage();
					if (damage < hasParts.length)
					{
						hasParts[damage] = true;
						slots[damage] = i;
					}
				}
			}

			for (boolean hasPart : hasParts)
			{
				if (!hasPart)
				{
					return false;
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
	{
		return objective;
	}

	@Override
	public void initQuestStack(Random random, QuestStack questStack)
	{

	}

	@Override
	public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
	{
		return null;
	}

	@Override
	public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer)
	{

	}

	@Override
	public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer)
	{
		boolean[] hasParts = new boolean[4];
		int[] slots = new int[4];

		for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
		{
			if (entityPlayer.inventory.getStackInSlot(i) != null && entityPlayer.inventory.getStackInSlot(i).getItem() == MatterOverdrive.items.androidParts)
			{
				int damage = entityPlayer.inventory.getStackInSlot(i).getItemDamage();
				if (damage < hasParts.length)
				{
					hasParts[damage] = true;
					slots[damage] = i;
				}
			}
		}

		for (boolean hasPart : hasParts)
		{
			if (!hasPart)
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					TextComponentString componentText = new TextComponentString(TextFormatting.GOLD + "<Mad Scientist>" + TextFormatting.RED + MOStringHelper.translateToLocal("entity.mad_scientist.line.fail." + entityPlayer.getRNG().nextInt(4)));
					componentText.setStyle(new Style().setColor(TextFormatting.RED));
					entityPlayer.addChatMessage(componentText);
				}
				return;
			}
		}

		if (!entityPlayer.worldObj.isRemote)
		{
			for (int slot : slots)
			{
				entityPlayer.inventory.decrStackSize(slot, 1);
			}
		}

		MOPlayerCapabilityProvider.GetAndroidCapability(entityPlayer).startConversion();
		entityPlayer.closeScreen();
	}

	@Override
	public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
	{

	}

	public QuestLogicBecomeAndroid setTalkToComplete(boolean talkToComplete)
	{
		this.talkToComplete = talkToComplete;
		return this;
	}
}
