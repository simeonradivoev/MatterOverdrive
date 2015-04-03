package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.element.ElementItemPreview;
import com.MO.MatterOverdrive.gui.element.ElementProgress;
import com.MO.MatterOverdrive.gui.element.ElementScanProgress;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageScanInfo extends ElementBase
{
    public static final String backgroundPath = Reference.PATH_GUI + "matter_scanner.png";

    NBTTagCompound itemNBT;
    ElementProgress scan_progress;
    ElementScanProgress scan_info_graph;
    ElementItemPreview itemPreview;

    public PageScanInfo(GuiBase gui, int posX, int posY, String name,NBTTagCompound itemNBT)
    {
        super(gui, posX, posY);
        this.setName(name);
        this.itemNBT = itemNBT;

        scan_info_graph = new ElementScanProgress(gui,87,44);
        itemPreview = new ElementItemPreview(gui,45,44,null);
        scan_progress = new ElementProgress(gui,46 + 35,146 + 2,46,146,39,202,62,188,105,14,142,18);


        scan_progress.setTexture(backgroundPath, 256, 256);
        scan_progress.setMaxValue(MatterDatabaseHelper.MAX_ITEM_PROGRESS);
        scan_progress.SetTextPostition(18, 5);
        scan_progress.setTextColor(new GuiColor(255, 255, 255).getColor());
        scan_info_graph.setProgress(1);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        scan_progress.drawBackground(mouseX,mouseY,gameTicks);
        scan_info_graph.drawBackground(mouseX,mouseY,gameTicks);
        itemPreview.drawBackground(mouseX,mouseY,gameTicks);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        scan_progress.drawForeground(mouseX,mouseY);
        scan_info_graph.drawForeground(mouseX,mouseY);
        itemPreview.drawForeground(mouseX,mouseY);
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
