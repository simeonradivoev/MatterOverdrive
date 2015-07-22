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

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.GuiElementList;
import matteroverdrive.gui.MOGuiBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class ElementBaseGroup extends MOElementBase implements IButtonHandler, GuiElementList
{
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();

    public ElementBaseGroup(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }
    public ElementBaseGroup(GuiBase gui, int posX, int posY,int width,int height)
    {
        super(gui, posX, posY, width, height);
    }

    @Override
    public void init()
    {
        elements.clear();
    }

    protected ElementBase getElementAtPosition(int mX, int mY)
    {
        mX -= this.getGlobalX();
        mY -= this.getGlobalY();

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase element = getElements().get(i);
            if (mY >= 0 && mY <= sizeY && mX >= 0 && mX <= sizeX && element.intersectsWith(mX, mY) && element.isVisible())
            {
                return element;
            }
        }
        return null;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        handleTooltips(mouseX,mouseY);

        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        GL11.glPushMatrix();
        GL11.glTranslatef(this.posX, this.posY, 0);
        GL11.glColor3f(1,1,1);
        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);

            if(c.isVisible())
                c.drawBackground(mouseX,mouseY,gameTicks);
        }
        GL11.glPopMatrix();
    }

    protected void handleTooltips(int mouseX,int mouseY)
    {
        if (gui.mc.thePlayer.inventory.getItemStack() == null) {
            ElementBase element = getElementAtPosition(mouseX, mouseY);
            if (element != null) {
                element.addTooltip(((MOGuiBase) gui).getTooltips());
            }
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        GL11.glPushMatrix();
        GL11.glTranslatef(this.posX, this.posY, 0);
        GL11.glColor3f(1, 1, 1);
        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if(c.isVisible())
                c.drawForeground(mouseX, mouseY);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        for (int i = elements.size(); i-- > 0;)
        {
            getElements().get(i).update(mouseX, mouseY);
        }

        update();
    }

    @Override
    public void updateInfo()
    {
        for (int i = elements.size(); i-- > 0;)
        {
            ElementBase element = elements.get(i);
            if(element instanceof MOElementBase)
            {
                ((MOElementBase) element).updateInfo();
            }
        }

    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMousePressed(mouseX, mouseY, mouseButton))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled()) {
                continue;
            }
            c.onMouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMouseWheel(mouseX, mouseY, movement)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyTyped(char characterTyped, int keyPressed) {

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled()) {
                continue;
            }
            if (c.onKeyTyped(characterTyped, keyPressed)) {
                return true;
            }
        }
        return false;
    }

    public MOElementBase setGroupVisible(boolean visible)
    {
        super.setVisible(visible);
        return this;
    }

    public void handleElementButtonClick(String buttonName, int mouseButton)
    {

    }

    public List<ElementBase> getElements()
    {
        return elements;
    }

    public ElementBase addElementAt(int i,ElementBase element)
    {
        if(element instanceof MOElementBase)
            ((MOElementBase) element).parent = this;

        elements.add(i,element);
        return element;
    }

    public ElementBase addElement(ElementBase element)
    {
        if(element instanceof MOElementBase)
            ((MOElementBase) element).parent = this;

        elements.add(element);
        return element;
    }
}
