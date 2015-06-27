package com.MO.MatterOverdrive.gui.pages;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.MOGuiMachine;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementStates;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.MO.MatterOverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ConfigPage extends ElementBaseGroup
{
    MOGuiMachine machineGui;
    ElementStates redstoneState;

    public ConfigPage(MOGuiMachine gui, int posX, int posY, int width,int height)
    {
        super(gui,posX,posY,width,height);
        machineGui = gui;
        redstoneState = new ElementStates(gui,gui,120,40,"RedstoneMode",60,21,new String[]{MOStringHelper.translateToLocal("gui.redstone_mode.high"),MOStringHelper.translateToLocal("gui.redstone_mode.low"),MOStringHelper.translateToLocal("gui.redstone_mode.disabled")});
        redstoneState.setLabel(MOStringHelper.translateToLocal("gui.config.redstone") + ": ");
        redstoneState.setNormalTexture(MOElementButton.HOVER_TEXTURE);
    }

    @Override
    public void init()
    {
        super.init();
        addElement(redstoneState);
        redstoneState.setSelectedState(machineGui.getMachine().getRedstoneMode());
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
