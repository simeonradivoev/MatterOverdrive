/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.util;

import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.starmap.PlanetStatType;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

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

    public static String formatNumber(double number)
    {
        return formatNumber(number,"0.00");
    }
    public static String formatNumber(double number, String decialFormat)
    {
        if (number > 1000000000000000D)
        {
            return new DecimalFormat(decialFormat+"Q").format((number / 1000000000000000.00D));
        }
        if (number > 1000000000000D)
        {
            return new DecimalFormat(decialFormat+"T").format((number / 1000000000000.00D));
        }
        else if (number > 1000000000D)
        {
            return new DecimalFormat(decialFormat+"B").format((number / 1000000000.00D));
        }
        else if (number > 1000000D) {
            return new DecimalFormat(decialFormat+"M").format((number / 1000000.00D));
        }
        else if (number > 1000D)
        {
            return new DecimalFormat(decialFormat+"K").format((number / 1000.00D));
        }
        else {
            return new DecimalFormat(decialFormat).format(number);
        }
    }

    public static String formatRemainingTime(float seccounds)
    {
       return formatRemainingTime(seccounds,false);
    }

    public static String formatRemainingTime(float seccounds,boolean shotSufix)
    {
        if (seccounds > 3600)
        {
            return String.format("%s%s",String.valueOf(Math.round(seccounds / 3600)),shotSufix ? "h" : " hr");
        }
        else if (seccounds > 60 && seccounds < 60 * 60)
        {
            return String.format("%s%s",String.valueOf(Math.round(seccounds / 60)),shotSufix ? "m" : " min");
        }else
        {
            return String.format("%s%s",String.valueOf(Math.round(seccounds)),shotSufix ? "s" : " sec");
        }
    }

    public static String typingAnimation(String message,int time,int maxTime)
    {
        float percent = ((float)time / (float)maxTime);
        int messageCount = message.length();
        return message.substring(0, MathHelper.clamp_int(Math.round(messageCount * percent),0,messageCount));
    }

    public static boolean hasTranslation(String string){return StatCollector.canTranslate(string);}

    public static String translateToLocal(String string)
    {
        return StatCollector.translateToLocal(string);
    }

    public static String translateToLocal(PlanetStatType statType)
    {
        return StatCollector.translateToLocal("planet_stat." + statType.getUnlocalizedName() + ".name");
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
            case Reference.WS_HEAL:
                return value > 0;
            default:
                return value >= 1;
        }
    }

    public static String[] formatVariations(String unlocalizedName,String unlocalizedSuffix,int count)
    {
        String[] variations = new String[count];
        for (int i = 0;i < count;i++)
        {
            variations[i] = unlocalizedName + "."+i+"." + unlocalizedSuffix;
        }
        return variations;
    }

    public static boolean isControlKeyDown() {
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }
    public static boolean isAltKeyDown() {return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);}
}
