package com.MO.MatterOverdrive.util;

import com.MO.MatterOverdrive.handler.MOConfigurationHandler;

/**
 * Created by Simeon on 5/9/2015.
 */
public interface IConfigSubscriber
{
     void onConfigChanged(MOConfigurationHandler config);
}
