package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;
import com.MO.MatterOverdrive.container.IButtonHandler;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOElementButton extends ElementButton
{
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
}
