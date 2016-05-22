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
import matteroverdrive.client.data.Color;
import matteroverdrive.client.model.MOModelRenderColored;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRendererRangedRougeAndroid extends EntityRendererRougeAndroid<EntityRangedRogueAndroidMob>
{
	public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTETIES + "android_ranged.png");
	final MOModelRenderColored visorModel;

	public EntityRendererRangedRougeAndroid(RenderManager renderManager)
	{
		super(renderManager, new ModelBiped(0, 0, 96, 64), 0, false);
		visorModel = new MOModelRenderColored(modelBipedMain, 64, 0);
		visorModel.setDisableLighting(true);
		visorModel.addBox(-4, -8, -4, 8, 8, 8);
		((ModelBiped)mainModel).bipedHead.addChild(visorModel);
		modelBipedMain.bipedHead.addChild(visorModel);
	}

    /*@Override
	protected void func_82422_c()
    {
        GlStateManager.translate(0,0.2,-0.3);
        GL11.glRotatef(-97, 0, 0, 1.0F);
        GL11.glRotatef(-60, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.6,0.6,0.6);
    }*/

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return texture;
	}

	@Override
	public void doRender(EntityLiving entityLiving, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		if (entityLiving instanceof EntityRougeAndroidMob)
		{
			visorModel.setColor(new Color(((EntityRougeAndroidMob)entityLiving).getVisorColor()));
		}

		this.modelBipedMain.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
		super.doRender(entityLiving, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}
