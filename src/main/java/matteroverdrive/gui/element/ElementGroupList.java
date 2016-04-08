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

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Simeon on 6/20/2015.
 */
public class ElementGroupList extends ElementBaseGroup {

    private int selectedIndex;
    private int scroll;
    private int smoothScroll;
    private float smoothScrollMultiply = 0.1f;
    private int padding = 6;
    private IListHandler listHandler;

    public ElementGroupList(MOGuiBase gui, IListHandler listHandler, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY, width, height);
        this.listHandler = listHandler;
    }

    @Override
    public void update(int mouseX, int mouseY,float partialTicks)
    {
        super.update(mouseX,mouseY,partialTicks);
        int heightCount = 0;

        for (MOElementBase element : elements)
        {
            element.setPosition(0, heightCount + smoothScroll);
            heightCount += element.getHeight() + padding;
            if (smoothScroll + heightCount >= 0 && heightCount + smoothScroll - element.getHeight() < sizeY)
            {
                element.setVisible(true);
            } else
            {
                element.setVisible(false);
            }
        }

        smoothScroll = (int)MOMathHelper.Lerp(smoothScroll,scroll,smoothScrollMultiply);
        selectedIndex = MathHelper.clamp_int(selectedIndex,0,elements.size()-1);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawBackground(mouseX,mouseY,gameTicks);
        RenderUtils.endStencil();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawForeground(mouseX, mouseY);
        RenderUtils.endStencil();
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMousePressed(mouseX, mouseY, mouseButton))
            {
                setSelectedIndex(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        boolean didScroll = super.onMouseWheel(mouseX,mouseY,movement);
        scroll += movement * 0.2;
        limitScroll();
        return didScroll;
    }

    public void limitScroll()
    {
        int elementsHeight = -(getElementsHeight() - sizeY + 32);
        scroll = Math.min(0,scroll);
        scroll = Math.max(elementsHeight,scroll);
    }

    public int getElementsHeight()
    {
        int height = 0;
        for (MOElementBase element : elements)
        {
            height += element.getHeight() + padding;
        }
        return height;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        int newSelectedIndex = MathHelper.clamp_int(selectedIndex,0,elements.size()-1);
        if (newSelectedIndex != this.selectedIndex)
        {
            this.selectedIndex = newSelectedIndex;
            listHandler.ListSelectionChange(getName(),selectedIndex);
        }else
        {
            this.selectedIndex = newSelectedIndex;
        }
    }

    public boolean isSelected(MOElementBase elementBase)
    {
        return selectedIndex < elements.size() && elements.get(selectedIndex).equals(elementBase);
    }
    public void setScroll(int scroll){this.scroll = scroll;}
    public void resetSmoothScroll(){this.smoothScroll = scroll;}
    public int getScroll(){return this.scroll;}
}
