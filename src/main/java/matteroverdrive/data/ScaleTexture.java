package matteroverdrive.data;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
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
    int width;
    int height;
    int u;
    int v;

    public ScaleTexture(ResourceLocation location,int width,int height)
    {
        this.location = location;
        this.texW = width;
        this.texH = height;
        this.width = width;
        this.height = height;
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

        float u = (float)this.u / (float)texW;
        float v = (float)this.v / (float)texH;
        float leftOffset = (float)this.leftOffset / (float)texW;
        float rightOffset = (float)this.rightOffset / (float)texW;
        float topOffset = (float)this.topOffset / (float)texH;
        float bottomOffset = (float)this.bottomOffset / (float)texH;
        int centerWidth = width - (this.leftOffset + this.rightOffset);
        int centerHeight = height - (this.topOffset + this.bottomOffset);
        float centerUWidth = ((float)this.width / (float)texW) - (leftOffset + rightOffset);
        float centerVHeight = ((float)this.height / (float)texH) - (topOffset + bottomOffset);

        glPushMatrix();
        glTranslatef(x, y, 0);
        Tessellator.instance.startDrawingQuads();
        //top left
        Tessellator.instance.addVertexWithUV(0,this.topOffset,0,u,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset,0,u + leftOffset,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset,0,0,u + leftOffset,v);
        Tessellator.instance.addVertexWithUV(0,0,0,u,v);
        //top middle
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset,0,u + leftOffset,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset,0,u + leftOffset + centerUWidth,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,0,0,u + leftOffset + centerUWidth,v);
        Tessellator.instance.addVertexWithUV(this.leftOffset,0,0,u + leftOffset,v);
        //top right
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset,0,u + leftOffset + centerUWidth,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,this.topOffset,0,u + leftOffset + centerUWidth + rightOffset,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,0,0,u + leftOffset + centerUWidth + rightOffset,v);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,0,0,u + leftOffset + centerUWidth,v);
        //middle left
        Tessellator.instance.addVertexWithUV(0,this.topOffset + centerHeight,0,u,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight,0,u + leftOffset,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset,0,u + leftOffset,v + topOffset);
        Tessellator.instance.addVertexWithUV(0,this.topOffset,0,u,v + topOffset);
        //middle
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight,0,u + leftOffset,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset,0,u + leftOffset + centerUWidth,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset,0,u + leftOffset,v + topOffset);
        //middle right
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth + rightOffset,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,this.topOffset,0,u + leftOffset + centerUWidth + rightOffset,v + topOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset,0,u + leftOffset + centerUWidth,v + topOffset);
        //bottom left
        Tessellator.instance.addVertexWithUV(0,this.topOffset + centerHeight + this.bottomOffset,0,u,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight + this.bottomOffset,0,u + leftOffset,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight,0,u + leftOffset,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(0,this.topOffset + centerHeight,0,u,v + topOffset + centerVHeight);
        //bottom middle
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight + this.bottomOffset,0,u + leftOffset,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight + this.bottomOffset,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset,this.topOffset + centerHeight,0,u + leftOffset,v + topOffset + centerVHeight);
        //bottom right
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight + this.bottomOffset,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,this.topOffset + centerHeight + this.bottomOffset,0,u + leftOffset + centerUWidth + rightOffset,v + topOffset + centerVHeight + bottomOffset);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth + this.rightOffset,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth + rightOffset,v + topOffset + centerVHeight);
        Tessellator.instance.addVertexWithUV(this.leftOffset + centerWidth,this.topOffset + centerHeight,0,u + leftOffset + centerUWidth,v + topOffset + centerVHeight);
        Tessellator.instance.draw();
        glPopMatrix();
    }

    private int clamp(int value,int max)
    {
        return MathHelper.clampI(value,0,max);
    }
    public void setLocation(ResourceLocation location)
    {
        this.location = location;
    }
    public void setSize(int width,int height)
    {
        this.width = width;
        this.height = height;
    }

    public ScaleTexture setTextureSize(int width,int height)
    {
        this.texW = width;
        this.texH = height;
        return this;
    }

    public ScaleTexture setUV(int u,int v)
    {
        this.u = u;
        this.v = v;
        return this;
    }
}
