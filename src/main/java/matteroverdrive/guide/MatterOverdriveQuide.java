package matteroverdrive.guide;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class MatterOverdriveQuide
{
    @SideOnly(Side.CLIENT)
    private static List<MOGuideEntry> entries = new ArrayList<MOGuideEntry>();

    @SideOnly(Side.CLIENT)
    public static MOGuideEntry Register(ItemStack itemStack)
    {
        MOGuideEntry entry = new MOGuideEntry(itemStack, new ResourceLocation(Reference.PATH_INFO + itemStack.getUnlocalizedName() + ".txt"),itemStack.getDisplayName());
        entry.tooltip = new ArrayList();
        entry.tooltip.add(itemStack.getDisplayName());
        entries.add(entry);
        return entry;
    }

    @SideOnly(Side.CLIENT)
    public static MOGuideEntry Register(Item item)
    {
        return Register(new ItemStack(item));
    }

    @SideOnly(Side.CLIENT)
    public static MOGuideEntry Register(Block block)
    {
        return Register(new ItemStack(block));
    }

    @SideOnly(Side.CLIENT)
    public static List<MOGuideEntry> getQuides()
    {
        return entries;
    }
}
