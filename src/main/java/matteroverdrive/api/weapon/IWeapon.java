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

package matteroverdrive.api.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Simeon on 4/13/2015.
 */
public interface IWeapon
{
    Vector2f getSlotPosition(int slot,ItemStack weapon);
    Vector2f getModuleScreenPosition(int slot,ItemStack weapon);
    boolean supportsModule(int slot,ItemStack weapon);
    boolean onLeftClick(ItemStack weapon,EntityPlayer entityPlayer);
    boolean onServerFire(ItemStack weapon, EntityPlayer entityPlayer, boolean zoomed,int seed,int latency,Vec3 position,Vec3 dir);
    boolean isAlwaysEquipped(ItemStack weapon);
    float getHeat(ItemStack weapon);
    float getMaxHeat(ItemStack itemStack);
    int getAmmo(ItemStack weapon);
    int getMaxAmmo(ItemStack weapon);
    int getShootCooldown();
}
