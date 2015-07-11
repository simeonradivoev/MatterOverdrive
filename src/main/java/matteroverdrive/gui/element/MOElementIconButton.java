package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 6/17/2015.
 */
public class MOElementIconButton extends MOElementButton
{
    IIcon icon;
    GuiColor iconColor;

    public MOElementIconButton(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture,IIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, disabledX, disabledY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    public MOElementIconButton(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture,IIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (icon != null)
        {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            ClientProxy.holoIcons.bindSheet();
            if (iconColor != null)
                RenderUtils.applyColor(iconColor);
            else
                GL11.glColor3f(1,1,1);
            RenderHelper.renderIcon(posX - icon.getIconWidth()/2 + sizeX/2,posY - icon.getIconHeight()/2 + sizeY/2,0,icon,icon.getIconWidth(),icon.getIconHeight());
        }
    }

    public void setIconColor(GuiColor iconColor)
    {
        this.iconColor = iconColor;
    }

    public void setIcon(IIcon icon)
    {
        this.icon = icon;
    }
}
