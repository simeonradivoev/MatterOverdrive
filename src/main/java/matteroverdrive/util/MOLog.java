package matteroverdrive.util;

import cpw.mods.fml.common.FMLLog;
import matteroverdrive.Reference;
import org.apache.logging.log4j.Level;

/**
 * Created by Simeon on 6/28/2015.
 */
public class MOLog
{
    public static void log(Level level,String message,Object... args)
    {
        FMLLog.log(Reference.MOD_NAME,level, message, args);
    }

    public static void log(Level level,Throwable throwable,String message,Object... args)
    {
        FMLLog.log(Reference.MOD_NAME,level,throwable, message, args);
    }
}
