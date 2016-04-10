package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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

	public QuestLogicBlockInteract()
	{
	}

	public QuestLogicBlockInteract(String regex, boolean mustBeInteractable, boolean destoryBlock)
	{
		this.regex = regex;
		this.mustBeInteractable = mustBeInteractable;
		this.destoryBlock = destoryBlock;
	}

	public static void setBlockPosition(QuestStack questStack, BlockPos pos)
	{
		if (questStack.getTagCompound() == null)
		{
			questStack.setTagCompound(new NBTTagCompound());
		}

		questStack.getTagCompound().setLong("pos", pos.toLong());
	}

	@Override
	public void loadFromJson(JsonObject jsonObject)
	{
		super.loadFromJson(jsonObject);
		if (jsonObject.has("block"))
		{
			block = new QuestBlock(jsonObject);
		}
		regex = MOJsonHelper.getString(jsonObject, "regex", null);
		mustBeInteractable = MOJsonHelper.getBool(jsonObject, "intractable", false);
		destoryBlock = MOJsonHelper.getBool(jsonObject, "destroy", false);
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
			objective = objective.replace("$distance", Integer.toString((int)distance));
		}
		//objective = objective.replace("$containerName",containerName);
		return objective;
	}

	@Override
	public void initQuestStack(Random random, QuestStack questStack)
	{

	}

	@Override
	public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
	{
		if (event instanceof PlayerInteractEvent.RightClickBlock)
		{
			PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
			if (!hasInteracted(questStack))
			{
				BlockPos pos = MOQuestHelper.getPosition(questStack);
				if (pos != null)
				{
					if (!pos.equals(((PlayerInteractEvent)event).getPos()))
					{
						return null;
					}
				}

				if (mustBeInteractable)
				{
					TileEntity tileEntity = interactEvent.getWorld().getTileEntity(interactEvent.getPos());
					if (!(tileEntity instanceof IInteractionObject))
					{
						return null;
					}

					if (regex != null && ((!((IInteractionObject)tileEntity).hasCustomName()) || !((IInteractionObject)tileEntity).getName().matches(regex)))
					{
						return null;
					}
				}

				if (destoryBlock && pos != null)
				{
					((PlayerInteractEvent)event).getWorld().setBlockToAir(pos);
				}

				setInteracted(questStack, true);
				markComplete(questStack, entityPlayer);
				return new QuestLogicState(QuestState.Type.COMPLETE, true);
			}
		}
		return null;
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
		{
			return questStack.getTagCompound().getBoolean("interacted");
		}
		return false;
	}

	public void setInteracted(QuestStack questStack, boolean interacted)
	{
		if (questStack.getTagCompound() == null)
		{
			questStack.setTagCompound(new NBTTagCompound());
		}
		questStack.getTagCompound().setBoolean("interacted", interacted);
	}
}
