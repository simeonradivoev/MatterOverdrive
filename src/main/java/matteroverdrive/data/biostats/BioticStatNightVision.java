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

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 7/11/2015.
 */
public class BioticStatNightVision extends AbstractBioticStat implements IConfigSubscriber
{
    public static int ENERGY_PER_TICK = 16;

    public BioticStatNightVision(String name, int xp) {
        super(name, xp);
        setShowOnWheel(true);
        setShowOnHud(true);
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
            if (isActive(android, level) && !MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this,level,android)))
            {
                android.extractEnergy(ENERGY_PER_TICK,false);
            }
        }else
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this,level,android)))
            {
                manageNightvision(android, level);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void manageNightvision(AndroidPlayer android, int level)
    {
        if (isActive(android, level))
        {
            android.getPlayer().addPotionEffect(new PotionEffect(Potion.nightVision.id, 500));
        }
    }

    public void setActive(AndroidPlayer androidPlayer,int level,boolean active)
    {
        androidPlayer.getPlayer().addPotionEffect(new PotionEffect(Potion.nightVision.id, 500));
        androidPlayer.getEffects().setBoolean("Nightvision", active);
        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, KeyBinding keyBinding)
    {
        if (androidPlayer.getActiveStat() != null && androidPlayer.getActiveStat().equals(this))
        {
            androidPlayer.setActionToServer(PacketSendAndroidAnction.ACTION_NIGHTVISION, !androidPlayer.getEffects().getBoolean("Nightvision"));
            if (!androidPlayer.getEffects().getBoolean("Nightvision"))
            {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":night_vision"), 0.05f + androidPlayer.getPlayer().getRNG().nextFloat() * 0.1f, 0.95f + androidPlayer.getPlayer().getRNG().nextFloat() * 0.1f));
            }
            else
            {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":power_down"), 0.05f + androidPlayer.getPlayer().getRNG().nextFloat() * 0.1f, 0.95f + androidPlayer.getPlayer().getRNG().nextFloat() * 0.1f));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        if (!isEnabled(androidPlayer,level) && isActive(androidPlayer,level))
        {
            androidPlayer.setActionToServer(PacketSendAndroidAnction.ACTION_NIGHTVISION, false);
        }
    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getEffects().getBoolean("Nightvision");
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        return 0;
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer,int level)
    {
        return super.isEnabled(androidPlayer,level) && androidPlayer.extractEnergy(ENERGY_PER_TICK,true) >= ENERGY_PER_TICK;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        ENERGY_PER_TICK = config.getInt("nighvision_energy_per_tick", ConfigurationHandler.CATEGORY_ABILITIES, 16, "The energy cost of the Nightvision");
    }
}
