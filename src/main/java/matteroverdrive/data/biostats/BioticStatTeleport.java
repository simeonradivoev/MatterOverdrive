/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.server.PacketTeleportPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatTeleport extends AbstractBioticStat implements IConfigSubscriber
{

	public static final int TELEPORT_DELAY = 40;
	public static int ENERGY_PER_TELEPORT = 4096;
	private static int MAX_TELEPORT_HEIGHT_CHECK = 8;
	private static int MAX_TELEPORT_DISTANCE = 32;
	private final HashSet<String> blackListedBlocks;
	@SideOnly(Side.CLIENT)
	private boolean hasPressedKey;

	public BioticStatTeleport(String name, int xp)
	{
		super(name, xp);
		setShowOnHud(true);
		setShowOnWheel(true);
		blackListedBlocks = new HashSet<>();
	}

	@Override
	public String getDetails(int level)
	{
		String keyName = TextFormatting.AQUA + GameSettings.getKeyDisplayString(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode()) + TextFormatting.GRAY;
		return MOStringHelper.translateToLocal(getUnlocalizedDetails(), keyName, TextFormatting.YELLOW.toString() + ENERGY_PER_TELEPORT + " RF" + TextFormatting.GRAY);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level)
	{
		if (android.getPlayer().worldObj.isRemote)
		{
			manageActivate(android);
		}
	}

	@SideOnly(Side.CLIENT)
	private void manageActivate(AndroidPlayer androidPlayer)
	{
		if (ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).isKeyDown() && this.equals(androidPlayer.getActiveStat()))
		{
			hasPressedKey = true;
		}
		else if (hasPressedKey)
		{
			Vec3d pos = getPos(androidPlayer);
			if (pos != null && !MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, androidPlayer.getUnlockedLevel(this), androidPlayer)))
			{
				MatterOverdrive.packetPipeline.sendToServer(new PacketTeleportPlayer(pos.xCoord, pos.yCoord, pos.zCoord));
				hasPressedKey = false;
			}
			hasPressedKey = false;
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

	public Vec3d getPos(AndroidPlayer androidPlayer)
	{
		RayTraceResult position = MOPhysicsHelper.rayTraceForBlocks(androidPlayer.getPlayer(), androidPlayer.getPlayer().worldObj, MAX_TELEPORT_DISTANCE, 0, new Vec3d(0, androidPlayer.getPlayer().getEyeHeight(), 0), true, true);
		if (position != null && position.typeOfHit != RayTraceResult.Type.MISS && position.getBlockPos() != null)
		{
			BlockPos pos = getTopSafeBlock(androidPlayer.getPlayer().worldObj, position.getBlockPos(), position.sideHit);
			if (pos != null)
			{
				return new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			}
			return null;
		}

		position = MOPhysicsHelper.rayTrace(androidPlayer.getPlayer(), androidPlayer.getPlayer().worldObj, 6, 0, new Vec3d(0, androidPlayer.getPlayer().getEyeHeight(), 0), true, true);
		if (position != null)
		{
			return position.hitVec;
		}
		return null;
	}

	private BlockPos getTopSafeBlock(World world, BlockPos pos, EnumFacing side)
	{
		int airBlockCount = 0;
		int heightCheck = MAX_TELEPORT_HEIGHT_CHECK;
		if (side == EnumFacing.UP)
		{
			heightCheck = 3;
		}
		int height = Math.min(pos.getY() + heightCheck, world.getActualHeight());
		IBlockState block;
		for (int i = pos.getY(); i < height; i++)
		{
			BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
			block = world.getBlockState(blockPos);
			String unlocalizedName = block.getBlock().getUnlocalizedName().substring(5);
			if (blackListedBlocks.contains(unlocalizedName))
			{
				return null;
			}

			if (!block.getBlock().isBlockSolid(world, blockPos, EnumFacing.UP) || block instanceof IFluidBlock)
			{
				airBlockCount++;
			}
			else
			{
				airBlockCount = 0;
			}

			if (airBlockCount >= 2)
			{
				return blockPos.offset(EnumFacing.DOWN);
			}
		}

		pos = pos.offset(side);

		IBlockState above = world.getBlockState(pos.offset(EnumFacing.UP));
		IBlockState aboveTwo = world.getBlockState(pos.offset(EnumFacing.UP, 2));
		if (!above.getBlock().isBlockSolid(world, pos.offset(EnumFacing.UP), EnumFacing.UP) && !aboveTwo.getBlock().isBlockSolid(world, pos.offset(EnumFacing.UP, 2), EnumFacing.UP))
		{
			return pos;
		}

		return null;
	}

	@Override
	public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
	{

	}

	@Override
	public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
	{
		if (androidPlayer.getPlayer().worldObj.isRemote)
		{
			if (!isEnabled(androidPlayer, level))
			{
				hasPressedKey = false;
			}
		}
	}

	@Override
	public Multimap attributes(AndroidPlayer androidPlayer, int level)
	{
		return null;
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level)
	{
		long lastTeleport = android.getAndroidEffects().getEffectLong(AndroidPlayer.EFFECT_LAST_TELEPORT);
		long worldTime = android.getPlayer().worldObj.getTotalWorldTime();
		return super.isEnabled(android, level) && lastTeleport <= worldTime && android.hasEnoughEnergyScaled(ENERGY_PER_TELEPORT) && this.equals(android.getActiveStat());
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean getHasPressedKey()
	{
		return hasPressedKey;
	}

	@Override
	public boolean showOnHud(AndroidPlayer android, int level)
	{
		return this.equals(android.getActiveStat());
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level)
	{
		return 0;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		this.blackListedBlocks.clear();
		String[] blackListedBlocks = config.config.getStringList("teleport_blacklist", ConfigurationHandler.CATEGORY_ABILITIES, new String[] {"hellsand", "barrier", "bedrock"}, "The Unlocalized names of the blacklist blocks that the player can't teleport to");
		Collections.addAll(this.blackListedBlocks, blackListedBlocks);
		MAX_TELEPORT_HEIGHT_CHECK = config.getInt("teleport_max_height_check", ConfigurationHandler.CATEGORY_ABILITIES, 8, "The max height amount that the teleport ability checks if there is no 2 blocks air space");
		ENERGY_PER_TELEPORT = config.getInt("teleport_energy_cost", ConfigurationHandler.CATEGORY_ABILITIES, 4096, "The Energy cost of each Teleportation");
		MAX_TELEPORT_DISTANCE = config.getInt("teleport_max_distance", ConfigurationHandler.CATEGORY_ABILITIES, 32, "The maximum distance in blocks, the player can teleport to");
	}
}
