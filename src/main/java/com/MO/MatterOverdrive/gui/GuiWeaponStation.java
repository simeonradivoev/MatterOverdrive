package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeapon;
import com.MO.MatterOverdrive.container.ContainerWeaponStation;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.gui.element.ElementModelPreview;
import com.MO.MatterOverdrive.gui.element.ElementSlot;
import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Simeon on 4/13/2015.
 */
public class GuiWeaponStation extends MOGuiMachine<TileEntityWeaponStation>
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_GUI + "weapon_station.png");

    ElementModelPreview weaponPreview;
    ElementSlot[] module_slots = new ElementSlot[5];
    String[] module_slots_info = {"battery","color","barrel","sights","other"};

    ItemStack lastItem;
    IInventory lastInventory;

    public GuiWeaponStation(InventoryPlayer inventoryPlayer, TileEntityWeaponStation machine)
    {
        super(new ContainerWeaponStation(inventoryPlayer, machine), machine);
        texture = BG;
        xSize = 255;
        ySize = 237;
        weaponPreview = new ElementModelPreview(this,130,90,xSize,ySize);

        for (int i = 0;i < module_slots.length;i++)
        {
            module_slots[i] = new ElementInventorySlot(this,(MOSlot)inventorySlots.getSlot(i+1),20,20,"holo",machine.getInventory().getSlot(i).getTexture());
            module_slots[i].setColor(Reference.COLOR_MATTER.getIntR(),Reference.COLOR_MATTER.getIntG(),Reference.COLOR_MATTER.getIntB(),78);
            module_slots[i].setInfo("module." + module_slots_info[i] + ".name");
        }
    }

    @Override
    public void initGui()
    {
        sidePannel.setPosition(243, 33);
        closeButton.setPosition(237, 6);
        indicator.setPosition(6, 220);
        slotsList.setIsDark(true);
        homePage.addElement(weaponPreview);

        for (int i = 0;i < module_slots.length;i++)
        {
            homePage.addElement(module_slots[i]);
        }

        super.initGui();
        AddPlayerSlots(inventorySlots, this, true, true);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        ItemStack item = machine.getStackInSlot(machine.INPUT_SLOT);
        weaponPreview.setItemStack(item);

        if (MatterHelper.isWeapon(item))
        {
            IWeapon weapon = (IWeapon)item.getItem();
            IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(item, IItemRenderer.ItemRenderType.INVENTORY);
            weaponPreview.setRenderer(renderer);

            for (int i = 0;i < module_slots.length;i++)
            {
                if (weapon.supportsModule(i,item))
                {
                    Vector2f pos = weapon.getSlotPosition(i,item);
                    module_slots[i].setColor(Reference.COLOR_MATTER.getIntR(),Reference.COLOR_MATTER.getIntG(),Reference.COLOR_MATTER.getIntB(),78);
                    module_slots[i].setPosition((int) pos.x, (int) pos.y);
                }
                else
                {
                    module_slots[i].setColor(30,30,30,78);
                    ResetModuleSlotPos(i);
                }
            }

        }else {
            for (int i = 0; i < module_slots.length; i++)
            {
                module_slots[i].setColor(30,30,30,78);
                ResetModuleSlotPos(i);
            }

            weaponPreview.setRenderer(null);
        }
    }

    private void ResetModuleSlotPos(int i)
    {
        if (i < module_slots.length)
        {
            module_slots[i].setPosition(216,121 + i * 22);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);

        ItemStack item = machine.getStackInSlot(machine.INPUT_SLOT);
        if (MatterHelper.isWeapon(item))
        {
            GL11.glPushMatrix();
            GL11.glLineWidth(1f);
            GL11.glColor3f(Reference.COLOR_MATTER.getFloatR(), Reference.COLOR_MATTER.getFloatG(), Reference.COLOR_MATTER.getFloatB());

            IWeapon weapon = (IWeapon)item.getItem();
            for (int i = 0; i < module_slots.length;i++)
            {
                if (weapon.supportsModule(i,item))
                {
                    GL11.glBegin(GL11.GL_LINES);
                    Vector2f slotPos = weapon.getSlotPosition(i, item);
                    Vector2f modulePos = weapon.getModuleScreenPosition(i, item);
                    slotPos = getClosestOnSlot(slotPos, modulePos);

                    GL11.glVertex3f(slotPos.x, slotPos.y, 0);
                    GL11.glVertex3f(modulePos.x, modulePos.y, 0);
                    GL11.glEnd();
                }
            }

            GL11.glPopMatrix();
        }
    }

    Vector2f getClosestOnSlot(Vector2f slotPos,Vector2f modulePos)
    {
        int slotWidth = 18;
        int slotHeight = 18;
        Vector2f center = new Vector2f(slotPos.x + slotWidth/2,slotPos.y + slotHeight/2);

        Vector2f intersect = null;
        intersect = MOMathHelper.Intersects(slotPos,new Vector2f(slotPos.x + slotWidth,slotPos.y),modulePos,center);
        if (intersect != null)
            return intersect;
        intersect = MOMathHelper.Intersects(slotPos,new Vector2f(slotPos.x,slotPos.y + slotHeight),modulePos,center);
        if (intersect != null)
            return intersect;
        intersect = MOMathHelper.Intersects(new Vector2f(slotPos.x + slotWidth,slotPos.y + slotHeight),new Vector2f(slotPos.x,slotPos.y + slotHeight),modulePos,center);
        if (intersect != null)
            return intersect;
        intersect = MOMathHelper.Intersects(new Vector2f(slotPos.x + slotWidth,slotPos.y + slotHeight),new Vector2f(slotPos.x + slotWidth,slotPos.y),modulePos,center);
        if (intersect != null)
            return intersect;
        return center;
    }

}
