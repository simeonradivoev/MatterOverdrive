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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.container.ContainerAndroidStation;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.entity.EntityRougeAndroidMob;
import matteroverdrive.gui.element.ElementBioStat;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/27/2015.
 */
public class GuiAndroidStation extends MOGuiMachine<TileEntityAndroidStation>
{
    private EntityRougeAndroidMob mob;
    ElementSlot[] parts_slots = new ElementSlot[Reference.BIONIC_BATTERY+1];
    List<ElementBioStat> stats = new ArrayList<>(MatterOverdrive.statRegistry.getStats().size());

    public GuiAndroidStation(InventoryPlayer inventoryPlayer, TileEntityAndroidStation machine)
    {
        super(new ContainerAndroidStation(inventoryPlayer,machine), machine,364,250);
        texW = 255;
        texH = 237;
        AndroidPlayer androidPlayer = AndroidPlayer.get(inventoryPlayer.player);

        background = GuiWeaponStation.BG;

        for (int i = 0;i < parts_slots.length;i++)
        {
            parts_slots[i] = new ElementInventorySlot(this,(MOSlot)inventorySlots.getSlot(i),20,20,"holo",androidPlayer.getInventory().getSlot(i).getHoloIcon());
            parts_slots[i].setColor(Reference.COLOR_MATTER.getIntR(),Reference.COLOR_MATTER.getIntG(),Reference.COLOR_MATTER.getIntB(),78);
            parts_slots[i].setInfo("biopart." + BionicSlot.names[i] + ".name");
        }

        parts_slots[Reference.BIONIC_HEAD].setPosition(220,ySize - 110);
        parts_slots[Reference.BIONIC_ARMS].setPosition(220,ySize - 80);
        parts_slots[Reference.BIONIC_LEGS].setPosition(220,ySize - 50);

        parts_slots[Reference.BIONIC_CHEST].setPosition(320,ySize - 110);
        parts_slots[Reference.BIONIC_OTHER].setPosition(320,ySize - 80);
        parts_slots[Reference.BIONIC_BATTERY].setPosition(320,ySize - 50);
        parts_slots[Reference.BIONIC_BATTERY].setIcon(ClientProxy.holoIcons.getIcon("battery"));

        addStat(androidPlayer, MatterOverdrive.statRegistry.teleport, 0, 0, ForgeDirection.UNKNOWN);
        addStat(androidPlayer, MatterOverdrive.statRegistry.nanobots, 1, 1, ForgeDirection.UNKNOWN);
        addStat(androidPlayer,MatterOverdrive.statRegistry.nanoArmor,0,1,ForgeDirection.EAST);
        addStat(androidPlayer,MatterOverdrive.statRegistry.flotation,2,0,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,MatterOverdrive.statRegistry.speed,3,0,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,MatterOverdrive.statRegistry.highJump,3,1,ForgeDirection.UP);
        addStat(androidPlayer,MatterOverdrive.statRegistry.equalizer,3,2,ForgeDirection.UP);
        addStat(androidPlayer,MatterOverdrive.statRegistry.shield,0,2,ForgeDirection.UP);
        addStat(androidPlayer,MatterOverdrive.statRegistry.attack,2,1,ForgeDirection.WEST);
        addStat(androidPlayer,MatterOverdrive.statRegistry.cloak,0,3,ForgeDirection.UP);
        addStat(androidPlayer,MatterOverdrive.statRegistry.nightvision,1,0,ForgeDirection.UNKNOWN);

        mob = new EntityRougeAndroidMob(Minecraft.getMinecraft().theWorld);
        mob.getEntityData().setBoolean("Hologram",true);
    }

    public void addStat(AndroidPlayer androidPlayer,IBionicStat stat,int x,int y,ForgeDirection direction)
    {
        ElementBioStat elemStat = new ElementBioStat(this,0,0,stat,androidPlayer.getUnlockedLevel(stat),androidPlayer,direction);
        elemStat.setPosition(54 + x * 30,36 + y * 30);
        stats.add(elemStat);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        for (ElementSlot elementSlot : parts_slots)
        {
            pages.get(0).addElement(elementSlot);
        }

        for (ElementBioStat stat : stats)
        {
            pages.get(0).addElement(stat);
        }

        AddMainPlayerSlots(inventorySlots, this);
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    @Override
    public void drawTooltip(List<String> list) {

        for (ElementBioStat stat : stats)
        {
            if (stat.intersectsWith(mouseX, mouseY))
            {
                int itemCount = 0;
                for (ItemStack stack : stat.getStat().getRequiredItems()) {
                    int x = guiLeft + mouseX + 12 + 22 * itemCount;
                    int y = guiTop + mouseY - 36;
                    RenderUtils.renderStack(x, y, stack);
                    glPushMatrix();
                    glTranslated(0,0,100);
                    fontRendererObj.drawString(Integer.toString(stack.stackSize),x + 13,y + 8,0xFFFFFF);
                    glPopMatrix();
                    itemCount++;
                }
            }
        }
        super.drawTooltip(list);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);

        if (pages.get(0).isVisible()) {
			glPushMatrix();
			glTranslatef(0, 0, 100);
			drawEntityOnScreen(280, ySize - 25, 50, -mouseX + 280, -mouseY + ySize - 100, Minecraft.getMinecraft().thePlayer);
			glPopMatrix();

            String info = Minecraft.getMinecraft().thePlayer.experienceLevel + " XP";
            glDisable(GL_LIGHTING);
            int width = fontRendererObj.getStringWidth(info);
            fontRendererObj.drawString(EnumChatFormatting.GREEN + info, 280 - width / 2, ySize - 20, 0xFFFFFF);
        }
    }

	/**
	 * Draws an entity on the screen
	 * Copied from {@link net.minecraft.client.gui.inventory.GuiInventory}
	 * @param x
	 * @param y
	 * @param scale
	 * @param mouseX
	 * @param mouseY
	 * @param entity
	 */
	private void drawEntityOnScreen(int x, int y, int scale, float mouseX, float mouseY, EntityLivingBase entity) {
		glEnable(GL_COLOR_MATERIAL);
		glPushMatrix();
		glTranslatef((float)x, (float)y, 50.0F);
		glScalef((float)(-scale), (float)scale, (float)scale);
		glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		glRotatef(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		entity.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
		entity.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
		entity.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		glTranslatef(0.0F, entity.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		entity.renderYawOffset = f2;
		entity.rotationYaw = f3;
		entity.rotationPitch = f4;
		entity.prevRotationYawHead = f5;
		entity.rotationYawHead = f6;
		glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		glDisable(GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
