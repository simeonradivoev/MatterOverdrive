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

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.client.render.weapons.WeaponItemRenderer;
import matteroverdrive.client.render.weapons.WeaponRenderHandler;
import matteroverdrive.container.ContainerWeaponStation;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by Simeon on 4/13/2015.
 */
public class GuiWeaponStation extends MOGuiMachine<TileEntityWeaponStation>
{
	public static final ScaleTexture BG = new ScaleTexture(new ResourceLocation(Reference.PATH_GUI + "weapon_station.png"), 255, 141).setOffsets(213, 34, 42, 94);

	//ElementModelPreview weaponPreview;
	ElementSlot[] module_slots = new ElementSlot[6];
	String[] module_slots_info = {"battery", "color", "barrel", "sights", "other", "other"};

	public GuiWeaponStation(InventoryPlayer inventoryPlayer, TileEntityWeaponStation machine)
	{
		super(new ContainerWeaponStation(inventoryPlayer, machine), machine, 255, 237);
		texW = 255;
		texH = 237;
		name = "weapon_station";

		background = BG;
		//weaponPreview = new ElementModelPreview(this,130,90,xSize,ySize);

		for (int i = 0; i < module_slots.length; i++)
		{
			module_slots[i] = new ElementInventorySlot(this, (MOSlot)inventorySlots.getSlot(i + 1), 20, 20, "holo", machine.getInventoryContainer().getSlot(i).getHoloIcon());
			module_slots[i].setColor(Reference.COLOR_MATTER.getIntR(), Reference.COLOR_MATTER.getIntG(), Reference.COLOR_MATTER.getIntB(), 78);
			module_slots[i].setInfo("module." + module_slots_info[i] + ".name");
		}

		sidePannel.setPosition(243, 33);
		closeButton.setPosition(237, 6);
		indicator.setPosition(6, 220);
		slotsList.getMainSlot().setType("big_main_dark");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		//pages.get(0).addElement(weaponPreview);

		for (ElementSlot module_slot : module_slots)
		{
			pages.get(0).addElement(module_slot);
		}

		AddMainPlayerSlots(inventorySlots, this);
		AddHotbarPlayerSlots(inventorySlots, this);
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();

		ItemStack item = machine.getStackInSlot(machine.INPUT_SLOT);
		//weaponPreview.setItemStack(item);

		if (WeaponHelper.isWeapon(item))
		{
			IWeapon weapon = (IWeapon)item.getItem();
			//Minecraft.getMinecraft().getRenderManager().doRenderEntity()

			for (int i = 0; i < module_slots.length; i++)
			{
				if (weapon.supportsModule(i, item))
				{
					Vector2f pos = weapon.getSlotPosition(i, item);
					module_slots[i].setColor(Reference.COLOR_MATTER.getIntR(), Reference.COLOR_MATTER.getIntG(), Reference.COLOR_MATTER.getIntB(), 78);
					module_slots[i].setPosition((int)pos.x, (int)pos.y);
				}
				else
				{
					module_slots[i].setColor(30, 30, 30, 78);
					ResetModuleSlotPos(i);
				}
			}

		}
		else
		{
			for (int i = 0; i < module_slots.length; i++)
			{
				module_slots[i].setColor(30, 30, 30, 78);
				ResetModuleSlotPos(i);
			}

			//weaponPreview.setRenderer(null);
		}
	}

	private void ResetModuleSlotPos(int i)
	{
		if (i < module_slots.length)
		{
			module_slots[i].setPosition(216, 96 + i * 22);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(partialTick, x, y);

		ItemStack item = machine.getStackInSlot(machine.INPUT_SLOT);
		if (WeaponHelper.isWeapon(item) && pages.get(0).isVisible())
		{
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.disableCull();

			WeaponRenderHandler weaponRenderHandler = ClientProxy.renderHandler.getWeaponRenderHandler();
			List<ItemStack> modules = MOInventoryHelper.getStacks(item);
			WeaponItemRenderer weaponItemRenderer = weaponRenderHandler.getWeaponModel(item);
			GlStateManager.pushMatrix();
			GlStateManager.translate(240, 130, 200);
			GlStateManager.scale(180, 180, 180);
			GlStateManager.rotate(-190, 1, 0, 0);
			GlStateManager.rotate(-140, 0, 1, 0);
			GlStateManager.rotate(10, 0, 0, 1);
			if (weaponItemRenderer != null)
			{
				weaponRenderHandler.renderWeaponAndModules(modules, weaponItemRenderer, item, 0, 0);
			}
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		ItemStack item = machine.getStackInSlot(machine.INPUT_SLOT);
		if (WeaponHelper.isWeapon(item) && pages.get(0).isVisible())
		{
			GlStateManager.disableTexture2D();
			GlStateManager.pushMatrix();
			GL11.glLineWidth(1f);
			GlStateManager.color(Reference.COLOR_MATTER.getFloatR(), Reference.COLOR_MATTER.getFloatG(), Reference.COLOR_MATTER.getFloatB(), 1);

			IWeapon weapon = (IWeapon)item.getItem();
			for (int i = 0; i < module_slots.length; i++)
			{
				if (weapon.supportsModule(i, item))
				{
					GL11.glBegin(GL11.GL_LINES);
					Vector2f slotPos = weapon.getSlotPosition(i, item);
					Vector2f modulePos = weapon.getModuleScreenPosition(i, item);
					slotPos = getClosestOnSlot(slotPos, modulePos);

					GL11.glVertex3f(slotPos.x, slotPos.y, 400);
					GL11.glVertex3f(modulePos.x, modulePos.y, 400);
					GL11.glEnd();
				}
			}

			GlStateManager.popMatrix();
			GlStateManager.enableTexture2D();
		}
	}

	Vector2f getClosestOnSlot(Vector2f slotPos, Vector2f modulePos)
	{
		int slotWidth = 18;
		int slotHeight = 18;
		Vector2f center = new Vector2f(slotPos.x + slotWidth / 2, slotPos.y + slotHeight / 2);

		Vector2f intersect;
		intersect = MOMathHelper.Intersects(slotPos, new Vector2f(slotPos.x + slotWidth, slotPos.y), modulePos, center);
		if (intersect != null)
		{
			return intersect;
		}
		intersect = MOMathHelper.Intersects(slotPos, new Vector2f(slotPos.x, slotPos.y + slotHeight), modulePos, center);
		if (intersect != null)
		{
			return intersect;
		}
		intersect = MOMathHelper.Intersects(new Vector2f(slotPos.x + slotWidth, slotPos.y + slotHeight), new Vector2f(slotPos.x, slotPos.y + slotHeight), modulePos, center);
		if (intersect != null)
		{
			return intersect;
		}
		intersect = MOMathHelper.Intersects(new Vector2f(slotPos.x + slotWidth, slotPos.y + slotHeight), new Vector2f(slotPos.x + slotWidth, slotPos.y), modulePos, center);
		if (intersect != null)
		{
			return intersect;
		}
		return center;
	}

}
