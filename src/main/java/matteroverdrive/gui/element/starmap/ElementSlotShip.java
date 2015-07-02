package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiBase;
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.util.IIcon;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 6/24/2015.
 */
public class ElementSlotShip extends ElementInventorySlot
{
    TileEntityMachineStarMap starMap;

    public ElementSlotShip(GuiBase gui, MOSlot slot, int posX, int posY, int width, int height, String type, IIcon icon,TileEntityMachineStarMap starMap) {
        super(gui, slot, posX, posY, width, height, type, icon);
        this.starMap = starMap;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (starMap.getPlanet() != null) {
            if (getSlot().getStack() != null) {
                if (getSlot().getStack().getItem() instanceof IShip)
                {
                    if (starMap.getPlanet().canBuild((IShip)getSlot().getStack().getItem(),getSlot().getStack())) {
                        int buildTime = ((IShip) getSlot().getStack().getItem()).getBuildTime(getSlot().getStack());
                        int maxBuildTime = ((IShip) getSlot().getStack().getItem()).maxBuildTime(getSlot().getStack(), starMap.getPlanet());
                        getFontRenderer().drawString(DecimalFormat.getPercentInstance().format((double) buildTime / (double) maxBuildTime), posX - 24, posY + 6, Reference.COLOR_HOLO.getColor());
                    }else
                    {
                        String info = "Can't build";
                        int width = getFontRenderer().getStringWidth(info);
                        getFontRenderer().drawString(info, posX - width - 4, posY + 7, Reference.COLOR_HOLO_RED.getColor());
                    }
                }
            }
        }
    }
}
