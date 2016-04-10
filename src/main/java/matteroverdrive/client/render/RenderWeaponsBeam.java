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

package matteroverdrive.client.render;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.tileentity.TileEntityRendererStation;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.items.weapon.OmniTool;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_ONE;

/**
 * Created by Simeon on 6/13/2015.
 */
@SideOnly(Side.CLIENT)
public class RenderWeaponsBeam extends RenderBeam<EntityPlayer>
{
	public static final ResourceLocation beamTexture = new ResourceLocation(Reference.PATH_FX + "plasmabeam.png");
	final Map<Entity, WeaponSound> soundMap = new HashMap<>();

	public void onRenderWorldLast(RenderHandler renderHandler, RenderWorldLastEvent event)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, 0, 0);

		GlStateManager.pushMatrix();
		GlStateManager.translate(-Minecraft.getMinecraft().thePlayer.posX, -Minecraft.getMinecraft().thePlayer.posY, -Minecraft.getMinecraft().thePlayer.posZ);
		renderClient(renderHandler, event.getPartialTicks());
		renderOthers(renderHandler, event.getPartialTicks());
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableCull();
	}

	@SuppressWarnings("unchecked")
	public void renderOthers(RenderHandler renderHandler, float ticks)
	{
		Minecraft.getMinecraft().theWorld.getLoadedEntityList().stream()
				.filter(o -> o instanceof EntityPlayer)
				.filter(player -> !player.equals(Minecraft.getMinecraft().thePlayer))
				.forEach(o -> {
					EntityPlayer player = (EntityPlayer)o;
					if (shouldRenderBeam(player))
					{
						renderRaycastedBeam(player.getPositionEyes(ticks).addVector(0, player.getEyeHeight(), 0), player.getLook(0), new Vec3d(-0.5, -0.3, 1), player);
					}
					else
					{
						stopWeaponSound(player);
					}
				});
	}

	public void renderClient(RenderHandler renderHandler, float ticks)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		if (shouldRenderBeam(player))
		{
			Vec3d pos = player.getPositionEyes(1);
			Vec3d look = player.getLook(0);
			renderRaycastedBeam(pos, look, new Vec3d(-0.1, -0.1, 0.15), player);
		}
		else
		{
			stopWeaponSound(player);
		}
	}

	@SideOnly(Side.CLIENT)
	private void playWeaponSound(EntityPlayer player, Random random)
	{
		if (!soundMap.containsKey(player))
		{
			ItemStack weaponStack = player.getActiveItemStack();
			if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
			{
				//WeaponSound sound = new WeaponSound(new ResourceLocation(((IWeapon)weaponStack.getItem()).getFireSound(weaponStack, player)), (float)player.posX, (float)player.posY, (float)player.posZ, random.nextFloat() * 0.05f + 0.2f, 1);
				WeaponSound sound = ((IWeapon)weaponStack.getItem()).getFireSound(weaponStack, player);
				soundMap.put(player, sound);
				Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			}

		}
		else if (soundMap.get(player).isDonePlaying())
		{
			stopWeaponSound(player);
			playWeaponSound(player, random);
		}
		else
		{
			soundMap.get(player).setPosition((float)player.posX, (float)player.posY, (float)player.posZ);
		}
	}

	private void stopWeaponSound(EntityPlayer entity)
	{
		if (soundMap.containsKey(entity))
		{
			WeaponSound sound = soundMap.get(entity);
			sound.stopPlaying();
			Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
			soundMap.remove(entity);

		}
	}

	@Override
	protected boolean shouldRenderBeam(EntityPlayer entity)
	{
		return entity.isHandActive() &&
				(entity.getActiveItemStack().getItem() instanceof Phaser ||
						entity.getActiveItemStack().getItem() instanceof OmniTool);
	}

	@Override
	protected void onBeamRaycastHit(RayTraceResult hit, EntityPlayer caster)
	{
		ItemStack weaponStack = caster.getActiveItemStack();
		if (weaponStack != null && weaponStack.getItem() instanceof EnergyWeapon)
		{
			((EnergyWeapon)weaponStack.getItem()).onProjectileHit(hit, weaponStack, caster.worldObj, 1);
			if (weaponStack.getItem() instanceof OmniTool && hit.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				GlStateManager.pushMatrix();
				RenderUtils.applyColorWithMultipy(getBeamColor(caster), 0.5f + (float)(1 + Math.sin(caster.worldObj.getWorldTime() * 0.5f)) * 0.5f);
				Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityRendererStation.glowTexture);
				GlStateManager.translate(hit.getBlockPos().getX() + 0.5, hit.getBlockPos().getY() + 0.5, hit.getBlockPos().getZ() + 0.5);
				GlStateManager.translate(hit.sideHit.getDirectionVec().getX() * 0.5, hit.sideHit.getDirectionVec().getY() * 0.5, hit.sideHit.getDirectionVec().getZ() * 0.5);
				if (hit.sideHit == EnumFacing.SOUTH)
				{
					GlStateManager.rotate(90, 1, 0, 0);

				}
				else if (hit.sideHit == EnumFacing.NORTH)
				{
					GlStateManager.rotate(90, -1, 0, 0);
				}
				else if (hit.sideHit == EnumFacing.EAST)
				{
					GlStateManager.rotate(90, 0, 0, -1);
				}
				else if (hit.sideHit == EnumFacing.WEST)
				{
					GlStateManager.rotate(90, 0, 0, 1);
				}
				else if (hit.sideHit == EnumFacing.DOWN)
				{
					GlStateManager.rotate(180, 1, 0, 0);
				}
				GlStateManager.scale(1, 1.5 + Math.sin(caster.worldObj.getWorldTime() * 0.5) * 0.5, 1);
				GlStateManager.popMatrix();
			}
		}
	}

	@Override
	protected void onBeamRender(EntityPlayer caster)
	{
		playWeaponSound(caster, random);
	}

	@Override
	protected Color getBeamColor(EntityPlayer caster)
	{
		return new Color(WeaponHelper.getColor(caster.getActiveItemStack()));
	}

	@Override
	protected ResourceLocation getBeamTexture(EntityPlayer caster)
	{
		ItemStack weaponStack = caster.getActiveItemStack();
		if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
		{
			if (weaponStack.getItem() instanceof Phaser)
			{
				return beamTexture;

			}
			else if (weaponStack.getItem() instanceof OmniTool)
			{
				return beamTexture;
			}
		}
		return null;
	}

	@Override
	protected float getBeamMaxDistance(EntityPlayer caster)
	{
		int range = Phaser.RANGE;
		ItemStack weaponStack = caster.getActiveItemStack();
		if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
		{
			range = ((IWeapon)weaponStack.getItem()).getRange(weaponStack);
		}
		return range;
	}

	@Override
	protected float getBeamThickness(EntityPlayer caster)
	{
		ItemStack weaponStack = caster.getActiveItemStack();
		if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
		{
			if (weaponStack.getItem() instanceof Phaser)
			{
				return 0.03f;
			}
			else if (weaponStack.getItem() instanceof OmniTool)
			{
				return 0.07f;
			}
		}
		return 0.05f;
	}
}
