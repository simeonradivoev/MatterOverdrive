package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IBionicStat;
import matteroverdrive.container.ContainerAndroidStation;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.entity.EntityRougeAndroidMob;
import matteroverdrive.gui.element.ElementBioStat;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.handler.AndroidStatRegistry;
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
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotated;

/**
 * Created by Simeon on 5/27/2015.
 */
public class GuiAndroidStation extends MOGuiMachine<TileEntityAndroidStation>
{
    private EntityRougeAndroidMob mob;
    ElementSlot[] parts_slots = new ElementSlot[Reference.BIONIC_BATTERY+1];
    List<ElementBioStat> stats = new ArrayList<ElementBioStat>(AndroidStatRegistry.stats.size());

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

        addStat(androidPlayer,AndroidStatRegistry.teleport,0,0,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,AndroidStatRegistry.nanobots,1,1,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,AndroidStatRegistry.nanoArmor,0,1,ForgeDirection.EAST);
        addStat(androidPlayer,AndroidStatRegistry.flotation,2,0,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,AndroidStatRegistry.speed,3,0,ForgeDirection.UNKNOWN);
        addStat(androidPlayer,AndroidStatRegistry.highJump,3,1,ForgeDirection.UP);
        addStat(androidPlayer,AndroidStatRegistry.equalizer,3,2,ForgeDirection.UP);
        addStat(androidPlayer,AndroidStatRegistry.shield,0,2,ForgeDirection.UP);
        addStat(androidPlayer,AndroidStatRegistry.attack,2,1,ForgeDirection.WEST);

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

        for (int i = 0;i < parts_slots.length;i++)
        {
            pages.get(0).addElement(parts_slots[i]);
        }

        for (int i = 0;i < stats.size();i++)
        {
            pages.get(0).addElement(stats.get(i));
        }

        AddMainPlayerSlots(inventorySlots, this);
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    @Override
    public void drawTooltip(List<String> list) {

        for (int i = 0;i < stats.size();i++)
        {
            if (stats.get(i).intersectsWith(mouseX,mouseY))
            {
                int itemCount = 0;
                for (ItemStack stack : stats.get(i).getStat().getRequiredItems()) {
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
