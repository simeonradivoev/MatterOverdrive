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
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Simeon on 9/10/2015.
 */
public abstract class BionicPart extends MOBaseItem implements IBionicPart
{
    public BionicPart(String name) {
        super(name);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack, player, infos);
        Multimap<String, AttributeModifier> multimap = getModifiers(AndroidPlayer.get(player), itemstack);
        if (multimap != null)
        {
            multimap.values().stream()
					.forEach(modifier -> {
						switch (modifier.getOperation()) {
							case 0:
								infos.add(String.format("%s: +%s", modifier.getName(), modifier.getAmount()));
								break;
							case 1:
								infos.add(String.format("%s: +%s", modifier.getName(), modifier.getAmount()));
								break;
							default:
								infos.add(String.format("%s: %s", modifier.getName(), DecimalFormat.getPercentInstance().format(modifier.getAmount() + 1)));
						}
					});
        }
    }

    public boolean hasDetails(ItemStack stack){return true;}
}
