package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.GuiMatterScanner;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageScanInfo extends ElementBaseGroup
{
    public static final String backgroundPath = Reference.PATH_GUI + "matter_scanner.png";
    public static final String LIST_ELEMENT_NAME = "list";
    public static final String SCROLL_UP_BUTTON_NAME = "scroll_up";
    public static final String SCROLL_DOWN_BUTTON_NAME = "scroll_down";

    NBTTagCompound itemNBT;
    public MatterDatabaseListBox list;
    ElementProgress scan_progress;
    ElementScanProgress scan_info_graph;
    MOElementTextField searchField;
    ElementButton scrollButtonUp;
    ElementButton scrollButtonDown;

    ElementItemPreview itemPreview;

    public PageScanInfo(MOGuiBase gui, int posX, int posY, String name,NBTTagCompound itemNBT,ItemStack scanner)
    {
        super(gui, posX, posY);
        this.setName(name);
        this.itemNBT = itemNBT;

        scan_info_graph = new ElementScanProgress(gui,87,44);
        itemPreview = new ElementItemPreview(gui,45,44,null);
        scan_progress = new ElementProgress(gui,46 + 35,146 + 2,46,146,39,202,62,188,105,14,142,18);
        searchField = new MOElementTextField(gui,41,26);
        list = new MatterDatabaseListBox(gui,3,39,37,100,scanner);
        scrollButtonUp = new ElementButton(gui,11,27,SCROLL_UP_BUTTON_NAME,22,188,32,188,10,10,backgroundPath);
        scrollButtonDown = new ElementButton(gui,11,142,SCROLL_DOWN_BUTTON_NAME,42,188,52,188,10,10,backgroundPath);

        list.background = GuiMatterScanner.background;
        list.setName(LIST_ELEMENT_NAME);

        list.setFilter("");
        list.updateList("");

        scan_progress.setTexture(backgroundPath, 256, 256);
        scan_progress.setMaxValue(MatterDatabaseHelper.MAX_ITEM_PROGRESS);
        scan_progress.SetTextPostition(18, 5);
        scan_progress.setTextColor(new GuiColor(255, 255, 255).getColor());
        scan_info_graph.setProgress(1);

        elements.add(scan_info_graph);
        elements.add(itemPreview);
        elements.add(scan_progress);
        elements.add(searchField);
        elements.add(scrollButtonUp);
        elements.add(scrollButtonDown);
        elements.add(list);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX,mouseY);

        if(searchField != null)
        {
            this.list.setFilter(searchField.getText());
        }

        ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemNBT);
        String Matter = "Matter: " + String.valueOf(MatterHelper.getMatterAmountFromItem(item)) + MatterHelper.MATTER_UNIT;

        List infos = item.getTooltip(null, false);
        infos.add(Matter);
        RenderUtils.DrawMultilineInfo(infos, 50, 98, 8, 32, new GuiColor(255, 255, 255).getColor());
        //pageScanInfo.setItemNBT(itemNBT);
    }

    public void setItemNBT(NBTTagCompound tagCompound)
    {
        itemNBT = tagCompound;
        scan_progress.setValue(MatterDatabaseHelper.GetProgressFromNBT(itemNBT));
        scan_progress.setText(String.valueOf((int) (((float) MatterDatabaseHelper.GetProgressFromNBT(itemNBT) / (float) 100) * 100)) + "%");

        scan_info_graph.setSeed(tagCompound.getShort("id"));
        itemPreview.setItemStack(ItemStack.loadItemStackFromNBT(tagCompound));
    }
}
