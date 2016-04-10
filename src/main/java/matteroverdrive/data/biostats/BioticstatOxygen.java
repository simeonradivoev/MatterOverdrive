package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 2/27/2016.
 */
public class BioticstatOxygen extends AbstractBioticStat
{
	public BioticstatOxygen(String name, int xp)
	{
		super(name, xp);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{
		if (android.getPlayer().getAir() < 0)
		{
			android.getPlayer().setAir(0);
		}
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
		return false;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level)
	{
		return 0;
	}
}
