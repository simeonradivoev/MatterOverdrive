package matteroverdrive.gui.element;

import cofh.lib.gui.element.ElementBase;
import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;

/**
 * Created by Simeon on 4/27/2015.
 */
public class ElementPatternsGrid extends ElementGrid
{
    String filter = "";

    public ElementPatternsGrid(MOGuiBase guiBase, int x, int y, int width, int height)
    {
        super(guiBase, x, y, width, height, width);
        setMargins(0,0,4,0);
    }


    public void updateStackList(HashSet<BlockPosition> positions)
    {
        elements.clear();

        for (BlockPosition position : positions)
        {
            TileEntity entity = position.getTileEntity(Minecraft.getMinecraft().theWorld);

            if (entity instanceof IMatterDatabase) {
                NBTTagList list = ((IMatterDatabase) entity).getItemsAsNBT();

                if (list != null) {

                    for (int p = 0; p < list.tagCount(); p++)
                    {
                        addElement(new ElementMonitorItemPattern(gui,list.getCompoundTagAt(p),(MOGuiBase)gui));
                    }
                }
            }
        }
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public boolean shouldBeDisplayed(ElementBase element)
    {
        return element.getName().toLowerCase().contains(filter.toLowerCase());
    }
}
