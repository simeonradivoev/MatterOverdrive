package matteroverdrive.gui.pages;

import cofh.lib.gui.GuiColor;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 4/11/2015.
 */
public class PageUpgrades extends ElementBaseGroup
{
    Container container;
    MOGuiMachine guiMachine;

    public PageUpgrades(MOGuiMachine gui, int posX, int posY,Container container)
    {
        this(gui, posX, posY, 0, 0, container);
    }

    public PageUpgrades(MOGuiMachine gui, int posX, int posY, int width, int height,Container container)
    {
        super(gui, posX, posY, width, height);
        this.container = container;
        this.guiMachine = gui;
    }

    @Override
    public void init()
    {
        super.init();
        AddUpgradeSlots(container, guiMachine.getMachine().getInventoryContainer());
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
            if (container.getSlot(i) instanceof SlotInventory && ((SlotInventory)container.getSlot(i)).getSlot() instanceof UpgradeSlot)
            {
                ItemStack upgradeItem = container.getSlot(i).getStack();
                if (upgradeItem != null && MatterHelper.isUpgrade(upgradeItem))
                {
                    IUpgrade upgrade = (IUpgrade)upgradeItem.getItem();
                    Map<UpgradeTypes,Double> upgradeMap = upgrade.getUpgrades(upgradeItem);
                    for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet())
                    {
                        if (guiMachine.getMachine().isAffectedByUpgrade(entry.getKey())) {
                            if (upgradesMap.containsKey(entry.getKey())) {
                                double previusValue = upgradesMap.get(entry.getKey());
                                upgradesMap.put(entry.getKey(), previusValue * entry.getValue());
                            } else {
                                upgradesMap.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }
        }

        List<String> infos = new ArrayList<String>();

        for (final Map.Entry<UpgradeTypes, Double> entry : upgradesMap.entrySet())
        {
            if (guiMachine.getMachine().isAffectedByUpgrade(entry.getKey())) {
                infos.add(MOStringHelper.toInfo(entry.getKey(), entry.getValue()));
            }
        }

        RenderUtils.DrawMultilineInfo(infos, 76, 78, 100, 300, new GuiColor(255, 255, 255).getColor());
    }

    public void AddUpgradeSlots(Container container,Inventory inventory)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.getSlot(i) instanceof SlotInventory && ((SlotInventory)container.getSlot(i)).getSlot() instanceof UpgradeSlot)
            {
                ElementInventorySlot slotElement = new ElementInventorySlot(gui, (MOSlot) container.inventorySlots.get(i), 22,22,"big");
                slotElement.setIcon(inventory.getSlot(((MOSlot) container.inventorySlots.get(i)).getSlotIndex()).getTexture());
                this.addElement(slotElement);
            }
        }
    }
}
