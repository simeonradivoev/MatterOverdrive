package matteroverdrive.gui.pages;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementStates;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.network.packet.server.PacketChangeRedstoneMode;
import matteroverdrive.util.MOStringHelper;

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
        redstoneState = new ElementStates(gui,this,120,40,"RedstoneMode",60,21,new String[]{MOStringHelper.translateToLocal("gui.redstone_mode.high"),MOStringHelper.translateToLocal("gui.redstone_mode.low"),MOStringHelper.translateToLocal("gui.redstone_mode.disabled")});
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
        int y = sizeY - 45;
        if (machineGui.getMachine().hasOwner())
        {
            String info = MOStringHelper.translateToLocal("gui.config.owner") + ": " + machineGui.getMachine().getOwner();
            int width = getFontRenderer().getStringWidth(info);
            getFontRenderer().drawString(info, sizeX - width - 20, y, Reference.COLOR_GUI_DARK.getColor());
            y -= 15;
        }
        if (machineGui.getMachine() instanceof IMatterNetworkConnection)
        {
            BlockPosition position = ((IMatterNetworkConnection) machineGui.getMachine()).getPosition();
            String info = String.format("%s : [%s,%s,%s]",MOStringHelper.translateToLocal("gui.config.mnet.address"),position.x,position.y,position.z);
            int width = getFontRenderer().getStringWidth(info);
            getFontRenderer().drawString(info, sizeX - width - 20, y, Reference.COLOR_GUI_DARK.getColor());
            y -= 15;
        }
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if (buttonName == "RedstoneMode")
        {
            //machineGui.getMachine().setRedstoneMode((byte)mouseButton);
            MatterOverdrive.packetPipeline.sendToServer(new PacketChangeRedstoneMode(machineGui.getMachine(), (byte) mouseButton));
        }
    }
}
