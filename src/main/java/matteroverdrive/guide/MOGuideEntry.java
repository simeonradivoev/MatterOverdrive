package matteroverdrive.guide;

import matteroverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class MOGuideEntry
{
    public ItemStack itemStack;
    public ResourceLocation description;
    public String name;
    public List tooltip;

    public MOGuideEntry(ItemStack itemStack,ResourceLocation description,String name)
    {
        this.itemStack = itemStack;
        this.description = description;
        this.name = name;
    }

    public void RenderIcon(int x,int y)
    {
        if(itemStack != null)
        {
            RenderUtils.renderStack(x, y, itemStack);
        }
    }
}
