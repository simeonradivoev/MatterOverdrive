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

package matteroverdrive.data.biostats;

import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 7/10/2015.
 */
public class BioticStatCloak extends AbstractBioticStat
{
    public static final int ENERGY_PER_TICK = 128;

    public BioticStatCloak(String name, int xp) {
        super(name, xp);
        setShowOnHud(true);
        setShowOnWheel(true);
    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.YELLOW.toString() + ENERGY_PER_TICK + MOEnergyHelper.ENERGY_UNIT);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (!android.getPlayer().worldObj.isRemote)
        {
            if (isActive(android, level)) {
                android.getPlayer().setInvisible(true);
                android.extractEnergy(ENERGY_PER_TICK,false);
            } else {
                android.getPlayer().setInvisible(false);
            }
        }
    }

    public void setActive(AndroidPlayer android, int level,boolean active)
    {
        android.getPlayer().setInvisible(active);
        android.getEffects().setBoolean("Cloaked", active);
        android.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {
        if (androidPlayer.getActiveStat() != null && androidPlayer.getActiveStat().equals(this) && keycode == ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode())
        {
            if (down)
            {
                androidPlayer.setActionToServer(PacketSendAndroidAnction.ACTION_CLOAK,!androidPlayer.getEffects().getBoolean("Cloaked"));
            }
        }
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        if (!isEnabled(androidPlayer,level) && isActive(androidPlayer,level))
        {
            androidPlayer.setActionToServer(PacketSendAndroidAnction.ACTION_CLOAK, false);
        }
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return androidPlayer.getEffects().getBoolean("Cloaked");
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer,int level)
    {
        return super.isEnabled(androidPlayer,level) && androidPlayer.extractEnergy(ENERGY_PER_TICK,true) >= ENERGY_PER_TICK;
    }
}
