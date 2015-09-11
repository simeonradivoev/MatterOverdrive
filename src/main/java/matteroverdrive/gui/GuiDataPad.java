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
import matteroverdrive.container.ContainerMatterScanner;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.element.ElementGuideCategory;
import matteroverdrive.gui.pages.PageGuideDescription;
import matteroverdrive.gui.pages.PageGuideEntries;
import matteroverdrive.guide.GuideCategory;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.network.packet.server.PacketDataPadCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 8/28/2015.
 */
public class GuiDataPad extends MOGuiBase
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_GUI + "pad.png");
    PageGuideDescription guideDescription;
    PageGuideEntries guideEntries;
    ItemStack dataPad;


    public GuiDataPad(ItemStack dataPadStack)
    {
        super(new ContainerMatterScanner(),300,260);
        background = new ScaleTexture(BG,93,115).setOffsets(46,46,40,73);
        dataPad = dataPadStack;
        setPage(MatterOverdriveItems.dataPad.getPage(dataPadStack));
        guideEntries.setDataPadStack(dataPadStack);
        guideDescription.setDataPadStack(dataPadStack);
    }

    @Override
    public void registerPages(MOBaseContainer container)
    {
        guideDescription = new PageGuideDescription(this,14,14,xSize-28,ySize-14-49,"Guide Description");
        guideEntries = new PageGuideEntries(this,14,14,xSize-28,ySize-14-49,"Guide Entries",guideDescription);

        AddPage(guideEntries, ClientProxy.holoIcons.getIcon("page_icon_home"), "Guide Entries");
        AddPage(guideDescription, ClientProxy.holoIcons.getIcon("page_icon_search"), MOStringHelper.translateToLocal("gui.tooltip.page.info_database"));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        elements.remove(sidePannel);
        closeButton.setPosition(xSize - 32, 20);
        for (ElementGuideCategory category : guideEntries.getCategories())
        {
            addElement(category);
        }

    }

    @Override
    public void onPageChange(int newPage)
    {
        if (newPage != MatterOverdriveItems.dataPad.getPage(dataPad)) {
            MatterOverdriveItems.dataPad.setOpenPage(dataPad, newPage);
            MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPad));
        }
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();
        GuideCategory category = guideEntries.getActiveCategory();
        for (int i = 0;i < guideEntries.getCategories().size();i++)
        {
            if (category.equals(guideEntries.getCategories().get(i).getCategory()))
            {
                guideEntries.getCategories().get(i).setEnabled(false);
            }else
            {
                guideEntries.getCategories().get(i).setEnabled(true);
            }

            guideEntries.getCategories().get(i).setPosition(16 + 32 * i, ySize - 28);
        }
    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {

    }

    public PageGuideDescription getGuideDescription() {
        return guideDescription;
    }
}
