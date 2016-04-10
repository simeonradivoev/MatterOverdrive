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

package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 5/26/2015.
 */
@SideOnly(Side.CLIENT)
public class EntityRendererRougeAndroid<T extends EntityRougeAndroidMob> extends RenderBiped
{
	public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTETIES + "android.png");
	public static final ResourceLocation texture_hologram = new ResourceLocation(Reference.PATH_ENTETIES + "android_holo.png");
	private final boolean hologram;

	public EntityRendererRougeAndroid(ModelBiped modelBase, float f, boolean hologram)
	{
		super(Minecraft.getMinecraft().getRenderManager(), modelBase, f, 1);
		this.hologram = hologram;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		if (hologram)
		{
			return texture_hologram;
		}
		else
		{
			return texture;
		}
	}

	@Override
	protected boolean canRenderName(EntityLiving entityLiving)
	{
		return entityLiving.getTeam() != null || Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityLiving) < 18;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entityLiving, float p_77041_2_)
	{
		if (entityLiving instanceof EntityRougeAndroidMob)
		{
			if (((EntityRougeAndroidMob)entityLiving).getIsLegendary())
			{
				GlStateManager.scale(1.5, 1.5, 1.5);
			}
		}
		super.preRenderCallback(entityLiving, p_77041_2_);
	}

	@Override
	public void doRender(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		//this.renderLeash(entity, x, y, z, entityYaw, partialTicks);
	}
}
