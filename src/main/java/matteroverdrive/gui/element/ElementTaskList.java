package matteroverdrive.gui.element;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/21/2015.
 */
public class ElementTaskList extends MOElementListBox
{
    MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue;
    List<String> lastTooltip;
    IButtonHandler buttonHandler;

    public ElementTaskList(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        this(gui,buttonHandler,posX,posY,0,0,taskQueue);
    }

    public ElementTaskList(MOGuiBase gui,IButtonHandler buttonHandler, int posX, int posY, int width, int height,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue) {
        super(gui, posX, posY, width, height);
        this.taskQueue = taskQueue;
        lastTooltip = new ArrayList<String>();
        this.buttonHandler = buttonHandler;
    }

    @Override
    public int getElementCount() {

        return taskQueue.size();
    }

    @Override
    public int getElementHeight(int id)
    {
        if (getSelectedIndex() == id)
        {
            return 44;
        }
        return 22;
    }

    @Override
    public int getElementWidth(int id)
    {
        return  132;
    }

    public IMOListBoxElement getElement(int index) {

        return null;
    }

    @Override
    public void DrawElement(int i,int x,int y,int selectedLineColor,int selectedTextColor, boolean selected,boolean BG)
    {
        GL11.glColor3f(1, 1, 1);

        if (selected) {
            if (i == 0)
                MOElementButton.NORMAL_TEXTURE.Render(x, y, getElementWidth(i), getElementHeight(i));
            else {
                MOElementButton.HOVER_TEXTURE.Render(x, y, getElementWidth(i), getElementHeight(i));

                MOElementButton.HOVER_TEXTURE_DARK.Render(x + 60, y + (getElementHeight(i) / 2) - 2, 50, (getElementHeight(i) / 2) - 4);
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("top", x + 76, y + 24, 0xFFFFFF);
            }

            MOElementButton.HOVER_TEXTURE_DARK.Render(x + 6, y + (getElementHeight(i) / 2) - 2, 50, (getElementHeight(i) / 2) - 4);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("remove", x + 13, y + 24, 0xFFFFFF);
        }
        else
        {
            if (i == 0)
                MOElementButton.NORMAL_TEXTURE.Render(x, y, getElementWidth(i), getElementHeight(i));
            else
                MOElementButton.HOVER_TEXTURE_DARK.Render(x, y, getElementWidth(i), getElementHeight(i));
        }

        MatterNetworkTask task = taskQueue.getAt(i);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(task.getName(), x + 8, y + 7, 0xFFFFFF);
    }

    @Override
    public void drawElementTooltip(int index,int mouseX,int mouseY)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(-posX, 0, 0);
        List tooltip = new ArrayList();
        taskQueue.getAt(index).addInfo(tooltip);
        ((MOGuiBase)gui).setTooltip(tooltip);
        GL11.glPopMatrix();
    }

    @Override
    protected void onElementClicked(int index,int mouseX,int mouseY)
    {
        if (index != 0)
        {
            if (mouseY > 22 && mouseX > 60 && mouseX < 110)
            {
                System.out.println("Top");
                return;
            }
        }

        if (mouseY > 22 && mouseX < 50)
        {
            buttonHandler.handleElementButtonClick("DropTask",index);
            System.out.println("Remove");
        }
    }

    /*@Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        lastTooltip.clear();

        for (int i = 0; i < taskQueue.size();i++)
        {
            if (i == 0)
                gui.bindTexture(TASK_ELEMENT_BG);
            else
                gui.bindTexture(TASK_ELEMENT_SECCOUNDARY_BG);

            int height = 22;
            int width = 132;
            int x = posX;
            int y = posY + (28 * (i - scroll));

            if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
            {
                gui.drawSizedTexturedModalRect(x, y, 0, height, width, height, width, height * 2);
                taskQueue.getAt(i).addInfo(lastTooltip);
                lastTooltip.add("");
                lastTooltip.add(EnumChatFormatting.GOLD + EnumChatFormatting.BOLD.toString() + "Shift" + EnumChatFormatting.GOLD + " Click to Deque");
            }
            else
            {
                gui.drawSizedTexturedModalRect(x,y,0,0,width,height,width,height*2);
            }
            MatterNetworkTask task = taskQueue.getAt(i);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(task.getName(), x + 8, y + 7, 0xFFFFFF);
        }
    }*/

    /*@Override
    public void addTooltip(List<String> list)
    {
        if (!lastTooltip.isEmpty())
        {
            list.addAll(lastTooltip);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }*/
}
