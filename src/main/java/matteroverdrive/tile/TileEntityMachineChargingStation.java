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

import cofh.repack.codechicken.lib.vec.BlockCoord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityMachineChargingStation extends MOTileEntityMachineEnergy implements IMultiBlockTileEntity {

    public static final int ENERGY_CAPACITY = 512000;
    public static final int ENERGY_TRANSFER = 1024;
    public static int RANGE = 16;

    public TileEntityMachineChargingStation()
    {
        super(3);
        energyStorage.setCapacity(ENERGY_CAPACITY);
        energyStorage.setMaxExtract(ENERGY_TRANSFER);
        energyStorage.setMaxReceive(ENERGY_TRANSFER);
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
            AxisAlignedBB radius = AxisAlignedBB.getBoundingBox(xCoord - RANGE, yCoord - RANGE, zCoord - RANGE, xCoord + RANGE, yCoord + RANGE, zCoord + RANGE);
            List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, radius);
            for (EntityPlayer player : players) {
                if (AndroidPlayer.get(player).isAndroid()) {
                    int energy = getReqiredEnergy(player);
                    int availableEnergy = Math.min(energy,extractEnergy(ForgeDirection.UNKNOWN, ENERGY_TRANSFER, true));
                    extractEnergy(ForgeDirection.UNKNOWN, AndroidPlayer.get(player).receiveEnergy(availableEnergy, false), false);
                }
            }
        }
    }

    private int getReqiredEnergy(EntityPlayer player)
    {
        return (int)(ENERGY_TRANSFER * (1.0D - MathHelper.clamp_double((Vec3.createVectorHelper(player.posX,player.posY,player.posZ).subtract(Vec3.createVectorHelper(xCoord,yCoord,zCoord)).lengthVector() / (double)RANGE),0,1)));
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
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onActiveChange() {

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

	@Override
	public List<BlockCoord> getBoundingBlocks() {
		List<BlockCoord> coords = new ArrayList<>();

		coords.add(new BlockCoord(xCoord, yCoord + 1, zCoord));
		coords.add(new BlockCoord(xCoord, yCoord + 2, zCoord));

		return coords;
	}
}
