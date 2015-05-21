package com.MO.MatterOverdrive.data;

import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 5/14/2015.
 */
public class IconHolder implements IIcon
{
    public float minX,minY,maxX,maxY;

    public IconHolder(float minX,float minY,float maxX,float maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public int getIconWidth() {
        return 0;
    }

    @Override
    public int getIconHeight() {
        return 0;
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
        return null;
    }
}
