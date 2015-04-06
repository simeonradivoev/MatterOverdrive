package com.MO.MatterOverdrive.util;

import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 4/6/2015.
 */
public class MOStringHelper
{
    public static final String MORE_INFO = EnumChatFormatting.RESET.toString() + EnumChatFormatting.GRAY + "Hold "+ EnumChatFormatting.ITALIC + EnumChatFormatting.YELLOW + "Shift" + EnumChatFormatting.RESET.toString() + EnumChatFormatting.GRAY + " for Details.";

    private static String[] suffix = new String[]{"","K", "M", "B", "T"};
    private static int MAX_LENGTH = 4;

    public static String formatNUmber(double number)
    {
        if (number > 1000000000000000D)
        {
            return new DecimalFormat("0.00Q").format((number / 1000000000000000.00D));
        }
        if (number > 1000000000000D)
        {
            return new DecimalFormat("0.00T").format((number / 1000000000000.00D));
        }
        else if (number > 1000000000D)
        {
            return new DecimalFormat("0.00B").format((number / 1000000000.00D));
        }
        else if (number > 1000000D) {
            return new DecimalFormat("0.00M").format((number / 1000000.00D));
        }
        else if (number > 1000D)
        {
            return new DecimalFormat("0.00K").format((number / 1000.00D));
        }
        else {
            return String.valueOf(number);
        }
    }
}
