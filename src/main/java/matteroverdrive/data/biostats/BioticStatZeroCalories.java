package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 2/2/2016.
 */
public class BioticStatZeroCalories extends AbstractBioticStat
{
	private final static int ENERGY_FOOD_MULTIPLY = 256;

	public BioticStatZeroCalories(String name, int xp)
	{
		super(name, xp);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{
		if (android.getPlayer().getFoodStats().needFood() && isEnabled(android, level))
		{
			int foodNeeded = 20 - android.getPlayer().getFoodStats().getFoodLevel();
			int extractedEnergy = android.extractEnergyRaw(foodNeeded * ENERGY_FOOD_MULTIPLY, false);
			android.getPlayer().getFoodStats().addStats(extractedEnergy / ENERGY_FOOD_MULTIPLY, 0);
		}
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		return super.isEnabled(android, level) && android.getEnergyStored() > ENERGY_FOOD_MULTIPLY;
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
