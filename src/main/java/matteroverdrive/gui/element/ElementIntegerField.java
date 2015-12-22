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

import cofh.lib.gui.element.ElementBase;
import matteroverdrive.Reference;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 5/3/2015.
 */
public class ElementIntegerField extends ElementBaseGroup implements IButtonHandler
{
    IButtonHandler buttonHandler;
    MOElementButtonScaled incBtn;
    MOElementButtonScaled decBtn;
    ScaleTexture numberBG;
    int number;
    int min;
    int max;
    private String label;
    private int labelColor = 0xffffff;

    public ElementIntegerField(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY, int height,int min,int max)
    {
        this(gui,buttonHandler,posX,posY,32+ Minecraft.getMinecraft().fontRenderer.getStringWidth(Integer.toString(max))+10,height,min,max);
    }

    public ElementIntegerField(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY, int width, int height,int min,int max) {
        super(gui, posX, posY, width, height);
        this.buttonHandler = buttonHandler;

        numberBG = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "field_over.png"), 30, 18).setOffsets(5, 5, 5, 5);

        incBtn = new MOElementButtonScaled(gui,this,0,0,"Inc",16,height);
        incBtn.setNormalTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal_left.png"),10,18).setOffsets(5,2,5,5));
        incBtn.setOverTexture(null);
        incBtn.setText("+");

        decBtn = new MOElementButtonScaled(gui,this,width - 16,0,"Dec",16,height);
        decBtn.setNormalTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal_right.png"), 10, 18).setOffsets(2, 5, 5, 5));
        decBtn.setOverTexture(null);
        decBtn.setText("-");
        this.min = min;
        this.max = max;
    }

    public ElementIntegerField(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY, int width, int height)
    {
        this(gui,buttonHandler, posX, posY, width, height, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void init()
    {
        super.init();

        addElement(incBtn);
        addElement(decBtn);
    }

    public ElementIntegerField(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY) {
        this(gui,buttonHandler, posX, posY, 120, 18, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int getNumber()
    {
        return MathHelper.clamp_int(number, min, max);
    }

    public void setNumber(int number)
    {
        this.number = MathHelper.clamp_int(number,min,max);
    }

    @Override
    public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
    {
        if (buttonName == "Inc")
        {
            int value = 1;
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                value = 64;
            else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                value = 16;

            setNumber(getNumber()+value);
            buttonHandler.handleElementButtonClick(this,getName(),value);
        }
        else if (buttonName == "Dec")
        {
            int value = -1;
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                value = -64;
            else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                value = -16;

            setNumber(getNumber()+value);
            buttonHandler.handleElementButtonClick(this,getName(),value);
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        super.drawBackground(mouseX,mouseY,gameTicks);
        numberBG.render(posX + 16,posY,sizeX - 32,sizeY);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);
        String number = Integer.toString(this.number);
        int numberWidth = getFontRenderer().getStringWidth(number);
        getFontRenderer().drawString(number,posX - numberWidth/2 + sizeX/2,posY - getFontRenderer().FONT_HEIGHT/2 + sizeY/2,Reference.COLOR_GUI_DARKER.getColor());
        if (label != null)
        {
            getFontRenderer().drawString(label,posX + sizeX + 2,posY - getFontRenderer().FONT_HEIGHT/2 + sizeY/2,labelColor);
        }
    }

    public void setBounds(int min,int max)
    {
        this.min = min;
        this.max = max;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setLabelColor(int labelColor)
    {
        this.labelColor = labelColor;
    }
}
