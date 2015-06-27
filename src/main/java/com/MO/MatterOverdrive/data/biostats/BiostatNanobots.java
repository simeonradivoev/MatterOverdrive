package com.MO.MatterOverdrive.data.biostats;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BiostatNanobots extends AbstractBioticStat
{
    public final static float REGEN_AMOUNT_PER_TICK = 0.03f;
    public final static int ENERGY_PER_REGEN = 32;

    public BiostatNanobots(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (android.getPlayer().worldObj.getWorldTime() % 20 == 0)
        {
            if (android.getPlayer().getHealth() > 0 && !android.getPlayer().isDead && android.getPlayer().getHealth() < android.getPlayer().getMaxHealth() && android.extractEnergy(ENERGY_PER_REGEN, true) == ENERGY_PER_REGEN) {
                android.getPlayer().heal(REGEN_AMOUNT_PER_TICK * 20);
                android.extractEnergy(ENERGY_PER_REGEN * 20, false);
            }
        }
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
        return super.isEnabled(android,level) && android.getEnergyStored() > 0;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return isEnabled(androidPlayer,level);
    }
}
