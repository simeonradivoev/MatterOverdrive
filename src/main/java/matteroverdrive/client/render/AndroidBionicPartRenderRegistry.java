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

package matteroverdrive.client.render;

import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 9/10/2015.
 */
public class AndroidBionicPartRenderRegistry implements IBionicPartRenderRegistry
{
    Map<Class<? extends IBionicPart>,IBionicPartRenderer> rendererMap;

    public AndroidBionicPartRenderRegistry()
    {
        rendererMap = new HashMap<>();
    }

    @Override
    public void register(Class<? extends IBionicPart> partClass,IBionicPartRenderer renderer)
    {
        rendererMap.put(partClass,renderer);
    }

    @Override
    public IBionicPartRenderer removeRenderer(Class<? extends IBionicPart> partClass) {
        return rendererMap.remove(partClass);
    }

    @Override
    public IBionicPartRenderer getRenderer(Class<? extends IBionicPart> partClass)
    {
        return rendererMap.get(partClass);
    }
}
