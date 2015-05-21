package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/17/2015.
 */
public class ElementDoubleCircleBar extends MOElementBase
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar.png");
    public static final ResourceLocation OVERLAY = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_top.png");
    public static final ResourceLocation BACK = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_bottom.png");
    private float progressLeft;
    private float progressRight;

    private GuiColor colorLeft;
    private GuiColor colorRight;

    public ElementDoubleCircleBar(GuiBase gui, int posX, int posY, int width, int height, GuiColor color)
    {
        super(gui, posX, posY, width, height);
        colorLeft = color;
        colorRight = color;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        gui.bindTexture(BACK);
        glColor4f(1,1,1,1);
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX,sizeY,135,135);

        //left
        gui.bindTexture(BG);
        glAlphaFunc(GL_GREATER,1 - progressLeft);
        if (colorLeft != null)
        glColor4f(colorLeft.getFloatR(),colorLeft.getFloatG(),colorLeft.getFloatB(),colorLeft.getFloatA());
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX / 2,sizeY,135,135);
        glAlphaFunc(GL_GREATER,0.2f);

        //right
        gui.bindTexture(BG);
        glAlphaFunc(GL_GREATER,1 - progressRight);
        if (colorRight != null)
            glColor4f(colorRight.getFloatR(),colorRight.getFloatG(),colorRight.getFloatB(),colorRight.getFloatA());
        gui.drawSizedTexturedModalRect(posX + 135 / 2,posY,135/2,0,sizeX / 2,sizeY,135,135);
        glAlphaFunc(GL_GREATER,0.2f);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        gui.bindTexture(OVERLAY);
        glColor4f(1,1,1,1);
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX,sizeY,135,135);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    public void setProgressLeft(float progressLeft)
    {
        this.progressLeft = progressLeft;
    }

    public void setProgressRight(float progressRight)
    {
        this.progressRight = progressRight;
    }

    public void setColorLeft(GuiColor color)
    {
        this.colorLeft = color;
    }

    public void setColorRight(GuiColor color)
    {
        this.colorRight = color;
    }

    public float getProgressLeft()
    {
        return progressLeft;
    }

    public float getProgressRight()
    {
        return progressRight;
    }
}
