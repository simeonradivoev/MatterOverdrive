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

package matteroverdrive.tile;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityMachineChargingStation extends MOTileEntityMachineEnergy implements IMultiBlockTileEntity
{

	public static final int ENERGY_CAPACITY = 512000;
	public static final int ENERGY_TRANSFER = 512;
	private static final UpgradeHandler upgradeHandler = new UpgradeHandler();
	public static int BASE_MAX_RANGE = 8;

	public TileEntityMachineChargingStation()
	{
		super(2);
		energyStorage.setCapacity(ENERGY_CAPACITY);
		energyStorage.setOutputRate(ENERGY_TRANSFER);
		energyStorage.setInputRate(ENERGY_TRANSFER);
		playerSlotsHotbar = true;
		playerSlotsMain = true;
	}

	@Override
	public void update()
	{
		super.update();
		manageAndroidCharging();
	}

	private void manageAndroidCharging()
	{
		if (!worldObj.isRemote && getEnergyStored(EnumFacing.DOWN) > 0)
		{
			int range = getRage();
			AxisAlignedBB radius = new AxisAlignedBB(getPos().add(-range, -range, -range), getPos().add(range, range, range));
			List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, radius);
			for (EntityPlayer player : players)
			{
				if (MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid())
				{
					int required = getRequiredEnergy(player, range);
					int max = Math.min(getEnergyStored(EnumFacing.DOWN), getMaxCharging());
					int toExtract = Math.min(required, max);
					extractEnergy(EnumFacing.DOWN, MOPlayerCapabilityProvider.GetAndroidCapability(player).receiveEnergy(toExtract, false), false);
				}
			}
		}
	}

	public int getRage()
	{
		return (int)(BASE_MAX_RANGE * getUpgradeMultiply(UpgradeTypes.Range));
	}

	public int getMaxCharging()
	{
		return (int)(ENERGY_TRANSFER / getUpgradeMultiply(UpgradeTypes.PowerUsage));
	}

	private int getRequiredEnergy(EntityPlayer player, int maxRange)
	{
		return (int)(ENERGY_TRANSFER * (1.0D - MathHelper.clamp_double((new Vec3d(player.posX, player.posY, player.posZ).subtract(new Vec3d(getPos())).lengthVector() / (double)maxRange), 0, 1)));
	}

	@Override
	public SoundEvent getSound()
	{
		return null;
	}

	@Override
	public boolean hasSound()
	{
		return false;
	}

	@Override
	public boolean getServerActive()
	{
		return false;
	}

	@Override
	public float soundVolume()
	{
		return 0;
	}

	@Override
	protected void onMachineEvent(MachineEvent event)
	{

	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type)
	{
		return type.equals(UpgradeTypes.Range) || type.equals(UpgradeTypes.PowerStorage) || type.equals(UpgradeTypes.PowerUsage);
	}

	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 8192.0D;
	}

	@Override
	public List<BlockPos> getBoundingBlocks()
	{
		List<BlockPos> coords = new ArrayList<>();

		coords.add(getPos().add(0, 1, 0));
		coords.add(getPos().add(0, 2, 0));

		return coords;
	}

	public IUpgradeHandler getUpgradeHandler()
	{
		return upgradeHandler;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[0];
	}

	public static class UpgradeHandler implements IUpgradeHandler
	{

		@Override
		public double affectUpgrade(UpgradeTypes type, double multiply)
		{
			if (type.equals(UpgradeTypes.Range))
			{
				return Math.min(8, multiply);
			}
			return multiply;
		}
	}
}
