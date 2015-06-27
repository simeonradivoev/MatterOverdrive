package com.MO.MatterOverdrive.data;

/**
 * Created by Simeon on 6/16/2015.
 */
public class HoloIcon extends IconHolder
{
    public HoloIcon(int minX, int minY, int width, int height,int canvasWidth,int canvasHeight)
    {
        super((float)minX / (float)canvasWidth, (float)minY / (float)canvasHeight, ((float)minX / (float)canvasWidth) + ((float)width / (float)canvasWidth), ((float)minY / (float)canvasHeight) + ((float)height / (float)canvasHeight), width, height);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
}
