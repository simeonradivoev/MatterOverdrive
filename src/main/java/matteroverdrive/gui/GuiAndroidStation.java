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

package matteroverdrive.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.BionicStatGuiInfo;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.container.ContainerAndroidStation;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.element.android_station.ElementBioStat;
import matteroverdrive.gui.element.android_station.ElementDoubleHelix;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityAndroidStation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;

/**
 * Created by Simeon on 5/27/2015.
 */
public class GuiAndroidStation extends MOGuiMachine<TileEntityAndroidStation>
{
	private static int scrollX;
	private static int scrollY = 65;
	ElementSlot[] parts_slots = new ElementSlot[Reference.BIONIC_BATTERY + 1];
	private EntityMeleeRougeAndroidMob mob;
	private MOElementButtonScaled hudConfigs;
	private ElementGroup2DScroll abilitiesGroup;

	public GuiAndroidStation(InventoryPlayer inventoryPlayer, TileEntityAndroidStation machine)
	{
		super(new ContainerAndroidStation(inventoryPlayer, machine), machine, 364, 280);
		texW = 255;
		texH = 237;
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(inventoryPlayer.player);

		background = GuiWeaponStation.BG;

		for (int i = 0; i < parts_slots.length; i++)
		{
			parts_slots[i] = new ElementInventorySlot(this, (MOSlot)inventorySlots.getSlot(i), 20, 20, "holo", androidPlayer.getInventory().getSlot(i).getHoloIcon());
			parts_slots[i].setColor(Reference.COLOR_MATTER.getIntR(), Reference.COLOR_MATTER.getIntG(), Reference.COLOR_MATTER.getIntB(), 78);
			parts_slots[i].setInfo("biopart." + BionicSlot.names[i] + ".name");
		}

		parts_slots[Reference.BIONIC_HEAD].setPosition(220, ySize - 110);
		parts_slots[Reference.BIONIC_ARMS].setPosition(220, ySize - 80);
		parts_slots[Reference.BIONIC_LEGS].setPosition(220, ySize - 50);

		parts_slots[Reference.BIONIC_CHEST].setPosition(320, ySize - 110);
		parts_slots[Reference.BIONIC_OTHER].setPosition(320, ySize - 80);
		parts_slots[Reference.BIONIC_BATTERY].setPosition(320, ySize - 50);
		parts_slots[Reference.BIONIC_BATTERY].setIcon(ClientProxy.holoIcons.getIcon("battery"));

		abilitiesGroup = new ElementGroup2DScroll(this, 46, 30, 300, 155);
		abilitiesGroup.setScrollBounds(-270, 370, -300, 300);
		abilitiesGroup.setScroll(scrollX, scrollY);
		ElementParallaxBackground background = new ElementParallaxBackground(this, 0, 0, abilitiesGroup.getWidth(), abilitiesGroup.getHeight(), false, 0.005f);
		background.setTexture(Reference.PATH_ELEMENTS + "grid_BG.png", 32, 32);
		background.setPosZ(-10);
		background.setColor(Reference.COLOR_HOLO.multiply(1, 1, 1, 0.1f));
		ElementDoubleHelix doubleHelix = new ElementDoubleHelix(this, 0, 0, 320, 240, 2f);
		doubleHelix.setPointColor(Reference.COLOR_HOLO.multiply(1, 1, 1, 0.3f));
		doubleHelix.setLineColor(Reference.COLOR_HOLO.multiply(1, 1, 1, 0.15f));
		doubleHelix.setFillColor(Reference.COLOR_HOLO.multiply(0.3f, 0.3f, 0.3f, 0.2f));
		abilitiesGroup.addElement(doubleHelix);
		abilitiesGroup.addElement(background);

        /*addStat(androidPlayer, MatterOverdriveBioticStats.teleport, -1, 1, null);
		addStat(androidPlayer,MatterOverdriveBioticStats.nightvision,1,1,null);
        addStat(androidPlayer,MatterOverdriveBioticStats.flotation,0,5,null);

        addStat(androidPlayer,MatterOverdriveBioticStats.speed,6,3,null);
        addStat(androidPlayer,MatterOverdriveBioticStats.highJump,6,1,EnumFacing.DOWN,true);
        addStat(androidPlayer,MatterOverdriveBioticStats.equalizer,6,0,EnumFacing.DOWN);


        addStat(androidPlayer, MatterOverdriveBioticStats.nanobots, 3, 3, null);
        addStat(androidPlayer,MatterOverdriveBioticStats.nanoArmor,3,5,EnumFacing.UP,true);
        addStat(androidPlayer,MatterOverdriveBioticStats.shield,3,6,EnumFacing.UP);
        addStat(androidPlayer,MatterOverdriveBioticStats.cloak,3,7,EnumFacing.UP);
        addStat(androidPlayer,MatterOverdriveBioticStats.attack,3,1,EnumFacing.DOWN,true);
        addStat(androidPlayer,MatterOverdriveBioticStats.flashCooling,3,0,EnumFacing.DOWN);
        addStat(androidPlayer,MatterOverdriveBioticStats.shockwave,3,-1,EnumFacing.DOWN);

        addStat(androidPlayer,MatterOverdriveBioticStats.minimap,7,5,null);*/

		//addStats(AndroidPlayer.get(Minecraft.getMinecraft().thePlayer));

		for (IBioticStat stat : MatterOverdrive.statRegistry.getStats())
		{
			int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
			BionicStatGuiInfo guiInfo = stat.getGuiInfo(androidPlayer, unlockedLevel);
			if (guiInfo != null)
			{
				ElementBioStat statElement = new ElementBioStat(this, guiInfo.getPosX(), guiInfo.getPosY(), stat, unlockedLevel, androidPlayer);
				statElement.setDirection(guiInfo.getDirection());
				statElement.setStrongConnection(guiInfo.isStrongConnection());
				abilitiesGroup.addElement(statElement);
			}
		}

		mob = new EntityMeleeRougeAndroidMob(Minecraft.getMinecraft().theWorld);
		mob.getEntityData().setBoolean("Hologram", true);

		hudConfigs = new MOElementButtonScaled(this, this, 48, 64, "hud_configs", 128, 24);
		hudConfigs.setText("HUD Options");
	}

