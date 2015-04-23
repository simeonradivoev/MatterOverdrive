package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.api.inventory.IUpgrade;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotUpgrade;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Simeon on 4/11/2015.
 */
public class PageUpgrades extends ElementBaseGroup
{
    Container container;

    public PageUpgrades(GuiBase gui, int posX, int posY,Container container,Inventory inventory)
    {
        this(gui, posX, posY, 0, 0, container, inventory);
    }

    public PageUpgrades(GuiBase gui, int posX, int posY, int width, int height,Container container,Inventory inventory)
    {
        super(gui, posX, posY, width, height);
        this.container = container;
        AddUpgradeSlots(container, inventory);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY,float ticks)
    {
        super.drawBackground(mouseX, mouseY,ticks);
        DrawUpgradeStats();
    }

    public void DrawUpgradeStats()
    {
        Map<UpgradeTypes,Double> upgradesMap = new HashMap<UpgradeTypes, Double>();

        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if (container.getSlot(i) instanceof SlotUpgrade)
            {
                ItemStack upgradeItem = container.getSlot(i).getStack();
                if (upgradeItem != null && MatterHelper.isUpgrade(upgradeItem))
                {
                    IUpgrade upgrade = (IUpgrade)upgradeItem.getItem();
                    Map<UpgradeTypes,Double> upgradeMap = upgrade.getUpgrades(upgradeItem);
                    for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet())
                    {
                        if (upgradesMap.containsKey(entry.getKey()))
                        {
                            double previusValue = upgradesMap.get(entry.getKey());
                            upgradesMap.put(entry.getKey(),previusValue * entry.getValue());
                        }
                        else
                        {
                            upgradesMap.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
            }
        }

        List<String> infos = new ArrayList<String>();

        for (final Map.Entry<UpgradeTypes, Double> entry : upgradesMap.entrySet())
        {
            infos.add(MOStringHelper.toInfo(entry.getKey(),entry.getValue()));
        }

        RenderUtils.DrawMultilineInfo(infos,76,78,100,300,new GuiColor(255,255,255).getColor());
    }

    public void AddUpgradeSlots(Container container,Inventory inventory)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotUpgrade)
            {
                ElementInventorySlot slotElement = new ElementInventorySlot(gui, (MOSlot) container.inventorySlots.get(i), 22,22,"big");
                slotElement.setIcon(inventory.getSlot(((MOSlot) container.inventorySlots.get(i)).getSlotIndex()).getTexture());
                this.addElement(slotElement);
            }
        }
    }
}
