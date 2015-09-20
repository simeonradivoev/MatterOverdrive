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

import matteroverdrive.api.renderer.IBionicPartRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 9/20/2015.
 */
public abstract class BionicPartRenderer implements IBionicPartRenderer
{
    protected void translateFromPlayer(EntityPlayer entityPlayer,float partialRenderTick)
    {
        if (entityPlayer != Minecraft.getMinecraft().thePlayer)
        {
            Vec3 clientPos = Minecraft.getMinecraft().thePlayer.getPosition(partialRenderTick);
            Vec3 pos = entityPlayer.getPosition(partialRenderTick);
            GL11.glTranslated(pos.xCoord - clientPos.xCoord, pos.yCoord - clientPos.yCoord + entityPlayer.getEyeHeight() - 0.2, pos.zCoord - clientPos.zCoord);
        }
    }
}
