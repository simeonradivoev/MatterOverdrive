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
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/13/2015.
 */
public class SidePannel extends ElementBaseGroup
{
    MOElementButton button;
    public ScaleTexture BACKGROUND_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_panel_bg.png"),15,18).setOffsets(7,7,8,9);
    private static boolean isOpen;
    private boolean openable;

    public SidePannel(MOGuiBase gui, int posX, int posY,int height)
    {
        this(gui,posX,posY,height,true);
    }

    public SidePannel(MOGuiBase gui, int posX, int posY,int height,boolean openable) {
        super(gui, posX, posY, 37, height);
        MOElementButtonScaled button = new MOElementButtonScaled(gui, this, 0, 0, "Toggle", 16, height);
        button.setNormalTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_over.png"), 32, 143).setOffsets(0, 0, 42, 100));
        button.setOverTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_over.png"), 32, 143).setOffsets(0, 0, 42, 100));
        button.setSounds("button_expand");
        this.button = button;
        this.openable = openable;

    }

    @Override
    public void init()
    {
        super.init();
        if (button != null)
            elements.add(button);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float ticks)
    {
        if (openable)
        {
            if (isOpen) {
                GL11.glColor3f(1, 1, 1);
                BACKGROUND_TEXTURE.render(posX, posY, 37, sizeY);
                button.setPosition(32, 0);
            } else {
                button.setPosition(0, 0);
            }
        }

        super.drawBackground(mouseX, mouseY, ticks);
    }

    public boolean IsOpen()
    {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
    {
        if (buttonName == "Toggle")
        {
            isOpen = !isOpen;
        }
    }

    @Override
    public void update()
    {
        if (openable)
        {
            if (isOpen) {
                button.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.close_menu"));
                this.setSize(37 + 16, sizeY);
            } else {
                button.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.open_menu"));
                this.setSize(37, sizeY);
            }
        }
    }

    @Override
    public void updateInfo()
    {
        for (int i = 0; i < elements.size(); i++)
        {
            elements.get(i).setVisible(isOpen);
        }

        button.setVisible(openable);
    }

    public void setOpenable(boolean openable)
    {
        this.openable = openable;
    }
}
