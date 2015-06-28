package com.MO.MatterOverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.client.render.tileentity.starmap.StarMapRendererStars;
import com.MO.MatterOverdrive.gui.GuiStarMap;
import com.MO.MatterOverdrive.gui.element.ElementGroupList;
import com.MO.MatterOverdrive.starmap.data.GalacticPosition;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Star;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 6/20/2015.
 */
public class ElementStarEntry extends ElementAbstractStarMapEntry<Star>
{

    public ElementStarEntry(GuiStarMap gui,ElementGroupList groupList, int width, int height,Star star)
    {
        super(gui,groupList,width,height,star);
    }

    @Override
    protected void drawElementName(Star star,GuiColor color,float multiply)
    {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || star.isClaimed(Minecraft.getMinecraft().thePlayer) >= 2)
        {
            RenderUtils.drawString(spaceBody.getName(), posX + 16, posY + 10, color, multiply);
        }else
        {
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer,spaceBody.getName(), posX + 16, posY + 10, color, multiply);
        }
    }

    @Override
    protected boolean canTravelTo(Star star, EntityPlayer player)
    {
        return star.isClaimed(player) >= 2;
    }

    @Override
    protected boolean canView(Star spaceBody, EntityPlayer player) {
        return true;
    }

    @Override
    protected void onTravelPress()
    {
        ((GuiStarMap) gui).getMachine().setGalaxticPosition(new GalacticPosition(spaceBody));
        ((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    @Override
    protected void onSelectPress() {
        ((GuiStarMap) gui).getMachine().setDestination(new GalacticPosition(spaceBody));
        ((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    protected void onViewPress()
    {
        ((GuiStarMap) gui).setPage(2);
    }

    @Override
    protected GuiColor getSpaceBodyColor(Star star)
    {
        return StarMapRendererStars.getStarColor(star, Minecraft.getMinecraft().thePlayer);
    }

    @Override
    boolean isSelected(Star star)
    {
        return ((GuiStarMap)gui).getMachine().getDestination().equals(star);
    }

    @Override
    public float getMultiply(Star star)
    {
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getDestination().equals(star))
        {
           return  1;
        }else if (guiStarMap.getMachine().getGalaxyPosition().equals(star))
        {
            return  0.5f;
        }
        return 0.1f;
    }
}


