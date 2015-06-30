package matteroverdrive.handler;

import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TooltipHandler
{
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event)
    {
        if (MatterHelper.containsMatter(event.itemStack))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                event.toolTip.add(EnumChatFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + EnumChatFormatting.GOLD + MatterHelper.formatMatter(MatterHelper.getMatterAmountFromItem(event.itemStack)));
            }
        }else
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                event.toolTip.add(EnumChatFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("gui.tooltip.matter.none"));
            }
        }
    }
}
