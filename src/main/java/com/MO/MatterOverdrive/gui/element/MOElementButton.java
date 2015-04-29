package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.data.ScaleTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOElementButton extends ElementButton
{
    public static final ScaleTexture NORMAL_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE_DARK = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over_dark.png"),18,18).setOffsets(7,7,7,7);
    IButtonHandler buttonHandler;

    public MOElementButton(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture) {
        super(gui, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.buttonHandler = handler;
    }


    public MOElementButton(GuiBase gui,IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX,
                         int sizeY, String texture) {
        super(gui, posX, posY, name, sheetX, sheetY, hoverX, hoverY,disabledX,disabledY, sizeX, sizeY, texture);
        this.buttonHandler = handler;
    }

    @Override
    public boolean onMousePressed(int x, int y, int mouseButton) {

        if (isEnabled())
        {
            buttonHandler.handleElementButtonClick(getName(), mouseButton);
            return true;
        }
        return false;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawBackground(mouseX, mouseY, gameTicks);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
