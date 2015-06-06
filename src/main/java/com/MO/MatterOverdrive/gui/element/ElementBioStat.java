package com.MO.MatterOverdrive.gui.element;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.network.packet.server.PacketUnlockBionicStat;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 5/27/2015.
 */
public class ElementBioStat extends MOElementButton {

    public static final ResourceLocation up_arrow_tex = new ResourceLocation(Reference.PATH_ELEMENTS + "up_arrow.png");

    IBionicStat stat;
    AndroidPlayer player;
    int level;

    public ElementBioStat(MOGuiBase gui, int posX, int posY,IBionicStat stat,int level,AndroidPlayer player)
    {
        super(gui,gui, posX, posY,stat.getUnlocalizedName(),0,0,0,0, 22, 22,"");
        texture = ElementSlot.getTexture("holo");
        texW = 22;
        texH = 22;
        this.stat = stat;
        this.player = player;
        this.level = level;
    }

    public boolean isEnabled() {

        if (stat.canBeUnlocked(player,level))
        {
            if (player.getUnlockedLevel(stat) < stat.maxLevel())
            {
                return true;
            }
        }
        return false;
    }

    protected void ApplyColor()
    {
        if (stat.canBeUnlocked(player,level) || player.isUnlocked(stat,level))
        {
            if (level <= 0)
            {
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO,0.5f);
            }
            else
            {
                RenderUtils.applyColor(Reference.COLOR_HOLO);
            }
        }
        else
        {
            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO_RED, 0.5f);
        }
    }

    protected void ResetColor()
    {
        GL11.glColor3f(1,1,1);
    }

    public void addTooltip(List<String> list)
    {
       stat.onTooltip(player,level,list, gui.getMouseX(), gui.getMouseY());
    }

    public void onAction(int mouseX, int mouseY,int mouseButton)
    {
        if (super.intersectsWith(mouseX,mouseY))
        {
            if (stat.canBeUnlocked(player,level+1) && level < stat.maxLevel())
            {
                gui.playSound(Reference.MOD_ID + ":" + "gui.biotic_stat_unlock",1,1);
                MatterOverdrive.packetPipeline.sendToServer(new PacketUnlockBionicStat(stat.getUnlocalizedName(),++level));
            }
        }
        super.onAction(mouseX,mouseY,mouseButton);
    }



    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        ApplyColor();
        super.drawBackground(mouseX, mouseY, gameTicks);
        drawIcon(stat.getIcon(level), posX + 3, posY + 3);
        if (stat.getRoot() != null)
        {
            gui.bindTexture(up_arrow_tex);
            gui.drawSizedTexturedModalRect(posX + 8,posY - 6,0,0,7,4,7,4);
        }
        if (stat.maxLevel() > 1 && level > 0)
        {
            getFontRenderer().drawString(Integer.toString(level),posX + 16,posY + 16,0xFFFFFF);
        }
        ResetColor();
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawIcon(ResourceLocation icon,int x,int y)
    {
        if(icon != null)
        {
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            gui.bindTexture(icon);
            gui.drawSizedTexturedModalRect(x, y,0,0,16,16,16,16);
            GL11.glDisable(GL11.GL_BLEND);

        }
    }

    public IBionicStat getStat()
    {
        return stat;
    }
}
