package com.MO.MatterOverdrive.guide;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class MatterOverdriveQuide
{
    private static List<MOGuideEntry> entries = new ArrayList<MOGuideEntry>();

    public static MOGuideEntry Register(ItemStack itemStack,String description)
    {
        MOGuideEntry entry = new MOGuideEntry(itemStack, Arrays.asList(description.split("/p")),itemStack.getDisplayName());
        entry.tooltip = itemStack.getTooltip(Minecraft.getMinecraft().thePlayer,false);
        entries.add(entry);
        return entry;
    }

    public static MOGuideEntry Register(Item item,String description)
    {
        return Register(new ItemStack(item),description);
    }

    public static MOGuideEntry Register(Block block,String description)
    {
        return Register(new ItemStack(block),description);
    }

    public static List<MOGuideEntry> getQuides()
    {
        return entries;
    }
}
