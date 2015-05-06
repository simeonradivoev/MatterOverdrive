package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.data.ScaleTexture;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/3/2015.
 */
public class MOElementButtonScaled extends MOElementButton
{
    private ScaleTexture normalTexture;
    private ScaleTexture overTexture;
    private ScaleTexture disabledTexture;

    public MOElementButtonScaled(GuiBase gui, IButtonHandler handler, int posX, int posY, String name,int sizeX,int sizeY) {
        super(gui, handler, posX, posY, name, 0, 0, 0, 0, sizeX, sizeY, "");
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        if (isEnabled()) {
            if (intersectsWith(mouseX, mouseY) && overTexture != null) {

                overTexture.Render(posX,posY,sizeX,sizeY);
            } else if (normalTexture != null){
                normalTexture.Render(posX,posY,sizeX,sizeY);
            }
        } else if (disabledTexture != null){
            disabledTexture.Render(posX,posY,sizeX,sizeY);
        }else if (normalTexture != null)
        {
            normalTexture.Render(posX,posY,sizeX,sizeY);
        }

        GL11.glDisable(GL11.GL_BLEND);




    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (text != null && !text.isEmpty())
        {
            int width = getFontRenderer().getStringWidth(text);
            getFontRenderer().drawString(text,posX + sizeX / 2 - (width / 2),posY + sizeY / 2 - 3,0xFFFFFF);
        }
    }

    public ScaleTexture getNormalTexture() {
        return normalTexture;
    }

    public void setNormalTexture(ScaleTexture normalTexture) {
        this.normalTexture = normalTexture;
    }

    public ScaleTexture getOverTexture() {
        return overTexture;
    }

    public void setOverTexture(ScaleTexture overTexture) {
        this.overTexture = overTexture;
    }

    public ScaleTexture getDisabledTextureTexture() {
        return disabledTexture;
    }

    public void setDisabledTexture(ScaleTexture disabledTexture) {
        this.disabledTexture = disabledTexture;
    }
}
