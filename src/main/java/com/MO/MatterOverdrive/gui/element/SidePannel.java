package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 3/13/2015.
 */
public class SidePannel extends ElementBaseGroup
{
    MOElementButton button;
    public static final ScaleTexture BACKGROUND_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_panel_bg.png"),15,18).setOffsets(7,7,8,9);
    private static boolean isOpen;

    public SidePannel(MOGuiBase gui, int posX, int posY,int height)
    {
        super(gui, posX, posY,37,height);
        button = new MOElementButtonVertical(gui,this, 0, 0, "Toggle", 0, 0, 16, 0, 16, height,"",9);
        button.setTexture(Reference.PATH_ELEMENTS + "right_side_bar.png", 32, 143);
        this.setTexture(Reference.PATH_ELEMENTS + "right_side_bar_panel_bg.png", 37, 143);

    }

    @Override
    public void init()
    {
        super.init();
        elements.add(button);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float ticks)
    {
        if(isOpen)
        {
            BACKGROUND_TEXTURE.Render(posX,posY,37,sizeY);
            button.setPosition(32,0);
        }
        else
        {
            button.setPosition(0,0);
        }

        super.drawBackground(mouseX, mouseY, ticks);
    }

    public boolean IsOpen()
    {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if (buttonName == "Toggle")
        {
            isOpen = !isOpen;
        }
    }

    @Override
    public void update()
    {
        if(isOpen)
        {
            button.setToolTip("Close Menu");
            this.setSize(37 + 16, sizeY);
        }
        else
        {
            button.setToolTip("Open Menu");
            this.setSize(37, sizeY);
        }
    }

    @Override
    public void updateInfo()
    {
        for (int i = 0;i < elements.size();i++)
        {
            elements.get(i).setVisible(isOpen);
        }

        button.setVisible(true);
    }
}
