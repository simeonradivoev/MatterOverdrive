package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 2/9/2016.
 */
public class BioticStatInertialDampers extends AbstractBioticStat
{
	public BioticStatInertialDampers(String name, int xp)
	{
		super(name, xp);
		setMaxLevel(2);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{

	}

	public String getDetails(int level)
	{
		return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN + DecimalFormat.getPercentInstance().format(level * 0.5f) + TextFormatting.GRAY);
	}

	@Override
	public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
	{

	}

	@Override
	public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
	{

	}

	@Override
	public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
	{
		if (event instanceof LivingFallEvent)
		{
			((LivingFallEvent)event).setDamageMultiplier(((LivingFallEvent)event).getDamageMultiplier() * Math.max(0, 1 - level * 0.5f));
			if ((int)((LivingFallEvent)event).getDistance() > 4)
			{
				androidPlayer.extractEnergyScaled((int)(((LivingFallEvent)event).getDistance() * level * 0.5f));
			}
		}
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
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level)
	{
		return false;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level)
	{
		return 0;
	}

	@Override
	public boolean showOnHud(AndroidPlayer android, int level)
	{
		return isEnabled(android, level) && android.getPlayer().fallDistance > 0;
	}
}
