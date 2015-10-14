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

package matteroverdrive.api.renderer;

import matteroverdrive.api.inventory.IBionicPart;

/**
 * Created by Simeon on 10/13/2015.
 * This is the registry for all bionic part renderers.
 * The main implementation is {@link matteroverdrive.client.render.AndroidBionicPartRenderRegistry}.
 */
public interface IBionicPartRenderRegistry
{
    /**
     * Registers a bionic part renderer.
     * @param partClass the bionic part class/type.
     * @param renderer the bionic part renderer instance.
     */
    void register(Class<? extends IBionicPart> partClass,IBionicPartRenderer renderer);

    /**
     * Removes and returns the renderer connected with that type of bionic part class/type.
     * @param partClass the class/type of the bionic part.
     * @return the bionic part's renderer. Returns Null if there is no renderer.
     */
    IBionicPartRenderer removeRenderer(Class<? extends IBionicPart> partClass);
    IBionicPartRenderer getRenderer(Class<? extends IBionicPart> partClass);
}
