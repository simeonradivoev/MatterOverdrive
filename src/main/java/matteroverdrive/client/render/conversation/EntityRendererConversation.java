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

package matteroverdrive.client.render.conversation;

import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.renderer.IDialogShot;
import matteroverdrive.client.render.entity.EntityFakePlayer;
import matteroverdrive.gui.GuiDialog;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

import java.util.Random;

/**
 * Created by Simeon on 8/9/2015.
 */
public class EntityRendererConversation extends EntityRenderer
{

    EntityFakePlayer fakePlayer;
    Minecraft mc;
    Random random;

    public EntityRendererConversation(Minecraft minecraft, IResourceManager resourceManager) {
        super(minecraft, resourceManager);
        this.mc = minecraft;
        random = new Random();
    }

    public void renderWorld(float ticks, long time)
    {
        if (fakePlayer == null)
        {
            fakePlayer = new EntityFakePlayer(mc.theWorld, mc.thePlayer.getGameProfile());
        }

        boolean lastHideGui = mc.gameSettings.hideGUI;
        EntityLivingBase lastRenderViewEntity = mc.renderViewEntity;

        if (mc.currentScreen instanceof GuiDialog)
        {
            mc.gameSettings.hideGUI = true;
            mc.renderViewEntity = fakePlayer;
            GuiDialog guiDialog = (GuiDialog)mc.currentScreen;
            IDialogMessage message = guiDialog.getCurrentMessage();
            if (message != null)
            {
                random.setSeed(guiDialog.getSeed());
                IDialogShot[] shots = message.getShots(guiDialog.getNpc(), mc.thePlayer);
                if (shots != null && shots.length > 0)
                {

                    shots[random.nextInt(shots.length)].positionCamera(guiDialog.getNpc().getEntity(), mc.thePlayer, ticks, this);
                }
                else
                {
                    DialogShot.wideNormal.positionCamera(guiDialog.getNpc().getEntity(), mc.thePlayer, ticks, this);
                }
            }
            updateFakePlayerPositions();
        }
        super.renderWorld(ticks, time);
        mc.renderViewEntity = lastRenderViewEntity;
        mc.gameSettings.hideGUI = lastHideGui;
    }

    public Vec3 getLook(EntityLivingBase active, EntityLivingBase other, float ticks)
    {
        return getPosition(other, ticks, false).subtract(getPosition(active, ticks, false));
    }

    public Vec3 getPosition(EntityLivingBase entityLivingBase, float ticks, boolean includeHeight)
    {
        Vec3 pos = entityLivingBase.getPosition(ticks);
        if (includeHeight)
            pos.addVector(0, entityLivingBase.getEyeHeight(), 0);
        return pos;
    }

    public void rotateCameraYawTo(Vec3 dir, float offset)
    {
        double yaw = Math.acos(Vec3.createVectorHelper(-1, 0, 0).dotProduct(dir));
        Vec3 cross = Vec3.createVectorHelper(-1, 0, 0).crossProduct(dir);
        Vec3 up = Vec3.createVectorHelper(0, -1, 0);
        if (up.dotProduct(cross) < 0)
        {
            yaw = -yaw;
        }
        yaw = Math.PI + yaw;
        setCameraYaw((float) Math.toDegrees(yaw) + offset);
    }

    private void rotatePitchToDir(Vec3 dir, float yaw,float offset)
    {
        setCameraPitch((float) Math.asin(Math.sqrt(dir.xCoord * dir.xCoord + dir.yCoord * dir.yCoord) / dir.zCoord) + offset);
    }

    public void setCameraPosition(double x, double y, double z)
    {
        fakePlayer.posX = x;
        fakePlayer.posY = y;
        fakePlayer.posZ = z;
    }
    public void setCameraPosition(Vec3 position)
    {
        fakePlayer.posX = position.xCoord;
        fakePlayer.posY = position.yCoord;
        fakePlayer.posZ = position.zCoord;
    }

    public void setCameraYaw(float angle)
    {
        fakePlayer.rotationYaw = angle;
    }

    public void setCameraPitch(float angle)
    {
        fakePlayer.rotationPitch = angle;
    }

    public void setCameraPositionSmooth(float angle, float speed)
    {
        fakePlayer.rotationPitch = MOMathHelper.Lerp(fakePlayer.rotationPitch,angle,speed);
    }

    private void updateFakePlayerPositions()
    {
        fakePlayer.prevPosX = fakePlayer.lastTickPosX = fakePlayer.posX;
        fakePlayer.prevPosY = fakePlayer.lastTickPosY = fakePlayer.posY;
        fakePlayer.prevPosZ = fakePlayer.lastTickPosZ = fakePlayer.posZ;

        fakePlayer.prevRotationPitch = fakePlayer.rotationPitch;
        fakePlayer.prevRotationYaw = fakePlayer.rotationYaw;
        fakePlayer.prevRotationYawHead = fakePlayer.rotationYawHead;
    }
}
