package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;

/**
 * Created by Simeon on 4/8/2015.
 */
public abstract class MOElementBase extends ElementBase
{
    public MOElementBase parent;
    public MOElementBase(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }
    public MOElementBase(GuiBase gui, int posX, int posY, int width, int height) {super(gui,posX,posY,width,height);}

    public void updateInfo()
    {

    }
}
