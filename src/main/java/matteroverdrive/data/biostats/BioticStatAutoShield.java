package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveBioticStats;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 2/1/2016.
 */
public class BioticStatAutoShield extends AbstractBioticStat
{
	public BioticStatAutoShield(String name, int xp)
	{
		super(name, xp);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{

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
		if (event instanceof LivingAttackEvent)
		{
			DamageSource source = ((LivingAttackEvent)event).getSource();
			if (!MatterOverdriveBioticStats.shield.getShieldState(androidPlayer))
			{
				if (MatterOverdriveBioticStats.shield.isDamageValid(source) && event.isCancelable() && MatterOverdriveBioticStats.shield.canActivate(androidPlayer))
				{
					MatterOverdriveBioticStats.shield.setShield(androidPlayer, true);
				}
			}
		}
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
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
