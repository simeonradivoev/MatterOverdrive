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
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRendererRougeAndroid extends RenderBiped
{
    private boolean hologram;
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "android.png");
    public static final ResourceLocation texture_hologram = new ResourceLocation(Reference.PATH_ENTITIES + "android_holo.png");

    public EntityRendererRougeAndroid(ModelBiped modelBase, float f,boolean hologram)
    {
        super(modelBase, f,1);
        this.hologram = hologram;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (hologram)
        {
            return texture_hologram;
        }else
        {
            return texture;
        }
    }

    @Override
    protected boolean func_110813_b(EntityLiving entityLiving)
    {
        if(entityLiving.getTeam() != null)
        {
            return true;
        }else
        {
            return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityLiving) < 18;
        }
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityLiving, float p_77041_2_)
    {
        if (entityLiving instanceof EntityRougeAndroidMob)
        {
            if (((EntityRougeAndroidMob) entityLiving).getIsLegendary())
            {
                GL11.glScaled(1.5,1.5,1.5);
            }
        }
        super.preRenderCallback(entityLiving, p_77041_2_);
    }

    @Override
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (hologram) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.mainModel.onGround = this.renderSwingProgress(p_76986_1_, p_76986_9_);

            if (this.renderPassModel != null) {
                this.renderPassModel.onGround = this.mainModel.onGround;
            }

            this.mainModel.isRiding = p_76986_1_.isRiding();

            if (this.renderPassModel != null) {
                this.renderPassModel.isRiding = this.mainModel.isRiding;
            }

            this.mainModel.isChild = p_76986_1_.isChild();

            if (this.renderPassModel != null) {
                this.renderPassModel.isChild = this.mainModel.isChild;
            }

            this.renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);

            try {
                float f2 = 0;
                float f3 = 0;
                float f4;


                f4 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
                this.rotateCorpse(p_76986_1_, f4, f2, p_76986_9_);
                float f5 = 0.0625F;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glScalef(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(p_76986_1_, p_76986_9_);
                GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
                float f6 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
                float f7 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0F - p_76986_9_);

                if (p_76986_1_.isChild()) {
                    f7 *= 3.0F;
                }

                if (f6 > 1.0F) {
                    f6 = 1.0F;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                this.mainModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
                this.renderModel(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);
                int j;
                float f8;
                float f9;
                float f10;

                for (int i = 0; i < 4; ++i) {
                    j = this.shouldRenderPass(p_76986_1_, i, p_76986_9_);

                    if (j > 0) {
                        this.renderPassModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
                        this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);

                        if ((j & 240) == 16) {
                            this.func_82408_c(p_76986_1_, i, p_76986_9_);
                            this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);
                        }

                        if ((j & 15) == 15) {
                            f8 = (float) p_76986_1_.ticksExisted + p_76986_9_;
                            //this.bindTexture(RES_ITEM_GLINT);
                            GL11.glEnable(GL11.GL_BLEND);
                            f9 = 0.5F;
                            GL11.glColor4f(f9, f9, f9, 1.0F);
                            GL11.glDepthFunc(GL11.GL_EQUAL);
                            GL11.glDepthMask(false);

                            for (int k = 0; k < 2; ++k) {
                                GL11.glDisable(GL11.GL_LIGHTING);
                                f10 = 0.76F;
                                GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
                                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                                GL11.glMatrixMode(GL11.GL_TEXTURE);
                                GL11.glLoadIdentity();
                                float f11 = f8 * (0.001F + (float) k * 0.003F) * 20.0F;
                                float f12 = 0.33333334F;
                                GL11.glScalef(f12, f12, f12);
                                GL11.glRotatef(30.0F - (float) k * 60.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslatef(0.0F, f11, 0.0F);
                                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                                this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);
                            }

                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glDepthMask(true);
                            GL11.glLoadIdentity();
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glDepthFunc(GL11.GL_LEQUAL);
                        }

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                    }
                }

                GL11.glDepthMask(true);
                this.renderEquippedItems(p_76986_1_, p_76986_9_);
                float f14 = p_76986_1_.getBrightness(p_76986_9_);
                j = this.getColorMultiplier(p_76986_1_, f14, p_76986_9_);
                OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

                if ((j >> 24 & 255) > 0 || p_76986_1_.hurtTime > 0 || p_76986_1_.deathTime > 0) {
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDepthFunc(GL11.GL_EQUAL);

                    if (p_76986_1_.hurtTime > 0 || p_76986_1_.deathTime > 0) {
                        GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                        this.mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);

                        for (int l = 0; l < 4; ++l) {
                            if (this.inheritRenderPass(p_76986_1_, l, p_76986_9_) >= 0) {
                                GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                                this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);
                            }
                        }
                    }

                    if ((j >> 24 & 255) > 0) {
                        f8 = (float) (j >> 16 & 255) / 255.0F;
                        f9 = (float) (j >> 8 & 255) / 255.0F;
                        float f15 = (float) (j & 255) / 255.0F;
                        f10 = (float) (j >> 24 & 255) / 255.0F;
                        GL11.glColor4f(f8, f9, f15, f10);
                        this.mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);

                        for (int i1 = 0; i1 < 4; ++i1) {
                            if (this.inheritRenderPass(p_76986_1_, i1, p_76986_9_) >= 0) {
                                GL11.glColor4f(f8, f9, f15, f10);
                                this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, p_76986_1_.rotationPitch, f5);
                            }
                        }
                    }

                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            } catch (Exception exception) {

            }

            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
            this.passSpecialRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
        }
        else
        {
            super.doRender(p_76986_1_,p_76986_2_,p_76986_4_,p_76986_6_,p_76986_8_,p_76986_9_);
        }
    }
}
