package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.client.RenderHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Created by Simeon on 6/13/2015.
 */
public interface IWorldLastRenderer
{
    void onRenderWorldLast(RenderHandler handler,RenderWorldLastEvent event);
}
