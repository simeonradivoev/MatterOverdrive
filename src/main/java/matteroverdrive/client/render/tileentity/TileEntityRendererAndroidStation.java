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

package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/27/2015.
 */
public class TileEntityRendererAndroidStation extends TileEntityRendererStation<TileEntityAndroidStation>
{
    EntityMeleeRougeAndroidMob mob;

    public TileEntityRendererAndroidStation()
    {
        super();
    }

    @Override
    protected void renderHologram(TileEntityAndroidStation station, double x, double y, double z, float partialTicks, double noise)
    {
        if ((station).isUseableByPlayer(Minecraft.getMinecraft().thePlayer)) {
            if (mob == null) {
                mob = new EntityMeleeRougeAndroidMob(Minecraft.getMinecraft().theWorld);
                mob.getEntityData().setBoolean("Hologram", true);
            }

            //glDisable(GL_BLEND);
            //glDepthMask(true);
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glPushMatrix();
            glTranslated(x + 0.5, y + 0.8, z + 0.5);
            rotate(station, noise);

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.3f);

            if (station.isUseableByPlayer(Minecraft.getMinecraft().thePlayer)) {
                ClientProxy.renderHandler.rendererRougeAndroidHologram.setRenderManager(RenderManager.instance);
                ClientProxy.renderHandler.rendererRougeAndroidHologram.doRender((EntityLivingBase) mob,0,0,0,0,0);
                //Render render = RenderManager.instance.getEntityRenderObject(mob);
                //render.doRender(mob,0,0,0,0,0);
            }
            glPopMatrix();
        }else
        {
            super.renderHologram(station, x, y, z, partialTicks, noise);
        }
    }
}
