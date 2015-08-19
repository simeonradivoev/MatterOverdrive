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

package matteroverdrive.api.android;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;

/**
 * Created by Simeon on 5/27/2015.
 */
public interface IBionicStat
{
    String getUnlocalizedName();
    String getDisplayName(AndroidPlayer androidPlayer,int level);
    boolean canBeUnlocked(AndroidPlayer android,int level);
    void onAndroidUpdate(AndroidPlayer android, int level);
    @SideOnly(Side.CLIENT)
    void onActionKeyPress(AndroidPlayer androidPlayer, int level, KeyBinding keyBinding);
    @SideOnly(Side.CLIENT)
    void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down);
    void onLivingEvent(AndroidPlayer androidPlayer,int level,LivingEvent event);
    void onUnlock(AndroidPlayer android,int level);
    void onTooltip(AndroidPlayer android, int level, List<String> list, int mouseX, int mouseY);
    void changeAndroidStats(AndroidPlayer androidPlayer,int level,boolean enabled);
    IBionicStat getRoot();
    List<ItemStack> getRequiredItems();
    boolean isEnabled(AndroidPlayer android, int level);
    boolean isActive(AndroidPlayer androidPlayer,int level);
    boolean showOnHud(AndroidPlayer android,int level);
    boolean showOnWheel(AndroidPlayer androidPlayer,int level);
    void registerIcons(TextureMap holoIcons);
    IIcon getIcon(int level);
    int maxLevel();
}
