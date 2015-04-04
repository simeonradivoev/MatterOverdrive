package com.MO.MatterOverdrive.guide;

import com.MO.MatterOverdrive.Reference;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class MatterOverdriveQuide
{
    private static List<MOGuideEntry> entries = new ArrayList<MOGuideEntry>();

    public static MOGuideEntry Register(ItemStack itemStack)
    {
        MOGuideEntry entry = new MOGuideEntry(itemStack, new ResourceLocation(Reference.PATH_INFO + itemStack.getUnlocalizedName() + ".txt"),itemStack.getDisplayName());
        entry.tooltip = itemStack.getTooltip(Minecraft.getMinecraft().thePlayer,false);
        entries.add(entry);
        return entry;
    }

    public static MOGuideEntry Register(Item item)
    {
        return Register(new ItemStack(item));
    }

    public static MOGuideEntry Register(Block block)
    {
        return Register(new ItemStack(block));
    }

    public static List<MOGuideEntry> getQuides()
    {
        return entries;
    }
}
