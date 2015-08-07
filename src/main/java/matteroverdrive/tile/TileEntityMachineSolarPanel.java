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

import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/9/2015.
 */
public class TileEntityMachineSolarPanel extends MOTileEntityMachineEnergy
{
    public static final int CHARGE_AMOUNT = 16;
    public static final int ENERGY_STORAGE = 64000;
    public static final int MAX_ENERGY_EXTRACT = 512;

    private byte chargeAmount;

    public TileEntityMachineSolarPanel()
    {
        super(2);
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxExtract(MAX_ENERGY_EXTRACT);
        energyStorage.setMaxReceive(0);
        redstoneMode = Reference.MODE_REDSTONE_LOW;
    }

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            manageExtract();
            manageChagrgeAmount();
        }

        super.updateEntity();
    }

    @Override
    protected void registerComponents()
    {

    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    protected void manageCharging()
    {
        if (!worldObj.isRemote)
        {
            if (isActive())
            {
                if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored())
                {
                    int energy = energyStorage.getEnergyStored();
                    energy = MathHelper.clampI(energy + getChargeAmount(),0,energyStorage.getMaxEnergyStored());
                    if (energy != energyStorage.getEnergyStored())
                    {
                        UpdateClientPower();
                    }
                    energyStorage.setEnergyStored(energy);
                }
            }
        }
    }

    @Override
    public boolean getServerActive()
    {
        if (!worldObj.provider.hasNoSky)
        {
            int i1 = worldObj.getSavedLightValue(EnumSkyBlock.Sky, xCoord, yCoord, zCoord) - worldObj.skylightSubtracted;
            float time = getTime();
            if(i1 >= 15 && time > 0.5)
            {
                return true;
            }
        }
        return false;
    }

    public void manageExtract()
    {
        int energy = energyStorage.getEnergyStored();

        if (energy > 0)
        {
            for (int i = 0; i < 6; i++)
            {
                int energyToTransfer = Math.min(energy,MAX_ENERGY_EXTRACT);
                if (energyToTransfer > 0)
                {
                    energy -= EnergyHelper.insertEnergyIntoAdjacentEnergyReceiver(this, i, energyToTransfer, false);
                }
            }

            energyStorage.setEnergyStored(energy);
        }
    }

    public void manageChagrgeAmount()
    {
        if (!worldObj.isRemote)
        {
            if (!worldObj.provider.hasNoSky)
            {
                float f = 0;
                int i1 = worldObj.getSavedLightValue(EnumSkyBlock.Sky, xCoord, yCoord, zCoord) - worldObj.skylightSubtracted;

                if(i1 >= 15)
                {
                    f = getTime();
                }

                chargeAmount = (byte)Math.round(CHARGE_AMOUNT * f);
            }else
            {
                chargeAmount = 0;
            }
        }
    }

    public float getTime()
    {
        float f = worldObj.getCelestialAngleRadians(1.0F);

        if (f < (float)Math.PI)
        {
            f += (0.0F - f) * 0.2F;
        }
        else
        {
            f += (((float)Math.PI * 2F) - f) * 0.2F;
        }

        return (float)Math.cos(f);
    }

    public byte getChargeAmount()
    {
        return chargeAmount;
    }

    public void setChargeAmount(byte chargeAmount)
    {
        this.chargeAmount = chargeAmount;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return from == ForgeDirection.DOWN;
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
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerStorage;
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
}
