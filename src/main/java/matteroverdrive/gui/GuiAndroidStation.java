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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

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
        super(new ContainerAndroidStation(inventoryPlayer,machine), machine,364,364);
        texW = 255;
        texH = 237;
        AndroidPlayer androidPlayer = AndroidPlayer.get(inventoryPlayer.player);

        background = GuiWeaponStation.BG;

        for (int i = 0;i < parts_slots.length;i++)
        {
            parts_slots[i] = new ElementInventorySlot(this,(MOSlot)inventorySlots.getSlot(i),20,20,"holo",androidPlayer.getInventory().getSlot(i).getTexture());
            parts_slots[i].setColor(Reference.COLOR_MATTER.getIntR(),Reference.COLOR_MATTER.getIntG(),Reference.COLOR_MATTER.getIntB(),78);
            parts_slots[i].setInfo("biopart." + BionicSlot.names[i] + ".name");
        }

        parts_slots[Reference.BIONIC_HEAD].setPosition(220,250);
        parts_slots[Reference.BIONIC_ARMS].setPosition(220,280);
        parts_slots[Reference.BIONIC_LEGS].setPosition(220,310);

        parts_slots[Reference.BIONIC_CHEST].setPosition(320,280);
        parts_slots[Reference.BIONIC_OTHER].setPosition(320,250);
        parts_slots[Reference.BIONIC_BATTERY].setPosition(320,310);
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
        elemStat.setPosition(54 + x * 30,42 + y * 30);
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
        super.drawGuiContainerForegroundLayer(x,y);

        if (pages.get(0).isVisible()) {
            glEnable(GL_BLEND);
            //glBlendFunc(GL_ONE, GL_ONE);
            glEnable(GL_LIGHTING);
            glPushMatrix();
            glTranslated(280, 255, 100);
            glScaled(50, 50, 50);
            glRotated(180, 0, 0, 1);
            glRotated(Minecraft.getMinecraft().theWorld.getWorldTime(), 0, 1, 0);
            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 1.0f);
            RenderManager.instance.func_147939_a(Minecraft.getMinecraft().thePlayer, 0, 0, 0, 0, 0, true);
            glPopMatrix();
            glDisable(GL_BLEND);

            String info = Minecraft.getMinecraft().thePlayer.experienceLevel + " XP";
            glDisable(GL_LIGHTING);
            int width = fontRendererObj.getStringWidth(info);
            fontRendererObj.drawString(EnumChatFormatting.GREEN + info, 280 - width / 2, 345, 0xFFFFFF);
        }
    }
}
