package matteroverdrive.data.quest.rewards;

import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/3/2016.
 */
public class ItemStackReward implements IQuestReward
{
    ItemStack itemStack;

    public ItemStackReward(Item item,int amount,int damage)
    {
        this.itemStack = new ItemStack(item,amount,damage);
    }

    public ItemStackReward(Item item,int amount)
    {
        this(item,amount,0);
    }

    public ItemStackReward(Item item)
    {
        this(item,1,0);
    }

    public ItemStackReward(Block block, int amount, int damage)
    {
        this.itemStack = new ItemStack(block,amount,damage);
    }

    public ItemStackReward(Block block, int amount)
    {
        this(block,amount,0);
    }

    public ItemStackReward(Block block)
    {
        this(block,1,0);
    }

    public ItemStackReward(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    @Override
    public void giveReward(QuestStack questStack,EntityPlayer entityPlayer)
    {
        if (!entityPlayer.inventory.addItemStackToInventory(itemStack))
        {
            entityPlayer.worldObj.spawnEntityInWorld(new EntityItem(entityPlayer.worldObj,entityPlayer.posX,entityPlayer.posY+entityPlayer.getEyeHeight(),entityPlayer.posZ,itemStack));
        }
    }

    public ItemStack getItemStack(){return itemStack;}
}
