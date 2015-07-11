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

import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.UUID;

/**
 * Created by Simeon on 5/30/2015.
 */
public class BioticStatSpeed extends AbstractBioticStat
{
    UUID modiferID;

    public BioticStatSpeed(String name, int xp) {
        super(name, xp);
        setMaxLevel(4);
        modiferID = UUID.fromString("d13345c8-14f7-48fd-bc52-c787c9857a6c");
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {

    }

    public float getSpeedModify(int level)
    {
        return level * 0.1f;
    }

    public String getDetails(int level)
    {
        return String.format(super.getDetails(level),EnumChatFormatting.GREEN + Integer.toString(Math.round(getSpeedModify(level) * 100f)) + "%" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        AttributeModifier instance = androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(modiferID);
        if (instance == null)
        {
            if (enabled) {
                AttributeModifier modifier = new MOAttributeModifier(modiferID, "Android Speed", getSpeedModify(level), 2).setSaved(false);
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(modifier);
            }
        }
        else if (instance instanceof MOAttributeModifier)
        {
            if (enabled) {
                ((MOAttributeModifier) instance).setAmount(getSpeedModify(level));
            }else
            {
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(instance);
            }
        }
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android,level) && android.getEnergyStored() > 0;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return false;
    }
}
