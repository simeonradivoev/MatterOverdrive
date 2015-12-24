package matteroverdrive.data.quest;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;

/**
 * Created by Simeon on 12/24/2015.
 */
public class QuestBlock
{
    Block block;
    String blockName;
    String mod;

    public QuestBlock(Block block)
    {
        this.block = block;
    }

    public QuestBlock(String blockName,String mod)
    {
        this.blockName = blockName;
        this.mod = mod;
    }

    public boolean isModded()
    {
        return mod != null && !mod.isEmpty();
    }

    public boolean isModPresent()
    {
        return Loader.isModLoaded(mod);
    }

    public boolean canBlockExist()
    {
        if (isModded())
        {
            return isModPresent();
        }return true;
    }

    public Block getBlock()
    {
        if (isModded())
        {
            return (Block) Block.blockRegistry.getObject(blockName);

        }else
        {
            return block;
        }
    }

    public static QuestBlock fromBlock(Block block)
    {
        return new QuestBlock(block);
    }
}
