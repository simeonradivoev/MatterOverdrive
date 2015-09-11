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

package matteroverdrive.guide;

import matteroverdrive.gui.MOGuiBase;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by Simeon on 8/28/2015.
 */
public interface IGuideElement
{
    void setGUI(MOGuiBase gui);
    void loadElement(MOGuideEntry entry,Element element,Map<String,String> styleSheetMap,int width,int height);
    void drawElement(int width,int mouseX,int mouseY);
    int getHeight();
    int getWidth();
    int getFloating();
}
