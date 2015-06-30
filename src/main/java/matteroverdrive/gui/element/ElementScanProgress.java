package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.util.helpers.MathHelper;

import matteroverdrive.Reference;
import matteroverdrive.util.math.MOMathHelper;

import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Simeon on 3/18/2015.
 */
public class ElementScanProgress extends MOElementBase
{
    Random random = new Random();
    int seed = 0;
    float progress;
    float[] values;
    private static float NoiseSize = 0.1f;

    public ElementScanProgress(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        this.setTexture(Reference.PATH_ELEMENTS + "screen.png",117,47);
        values = new float[26];
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX,posY,0);

        gui.bindTexture(texture);
        this.drawTexturedModalRect(0,0,0,0,117,47);
        GuiColor color = new GuiColor(191,228,230);

        random.setSeed(seed);
        int progress = MathHelper.floor(this.progress * 26);
        
        int marginsTop = 8;
        int marginsLeft = 7;
        int maxHeight = 32;

        for (int i = 0;i < 26;i++)
        {
            float newValue = 0;

            if(i < progress)
            {
            	double noiseValue = ((MOMathHelper.noise(0, 0.05f * i, Math.pow(seed * 100,2)) + 1.0) / 2.0);
            	double contrastFactor = 2;
            	noiseValue = contrastFactor * (noiseValue - 0.5) + 0.5;
            	noiseValue = Math.pow(Math.min(noiseValue, 1), 2);
            	noiseValue = noiseValue * 0.8 + random.nextDouble() * 0.2;
            	
                newValue = (float)noiseValue;
                int height = MathHelper.round(values[i] * maxHeight);
                int x1 = marginsLeft + i * 4;
                int y1 = maxHeight + marginsTop;
                int x2 = x1 + 2;
                int y2 = maxHeight-height + marginsTop;
                
                gui.drawSizedRect(x1, y1, x2, y2, color.getColor());
                gui.drawSizedRect(x1, y2 - 1, x2, y2 - 2, color.getColor());
            }else
            {
                newValue = 0;
            }

            values[i] = MOMathHelper.Lerp(values[i],newValue,0.05f);
        }

        GL11.glPopMatrix();
        GL11.glColor3f(1, 1, 1);
    }

    public void setSeed(int seed)
    {
        this.seed = seed;
    }

    public void setProgress(float progress)
    {
        this.progress = progress;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
