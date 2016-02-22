package matteroverdrive.data.biostats;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 2/2/2016.
 */
public class BioticStatWirelessCharger extends AbstractBioticStat
{
    public static final int CHARGE_SPEED = 32;

    public BioticStatWirelessCharger(String name, int xp)
    {
        super(name, xp);
    }

    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.YELLOW.toString() + CHARGE_SPEED + EnumChatFormatting.GRAY);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (!android.getPlayer().getEntityWorld().isRemote && isActive(android,level))
        {
            for (int i = 0; i < 9; i++)
            {
                ItemStack itemStack = android.getPlayer().inventory.getStackInSlot(i);
                if (android.getPlayer().inventory.currentItem != i && itemStack != null && itemStack.getItem() instanceof IEnergyContainerItem)
                {
                    int requestedEnergy = ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, CHARGE_SPEED, true);
                    if (android.extractEnergy(requestedEnergy, true) == requestedEnergy)
                    {
                        ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, requestedEnergy, false);
                        android.extractEnergy(requestedEnergy, false);
                    }

                }
            }
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
    {
        if (server && this.equals(androidPlayer.getActiveStat()))
        {
            androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_WIRELESS_CHARGING,!androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_WIRELESS_CHARGING));
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
        }
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level)
    {
        return isActive(android,level);
    }

    @Override
    public boolean showOnWheel(AndroidPlayer androidPlayer, int level)
    {
        return androidPlayer.getPlayer().isSneaking();
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level)
    {
        return super.isEnabled(android,level) && android.getEnergyStored() > CHARGE_SPEED;
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
    public Multimap attributes(AndroidPlayer androidPlayer, int level)
    {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_WIRELESS_CHARGING);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        return 0;
    }
}
