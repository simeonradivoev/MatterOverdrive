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

package matteroverdrive.items.android;

import com.google.common.collect.Multimap;
import matteroverdrive.Reference;
import matteroverdrive.entity.android_player.AndroidAttributes;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

/**
 * Created by Simeon on 9/8/2015.
 */
public class TritaniumSpine extends BionicPart
{
    public final UUID healthModifierID = UUID.fromString("208b4d4c-50ef-4b45-a097-4bed633cdbff");
    public final UUID glitchModifierID = UUID.fromString("83e92f1b-12af-4302-98b2-422c16a06c89");

    public TritaniumSpine(String name)
    {
        super(name);
    }

    @Override
    public int getType(ItemStack itemStack) {
        return 4;
    }

    @Override
    public boolean affectAndroid(AndroidPlayer player, ItemStack itemStack)
    {
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack)
    {
        Multimap multimap = super.getModifiers(player,itemStack);
        if (multimap.isEmpty())
        {
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), new AttributeModifier(healthModifierID, MOStringHelper.translateToLocal("attribute.name." + SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName()), 2f, 0));
            multimap.put(AndroidAttributes.attributeGlitchTime.getAttributeUnlocalizedName(), new AttributeModifier(glitchModifierID, MOStringHelper.translateToLocal("attribute.name.android.glitchTime"), -0.5f, 2));
        }
        return multimap;
    }

    @Override
    public ResourceLocation getTexture(AndroidPlayer androidPlayer, ItemStack itemStack)
    {
        return new ResourceLocation(Reference.PATH_ARMOR + "tritanium_spine.png");
    }

    @Override
    public ModelBiped getModel(AndroidPlayer androidPlayer, ItemStack itemStack)
    {
        return null;
    }
}
