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

package matteroverdrive.gui.element.android_station;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.handler.GoogleAnalyticsCommon;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.network.packet.server.PacketUnlockBioticStat;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created by Simeon on 5/27/2015.
 */
public class ElementBioStat extends MOElementButton
{
	private IBioticStat stat;
	private AndroidPlayer player;
	private int level;
	private EnumFacing direction;
	private boolean strongConnection;
	private ResourceLocation strongConnectionTex = new ResourceLocation(Reference.PATH_ELEMENTS + "connection.png");
	private ResourceLocation strongConnectionBrokenTex = new ResourceLocation(Reference.PATH_ELEMENTS + "connection_broken.png");

	public ElementBioStat(MOGuiBase gui, int posX, int posY, IBioticStat stat, int level, AndroidPlayer player)
	{
		super(gui, gui, posX, posY, stat.getUnlocalizedName(), 0, 0, 0, 0, 22, 22, "");
		texture = ElementSlot.getTexture("holo");
		texW = 22;
		texH = 22;
		this.stat = stat;
		this.player = player;
		this.level = level;
	}

	@Override
	public boolean isEnabled()
	{

		if (stat.canBeUnlocked(player, level))
		{
			if (player.getUnlockedLevel(stat) < stat.maxLevel())
			{
				return true;
			}
		}
		return false;
	}


	protected void ApplyColor()
	{
		if (stat.canBeUnlocked(player, level) || player.isUnlocked(stat, level))
		{
			if (level <= 0)
			{
				RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
			}
			else
			{
				RenderUtils.applyColor(Reference.COLOR_HOLO);
			}
		}
		else
		{
			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO_RED, 0.5f);
		}
	}

	protected void ResetColor()
	{
		GlStateManager.color(1, 1, 1);
	}

	@Override
	public void addTooltip(List<String> list, int mouseX, int mouseY)
	{
		stat.onTooltip(player, MathHelper.clamp_int(level + 1, 0, stat.maxLevel()), list, mouseX, mouseY);
	}

	@Override
	public void onAction(int mouseX, int mouseY, int mouseButton)
	{
		if (super.intersectsWith(mouseX, mouseY))
		{
			if (stat.canBeUnlocked(player, level + 1) && level < stat.maxLevel())
			{
				MOGuiBase.playSound(MatterOverdriveSounds.guiBioticStatUnlock, 1, 1);
				MatterOverdrive.packetPipeline.sendToServer(new PacketUnlockBioticStat(stat.getUnlocalizedName(), ++level));
				MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_BIOTIC_STATS, GoogleAnalyticsCommon.EVENT_ACTION_BIOTIC_STAT_UNLOCK, stat.getUnlocalizedName(), null);
			}
		}
		super.onAction(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6)
	{
		ApplyColor();
		this.gui.drawSizedTexturedModalRect(var1, var2, var3, var4, var5, var6, (float)this.texW, (float)this.texH);
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks)
	{
		GlStateManager.enableBlend();
		ApplyColor();
		super.drawBackground(mouseX, mouseY, gameTicks);
		drawIcon(stat.getIcon(level), posX + 3, posY + 3);
		if (direction != null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, 0);
			GlStateManager.translate(sizeX / 2, sizeY / 2, 0);
			GlStateManager.translate(direction.getFrontOffsetX() * (sizeX * 0.75), -direction.getFrontOffsetY() * (sizeY * 0.75), 0);
			if (direction == EnumFacing.EAST)
			{
				GlStateManager.rotate(90, 0, 0, 1);
			}
			else if (direction == EnumFacing.WEST)
			{
				GlStateManager.rotate(-90, 0, 0, 1);
			}
			else if (direction == EnumFacing.DOWN)
			{
				GlStateManager.rotate(180, 0, 0, 1);
			}
			if (strongConnection)
			{
				GlStateManager.translate(-3.5, -26, 0);
				if (stat.isLocked(player, level))
				{
					RenderUtils.bindTexture(strongConnectionBrokenTex);
				}
				else
				{
					RenderUtils.bindTexture(strongConnectionTex);
				}
				RenderUtils.drawPlane(7, 30);
			}
			else
			{
				GlStateManager.translate(-3.5, -3.5, 0);
				ClientProxy.holoIcons.renderIcon("up_arrow", 0, 0);
			}
			GlStateManager.popMatrix();
		}
		ResetColor();
		GlStateManager.disableBlend();
	}

	public void drawForeground(int x, int y)
	{
		if (stat.maxLevel() > 1 && level > 0)
		{
			String levelInfo = Integer.toString(level);
			ClientProxy.holoIcons.renderIcon("black_circle", posX + 14, posY + 14, 10, 10);
			getFontRenderer().drawString(levelInfo, posX + 16, posY + 16, 0xFFFFFF);
		}
	}

	public void drawIcon(HoloIcon icon, int x, int y)
	{
		if (icon != null)
		{
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			ClientProxy.holoIcons.renderIcon(icon, x, y, 16, 16);
			GlStateManager.disableBlend();
		}
	}

	public IBioticStat getStat()
	{
		return stat;
	}

	public void setStrongConnection(boolean strongConnection)
	{
		this.strongConnection = strongConnection;
	}

	public EnumFacing getDirection()
	{
		return direction;
	}

	public void setDirection(EnumFacing direction)
	{
		this.direction = direction;
	}
}
