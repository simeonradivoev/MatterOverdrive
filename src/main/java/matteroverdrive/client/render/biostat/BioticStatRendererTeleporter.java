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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.client.render.tileentity.TileEntityRendererGravitationalAnomaly;
import matteroverdrive.data.biostats.BioticStatTeleport;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BioticStatRendererTeleporter implements IBioticStatRenderer<BioticStatTeleport>
{
    @Override
    public void onWorldRender(BioticStatTeleport stat,int level,RenderWorldLastEvent event)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(Minecraft.getMinecraft().thePlayer);
        Vec3 playerPos = androidPlayer.getPlayer().getPosition(event.partialTicks);

        if (androidPlayer != null && androidPlayer.isAndroid() && androidPlayer.isUnlocked(MatterOverdrive.statRegistry.teleport,MatterOverdrive.statRegistry.teleport.maxLevel()) && MatterOverdrive.statRegistry.teleport.isEnabled(androidPlayer,0) && MatterOverdrive.statRegistry.teleport.getHasPressedKey())
        {
            if(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed())
            {
                glPushMatrix();
                glEnable(GL_BLEND);
                glBlendFunc(GL_ONE, GL_ONE);
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
                glTranslated(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);

                //mob.rotationYawHead = androidPlayer.getPlayer().rotationYawHead;

                Vec3 pos = MatterOverdrive.statRegistry.teleport.getPos(androidPlayer);
                if (pos != null)
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityRendererGravitationalAnomaly.glow);
                    glTranslated(pos.xCoord, pos.yCoord, pos.zCoord);
                    glRotated(androidPlayer.getPlayer().rotationYaw,0,-1,0);
                    glRotated(androidPlayer.getPlayer().rotationPitch,1,0,0);
                    glRotated(Minecraft.getMinecraft().theWorld.getWorldTime() * 10,0,0,1);
                    glTranslated(-0.5,-0.5,0);
                    RenderUtils.drawPlane(1);
                    //RenderManager.instance.func_147939_a(mob,0,0,0,0,0,true);
                }

                glDisable(GL_BLEND);
                glPopMatrix();
            }
        }
    }
}
