package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.render.RenderHelper;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.HoloIcon;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 3/20/2015.
 */
public class ElementSlot extends MOElementBase
{
    protected IIcon icon;
    protected String type = "small";
    protected int iconOffsetX;
    protected int iconOffsetY;

    protected String info = "";

    public ElementSlot(GuiBase gui, int posX, int posY,int width,int height,String type,IIcon icon)
    {
        super(gui, posX, posY,width,height);
        iconOffsetX = ((sizeX - 16) / 2);
        iconOffsetY = ((sizeY - 16) / 2);
        this.type = type;
        this.icon = icon;
    }

    public ElementSlot(GuiBase gui, int posX, int posY,int width,int height,String type)
    {
        this(gui,posX,posY,width,height,type,null);
    }

    public void addTooltip(List<String> list)
    {
        if (!info.isEmpty())
        {
            list.add(StringHelper.localize(info));
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        ApplyColor();
        gui.bindTexture(getTexture(type));
        gui.drawSizedTexturedModalRect(this.posX, this.posY, 0, 0, sizeX, sizeY, sizeX, sizeY);
        drawSlotIcon(icon,posX + iconOffsetX,posY + iconOffsetY);
        ResetColor();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {

    }

    public void drawSlotIcon(IIcon icon,int x,int y)
    {
        if(icon != null)
        {
            GL11.glEnable(GL11.GL_BLEND);
            ApplyColor();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            ClientProxy.holoIcons.bindSheet();
            RenderHelper.renderIcon(x,y,0,icon,16,16);
            GL11.glDisable(GL11.GL_BLEND);
            ResetColor();
        }
    }

    public void setItemOffset(int x,int y)
    {
        this.iconOffsetX = x;
        this.iconOffsetY = y;
    }

    public static ResourceLocation getTexture(String type)
    {
        return new ResourceLocation(Reference.PATH_ELEMENTS + "slot_"+type+".png");
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public IIcon getIcon() {
        return icon;
    }

    public void setIcon(IIcon icon) {
        this.icon = icon;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
