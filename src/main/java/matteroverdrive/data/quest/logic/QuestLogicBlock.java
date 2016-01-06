package matteroverdrive.data.quest.logic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/5/2016.
 */
public abstract class QuestLogicBlock extends AbstractQuestLogic
{
    protected Block block;
    protected int blockMetadata;
    protected ItemStack blockStack;

    public QuestLogicBlock(Block block,int blockMetadata)
    {
        this.block = block;
        this.blockMetadata = blockMetadata;
    }

    public QuestLogicBlock(ItemStack blockStack)
    {
        this.blockStack = blockStack;
    }

    protected boolean areBlockStackTheSame(ItemStack stack)
    {
        if (blockStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(blockStack,stack))
        {
            return true;
        }
        return false;
    }

    protected boolean areBlocksTheSame(Block block,int blockMetadata)
    {
        return this.block == block && (this.blockMetadata >= 0 ? this.blockMetadata == blockMetadata : true);
    }

    protected String replaceBlockNameInText(String text)
    {
        if (blockStack != null)
        {
            text = text.replace("$block", blockStack.getDisplayName());
        }else
        {
            text = text.replace("$block", block.getLocalizedName());
        }
        return text;
    }
}
