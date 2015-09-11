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
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class ElementGuideEntry extends MOElementBase
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "quide_element_bg.png");
    private MOGuideEntry entry;
    private IButtonHandler buttonHandler;
    private boolean showLabel;

    public ElementGuideEntry(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY,MOGuideEntry entry)
    {
        super(gui, posX, posY);
        this.entry = entry;
        this.setSize(22,22);
        this.buttonHandler = buttonHandler;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        list.add(entry.getDisplayName());
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1, 1, 1);
        gui.bindTexture(BG);
        gui.drawSizedTexturedModalRect(this.posX, this.posY, 0, 0, 22, 22, 22, 22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        int iconIndex = (int)((Minecraft.getMinecraft().theWorld.getWorldTime()/20) % entry.getStackIcons().length);
        RenderUtils.renderStack(this.posX+3,this.posY+3,entry.getStackIcons()[iconIndex]);

        if (showLabel) {
            getFontRenderer().setUnicodeFlag(true);
            getFontRenderer().drawString(entry.getDisplayName(), this.posX + sizeX + 4, this.posY + this.sizeY / 2 - 5, Reference.COLOR_MATTER.getColor());
            getFontRenderer().setUnicodeFlag(false);
        }
    }

    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        buttonHandler.handleElementButtonClick(this, name, mouseButton);
        return true;
    }

    public MOGuideEntry getEntry()
    {
        return entry;
    }

    public void setShowLabel(boolean showLabel)
    {
        this.showLabel = showLabel;
    }
}
