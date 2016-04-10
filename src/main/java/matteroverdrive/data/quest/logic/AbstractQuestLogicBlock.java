package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.data.quest.QuestItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/5/2016.
 */
public abstract class AbstractQuestLogicBlock extends AbstractQuestLogic
{
	protected QuestBlock block;
	protected QuestItem blockStack;

	public AbstractQuestLogicBlock()
	{
	}

	public AbstractQuestLogicBlock(QuestBlock block)
	{
		this.block = block;
	}

	public AbstractQuestLogicBlock(QuestItem blockStack)
	{
		this.blockStack = blockStack;
	}

	@Override
	public void loadFromJson(JsonObject jsonObject)
	{
		if (jsonObject.has("block"))
		{
			block = new QuestBlock(jsonObject.getAsJsonObject("block"));
		}
		else
		{
			blockStack = new QuestItem(jsonObject.getAsJsonObject("item"));
		}
	}

	protected boolean areBlockStackTheSame(ItemStack stack)
	{
		return blockStack.getItemStack().isItemEqual(stack) && ItemStack.areItemStackTagsEqual(blockStack.getItemStack(), stack);
	}

	protected boolean areBlocksTheSame(IBlockState blockState)
	{
		return this.block.isTheSame(blockState);
	}

	protected String replaceBlockNameInText(String text)
	{
		if (blockStack != null)
		{
			text = text.replace("$block", blockStack.getItemStack().getDisplayName());
		}
		else
		{
			text = text.replace("$block", block.getBlockState().getBlock().getLocalizedName());
		}
		return text;
	}
}
