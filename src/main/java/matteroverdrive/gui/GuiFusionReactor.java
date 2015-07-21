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

import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFusionReactor;
import matteroverdrive.gui.element.ElementDoubleCircleBar;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 5/17/2015.
 */
public class GuiFusionReactor extends MOGuiMachine<TileEntityMachineFusionReactorController>
{
    ElementDoubleCircleBar powerBar;
    DecimalFormat format;

    public GuiFusionReactor(InventoryPlayer inventoryPlayer, TileEntityMachineFusionReactorController machine) {
        super(new ContainerFusionReactor(inventoryPlayer,machine), machine,256,230);
        format = new DecimalFormat("#.###");
        name = "fusion_reactor";
        powerBar = new ElementDoubleCircleBar(this,70,40,135,135, Reference.COLOR_GUI_ENERGY);
        powerBar.setColorRight(Reference.COLOR_HOLO);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        pages.get(0).addElement(powerBar);
        AddHotbarPlayerSlots(this.inventorySlots,this);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        powerBar.setProgressRight((float) machine.getMatterStorage().getMatterStored() / (float) machine.getMatterStorage().getCapacity());
        powerBar.setProgressLeft((float) machine.getEnergyStorage().getEnergyStored() / (float) machine.getEnergyStorage().getMaxEnergyStored());

        ManageReqiremnetsTooltips();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x,y);
        if (pages.get(0).isVisible())
        {
            String info = "Efficiency";
            int width = fontRendererObj.getStringWidth(info);
            fontRendererObj.drawString(info, 140 - width / 2, 132, Reference.COLOR_GUI_DARKER.getColor());
            info = Math.round(machine.getEnergyEfficiency() * 100) + "%";
            width = fontRendererObj.getStringWidth(info);
            fontRendererObj.drawString(info, 140 - width / 2, 142, Reference.COLOR_GUI_DARKER.getColor());

            double angle = -(Math.PI * 0.87) * powerBar.getProgressLeft() - ((Math.PI * 2) * 0.03);
            int xPos = 137 + MathHelper.round(Math.sin(angle) * 76);
            int yPos = 104 + MathHelper.round(Math.cos(angle) * 74);
            drawCenteredString(fontRendererObj, format.format(powerBar.getProgressLeft() * 100) + "%", xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

            angle = (Math.PI * 0.87) * powerBar.getProgressRight() + ((Math.PI * 2) * 0.03);
            xPos = 137 + MathHelper.round(Math.sin(angle) * 76);
            yPos = 104 + MathHelper.round(Math.cos(angle) * 74);
            drawCenteredString(fontRendererObj, format.format(powerBar.getProgressRight() * 100) + "%", xPos, yPos, Reference.COLOR_MATTER.getColor());

            info = "+" + machine.getEnergyPerTick() + MOEnergyHelper.ENERGY_UNIT + "/t";
            width = fontRendererObj.getStringWidth(info);
            xPos = 140 - width / 2;
            yPos = 110;
            fontRendererObj.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

            info = "-" + format.format(machine.getMatterDrainPerTick()) + MatterHelper.MATTER_UNIT + "/t";
            width = fontRendererObj.getStringWidth(info);
            xPos = 140 - width / 2;
            yPos = 98;
            fontRendererObj.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_MATTER.getColor());
        }
    }

    void ManageReqiremnetsTooltips()
    {

    }
}
