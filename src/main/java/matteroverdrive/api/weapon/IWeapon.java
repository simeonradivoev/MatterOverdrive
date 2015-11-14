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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.client.sound.WeaponSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Simeon on 4/13/2015.
 * Used by all Energy Weapons.
 */
public interface IWeapon
{
    /**
     * Gets the GUI position of each slot type.
     * This is different to the {@link #getModuleScreenPosition(int, ItemStack)}.
     * As this method only gives the slot position.
     * Not the line that points to the spot that module resides in the weapon model.
     * @param slot the slot type.
     *             <ol>
     *             <li>Battery</li>
     *             <li>Color</li>
     *             <li>Barrel</li>
     *             <li>Sights</li>
     *             <li>Other</li>
     *             </ol>
     * @param weapon the weapon stack.
     * @return the 2d GUI coordinate of the slot.
     */
    Vector2f getSlotPosition(int slot, ItemStack weapon);

    /**
     * Gets the Module position on the screen.
     * This is different than {@link #getSlotPosition(int, ItemStack)}.
     * This methods gives the position the line from the slot points to.
     * This usually shows where the module resides in the weapon model.
     * @param slot the slot type.
     *             @see #getSlotPosition(int, ItemStack)
     * @param weapon the weapon stack.
     * @return the 2d GUI position of the module.
     */
    Vector2f getModuleScreenPosition(int slot, ItemStack weapon);

    /**
     * Checks if the weapon supports a module of a given type
     * @param slot the slot/module type.
     *             @see #getSlotPosition(int, ItemStack)
     * @param weapon the weapon stack.
     * @return is module/slot type supported by the weapon.
     */
    boolean supportsModule(int slot, ItemStack weapon);

    /**
     * Checks if weapon supports a specific module.
     * @param weapon the weapon stack.
     * @param module the module stack.
     * @return is the module stack supported by the weapon.
     */
    boolean supportsModule(ItemStack weapon,ItemStack module);

    /**
     * Called when the player clicks the left mouse button while holding the weapon.
     * If the methods returns true, then all other action made by left clicking will be canceled.
     * This method is only called on the client.
     * @param weapon
     * @param entityPlayer
     * @return
     */
    @SideOnly(Side.CLIENT)
    boolean onLeftClick(ItemStack weapon, EntityPlayer entityPlayer);

    @SideOnly(Side.CLIENT)
    boolean onLeftClickTick(ItemStack weapon, EntityPlayer entityPlayer);

    /**
     * Called when the weapon is fired by the player.
     * This is called after the player fires in his client.
     * After that, a command is send to the server that the player has fired that weapon.
     * That's when this method is called. On the server side.
     * @param weapon the weapon stack being fired.
     * @param entityPlayer the player firing the weapon.
     * @param shot all information about the shot.
     * @param position the position of the fired shot/bullet.
     * @param dir the direction of the weapon/bullet.
     * @return was the fire successful.
     */
    boolean onServerFire(ItemStack weapon, EntityPlayer entityPlayer, WeaponShot shot, Vec3 position, Vec3 dir);

    /**
     * Shows if the gun is always equipped like a bow in third person.
     * @param weapon the weapon stack.
     * @return is the weapon always equipped like a bow in third person.
     */
    boolean isAlwaysEquipped(ItemStack weapon);

    /**
     * Gets the current heat of the weapon.
     * @param weapon the weapon stack.
     * @return the heat of the weapon.
     */
    float getHeat(ItemStack weapon);

    /**
     * Gets the maximum amount of heat the weapons supports.
     * After this maximum is reached the weapon should overheat.
     * @param weapon the weapon stack.
     * @return the heat capacity of the weapon.
     */
    float getMaxHeat(ItemStack weapon);

    /**
     * Gets the current ammo of the weapon.
     * This is a general purpose ammo stat.
     * It could be applied to energy or matter, or anything that acts like ammo.
     * @param weapon the weapon stack.
     * @return the current ammo of the weapon.
     */
    int getAmmo(ItemStack weapon);

    /**
     * Gets the maximum amount of ammo the weapon can hold.
     * This is a genera purpose ammo stat.
     * It could be applied to energy or matter, or anything that acts like ammo.
     * @param weapon the weapon stack.
     * @return the ammo capacity of the weapon.
     */
    int getMaxAmmo(ItemStack weapon);

    /**
     * The Shot cooldown of the weapon.
     * The time is represented in game ticks. 20 in a second.
     * This controls how far apart each shot should be.
     * @return the cooldown time in ticks after each shot.
     */
    int getShootCooldown();

    /**
     * Gets the range of the weapon.
     * @param weapon the weapon stack.
     * @return the range.
     */
    int getRange(ItemStack weapon);

    /**
     * Gets the accuracy of the weapon.
     * @param weapon the weapon stack
     * @param entityPlayer the player holding the weapon.
     * @param zoomed is the weapon zoomed.
     * @return the accuracy of the weapon. Ranges from 0 - infinity
     */
    float getAccuracy(ItemStack weapon, EntityPlayer entityPlayer,boolean zoomed);

    /**
     * Returns if the weapon is currently zoomed.
     * This method works only in the client.
     * @param weapon the weapon stack.
     * @return is the weapon zoomed.
     */
    @SideOnly(Side.CLIENT)
    boolean isWeaponZoomed(ItemStack weapon);

    /**
     * @param weapon the weapon stack.
     * @return the weapon firing sound.
     */
    @SideOnly(Side.CLIENT)
    WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity);
}
