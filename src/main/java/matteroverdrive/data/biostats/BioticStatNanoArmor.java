package matteroverdrive.data.biostats;

import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatNanoArmor extends AbstractBioticStat
{
    public BioticStatNanoArmor(String name, int xp)
    {
        super(name, xp);
        setMaxLevel(4);
        setShowOnHud(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.GREEN + DecimalFormat.getPercentInstance().format(getDamageNegate(level)) + EnumChatFormatting.GRAY);
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if (event instanceof LivingHurtEvent)
        {
            ((LivingHurtEvent) event).ammount *= getDamageNegate(level);
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android,level) && android.getEnergyStored() > 0;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return isEnabled(androidPlayer,level);
    }

    public float getDamageNegate(int level)
    {
        return (1 + level) * 0.06f;
    }
}
