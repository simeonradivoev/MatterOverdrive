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
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 5/30/2015.
 */
public class BioticStatHighJump extends AbstractBioticStat implements IConfigSubscriber {

    private static int ENERGY_PER_JUMP = 1024;

    public BioticStatHighJump(String name, int xp)
    {
        super(name, xp);
        setMaxLevel(2);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.YELLOW.toString() + ENERGY_PER_JUMP + " RF" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
    {
        if (server && androidPlayer.getActiveStat() == this)
        {
            androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_HIGH_JUMP,!androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_HIGH_JUMP));
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if (event instanceof LivingEvent.LivingJumpEvent && isActive(androidPlayer,level))
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, level, androidPlayer)))
            {
                if (!androidPlayer.getPlayer().isSneaking())
                {
                    if (!event.entity.worldObj.isRemote)
                        androidPlayer.extractEnergyScaled(ENERGY_PER_JUMP * level);

                    Vec3 motion = new Vec3(event.entityLiving.motionX, event.entityLiving.motionY, event.entityLiving.motionZ);
                    motion = motion.normalize().addVector(0, 1, 0).normalize();
                    event.entityLiving.addVelocity(motion.xCoord * 0.25 * level, motion.yCoord * 0.25 * level, motion.zCoord * 0.25 * level);
                }
            }
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level)
    {
        return super.isEnabled(android,level) && android.hasEnoughEnergyScaled(ENERGY_PER_JUMP) && android.getPlayer().onGround;
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level)
    {
        return isActive(android,level);
    }

    @Override
    public boolean showOnWheel(AndroidPlayer androidPlayer, int level)
    {
        return androidPlayer.getPlayer().isSneaking();
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_HIGH_JUMP);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        return 0;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        ENERGY_PER_JUMP = config.getInt("high_jump_energy", ConfigurationHandler.CATEGORY_ABILITIES, 1024, "The energy cost of each High Jump");
    }
}
