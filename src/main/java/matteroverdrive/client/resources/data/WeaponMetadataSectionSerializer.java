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

import com.google.gson.*;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Vec3;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Simeon on 12/9/2015.
 */
public class WeaponMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer<WeaponMetadataSection>
{
    @Override
    public String getSectionName() {
        return "weapon";
    }

    @Override
    public WeaponMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        WeaponMetadataSection weaponMetadataSection = new WeaponMetadataSection();
        JsonObject jsonobject = JsonUtils.getJsonObject(json, "metadata section");
        try
        {
            JsonObject modules = jsonobject.getAsJsonObject("modules");
            if (modules != null)
            {
                for (Map.Entry<String, JsonElement> element : modules.entrySet())
                {
                    weaponMetadataSection.setModulePosition(element.getKey(), fromJson(element.getValue().getAsJsonObject()));
                }
            }
        }catch (ClassCastException classcastexception)
        {
            throw new JsonParseException("Invalid weapon->scope_position: expected array, was " + jsonobject.get("scope_position"), classcastexception);
        }
        return weaponMetadataSection;
    }

    @Override
    public JsonElement serialize(WeaponMetadataSection section, Type type, JsonSerializationContext context)
    {
        JsonObject jsonobject = new JsonObject();
        JsonObject modules = new JsonObject();
        for (Map.Entry<String,Vec3> position : section.getModulePositions().entrySet())
        {

            modules.add(position.getKey(),toObject(position.getValue()));
        }
        jsonobject.add("modules",modules);
        return jsonobject;
    }

    public JsonObject toObject(Vec3 vec3)
    {
        JsonObject object = new JsonObject();
        object.add("x",new JsonPrimitive(vec3.xCoord));
        object.add("y",new JsonPrimitive(vec3.yCoord));
        object.add("z",new JsonPrimitive(vec3.zCoord));
        return object;
    }

    public Vec3 fromJson(JsonObject object)
    {
        return new Vec3(object.get("x").getAsDouble(),object.get("y").getAsDouble(),object.get("z").getAsDouble());
    }
}
