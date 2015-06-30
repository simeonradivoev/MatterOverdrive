package matteroverdrive.util;

import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.api.inventory.UpgradeTypes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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

    public static String typingAnimation(String message,int time,int maxTime)
    {
        float percent = ((float)time / (float)maxTime);
        int messageCount = message.length();
        return message.substring(0, MathHelper.clampI(Math.round(messageCount * percent),0,messageCount));
    }

    public static boolean hasTranslation(String string){return StatCollector.canTranslate(string);}

    public static String translateToLocal(String string)
    {
        return StatCollector.translateToLocal(string);
    }

    public static String translateToLocal(UpgradeTypes type)
    {
        return StatCollector.translateToLocal("upgradetype." + type.name() + ".name");
    }
    public static String weaponStatTranslateToLocal(int type)
    {
        return StatCollector.translateToLocal("weaponstat." + type + ".name");
    }
    public static String toInfo(UpgradeTypes type,double value,boolean good)
    {
        String info = "";
        if (good)
            info += EnumChatFormatting.GREEN;
        else
            info += EnumChatFormatting.RED;


        DecimalFormat format = new DecimalFormat("##");
        info += translateToLocal(type) + ": ";
        info += format.format(value * 100);
        return info + "%";
    }

    public static String weaponStatToInfo(int type,double value,boolean good)
    {
        String info = "";
        if (good)
            info += EnumChatFormatting.GREEN;
        else
            info += EnumChatFormatting.RED;


        DecimalFormat format = new DecimalFormat("##");
        info += weaponStatTranslateToLocal(type) + ": ";
        info += format.format(value * 100);
        return info + "%";
    }

    public static String toInfo(UpgradeTypes type,double value)
    {
        return toInfo(type,value,getGood(type,value));
    }

    public static String weaponStatToInfo(int type,double value)
    {
        return weaponStatToInfo(type, value, weaponStatGetGood(type, value));
    }

    public static boolean getGood(UpgradeTypes type,double value)
    {
        switch (type)
        {
            case Speed:
               return value < 1;
            case PowerUsage:
                return value < 1;
            case Fail:
                return value < 1;
            default:
                return value >= 1;
        }
    }

    public static String readTextFile(ResourceLocation location)
    {
       StringBuilder text = new StringBuilder();
        try {
            String path = "/assets/"+location.getResourceDomain()+"/" + location.getResourcePath();
            InputStream descriptionStream = MOStringHelper.class.getResourceAsStream(path);
            LineNumberReader descriptionReader = new LineNumberReader(new InputStreamReader(descriptionStream));
            String line;

            while ((line = descriptionReader.readLine()) != null) {
                text.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    public static String addPrefix(String name,String prefix)
    {
        if (prefix.endsWith("-"))
        {
            return prefix.substring(0,prefix.length()-2) + Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        else
        {
            return prefix + " " + name;
        }
    }

    public static String addSuffix(String name, String suffix)
    {
        if (suffix.startsWith("-"))
        {
            return name + suffix.substring(1);
        }
        else
        {
            return name + " " + suffix;
        }
    }

    public static boolean weaponStatGetGood(int type,double value)
    {
        switch (type)
        {
            default:
                return value >= 1;
        }
    }

}
