package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.util.RenderUtils;

/**
 * Created by Simeon on 4/25/2015.
 */
public class MOElementButtonVertical extends MOElementButton
{
    int cornersSize;

    public MOElementButtonVertical(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture,int cornersSize) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.cornersSize = cornersSize;
    }

    public MOElementButtonVertical(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture,int cornersSize) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, disabledX, disabledY, sizeX, sizeY, texture);
        this.cornersSize = cornersSize;
    }

    @Override
    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        RenderUtils.drawSizeableVertical(x, y, u, v, width, height, texW, texH,texture,0,cornersSize);
    }
}
