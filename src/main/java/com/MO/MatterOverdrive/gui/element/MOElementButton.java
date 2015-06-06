package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.data.ScaleTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOElementButton extends ElementButton
{
    public static final ScaleTexture NORMAL_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over.png"),18,18).setOffsets(7,7,7,7);
    public static final ScaleTexture HOVER_TEXTURE_DARK = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_over_dark.png"),18,18).setOffsets(7,7,7,7);
    public static final Random rand = new Random();

    protected String[] sounds = new String[]{"button_soft_0","button_soft_1"};
    protected String text;
    protected boolean isDown;
    protected int lastMouseButton;
    IButtonHandler buttonHandler;

    public MOElementButton(GuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture) {
        super(gui, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.buttonHandler = handler;
    }


    public MOElementButton(GuiBase gui,IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX,
                         int sizeY, String texture) {
        super(gui, posX, posY, name, sheetX, sheetY, hoverX, hoverY,disabledX,disabledY, sizeX, sizeY, texture);
        this.buttonHandler = handler;
    }

    @Override
    public boolean onMousePressed(int x, int y, int mouseButton) {

        if (isEnabled())
        {
            isDown = true;
            lastMouseButton = mouseButton;
            return true;
        }
        return false;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY)
    {
        if (isEnabled() && intersectsWith(mouseX, mouseY) && isDown)
        {
            String sound = getSound();
            if (sound != null && !sound.isEmpty()) {
                gui.playSound(Reference.MOD_ID + ":gui." + sound, getSoundVolume(), 0.9f + rand.nextFloat() * 0.2f);
            }
            onAction(mouseX, mouseY, lastMouseButton);
        }

        isDown = false;
    }

    public String getSound()
    {
        if (sounds != null && sounds.length > 0)
        {
            return sounds[rand.nextInt(sounds.length)];
        }
        return null;
    }

    public void setSounds(String... sounds)
    {
        this.sounds = sounds;
    }

    public float getSoundVolume()
    {
        return 0.5f;
    }

    public void onAction(int mouseX, int mouseY,int mouseButton)
    {
        buttonHandler.handleElementButtonClick(getName(), lastMouseButton);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawBackground(mouseX, mouseY, gameTicks);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
