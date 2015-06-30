package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.render.RenderHelper;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/3/2015.
 */
public class MOElementButtonScaled extends MOElementButton
{
    private ScaleTexture normalTexture;
    private ScaleTexture overTexture;
    private ScaleTexture disabledTexture;
    private ScaleTexture downTexture;

    public MOElementButtonScaled(GuiBase gui, IButtonHandler handler, int posX, int posY, String name,int sizeX,int sizeY) {
        super(gui, handler, posX, posY, name, 0, 0, 0, 0, sizeX, sizeY, "");
        normalTexture = NORMAL_TEXTURE;
        overTexture = HOVER_TEXTURE;
        downTexture = HOVER_TEXTURE_DARK;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        if (color != null)
            RenderUtils.applyColor(color);
        else
            GL11.glColor4f(1, 1, 1, 1);
        if (isEnabled()) {
            if (!isDown) {
                if (intersectsWith(mouseX, mouseY) && overTexture != null) {
                    overTexture.Render(posX, posY, sizeX, sizeY);
                } else if (normalTexture != null) {
                    normalTexture.Render(posX, posY, sizeX, sizeY);
                }
            }else
            {
                if (downTexture != null)
                    downTexture.Render(posX,posY,sizeX,sizeY);
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
            if (icon != null)
            {
                int width = getFontRenderer().getStringWidth(text) - icon.getIconWidth();
                getFontRenderer().drawString(text,posX + sizeX / 2 - (width / 2),posY + sizeY / 2 - 3,labelColor);
                ClientProxy.holoIcons.bindSheet();
                RenderHelper.renderIcon(posX + sizeX / 2 - icon.getIconWidth() - width/2,posY + sizeY / 2 - icon.getIconHeight()/2,0,icon,icon.getIconWidth(),icon.getIconHeight());
            }else
            {
                int width = getFontRenderer().getStringWidth(text);
                getFontRenderer().drawString(text,posX + sizeX / 2 - (width / 2),posY + sizeY / 2 - 3,labelColor);
            }
        }else
        {
            if (icon != null) {
                ClientProxy.holoIcons.bindSheet();
                RenderHelper.renderIcon(posX + sizeX / 2 - icon.getIconWidth()/2,posY + sizeY / 2 - icon.getIconHeight()/2,0,icon,icon.getIconWidth(),icon.getIconHeight());
            }
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

    public ScaleTexture getDownTexture(){return downTexture;}
    public void setDownTexture(ScaleTexture downTexture){this.downTexture = downTexture;}

    public ScaleTexture getDisabledTextureTexture() {
        return disabledTexture;
    }

    public void setDisabledTexture(ScaleTexture disabledTexture) {
        this.disabledTexture = disabledTexture;
    }
}
