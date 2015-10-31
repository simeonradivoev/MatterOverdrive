package matteroverdrive.data;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/14/2015.
 */
public class IconHolder implements IIcon
{
    public float minX, minY, maxX, maxY;
    public int width;
    public int height;
    public int canvasWidth;
    public int canvasHeight;
    ResourceLocation canvas;
    String name;

    public IconHolder(float minX, float minY, float maxX, float maxY, int width, int height)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
    }

    public void setCanvasWidth(int width)
    {
        canvasWidth = width;
    }

    public void setCanvasHeight(int height)
    {
        canvasHeight = height;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public IconHolder setCanvas(ResourceLocation canvas)
    {
        this.canvas = canvas;
        return this;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public float getMinU() {
        return minX;
    }

    @Override
    public float getMaxU() {
        return maxX;
    }

    @Override
    public float getInterpolatedU(double p_94214_1_) {
        return 0;
    }

    @Override
    public float getMinV() {
        return minY;
    }

    @Override
    public float getMaxV() {
        return maxY;
    }

    @Override
    public float getInterpolatedV(double p_94207_1_) {
        return 0;
    }

    @Override
    public String getIconName() {
        return name;
    }

    public ResourceLocation getCanvas()
    {
        return canvas;
    }
    public IconHolder setCanvasSize(int width,int height)
    {
        this.canvasWidth = width;
        this.canvasHeight = height;
        return this;
    }
}
