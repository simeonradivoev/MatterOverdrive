package com.MO.MatterOverdrive.data.biostats;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 5/30/2015.
 */
public class BioticStatHighJump extends AbstractBioticStat {

    public static final ResourceLocation icon = new ResourceLocation(Reference.PATH_GUI_ITEM + "biotic_stat_high_jump.png");
    public final static int ENERGY_PER_JUMP = 1024;

    public BioticStatHighJump(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public String getDetails(int level)
    {
        return super.getDetails(level).replace("$0", EnumChatFormatting.YELLOW.toString() + ENERGY_PER_JUMP + " RF" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if (event instanceof LivingEvent.LivingJumpEvent)
        {
            if (event.entityLiving.isSneaking())
            {
                if (!event.entity.worldObj.isRemote)
                    androidPlayer.extractEnergy(ENERGY_PER_JUMP, false);

                event.entityLiving.addVelocity(0, 0.5, 0);
            }
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level)
    {
        return super.isEnabled(android,level) && android.extractEnergy(ENERGY_PER_JUMP,true) == ENERGY_PER_JUMP;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return isEnabled(androidPlayer,level);
    }

    @Override
    public ResourceLocation getIcon(int level) {
        return icon;
    }
}
