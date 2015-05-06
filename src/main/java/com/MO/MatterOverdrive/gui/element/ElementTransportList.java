package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.api.transport.ITransportList;
import com.MO.MatterOverdrive.data.TransportLocation;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.gui.events.IListHandler;
import com.MO.MatterOverdrive.tile.TileEntityMachineTransporter;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Simeon on 5/5/2015.
 */
public class ElementTransportList extends MOElementListBox
{
    TileEntityMachineTransporter transporter;

    public ElementTransportList(MOGuiBase containerScreen,IListHandler listHandler, int x, int y, int width, int height,TileEntityMachineTransporter transporter)
    {
        super(containerScreen,listHandler, x, y, width, height);
        this.transporter = transporter;
    }

    @Override
    public void DrawElement(int i,int x,int y,int selectedLineColor,int selectedTextColor, boolean selected,boolean BG)
    {
        if (BG)
        {
            if (selected)
                MOElementButton.NORMAL_TEXTURE.Render(x,y,getElementWidth(i),getElementHeight(i));
            else
                MOElementButton.HOVER_TEXTURE_DARK.Render(x,y,getElementWidth(i),getElementHeight(i));
        }
        else
        {
            TransportLocation position = transporter.getPositions().get(i);
            String info = "[ X: " + (position.x + transporter.xCoord) + ", Y: " + (position.y + transporter.yCoord) + ", Z: " + (position.z + transporter.zCoord) + " ]";
            gui.drawCenteredString(getFontRenderer(),position.name,x + getElementWidth(i)/2,y + getElementHeight(i) / 2 - 4,selectedTextColor);
            //gui.drawCenteredString(getFontRenderer(), EnumChatFormatting.YELLOW + info,x + getElementWidth(i)/2,y + getElementHeight(i) / 2 + 2,0xFFFFFF);
        }
    }

    @Override
    public void drawElementTooltip(int index,int mouseX,int mouseY)
    {

    }

    @Override
    public int getElementHeight(int id)
    {
        return 20;
    }

    @Override
    public int getElementWidth(int id)
    {
        return  sizeX - 4;
    }

    @Override
    protected boolean shouldBeDisplayed(IMOListBoxElement element)
    {
        return true;
    }

    @Override
    public IMOListBoxElement getElement(int index) {

        return null;
    }

    public int getElementCount() {

        return transporter.getPositions().size();
    }
}
