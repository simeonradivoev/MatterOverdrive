package com.MO.MatterOverdrive.tile;

import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.world.EnumSkyBlock;
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
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote) {
            manageExtract();
            manageChagrgeAmount();
        }
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
                    energyStorage.setEnergyStored(energy);
                }
            }
        }
    }

    @Override
    public boolean isActive()
    {
        if (!worldObj.provider.hasNoSky)
        {
            int i1 = worldObj.getSavedLightValue(EnumSkyBlock.Sky, xCoord, yCoord, zCoord) - worldObj.skylightSubtracted;

            if(i1 >= 15)
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
                int energyToTransfer = Math.min(energy,getChargeAmount());
                if (energyToTransfer > 0)
                {
                    energy -= EnergyHelper.insertEnergyIntoAdjacentEnergyReceiver(this, i, MAX_ENERGY_EXTRACT, false);
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
                    f = worldObj.getCelestialAngleRadians(1.0F);

                    if (f < (float)Math.PI)
                    {
                        f += (0.0F - f) * 0.2F;
                    }
                    else
                    {
                        f += (((float)Math.PI * 2F) - f) * 0.2F;
                    }

                    f = (float)Math.cos(f);
                }

                chargeAmount = (byte)Math.round(CHARGE_AMOUNT * f);
            }else
            {
                chargeAmount = 0;
            }
        }
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
    public boolean canConnectToNetwork(ForgeDirection direction) {
        return false;
    }
}
