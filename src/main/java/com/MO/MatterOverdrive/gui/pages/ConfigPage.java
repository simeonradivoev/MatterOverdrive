package com.MO.MatterOverdrive.gui.pages;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.MOGuiMachine;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ConfigPage extends ElementBaseGroup
{
    MOGuiMachine machineGui;

    public ConfigPage(MOGuiMachine gui, int posX, int posY, int width,int height)
    {
        super(gui,posX,posY,width,height);
        machineGui = gui;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);
        if (machineGui.getMachine().hasOwner()) {
            String info = MOStringHelper.translateToLocal("gui.config.owner") + ": " + machineGui.getMachine().getOwner();
            int width = getFontRenderer().getStringWidth(info);
            getFontRenderer().drawString(info, sizeX - width - 20, sizeY - 45, Reference.COLOR_GUI_DARK.getColor());
        }
    }
}
