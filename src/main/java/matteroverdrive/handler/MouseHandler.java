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

package matteroverdrive.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.gui.GuiAndroidHud;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;

/**
 * Created by Simeon on 7/9/2015.
 */
@SideOnly(Side.CLIENT)
public class MouseHandler
{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onMouseEvent(MouseEvent event)
    {
        if (GuiAndroidHud.showRadial)
        {
            GuiAndroidHud.radialDeltaX -= event.dx / 100D;
            GuiAndroidHud.radialDeltaY += event.dy / 100D;

            double mag = Math.sqrt(GuiAndroidHud.radialDeltaX * GuiAndroidHud.radialDeltaX + GuiAndroidHud.radialDeltaY * GuiAndroidHud.radialDeltaY);
            if (mag > 1.0D) {
                GuiAndroidHud.radialDeltaX /= mag;
                GuiAndroidHud.radialDeltaY /= mag;
            }
        }
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof IWeapon)
        {
            if (event.button == 0 && event.buttonstate)
            {
                if (((IWeapon) Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()).onLeftClick(Minecraft.getMinecraft().thePlayer.getHeldItem(), Minecraft.getMinecraft().thePlayer))
                {
                    event.setCanceled(true);
                }
            }
        }
    }
}
