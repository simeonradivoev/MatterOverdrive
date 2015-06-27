package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.container.IButtonHandler;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ElementStates extends MOElementButtonScaled
{
    String[] states;
    int selectedState;
    String label;

    public ElementStates(GuiBase gui,IButtonHandler buttonHandler, int posX, int posY,String name, int width, int height,String[] states) {
        super(gui,buttonHandler, posX, posY,name, width, height);
        this.name = name;
        this.states = states;
    }

    public String[] getStates()
    {
        return states;
    }

    public void setStates(String[] states)
    {
        this.states = states;
    }

    public void setSelectedState(int selectedState)
    {
        this.selectedState = selectedState;
        this.text = states[selectedState];
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);
        GL11.glColor4d(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER,0.5f);
        GL11.glCullFace(GL11.GL_BACK);
        int width = getFontRenderer().getStringWidth(label);
        getFontRenderer().drawString(label,posX - width - 4,posY + 6,0xFFFFFF);
    }

    @Override
    public void onAction(int mouseX, int mouseY,int mouseButton)
    {
        selectedState++;
        if (selectedState >= states.length)
            selectedState = 0;

        if (selectedState < states.length)
            text = states[selectedState];


        buttonHandler.handleElementButtonClick(name,selectedState);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
}
