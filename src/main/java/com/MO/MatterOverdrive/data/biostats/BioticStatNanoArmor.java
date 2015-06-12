package com.MO.MatterOverdrive.data.biostats;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatNanoArmor extends AbstractBioticStat
{
    public final ResourceLocation nanoArmorIcon = new ResourceLocation(Reference.PATH_GUI_ITEM + "biotic_stat_nano_armor.png");

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
        return super.getDetails(level).replace("$0", EnumChatFormatting.GREEN + Integer.toString(Math.round((getDamageNegate(level)) * 100)) + "%" + EnumChatFormatting.GRAY);
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
        return 1 + level * 0.1f;
    }

    @Override
    public ResourceLocation getIcon(int level) {
        return nanoArmorIcon;
    }
}
