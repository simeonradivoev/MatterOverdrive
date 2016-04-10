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

package matteroverdrive.client.render.biostat;

import matteroverdrive.Reference;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.data.biostats.BioticStatShield;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.IModel;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.GL_ONE;

/**
 * Created by Simeon on 6/7/2015.
 */
public class BioticStatRendererShield implements IBioticStatRenderer<BioticStatShield>
{
	public static final ResourceLocation model_path = new ResourceLocation(Reference.PATH_MODEL + "shield_sphere.obj");
	private static final ResourceLocation forcefield_damage_tex = new ResourceLocation(Reference.PATH_FX + "shield_damage.png");
	private static final ResourceLocation forcefield_tex = new ResourceLocation(Reference.PATH_FX + "forcefield_plasma.png");
	private static final ResourceLocation forcefield_plasma_tex = new ResourceLocation(Reference.PATH_FX + "forcefield_plasma_2.png");
	private static final ResourceLocation shield_texture = new ResourceLocation(Reference.PATH_FX + "shield.png");
	IModel normal_sphere;
	IModel shield_model;

	private float opacityLerp;

	public BioticStatRendererShield()
	{
		/*try
        {
            //shield_model = OBJLoader.instance.loadModel(model_path);
            //normal_sphere = OBJLoader.instance.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
		opacityLerp = 0;
	}

	@Override
	public void onWorldRender(BioticStatShield stat, int level, RenderWorldLastEvent event)
	{
		for (Object entity : Minecraft.getMinecraft().theWorld.playerEntities)
		{
			renderPlayerShield(event, (EntityPlayer)entity);
		}
	}

	private void renderPlayerShield(RenderWorldLastEvent event, EntityPlayer player)
	{
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
		boolean isVisible = manageOpacityLerp(androidPlayer, event.getPartialTicks());

		if (isVisible)
		{
			double time = Minecraft.getMinecraft().theWorld.getWorldTime();

			GlStateManager.pushMatrix();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GL_ONE, GL_ONE);
			Vec3d playerPosition = player.getPositionEyes(event.getPartialTicks());
			Vec3d clientPosition = Minecraft.getMinecraft().thePlayer.getPositionEyes(event.getPartialTicks());
			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.2f * getOpacityLerp(player));
			Minecraft.getMinecraft().renderEngine.bindTexture(shield_texture);
			if (!isClient(player))
			{
				GlStateManager.translate(0, player.height - 0.5, 0);
				GlStateManager.enableCull();
			}
			else
			{
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)
				{
					GlStateManager.enableCull();
				}
			}
			GlStateManager.translate(playerPosition.xCoord - clientPosition.xCoord, playerPosition.yCoord - clientPosition.yCoord, playerPosition.zCoord - clientPosition.zCoord);
			GlStateManager.translate(0, -0.5, 0);
			GlStateManager.scale(3, 3, 3);
			GlStateManager.rotate((float)player.motionZ * 45, -1, 0, 0);
			GlStateManager.rotate((float)player.motionX * 45, 0, 0, 1);
			//shield_model.renderAll();

			GlStateManager.disableCull();
			renderAttacks(androidPlayer);

			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.1f * getOpacityLerp(player));
			Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_tex);
			GlStateManager.scale(1.02, 1.02, 1.02);
			//normal_sphere.renderAll();
			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.05f * getOpacityLerp(player));
			GlStateManager.pushMatrix();
			GlStateManager.rotate((float)time * 0.005f, (float)Math.sin(time * 0.01), (float)Math.cos(time * 0.01), 0);
			Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_plasma_tex);
			GlStateManager.scale(1.01, 1.01, 1.01);
			//normal_sphere.renderAll();
			GlStateManager.popMatrix();
			GlStateManager.disableBlend();
			GlStateManager.enableCull();
			GlStateManager.enableAlpha();
			GlStateManager.depthMask(true);
			GlStateManager.popMatrix();
		}
	}

	private boolean manageOpacityLerp(AndroidPlayer androidPlayer, float partialTicks)
	{
		if (MatterOverdriveBioticStats.shield.getShieldState(androidPlayer))
		{
			if (isClient(androidPlayer.getPlayer()))
			{
				if (opacityLerp < 1)
				{
					opacityLerp = Math.min(1, opacityLerp + partialTicks * 0.1f);
				}
			}

			return true;
		}
		else
		{
			if (isClient(androidPlayer.getPlayer()) && opacityLerp > 0)
			{
				opacityLerp = Math.max(0, opacityLerp - partialTicks * 0.2f);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private boolean isClient(EntityPlayer player)
	{
		return player == Minecraft.getMinecraft().thePlayer;
	}

	private float getOpacityLerp(EntityPlayer player)
	{
		if (Minecraft.getMinecraft().thePlayer == player)
		{
			return opacityLerp;
		}
		return 1;
	}

	private void renderAttacks(AndroidPlayer androidPlayer)
	{
		float opacity = getOpacityLerp(androidPlayer.getPlayer());
        /*if (androidPlayer.hasEffect(AndroidPlayer.NBT_HITS))
        {
            NBTTagList hits = androidPlayer.getEffectTagList(AndroidPlayer.NBT_HITS, 10);
            for (int i = 0; i < hits.tagCount(); i++) {
                renderAttack(new Vector3f(hits.getCompoundTagAt(i).getFloat("x"), -hits.getCompoundTagAt(i).getFloat("y"), -hits.getCompoundTagAt(i).getFloat("z")).normalise(null), (hits.getCompoundTagAt(i).getInteger("time") / 10f) * opacity);
            }
        }*/
	}

	private void renderAttack(Vector3f dir, float percent)
	{
		GlStateManager.pushMatrix();
		Vector3f front = new Vector3f(1, 0, 0);
		Vector3f c = Vector3f.cross(dir, front, null);
		double omega = Math.acos(Vector3f.dot(dir, front));
		GlStateManager.rotate((float)(omega * (180 / Math.PI)), c.x, c.y, c.z);
		RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 1 * percent);
		Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_damage_tex);
		//normal_sphere.renderAll();
		GlStateManager.popMatrix();
	}
}
