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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by Simeon on 6/30/2015.
 */
public class BioticStatAttack extends AbstractBioticStat
{
	private final UUID modifierID = UUID.fromString("caf3f2ba-75f5-4f2f-84b9-ddfab1fcef25");

	public BioticStatAttack(String name, int xp)
	{
		super(name, xp);
		setMaxLevel(4);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{

	}

	@Override
	public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
	{

	}

	@Override
	public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
	{

	}

	@Override
	public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
	{

	}

	@Override
	public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
	{

	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new MOAttributeModifier(modifierID, "Android Attack Damage", getAttackPower(level), 1).setSaved(false));
		return multimap;
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level)
	{
		return false;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level)
	{
		return 0;
	}

	@Override
	public String getDetails(int level)
	{
		return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN + DecimalFormat.getPercentInstance().format(getAttackPower(level)) + TextFormatting.GRAY);
	}

	private float getAttackPower(int level)
	{
		return (level + 1) * 0.05f;
	}
}
