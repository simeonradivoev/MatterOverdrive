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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityMachineChargingStation extends MOTileEntityMachineEnergy {

    public static final int ENERGY_CAPACITY = 512000;
    public static final int ENERGY_TRANSFER = 1024;

    public TileEntityMachineChargingStation()
    {
        super(3);
        energyStorage.setCapacity(ENERGY_CAPACITY);
        energyStorage.setMaxExtract(ENERGY_TRANSFER);
        energyStorage.setMaxReceive(ENERGY_TRANSFER);
        redstoneMode = Reference.MODE_REDSTONE_LOW;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        manageAndroidCharging();
    }

    private void manageAndroidCharging()
    {
        if (!worldObj.isRemote) {
            AxisAlignedBB radius = AxisAlignedBB.getBoundingBox(xCoord - 8, yCoord - 8, zCoord - 8, xCoord + 8, yCoord + 8, zCoord + 8);
            List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, radius);
            for (EntityPlayer player : players) {
                if (AndroidPlayer.get(player).isAndroid()) {
                    int avalibleEnergy = extractEnergy(ForgeDirection.UNKNOWN, ENERGY_TRANSFER, true);
                    extractEnergy(ForgeDirection.UNKNOWN, AndroidPlayer.get(player).receiveEnergy(avalibleEnergy, false), false);
                }
            }
        }
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    protected void onAwake(Side side) {

    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 8192.0D;
    }
}