	public void addStat(AndroidPlayer androidPlayer, IBioticStat stat, int x, int y, EnumFacing direction, boolean strong)
	{
		ElementBioStat elemStat = new ElementBioStat(this, 0, 0, stat, androidPlayer.getUnlockedLevel(stat), androidPlayer);
		elemStat.setDirection(direction);
		elemStat.setStrongConnection(strong);
		elemStat.setPosition(12 + x * 30, 6 + y * 30);
		//stats.add(elemStat);
	}

	public void addStat(AndroidPlayer androidPlayer, IBioticStat stat, int x, int y, EnumFacing direction)
	{
		addStat(androidPlayer, stat, x, y, direction, false);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		pages.get(0).addElement(abilitiesGroup);

		for (ElementSlot elementSlot : parts_slots)
		{
			pages.get(0).addElement(elementSlot);
		}

		pages.get(1).addElement(hudConfigs);

		AddMainPlayerSlots(inventorySlots, this);
		AddHotbarPlayerSlots(inventorySlots, this);
	}


	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		scrollX = abilitiesGroup.getScrollX();
		scrollY = abilitiesGroup.getScrollY();

		if (pages.get(0).isVisible())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 300);
			drawEntityOnScreen(280, ySize - 25, 50, -this.mouseX + 280, -this.mouseY + ySize - 100, mc.thePlayer);
			GlStateManager.popMatrix();

			String info = Minecraft.getMinecraft().thePlayer.experienceLevel + " XP";
			GlStateManager.disableLighting();
			int width = fontRendererObj.getStringWidth(info);
			fontRendererObj.drawString(ChatFormatting.GREEN + info, 280 - width / 2, ySize - 20, 0xFFFFFF);
		}
	}

	public void handleElementButtonClick(MOElementBase element, String elementName, int mouseButton)
	{
		super.handleElementButtonClick(element, elementName, mouseButton);
		if (element.equals(hudConfigs))
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig(this, ConfigurationHandler.CATEGORY_ANDROID_HUD));
		}
	}

	/**
	 * Draws an entity on the screen
	 * Copied from {@link net.minecraft.client.gui.inventory.GuiInventory}
	 * @param posX
	 * @param posY
	 * @param scale
	 * @param mouseX
	 * @param mouseY
	 * @param ent
	 */
	private void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
	{
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)posX, (float)posY, 1f);
		GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(ent.worldObj.getWorldTime(), 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		//GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = 0;
		ent.rotationYaw = 0;
		ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
