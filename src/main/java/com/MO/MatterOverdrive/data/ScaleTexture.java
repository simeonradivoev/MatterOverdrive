package com.MO.MatterOverdrive.data;

import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/25/2015.
 */
public class ScaleTexture
{
    private ResourceLocation location;
    int texW;
    int texH;
    int leftOffset;
    int rightOffset;
    int topOffset;
    int bottomOffset;

    public ScaleTexture(ResourceLocation location,int width,int height)
    {
        this.location = location;
        this.texW = width;
        this.texH = height;
    }

    public ScaleTexture setOffsets(int left,int right,int top,int bottom)
    {
        leftOffset = clamp(left,texW);
        rightOffset = clamp(right,texW-left);
        topOffset = clamp(top,texH);
        bottomOffset = clamp(bottom,texH-top);
        return this;
    }

    public void Render(int x,int y,int width,int height)
    {
        Render(x,y,width,height,0);
    }

    public void Render(int x,int y,int width,int height,int zLevel)
    {
        //top
        Minecraft.getMinecraft().renderEngine.bindTexture(location);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_S,
                GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_T,
                GL_CLAMP);


        glPushMatrix();
        glTranslatef(x, y, 0);
        //top left
        RenderUtils.drawSizedTexturedModalRect(0, 0, 0, 0, leftOffset, topOffset, leftOffset, topOffset, texW, texH, zLevel);
        //top middle
        RenderUtils.drawSizedTexturedModalRect(leftOffset, 0, leftOffset, 0, clamp(width - leftOffset - rightOffset,width), topOffset, texW - leftOffset - rightOffset, topOffset, texW, texH, zLevel);
        //top right
        RenderUtils.drawSizedTexturedModalRect(clamp(width - rightOffset,width),0,texW - rightOffset,0,rightOffset,topOffset,rightOffset,topOffset,texW,texH,zLevel);
        //middle left
        RenderUtils.drawSizedTexturedModalRect(0, topOffset, 0, topOffset, leftOffset, clamp(height - topOffset - bottomOffset,height), leftOffset, texH - topOffset - bottomOffset, texW, texH, zLevel);
        //middle
        RenderUtils.drawSizedTexturedModalRect(leftOffset, topOffset, leftOffset, topOffset, clamp(width - leftOffset - rightOffset,width), clamp(height - topOffset - bottomOffset,height), texW - leftOffset - rightOffset, texH - topOffset - bottomOffset, texW, texH, zLevel);
        //middle right
        RenderUtils.drawSizedTexturedModalRect(clamp(width - rightOffset,width), topOffset, texW - rightOffset, topOffset, rightOffset, clamp(height - bottomOffset - topOffset,height), rightOffset, texH - bottomOffset - topOffset, texW, texH, zLevel);
        //bottom left
        RenderUtils.drawSizedTexturedModalRect(0, clamp(height - bottomOffset,height), 0, texH - bottomOffset, leftOffset, bottomOffset, leftOffset, bottomOffset, texW, texH, zLevel);
        //bottom middle
        RenderUtils.drawSizedTexturedModalRect(leftOffset, clamp(height - bottomOffset,height), leftOffset, texH - bottomOffset, clamp(width - leftOffset - rightOffset,width), bottomOffset, texW - leftOffset - rightOffset, bottomOffset, texW, texH, zLevel);
        //bottom right
        RenderUtils.drawSizedTexturedModalRect(clamp(width - rightOffset,width),clamp(height - bottomOffset,height),texW - rightOffset,texH - bottomOffset,rightOffset,bottomOffset,rightOffset ,bottomOffset,texW,texH,zLevel);
        glPopMatrix();
    }

    private int clamp(int value,int max)
    {
        return MathHelper.clampI(value,0,max);
    }
}
