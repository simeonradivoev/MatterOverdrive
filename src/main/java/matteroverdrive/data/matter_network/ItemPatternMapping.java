package matteroverdrive.data.matter_network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 1/30/2016.
 */
public class ItemPatternMapping
{
    private ItemPattern itemPattern;
    private BlockPos databaseId;
    private int storageId;
    private int patternId;

    public ItemPatternMapping(ByteBuf byteBuf)
    {
        itemPattern = ItemPattern.fromBuffer(byteBuf);
        databaseId = BlockPos.fromLong(byteBuf.readLong());
        storageId = byteBuf.readByte();
        patternId = byteBuf.readByte();
    }

    public ItemPatternMapping(ItemPattern itemPattern,BlockPos databaseId,int storageId,int patternId)
    {
        this.databaseId = databaseId;
        this.itemPattern = itemPattern;
        this.storageId = storageId;
        this.patternId = patternId;
    }

    public ItemPattern getItemPattern()
    {
        return itemPattern;
    }

    public BlockPos getDatabaseId()
    {
        return databaseId;
    }

    public void writeToBuffer(ByteBuf byteBuf)
    {
        ItemPattern.writeToBuffer(byteBuf,itemPattern);
        byteBuf.writeLong(databaseId.toLong());
        byteBuf.writeByte(storageId);
        byteBuf.writeByte(patternId);
    }

    public int getStorageId(){return storageId;}

    public int getPatternId(){return patternId;}
}
