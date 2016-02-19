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

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.GuiElementList;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.events.ITextHandler;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class ElementBaseGroup extends MOElementBase implements IButtonHandler, GuiElementList, ITextHandler
{
    protected final ArrayList<MOElementBase> elements = new ArrayList<>();

    public ElementBaseGroup(MOGuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }
    public ElementBaseGroup(MOGuiBase gui, int posX, int posY,int width,int height)
    {
        super(gui, posX, posY, width, height);
    }

    @Override
    public void init()
    {
        elements.clear();
    }

    protected MOElementBase getElementAtPosition(int mX, int mY)
    {
        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase element = getElements().get(i);
            if (mY >= 0 && mY <= sizeY && mX >= 0 && mX <= sizeX && element.intersectsWith(mX, mY) && element.isVisible())
            {
                return element;
            }
        }
        return null;
    }

    public void addTooltip(List<String> var1,int mouseX,int mouseY)
    {
        mouseX -= posX;
        mouseY -= posY;

        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase element = getElements().get(i);
            if (mouseY >= 0 && mouseY <= sizeY && mouseX >= 0 && mouseX <= sizeX && element.intersectsWith(mouseX, mouseY) && element.isVisible())
            {
                element.addTooltip(var1,mouseX,mouseY);
            }
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        mouseX -= posX;
        mouseY -= posY;

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.posX, this.posY, 0);
        GlStateManager.color(1,1,1);
        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase c = getElements().get(i);

            if(c.isVisible())
                c.drawBackground(mouseX,mouseY,gameTicks);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        mouseX -= posX;
        mouseY -= posY;

        GlStateManager.pushMatrix();
        GL11.glTranslatef(this.posX, this.posY, 0);
        GL11.glColor3f(1, 1, 1);
        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase c = getElements().get(i);
            if(c.isVisible())
                c.drawForeground(mouseX, mouseY);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void update(int mouseX, int mouseY,float partialTicks)
    {
        mouseX -= posX;
        mouseY -= posY;

        for (int i = elements.size(); i-- > 0;)
        {
            getElements().get(i).update(mouseX, mouseY,partialTicks);
        }

        update();
    }

    @Override
    public void updateInfo()
    {
        for (int i = elements.size(); i-- > 0;)
        {
            MOElementBase element = elements.get(i);
            element.updateInfo();
        }

    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        mouseX -= posX;
        mouseY -= posY;

        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase c = getElements().get(i);
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

        mouseX -= posX;
        mouseY -= posY;

        for (MOElementBase child : elements)
        {
            if (!child.isVisible() || !child.isEnabled()) {
                continue;
            }
            child.onMouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        mouseX -= posX;
        mouseY -= posY;

        for (int i = getElements().size(); i-- > 0;)
        {
            MOElementBase c = getElements().get(i);
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
            MOElementBase c = getElements().get(i);
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

    @Override
    public void handleElementButtonClick(MOElementBase element,String buttonName, int mouseButton)
    {

    }

    public List<MOElementBase> getElements()
    {
        return elements;
    }

    public void clearElements()
    {
        this.elements.clear();
    }

    public MOElementBase addElementAt(int i,MOElementBase element)
    {
        element.parent = this;
        elements.add(i,element);
        return element;
    }

    public MOElementBase addElement(MOElementBase element)
    {
        if (element == null)
            return null;

        element.parent = this;
        elements.add(element);
        return element;
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {

    }
}
