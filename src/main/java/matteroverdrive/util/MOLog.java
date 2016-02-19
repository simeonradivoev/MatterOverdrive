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
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.apache.logging.log4j.Level;

/**
 * Created by Simeon on 8/11/2015.
 */
public class MOLog
{
    public static String LOG_PREFIX = String.format("[%s]: ",Reference.MOD_NAME);

    public static void log(Level level, String format, Object... data)
    {
        FMLRelaunchLog.log(Reference.MOD_NAME, level, LOG_PREFIX + format, data);
    }

    public static void log(Level level, Throwable ex, String format, Object... data)
    {
        FMLRelaunchLog.log(Reference.MOD_NAME, level, ex, LOG_PREFIX + format, data);
    }

    public static void bigWarning(String format, Object... data)
    {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        log(Level.WARN, "****************************************");
        log(Level.WARN, "* "+format, data);
        for (int i = 2; i < 8 && i < trace.length; i++)
        {
            log(Level.WARN, "*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        log(Level.WARN, "****************************************");
    }

    public static void warn(String format, Object... data)
    {
        log(Level.WARN, format, data);
    }

    public static void info(String format, Object... data)
    {
        log(Level.INFO, format, data);
    }

    public static void debug(String format, Object... data)
    {
        log(Level.DEBUG, format, data);
    }

    public static void error(String format, Object... data)
    {
        log(Level.ERROR, format, data);
    }

    public static void error(String format,Throwable t, Object... data)
    {
        log(Level.ERROR,t, format, data);
    }

    public static void fatal(String format, Object... data)
    {
        log(Level.FATAL, format, data);
    }

    public static void fatal(String format,Throwable t, Object... data)
    {
        log(Level.FATAL,t, format, data);
    }
}
