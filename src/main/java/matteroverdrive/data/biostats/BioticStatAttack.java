package matteroverdrive.data.biostats;

import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by Simeon on 6/30/2015.
 */
public class BioticStatAttack extends AbstractBioticStat
{
    UUID modiferID;

    public BioticStatAttack(String name, int xp) {
        super(name, xp);
        setMaxLevel(4);
        modiferID = UUID.fromString("caf3f2ba-75f5-4f2f-84b9-ddfab1fcef25");
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        AttributeModifier instance = androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.attackDamage).getModifier(modiferID);
        if (instance == null)
        {
            if (enabled) {
                AttributeModifier modifier = new MOAttributeModifier(modiferID, "Android Attack Damage", 5 * getAttackPower(level), 2).setSaved(false);
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(modifier);
            }
        }
        else if (instance instanceof MOAttributeModifier)
        {
            if (enabled) {
                ((MOAttributeModifier) instance).setAmount(5 * getAttackPower(level));
            }else
            {
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.attackDamage).removeModifier(instance);
            }
        }
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return false;
    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.GREEN + DecimalFormat.getPercentInstance().format(getAttackPower(level)) + EnumChatFormatting.GRAY);
    }

    public float getAttackPower(int level)
    {
        return (level + 1) * 0.05f;
    }
}
