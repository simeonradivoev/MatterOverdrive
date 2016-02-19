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

package matteroverdrive.gui.android;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 9/8/2015.
 */
public class AndroidHudMinimap extends AndroidHudElement
{
    private final Sphere sphere;
    private final Cylinder cylinder;
    private final float OPACITY = 0.6f;
    private final int ROTATION = 55;
    private final float ZOOM = 1;
    private final int RADIUS = 64;

    public AndroidHudMinimap(AndroidHudPosition position,String name) {
        super(position,name, 188, 188);
        sphere = new Sphere();
        cylinder = new Cylinder();
    }

    @Override
    public boolean isVisible(AndroidPlayer android)
    {
        return android.isUnlocked(MatterOverdriveBioticStats.minimap,0);
    }

    @Override
    public void drawElement(AndroidPlayer androidPlayer,ScaledResolution resolution,float ticks)
    {
        int x = getWidth(resolution,androidPlayer)/2;
        int y = getHeight(resolution,androidPlayer)/2;
        float scale = getScale(resolution);

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, -100);
        GlStateManager.rotate(ROTATION, 1, 0, 0);
        GlStateManager.scale(scale, scale, scale);
        drawBackground(resolution);

        beginMask();
        GlStateManager.popMatrix();

        for (Object entityObj : mc.theWorld.loadedEntityList)
        {
            if (entityObj instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entityObj;
                Vec3 pos = (entityLivingBase).getPositionEyes(ticks);
                Vec3 playerPosition = mc.thePlayer.getPositionEyes(ticks);
                pos = pos.subtract(playerPosition);
                pos = new Vec3(pos.xCoord*ZOOM,pos.yCoord*ZOOM,pos.zCoord*ZOOM);

                if (AndroidPlayer.isVisibleOnMinimap((EntityLivingBase) entityObj, mc.thePlayer, pos)) {

                    if (pos.lengthVector() < Math.min(256, (RADIUS+16 / ZOOM))) {

                        //region Push
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0,0,-130);
                        drawEntity(entityLivingBase,scale,x,y,pos);
                        GlStateManager.popMatrix();
                        //endregion
                    }
                }
            }
        }

        endMask();

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }

    private void beginMask()
    {
        GlStateManager.pushMatrix();
        GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
        GlStateManager.clearDepth(1f);
        GlStateManager.depthFunc(GL11.GL_LESS);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(false,false,false,false);
        GlStateManager.disableTexture2D();
        GlStateManager.translate(0,0,1);
        RenderUtils.drawCircle(RADIUS, 32);
        GlStateManager.enableTexture2D();

        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true,true,true,true);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_GREATER);
        GlStateManager.popMatrix();
    }

    private void endMask()
    {
        GlStateManager.depthFunc(GL_LEQUAL);
        GlStateManager.depthMask(true);
        GlStateManager.disableDepth();
    }

    private void drawBackground(ScaledResolution resolution)
    {
        drawCompas();

        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glLineWidth(1);

        RenderUtils.applyColorWithAlpha(baseColor, 0.5f * OPACITY);
        RenderUtils.drawCircle(RADIUS, 32);

        drawFov(resolution);

        double radarPercent = (mc.theWorld.getWorldTime() % AndroidPlayer.MINIMAP_SEND_TIMEOUT) / (double)AndroidPlayer.MINIMAP_SEND_TIMEOUT;
        RenderUtils.applyColorWithAlpha(baseColor, 0.8f * OPACITY * (float) radarPercent);
        RenderUtils.drawCircle(radarPercent * RADIUS, 32);

        RenderUtils.applyColorWithAlpha(baseColor, 0.5f * OPACITY);

        GlStateManager.cullFace(GL_FRONT);
        cylinder.draw(RADIUS, RADIUS, 5, 64, 1);
        glNormal3f(0,0,1);
        GlStateManager.cullFace(GL_BACK);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        GlStateManager.pushMatrix();
        drawPlayer();
        GlStateManager.popMatrix();
    }

    private void drawCompas()
    {
        int rad = 74;
        mc.fontRendererObj.drawString("S", (int) (Math.sin(Math.toRadians(180 - mc.getRenderViewEntity().rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(180 - mc.getRenderViewEntity().rotationYaw)) * rad), Reference.COLOR_MATTER.getColor());
        mc.fontRendererObj.drawString("N", (int) (Math.sin(Math.toRadians(-mc.getRenderViewEntity().rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(-mc.getRenderViewEntity().rotationYaw)) * 64), Reference.COLOR_MATTER.getColor());
        mc.fontRendererObj.drawString("E", (int) (Math.sin(Math.toRadians(90 - mc.getRenderViewEntity().rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(90 - mc.getRenderViewEntity().rotationYaw)) * rad), Reference.COLOR_MATTER.getColor());
        mc.fontRendererObj.drawString("W", (int) (Math.sin(Math.toRadians(-mc.getRenderViewEntity().rotationYaw - 90)) * rad), (int) (Math.cos(Math.toRadians(-mc.getRenderViewEntity().rotationYaw - 90)) * rad), Reference.COLOR_MATTER.getColor());
    }

    private void drawPlayer()
    {
        RenderUtils.applyColor(Reference.COLOR_HOLO_GREEN);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.rotate(90, 1, 0, 0);
        GlStateManager.translate(0, 0, 0);
        RenderUtils.drawShip(0, 0, 0, 3);
    }

    private void drawFov(ScaledResolution resolution)
    {
        double aspectRatio = resolution.getScaledWidth_double() / resolution.getScaledHeight_double();
        float angleAdd = (float)180;
        float fovAngle = mc.gameSettings.fovSetting*0.5f*(float)aspectRatio;
        glBegin(GL_LINE_STRIP);
        glVertex3d(0, 0, 0);
        glVertex3d(Math.sin(Math.toRadians(fovAngle + angleAdd)) * RADIUS, Math.cos(Math.toRadians(fovAngle + angleAdd)) * RADIUS, 0);
        glVertex3d(0, 0, 0);
        glVertex3d(Math.sin(Math.toRadians(-fovAngle + angleAdd)) * RADIUS, Math.cos(Math.toRadians(-fovAngle + angleAdd)) * RADIUS, 0);
        glEnd();
    }

    private void drawEntity(EntityLivingBase entityLivingBase,float scale,int x,int y,Vec3 pos)
    {
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(ROTATION, 1, 0, 0);
        GlStateManager.scale(scale, scale, scale);
        if (!entityLivingBase.equals(mc.thePlayer))
        {
            int size = getMinimapSize(entityLivingBase);
            Color color = getMinimapColor(entityLivingBase);
            float opacity = mc.thePlayer.canEntityBeSeen(entityLivingBase) ? 1 : 0.7f;
            opacity *= baseColor.getFloatA();
            GlStateManager.enableTexture2D();
            RenderUtils.applyColorWithAlpha(color, OPACITY * opacity);
            GlStateManager.rotate(mc.getRenderViewEntity().rotationYaw, 0, 0, -1);
            GlStateManager.translate(pos.xCoord, pos.zCoord, 0);
            GlStateManager.rotate(entityLivingBase.getRotationYawHead(), 0, 0, 1);
            GlStateManager.disableTexture2D();

            //region Depth Meter
            GlStateManager.pushMatrix();
            RenderUtils.applyColorWithAlpha(color, OPACITY * opacity);
            RenderUtils.drawCircle(2, 18);

            if (Math.abs(pos.yCoord) > 4) {
                glBegin(GL_LINES);
                glVertex3d(0, 0, 0);
                glVertex3d(0, 0, pos.yCoord);
                glEnd();

                GlStateManager.translate(0, 0, pos.yCoord);
                sphere.draw(2 * opacity, 6, 6);
                glNormal3f(0,0,1);
            }
            GlStateManager.popMatrix();
            //endregion

            RenderUtils.applyColorWithAlpha(color, 0.2f * OPACITY * opacity);
            RenderUtils.drawCircle(size, 18);
        }
    }

    private int getMinimapSize(EntityLivingBase entityLivingBase)
    {
        if (entityLivingBase instanceof IMob && entityLivingBase instanceof EntityCreature) {
            return 17;
        }else
        {
            return 4;
        }
    }

    private Color getMinimapColor(EntityLivingBase entityLivingBase)
    {
        if (entityLivingBase instanceof IMob && !entityLivingBase.isOnSameTeam(Minecraft.getMinecraft().thePlayer))
        {
            MinimapEntityInfo entityInfo = AndroidPlayer.getMinimapEntityInfo(entityLivingBase);
            if (entityInfo != null && entityInfo.isAttacking())
            {
                return Reference.COLOR_GUI_ENERGY;
            }else
            {
                return Reference.COLOR_HOLO_RED;
            }

        }
        else if (entityLivingBase instanceof EntityPlayer)
        {
            return Reference.COLOR_HOLO_YELLOW;
        }
        else if (entityLivingBase instanceof IMerchant || entityLivingBase.isOnSameTeam(Minecraft.getMinecraft().thePlayer))
        {
            return Reference.COLOR_HOLO_GREEN;
        }
        else
        {
            return Reference.COLOR_HOLO;
        }
    }

    private float getScale(ScaledResolution resolution)
    {
        return 1.5f - 0.2f * resolution.getScaleFactor();
    }

    @Override
    public int getWidth(ScaledResolution resolution,AndroidPlayer androidPlayer) {
        return (int)(width * getScale(resolution));
    }

    @Override
    public int getHeight(ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        return (int)(height * getScale(resolution));
    }
}
