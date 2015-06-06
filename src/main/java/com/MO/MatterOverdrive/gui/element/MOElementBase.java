package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.util.RenderUtils;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 4/8/2015.
 */
public abstract class MOElementBase extends ElementBase
{
    private GuiColor color = new GuiColor(255,255,255);
    public MOElementBase parent;
    public MOElementBase(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }
    public MOElementBase(GuiBase gui, int posX, int posY, int width, int height) {super(gui,posX,posY,width,height);}

    public void updateInfo()
    {

    }

    public void init()
    {

    }

    public GuiColor getColor() {
        return color;
    }

    public void setColor(int r,int g,int b,int alpha)
    {
        this.color = new GuiColor(r,g,b,alpha);
    }

    public void setColor(GuiColor color)
    {
        this.color = color;
    }

    protected void ApplyColor()
    {
        RenderUtils.applyColor(color);
    }

    protected void ResetColor()
    {
        GL11.glColor3f(1, 1, 1);
    }

    protected int getGlobalX()
    {
        int x = posX;

        if (parent != null)
        {
            x += parent.getGlobalX();
        }
        return x;
    }

    protected int getGlobalY()
    {
        int y = posY;

        if (parent != null)
        {
            y += parent.getGlobalY();
        }
        return y;
    }
}
