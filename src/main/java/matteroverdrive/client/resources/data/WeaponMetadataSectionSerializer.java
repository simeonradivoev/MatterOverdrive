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
        JsonObject jsonobject = JsonUtils.getJsonElementAsJsonObject(json, "metadata section");
        Vec3 scopePosition = null;
        try
        {
            JsonArray array = jsonobject.getAsJsonArray("scope_position");
            if (array.size() >= 3)
            {
                scopePosition = Vec3.createVectorHelper(array.get(0).getAsDouble(),array.get(1).getAsDouble(),array.get(2).getAsDouble());
            }
        }catch (ClassCastException classcastexception)
        {
            throw new JsonParseException("Invalid weapon->scope_position: expected array, was " + jsonobject.get("scope_position"), classcastexception);
        }
        return new WeaponMetadataSection(scopePosition);
    }

    @Override
    public JsonElement serialize(WeaponMetadataSection section, Type type, JsonSerializationContext context)
    {
        JsonObject jsonobject = new JsonObject();
        JsonArray scopePosition = new JsonArray();
        scopePosition.add(new JsonPrimitive(section.getScopePosition().xCoord));
        scopePosition.add(new JsonPrimitive(section.getScopePosition().yCoord));
        scopePosition.add(new JsonPrimitive(section.getScopePosition().zCoord));
        jsonobject.add("scope_position",scopePosition);
        return jsonobject;
    }
}
