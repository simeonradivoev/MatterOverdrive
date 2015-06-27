package com.MO.MatterOverdrive.data.biostats;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.MOAttributeModifier;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.stats.StatBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.UUID;

/**
 * Created by Simeon on 5/30/2015.
 */
public class BioticStatSpeed extends AbstractBioticStat
{
    UUID modiferID;

    public BioticStatSpeed(String name, int xp) {
        super(name, xp);
        setMaxLevel(4);
        modiferID = UUID.fromString("d13345c8-14f7-48fd-bc52-c787c9857a6c");
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {

    }

    public float getSpeedModify(int level)
    {
        return level * 0.1f;
    }

    public String getDetails(int level)
    {
        return super.getDetails(level).replace("$0", EnumChatFormatting.GREEN + Integer.toString(Math.round(getSpeedModify(level) * 100f)) + "%" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        AttributeModifier instance = androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(modiferID);
        if (instance == null)
        {
            if (enabled) {
                AttributeModifier modifier = new MOAttributeModifier(modiferID, "Android Speed", getSpeedModify(level), 2).setSaved(false);
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(modifier);
            }
        }
        else if (instance instanceof MOAttributeModifier)
        {
            if (enabled) {
                ((MOAttributeModifier) instance).setAmount(getSpeedModify(level));
            }else
            {
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(instance);
            }
        }
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
}
