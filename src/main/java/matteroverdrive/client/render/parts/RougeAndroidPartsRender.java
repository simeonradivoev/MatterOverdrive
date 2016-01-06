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

package matteroverdrive.client.render.parts;

import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderer;
import matteroverdrive.client.render.entity.EntityRendererRougeAndroid;
import matteroverdrive.entity.player.AndroidPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 9/13/2015.
 */
public class RougeAndroidPartsRender implements IBionicPartRenderer
{
    private final RougeAndroidPartsRenderPlayer moRenderPlayer = new RougeAndroidPartsRenderPlayer();

    @Override
    public void renderPart(ItemStack partStack, AndroidPlayer androidPlayer, RenderPlayer renderPlayer,float ticks)
    {
        moRenderPlayer.setRenderManager(RenderManager.instance);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EntityRendererRougeAndroid.texture);
        EntityPlayer entityPlayer = androidPlayer.getPlayer();

        if (entityPlayer.ticksExisted == 0)
        {
            entityPlayer.lastTickPosX = entityPlayer.posX;
            entityPlayer.lastTickPosY = entityPlayer.posY;
            entityPlayer.lastTickPosZ = entityPlayer.posZ;
        }

        double d0 = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)ticks;
        double d1 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)ticks;
        double d2 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)ticks;
        float f1 = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * ticks;
        IBionicPart bionicPart = (IBionicPart)partStack.getItem();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        moRenderPlayer.setTexture(bionicPart.getTexture(androidPlayer,partStack));
        moRenderPlayer.setModelBipedMain(bionicPart.getModel(androidPlayer,partStack));
        if (moRenderPlayer.modelBipedMain != null)
        {
            moRenderPlayer.doRender(androidPlayer,d0 - RenderManager.renderPosX,d1 - RenderManager.renderPosY,d2 - RenderManager.renderPosZ,f1,ticks);
        }
    }

    @Override
    public void affectPlayerRenderer(ItemStack partStack, AndroidPlayer androidPlayer, RenderPlayer renderPlayer, float ticks)
    {

    }

    public class RougeAndroidPartsRenderPlayer extends RendererLivingEntity
    {
        private ResourceLocation textureLocation;
        public ModelBiped modelBipedMain;

        public RougeAndroidPartsRenderPlayer()
        {
            super(new ModelBiped(0.0F), 0.5F);
        }

        public void doRender(AndroidPlayer androidPlayer, double x, double y, double z, float rotationYaw, float ticks)
        {
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            ItemStack itemstack = androidPlayer.getPlayer().inventory.getCurrentItem();
            this.modelBipedMain.heldItemRight = itemstack != null ? 1 : 0;

            if (itemstack != null && androidPlayer.getPlayer().getItemInUseCount() > 0)
            {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.block)
                {
                    this.modelBipedMain.heldItemRight = 3;
                }
                else if (enumaction == EnumAction.bow)
                {
                    this.modelBipedMain.aimedBow = true;
                }
            }

            this.modelBipedMain.isSneak = androidPlayer.getPlayer().isSneaking();
            double d3 = y - (double)androidPlayer.getPlayer().yOffset;

            if (androidPlayer.getPlayer().isSneaking() && !(androidPlayer.getPlayer() instanceof EntityPlayerSP))
            {
                d3 -= 0.125D;
            }
            super.doRender(androidPlayer.getPlayer(), x, d3, z, rotationYaw, ticks);
            this.modelBipedMain.aimedBow = false;
            this.modelBipedMain.isSneak = false;
            this.modelBipedMain.heldItemRight = 0;
        }

        protected ResourceLocation getEntityTexture(Entity p_110775_1_)
        {
            if (textureLocation != null)
            {
                return textureLocation;
            }
            return EntityRendererRougeAndroid.texture;
        }

        @Override
        protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
        {
            float f1 = 0.9375F;
            GL11.glScalef(f1, f1, f1);
        }

        public void setTexture(ResourceLocation location)
        {
            this.textureLocation = location;
        }

        public void setModelBipedMain(ModelBiped modelBipedMain)
        {
            this.modelBipedMain = modelBipedMain;
            this.mainModel = modelBipedMain;
        }
    }
}
