package com.MO.MatterOverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.GuiStarMap;
import com.MO.MatterOverdrive.gui.element.ElementGroupList;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.starmap.data.SpaceBody;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 6/21/2015.
 */
public abstract class ElementAbstractStarMapEntry<T extends SpaceBody> extends MOElementButton
{
    public static ScaleTexture BG = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry.png"),32,32).setOffsets(18,12,15,15);
    public static ScaleTexture BG_FLIPPED = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry_flipped.png"),32,32).setOffsets(12,18,15,15);
    public static ScaleTexture BG_MIDDLE_NORMAL = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry_middle.png"),32,32).setOffsets(15,15,15,15).setTextureSize(96, 32);
    public static ScaleTexture BG_MIDDLE_OVER = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry_middle.png"),32,32).setOffsets(15,15,15,15).setTextureSize(96, 32).setUV(32, 0);
    public static ScaleTexture BG_MIDDLE_DOWN = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry_middle.png"),32,32).setOffsets(15,15,15,15).setTextureSize(96,32).setUV(64,0);
    protected T spaceBody;
    protected ElementGroupList groupList;
    protected IIcon travelIcon,searchIcon;

    public ElementAbstractStarMapEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, T spaceBody)
    {
        super(gui, groupList, 0, 0, spaceBody.getName(), 0, 0, 0, 0, width, height, "");
        this.spaceBody = spaceBody;
        this.groupList = groupList;
        this.travelIcon = ClientProxy.holoIcons.getIcon("travel_icon");
        this.searchIcon = ClientProxy.holoIcons.getIcon("icon_search");
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        float multiply = getMultiply(spaceBody);

        RenderUtils.applyColorWithMultipy(getSpaceBodyColor(spaceBody), multiply);
        if (isSelected(spaceBody))
        {
            this.BG.Render(posX, posY, sizeX - 64, sizeY);
            if (canView(spaceBody,Minecraft.getMinecraft().thePlayer)) {
                this.BG_MIDDLE_NORMAL.Render(posX + sizeX - 64, posY, 32, sizeY);
            }
            if (canTravelTo(spaceBody, Minecraft.getMinecraft().thePlayer))
                this.BG_FLIPPED.Render(posX + sizeX - 32, posY, 32, sizeY);
            RenderUtils.applyColorWithMultipy(getSpaceBodyColor(spaceBody), multiply * 0.75f);

        }else
        {
            if (intersectsWith(mouseX,mouseY))
            {
                this.BG.Render(posX, posY, sizeX - 64, sizeY);
            }else {
                this.BG.Render(posX, posY, sizeX - 64, sizeY);
            }
        }
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    float getMultiply(T spaceBody)
    {
        return 0.1f;
    }

    boolean isSelected(T spaceBody)
    {
        return groupList.isSelected(this);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (isSelected(spaceBody))
        {
            float multiply = 1f;
            GuiColor color = getSpaceBodyColor(spaceBody);
            drawElementName(spaceBody,color,multiply);

            if (canTravelTo(spaceBody, Minecraft.getMinecraft().thePlayer)) {
                multiply = 0.5f;
                if (intersectsWith(mouseX, mouseY) && mouseX > sizeX - 32 && mouseX < sizeX) {
                    multiply = 1f;
                }

                RenderUtils.applyColorWithMultipy(color, multiply);
                ClientProxy.holoIcons.bindSheet();
                RenderHelper.renderIcon(posX + sizeX - 32 + 6, posY + 5, 0, travelIcon, travelIcon.getIconWidth(), travelIcon.getIconHeight());
            }

            if (canView(spaceBody,Minecraft.getMinecraft().thePlayer)) {
                multiply = 0.5f;
                if (intersectsWith(mouseX, mouseY) && mouseX > sizeX - 64 && mouseX < sizeX - 32) {
                    multiply = 1f;
                }

                RenderUtils.applyColorWithMultipy(color, multiply);
                ClientProxy.holoIcons.bindSheet();
                RenderHelper.renderIcon(posX + sizeX - 64 + searchIcon.getIconWidth() / 2, posY + searchIcon.getIconHeight() / 2, 0, searchIcon, searchIcon.getIconWidth(), searchIcon.getIconHeight());
            }
        }else
        {
            drawElementName(spaceBody,getSpaceBodyColor(spaceBody),0.3f);

        }
    }

    protected abstract void drawElementName(T spaceBody,GuiColor color,float multiply);

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {

        if (isSelected(spaceBody)) {
            if (mouseX > sizeX - 32 && mouseX < sizeX) {
                if (canTravelTo(spaceBody,Minecraft.getMinecraft().thePlayer))
                {
                    onTravelPress();
                }else
                {
                    return false;
                }
            } else if (mouseX > sizeX - 64 && mouseX < sizeX - 32) {
                if (canView(spaceBody,Minecraft.getMinecraft().thePlayer)) {
                    onViewPress();
                }
            }
            playSound();
        }
        else
        {
            if (mouseX < sizeX - 64)
            {
                playSound();
                onSelectPress();
                return true;
            }
        }
        return false;
    }

    protected abstract boolean canTravelTo(T spaceBody,EntityPlayer player);
    protected abstract boolean canView(T spaceBody,EntityPlayer player);

    protected void playSound()
    {
        String sound = getSound();
        if (sound != null && !sound.isEmpty()) {
            gui.playSound(Reference.MOD_ID + ":gui." + sound, getSoundVolume(), 0.9f + rand.nextFloat() * 0.2f);
        }
    }

    protected abstract void onViewPress();
    protected abstract void onTravelPress();
    protected abstract void onSelectPress();

    protected GuiColor getSpaceBodyColor(T spaceBody)
    {
        return Reference.COLOR_HOLO;
    }

    public T getSpaceBody()
    {
        return spaceBody;
    }
}
