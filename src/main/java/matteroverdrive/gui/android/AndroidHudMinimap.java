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

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
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
    private Sphere sphere;
    private Cylinder cylinder;
    private final float OPACITY = 0.6f;
    private final int ROTATION = 55;
    private float ZOOM = 1;
    private int RADIUS = 64;

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
    public void drawElement(AndroidPlayer androidPlayer,int mouseX, int mouseY, ScaledResolution resolution, float ticks)
    {
        int x = getWidth(resolution,androidPlayer)/2;
        int y = getHeight(resolution,androidPlayer)/2;
        float scale = getScale(resolution);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        glPushMatrix();
        glTranslated(x, y, -100);
        glRotated(ROTATION, 1, 0, 0);
        glScaled(scale, scale, scale);
        drawBackground(resolution);

        beginMask();
        glPopMatrix();

        for (Object entityObj : mc.theWorld.loadedEntityList)
        {
            if (entityObj instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entityObj;
                Vec3 pos = (entityLivingBase).getPosition(ticks);
                Vec3 playerPosition = mc.thePlayer.getPosition(ticks);
                pos = pos.subtract(playerPosition);
                pos.xCoord *= ZOOM;
                pos.yCoord *= ZOOM;
                pos.zCoord *= ZOOM;

                if (AndroidPlayer.isVisibleOnMinimap((EntityLivingBase) entityObj, mc.thePlayer, pos)) {

                    if (pos.lengthVector() < Math.min(256, (RADIUS+16 / ZOOM))) {

                        //region Push
                        glPushMatrix();
                        glTranslated(0,0,-130);
                        drawEntity(entityLivingBase,scale,x,y,pos);
                        glPopMatrix();
                        //endregion
                    }
                }
            }
        }

        endMask();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
    }

    private void beginMask()
    {
        glPushMatrix();
        glClear(GL_DEPTH_BUFFER_BIT);
        glClearDepth(1f);
        GL11.glDepthFunc(GL11.GL_LESS);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glColorMask(false,false,false,false);
        glDisable(GL_TEXTURE_2D);
        glTranslated(0,0,1);
        RenderUtils.drawCircle(RADIUS, 32);
        glEnable(GL_TEXTURE_2D);

        glDepthMask(false);
        glColorMask(true,true,true,true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_GREATER);
        glPopMatrix();
    }

    private void endMask()
    {
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private void drawBackground(ScaledResolution resolution)
    {
        drawCompas();

        glDisable(GL_ALPHA_TEST);
        glDisable(GL_TEXTURE_2D);

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glLineWidth(1);

        RenderUtils.applyColorWithAlpha(baseColor, 0.5f * OPACITY);
        RenderUtils.drawCircle(RADIUS, 32);

        drawFov(resolution);

        double radarPercent = (mc.theWorld.getWorldTime() % AndroidPlayer.MINIMAP_SEND_TIMEOUT) / (double)AndroidPlayer.MINIMAP_SEND_TIMEOUT;
        RenderUtils.applyColorWithAlpha(baseColor, 0.8f * OPACITY * (float) radarPercent);
        RenderUtils.drawCircle(radarPercent * RADIUS, 32);

        RenderUtils.applyColorWithAlpha(baseColor, 0.5f * OPACITY);

        glCullFace(GL_FRONT);
        cylinder.draw(RADIUS, RADIUS, 5, 64, 1);
        glCullFace(GL_BACK);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        glPushMatrix();
        drawPlayer();
        glPopMatrix();
    }

    private void drawCompas()
    {
        int rad = 74;
        mc.fontRenderer.drawString("S", (int) (Math.sin(Math.toRadians(180 - mc.renderViewEntity.rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(180 - mc.renderViewEntity.rotationYaw)) * rad), Reference.COLOR_MATTER.getColor());
        mc.fontRenderer.drawString("N", (int) (Math.sin(Math.toRadians(-mc.renderViewEntity.rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(-mc.renderViewEntity.rotationYaw)) * 64), Reference.COLOR_MATTER.getColor());
        mc.fontRenderer.drawString("E", (int) (Math.sin(Math.toRadians(90 - mc.renderViewEntity.rotationYaw)) * rad), (int) (Math.cos(Math.toRadians(90 - mc.renderViewEntity.rotationYaw)) * rad), Reference.COLOR_MATTER.getColor());
        mc.fontRenderer.drawString("W", (int) (Math.sin(Math.toRadians(-mc.renderViewEntity.rotationYaw - 90)) * rad), (int) (Math.cos(Math.toRadians(-mc.renderViewEntity.rotationYaw - 90)) * rad), Reference.COLOR_MATTER.getColor());
    }

    private void drawPlayer()
    {
        RenderUtils.applyColor(Reference.COLOR_HOLO_GREEN);
        glRotated(90, 0, 0, 1);
        glRotated(90, 1, 0, 0);
        glTranslated(0, 0, 0);
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
        glTranslated(x, y, 0);
        glRotated(ROTATION, 1, 0, 0);
        glScaled(scale, scale, scale);
        if (!entityLivingBase.equals(mc.thePlayer))
        {
            int size = getMinimapSize(entityLivingBase);
            GuiColor color = getMinimapColor(entityLivingBase);
            float opacity = mc.thePlayer.canEntityBeSeen(entityLivingBase) ? 1 : 0.7f;
            opacity *= baseColor.getFloatA();
            glEnable(GL_TEXTURE_2D);
            RenderUtils.applyColorWithAlpha(color, OPACITY * opacity);
            glRotated(mc.renderViewEntity.rotationYaw, 0, 0, -1);
            glTranslated(pos.xCoord, pos.zCoord, 0);
            glRotated(entityLivingBase.getRotationYawHead(), 0, 0, 1);
            glDisable(GL_TEXTURE_2D);

            //region Depth Meter
            glPushMatrix();
            RenderUtils.applyColorWithAlpha(color, OPACITY * opacity);
            RenderUtils.drawCircle(2, 18);

            if (Math.abs(pos.yCoord) > 4) {
                glBegin(GL_LINES);
                glVertex3d(0, 0, 0);
                glVertex3d(0, 0, pos.yCoord);
                glEnd();

                glTranslated(0, 0, pos.yCoord);
                sphere.draw(2 * opacity, 6, 6);
            }
            glPopMatrix();
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

    private GuiColor getMinimapColor(EntityLivingBase entityLivingBase)
    {
        if (entityLivingBase instanceof IMob)
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
        else if (entityLivingBase instanceof IMerchant)
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
