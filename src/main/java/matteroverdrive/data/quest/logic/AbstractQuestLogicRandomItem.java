package matteroverdrive.data.quest.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/24/2015.
 */
public abstract class AbstractQuestLogicRandomItem extends AbstractQuestLogic
{
    QuestItem[] items;
    boolean randomItem;
    boolean anyItem;

    protected void init(QuestItem[] questItems)
    {
        this.items = questItems;
        this.randomItem = true;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        JsonArray itemsElement = jsonObject.getAsJsonArray("items");
        items = new QuestItem[itemsElement.size()];
        for (int i = 0;i < itemsElement.size();i++)
        {
            items[i] = new QuestItem(itemsElement.get(i).getAsJsonObject());
        }
        randomItem = MOJsonHelper.getBool(jsonObject,"random",false);
        anyItem = MOJsonHelper.getBool(jsonObject,"any",false);
    }

    protected void initItemType(Random random, QuestStack questStack)
    {
        if (randomItem)
        {
            List<Integer> avalibleBlocks = new ArrayList<>();
            for (int i = 0;i < items.length;i++)
            {
                ItemStack itemStack = items[i].getItemStack();
                if (itemStack != null)
                {
                    avalibleBlocks.add(i);
                }
            }
            if (avalibleBlocks.size() > 0)
            {
                setItemType(questStack,avalibleBlocks.get(random.nextInt(avalibleBlocks.size())));
            }
        }
        else if (anyItem)
        {

        }
        else
        {
            for (int i = 0;i < items.length;i++)
            {
                ItemStack itemStack = items[i].getItemStack();
                if (itemStack != null)
                {
                    setItemType(questStack,i);
                    return;
                }
            }
        }
    }

    @Override
    public boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer)
    {
        for (QuestItem item : items)
        {
            if (item.canItemExist())
            {
                return true;
            }
        }
        return false;
    }

    public boolean matches(QuestStack questStack,ItemStack itemStack)
    {
        if (anyItem)
        {
            for (QuestItem item : items)
            {
                if (item.matches(itemStack))
                {
                    return true;
                }
            }
        }else
        {
            return items[getItemType(questStack)].matches(itemStack);
        }
        return false;
    }

    public String getItemName(QuestStack questStack)
    {
        if (anyItem)
        {
            String name = "";
            for (QuestItem item : items)
            {
                ItemStack itemStack = item.getItemStack();
                if (itemStack != null)
                    name += itemStack.getDisplayName() + ", ";
                else
                    name += "Unknown Item, ";
            }
            return name;
        }else
        {
            ItemStack itemStack = items[getItemType(questStack)].getItemStack();
            if (itemStack != null)
                return itemStack.getDisplayName();
            else
                return "Unknown Item";
        }
    }

    public int getItemType(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getByte("ItemType");
        }
        return 0;
    }

    public void setItemType(QuestStack questStack,int itemType)
    {
        initTag(questStack);
        getTag(questStack).setByte("ItemType",(byte) itemType);
    }

    public AbstractQuestLogicRandomItem setRandomItem(boolean randomItem)
    {
        this.randomItem = randomItem;
        return this;
    }
}
