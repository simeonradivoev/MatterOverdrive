/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 3/20/2015.
 */
public class ElementSlot extends MOElementBase
{
    protected HoloIcon icon;
    protected String type = "small";
    protected int iconOffsetX;
    protected int iconOffsetY;

    protected String info = "";

    public ElementSlot(MOGuiBase gui, int posX, int posY, int width, int height, String type, HoloIcon icon)
    {
        super(gui, posX, posY,width,height);
        iconOffsetX = ((sizeX - 16) / 2);
        iconOffsetY = ((sizeY - 16) / 2);
        this.type = type;
        this.icon = icon;
    }

    public ElementSlot(MOGuiBase gui, int posX, int posY,int width,int height,String type)
    {
        this(gui,posX,posY,width,height,type,null);
    }

    public void addTooltip(List<String> list)
    {
        if (!info.isEmpty())
        {
            list.add(MOStringHelper.translateToLocal(info));
        }
    }

    @Override
    public void updateInfo()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public void addTooltip(List<String> var1, int mouseX, int mouseY)
    {

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

    public void drawSlotIcon(HoloIcon icon,int x,int y)
    {
        if(icon != null && canDrawIcon(icon))
        {
            GL11.glEnable(GL11.GL_BLEND);
            ApplyColor();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            ClientProxy.holoIcons.bindSheet();
            ClientProxy.holoIcons.renderIcon(icon,x,y);
            GL11.glDisable(GL11.GL_BLEND);
            ResetColor();
        }
    }

    protected boolean canDrawIcon(HoloIcon icon)
    {
        return true;
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

    public HoloIcon getIcon() {
        return icon;
    }

    public void setIcon(HoloIcon icon) {
        this.icon = icon;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
