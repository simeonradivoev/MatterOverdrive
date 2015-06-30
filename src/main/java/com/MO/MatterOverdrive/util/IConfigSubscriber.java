package com.MO.MatterOverdrive.util;

import com.MO.MatterOverdrive.handler.ConfigurationHandler;

/**
 * Created by Simeon on 5/9/2015.
 */
public interface IConfigSubscriber
{
     void onConfigChanged(ConfigurationHandler config);
}
