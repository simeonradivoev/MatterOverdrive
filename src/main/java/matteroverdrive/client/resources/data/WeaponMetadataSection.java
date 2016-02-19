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

package matteroverdrive.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 12/9/2015.
 */
public class WeaponMetadataSection implements IMetadataSection
{
    private final Map<String,Vec3> modulePositions;

    public WeaponMetadataSection()
    {
        this.modulePositions = new HashMap<>();
    }

    public Map<String, Vec3> getModulePositions()
    {
        return modulePositions;
    }

    public void setModulePosition(String module,Vec3 pos)
    {
        modulePositions.put(module,pos);
    }

    public Vec3 getModulePosition(String module,Vec3 def)
    {
        Vec3 moduelPos = modulePositions.get(module);
        if (moduelPos != null)
            return moduelPos;
        else
            return def;
    }

    public Vec3 getModulePosition(String module)
    {
        return modulePositions.get(module);
    }
}
