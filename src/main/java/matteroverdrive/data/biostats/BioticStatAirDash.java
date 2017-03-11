package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/10/2016.
 */
public class BioticStatAirDash extends AbstractBioticStat
{
	@SideOnly(Side.CLIENT)
	private int lastClickTime;
	@SideOnly(Side.CLIENT)
	private int clickCount;
	@SideOnly(Side.CLIENT)
	private boolean hasNotReleased;
	@SideOnly(Side.CLIENT)
	private boolean hasDashed;

	public BioticStatAirDash(String name, int xp)
	{
		super(name, xp);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{
		if (android.getPlayer().worldObj.isRemote)
		{
			manageDashing(android);
		}
	}

	@SideOnly(Side.CLIENT)
	private void manageDashing(AndroidPlayer android)
	{
		EntityPlayerSP playerSP = (EntityPlayerSP)android.getPlayer();

		if (!playerSP.onGround)
		{
			if (!hasDashed)
			{
				if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown())
				{
					if (!hasNotReleased)
					{
						hasNotReleased = true;
						if (lastClickTime > 0)
						{
							clickCount++;
							MOLog.info("clickCount: %s", lastClickTime);
						}
						lastClickTime = 5;
					}
				}
				else
				{
					hasNotReleased = false;
				}

				if (clickCount >= 1)
				{
					clickCount = 0;
					dash(playerSP);
					hasDashed = true;
				}

				if (lastClickTime > 0)
				{
					lastClickTime--;
				}
			}
		}
		else
		{
			hasNotReleased = false;
			hasDashed = false;
			clickCount = 0;
			lastClickTime = 0;
		}
	}

	@SideOnly(Side.CLIENT)
	private void dash(EntityPlayerSP playerSP)
	{
		Vec3d look = playerSP.getLookVec().addVector(0, 0.75, 0).normalize();
		playerSP.addVelocity(look.xCoord, look.yCoord, look.zCoord);
		for (int i = 0; i < 30; i++)
		{
			playerSP.worldObj.spawnParticle(EnumParticleTypes.CLOUD, playerSP.posX + playerSP.getRNG().nextGaussian() * 0.5, playerSP.posY + playerSP.getRNG().nextFloat() * playerSP.getEyeHeight(), playerSP.posZ + playerSP.getRNG().nextGaussian() * 0.5, -look.xCoord, -look.zCoord, -look.zCoord);
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
	public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level)
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
