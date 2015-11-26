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
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRendererRangedRougeAndroid extends EntityRendererRougeAndroid
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "android_ranged.png");
    public EntityRendererRangedRougeAndroid(ModelBiped modelBase, float f) {
        super(modelBase, f,false);
    }

    @Override
    protected void func_82422_c()
    {
        GL11.glTranslated(0,0.2,-0.3);
        GL11.glRotatef(-97, 0, 0, 1.0F);
        GL11.glRotatef(-60, 0.0F, 1.0F, 0.0F);
        GL11.glScaled(0.6,0.6,0.6);
        //GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }

    @Override
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.field_82423_g.aimedBow = this.field_82425_h.aimedBow = this.modelBipedMain.aimedBow = true;
        super.doRender(p_76986_1_,p_76986_2_,p_76986_4_,p_76986_6_,p_76986_8_,p_76986_9_);
    }
}
