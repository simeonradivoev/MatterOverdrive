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

import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.gui.GuiAndroidHud;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			GuiAndroidHud.radialDeltaX -= event.getDx() / 100D;
			GuiAndroidHud.radialDeltaY += event.getDy() / 100D;

			double mag = Math.sqrt(GuiAndroidHud.radialDeltaX * GuiAndroidHud.radialDeltaX + GuiAndroidHud.radialDeltaY * GuiAndroidHud.radialDeltaY);
			if (mag > 1.0D)
			{
				GuiAndroidHud.radialDeltaX /= mag;
				GuiAndroidHud.radialDeltaY /= mag;
			}
		}
		if (Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND) != null && Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IWeapon)
		{
			if (event.getButton() == 0 && event.isButtonstate())
			{
				if (((IWeapon)Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND).getItem()).onLeftClick(Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND), Minecraft.getMinecraft().thePlayer))
				{
					event.setCanceled(true);
				}
			}
		}
	}
}
