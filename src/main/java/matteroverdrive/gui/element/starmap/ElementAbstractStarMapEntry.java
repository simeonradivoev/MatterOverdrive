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

package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Map;

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
    public static ScaleTexture BG_CIRCLE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "holo_list_entry_circle.png"),32,32).setOffsets(15,15,15,15);
    protected T spaceBody;
    protected ElementGroupList groupList;
    protected HoloIcon travelIcon,searchIcon;

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
            getBG(spaceBody).Render(posX, posY, sizeX - 64, sizeY);
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
                getBG(spaceBody).Render(posX, posY, sizeX - 64, sizeY);
            }else {
                getBG(spaceBody).Render(posX, posY, sizeX - 64, sizeY);
            }
        }
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected ScaleTexture getBG(T spaceBody)
    {
        return BG;
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
            int iconsX = 0;

            if (canTravelTo(spaceBody, Minecraft.getMinecraft().thePlayer)) {
                multiply = 0.5f;
                if (intersectsWith(mouseX, mouseY) && mouseX > sizeX - 32 && mouseX < sizeX) {
                    multiply = 1f;
                }

                RenderUtils.applyColorWithMultipy(color, multiply);
                ClientProxy.holoIcons.bindSheet();
                ClientProxy.holoIcons.renderIcon(travelIcon,posX + sizeX - 32 + 6, posY + 5);
                iconsX +=32;
            }

            if (canView(spaceBody,Minecraft.getMinecraft().thePlayer)) {
                multiply = 0.5f;
                if (intersectsWith(mouseX, mouseY) && mouseX > sizeX - 64 && mouseX < sizeX - 32) {
                    multiply = 1f;
                }

                RenderUtils.applyColorWithMultipy(color, multiply);
                ClientProxy.holoIcons.bindSheet();
                ClientProxy.holoIcons.renderIcon(searchIcon, posX + sizeX - 64 + searchIcon.getOriginalWidth() / 2, posY + searchIcon.getOriginalHeight() / 2);
                iconsX+=32;
            }

            multiply = 0.8f;
            Map<HoloIcon,Integer> icons = getIcons(spaceBody);
            if (icons != null) {
                for (Map.Entry<HoloIcon,Integer> entry : icons.entrySet()) {
                    if (entry.getValue() != 0) {
                        GL11.glEnable(GL11.GL_BLEND);
                        RenderUtils.applyColorWithMultipy(getSpaceBodyColor(spaceBody), multiply);
                        BG_CIRCLE.Render(posX + 128 + iconsX, posY, 32, 32);
                        ClientProxy.holoIcons.renderIcon(entry.getKey(),posX + iconsX + 128 + 16 - entry.getKey().getOriginalWidth() / 2, posY + 16 - entry.getKey().getOriginalHeight() / 2);
                        if (entry.getValue() > 0)
                            RenderUtils.drawString(String.valueOf(entry.getValue()), posX + iconsX + 128 + 16 + 3, posY + 16 + 3, Reference.COLOR_HOLO,1);
                        iconsX += 32;
                    }
                }
            }
        }else
        {
            drawElementName(spaceBody,getSpaceBodyColor(spaceBody),0.3f);
            int x = 0;
            Map<HoloIcon,Integer> icons = getIcons(spaceBody);
            if (icons != null) {
                for (Map.Entry<HoloIcon,Integer> entry : icons.entrySet()) {
                    if (entry.getValue() != 0) {
                        GL11.glEnable(GL11.GL_BLEND);
                        RenderUtils.applyColorWithMultipy(getSpaceBodyColor(spaceBody), 0.3f);
                        BG_CIRCLE.Render(posX + 128 + x, posY, 32, 32);
                        ClientProxy.holoIcons.renderIcon(entry.getKey(),posX + x + 128 + 16 - entry.getKey().getOriginalWidth() / 2, posY + 16 - entry.getKey().getOriginalHeight() / 2);
                        if (entry.getValue() > 0)
                            RenderUtils.drawString(String.valueOf(entry.getValue()), posX + x + 128 + 16 + 3, posY + 16 + 3, getSpaceBodyColor(spaceBody),0.6f);
                        x += 32;
                    }
                }
            }
        }
    }

    protected abstract void drawElementName(T spaceBody,GuiColor color,float multiply);
    protected abstract Map<HoloIcon,Integer> getIcons(T spaceBody);

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
