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
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOElementButton extends MOElementBase
{
    public static final ScaleTexture NORMAL_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE_DARK = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over_dark.png"),18,18).setOffsets(7,7,7,7);
    public static final Random rand = new Random();

    private int hoverX,hoverY,sheetX,sheetY,disabledX,disabledY;
    protected String[] sounds = new String[]{"button_soft"};
    protected String text;
    protected boolean isDown;
    protected int lastMouseButton;
    protected Color color;
    int labelColor = 0xFFFFFFFF;
    IButtonHandler buttonHandler;
    HoloIcon icon;
    private String tooltip;

    public MOElementButton(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture) {
        this(gui,handler,posX,posY,name,sheetX,sheetY,hoverX,hoverY,0,0,sizeX,sizeY,texture);
    }

    public MOElementButton(MOGuiBase gui,IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture) {
        super(gui, posX, posY,sizeX, sizeY);
        this.buttonHandler = handler;
        this.name = name;
        this.buttonHandler = handler;
        this.sheetX = sheetX;
        this.sheetY = sheetY;
        this.hoverX = hoverX;
        this.hoverY = hoverY;
        this.disabledX = disabledX;
        this.disabledY = disabledY;
        this.setTexture(texture,this.texH,this.texW);
    }

    @Override
    public boolean onMousePressed(int x, int y, int mouseButton) {

        if (isEnabled())
        {
            isDown = true;
            lastMouseButton = mouseButton;
            return true;
        }
        return false;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY)
    {
        if (isEnabled() && intersectsWith(mouseX, mouseY) && isDown)
        {
            String sound = getSound();
            if (sound != null && !sound.isEmpty()) {
                gui.playSound(Reference.MOD_ID + ":gui." + sound, getSoundVolume(), 0.9f + rand.nextFloat() * 0.2f);
            }
            onAction(mouseX, mouseY, lastMouseButton);
        }

        isDown = false;
    }

    public String getSound()
    {
        if (sounds != null && sounds.length > 0)
        {
            return sounds[rand.nextInt(sounds.length)];
        }
        return null;
    }

    public void setSounds(String... sounds)
    {
        this.sounds = sounds;
    }

    public float getSoundVolume()
    {
        return 0.5f;
    }

    public void onAction(int mouseX, int mouseY,int mouseButton)
    {
        buttonHandler.handleElementButtonClick(this,getName(), lastMouseButton);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        if (color != null)
            RenderUtils.applyColor(color);
        GL11.glEnable(GL11.GL_BLEND);
        RenderUtils.bindTexture(this.texture);
        if(this.isEnabled()) {
            if(this.intersectsWith(mouseX, mouseY)) {
                this.drawTexturedModalRect(this.posX, this.posY, this.hoverX, this.hoverY, this.sizeX, this.sizeY);
            } else {
                this.drawTexturedModalRect(this.posX, this.posY, this.sheetX, this.sheetY, this.sizeX, this.sizeY);
            }
        } else {
            this.drawTexturedModalRect(this.posX, this.posY, this.disabledX, this.disabledY, this.sizeX, this.sizeY);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void drawForeground(int i, int i1)
    {

    }

    @Override
    public void addTooltip(List<String> var1,int mouseX,int mouseY)
    {
        if (this.tooltip != null)
        {
            var1.add(this.tooltip);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color){this.color = color;}

    @Override
    public void updateInfo()
    {

    }

    @Override
    public void init()
    {

    }

    public void setColor(Color color,float multiplay){this.color = new Color((int)(color.getIntR() * multiplay),(int)(color.getIntG() * multiplay),(int)(color.getIntB() * multiplay));}

    public Color getColor(){return this.color;}

    public void setTextColor(int color){this.labelColor = color;}
    public int getTextColor(){return this.labelColor;}
    public void setIcon(HoloIcon icon){this.icon = icon;}
    public HoloIcon getIcon(){return this.icon;}
    public void setToolTip(String tooltip){this.tooltip = tooltip;}
}
