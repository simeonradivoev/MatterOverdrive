package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.renderer.ISpaceBodyHoloRenderer;
import com.MO.MatterOverdrive.client.render.StarMapEntityRenderer;
import com.MO.MatterOverdrive.client.render.tileentity.TileEntityRendererStarMap;
import com.MO.MatterOverdrive.container.ContainerStarMap;
import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.pages.*;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.GalacticPosition;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;
import com.MO.MatterOverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/12/2015.
 */
public class GuiStarMap extends MOGuiMachine<TileEntityMachineStarMap>
{
    public static ScaleTexture BG = new ScaleTexture(new ResourceLocation(Reference.PATH_GUI + "star_map.png"),255,141).setOffsets(213,34,42,94);
    Minecraft mc;
    IModelCustom sphere;
    public static EntityRenderer prevRenderer;
    public static StarMapEntityRenderer renderer;
    PagePlanetMenu planetPage;
    PageQuadrant pageQuadrant;
    PageStar pageStar;
    PagePlanetStats pagePlanetStats;

    public GuiStarMap(InventoryPlayer inventoryPlayer, TileEntityMachineStarMap machine)
    {
        super(new ContainerStarMap(inventoryPlayer,machine),machine,480,360);
        mc = Minecraft.getMinecraft();
        sphere = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        background = BG;
        texW = 255;
        texH = 237;

        slotsList.getElements().clear();
        sidePannel.BACKGROUND_TEXTURE.setLocation(new ResourceLocation(Reference.PATH_ELEMENTS + "right_side_bar_panel_bg_holo.png"));
        sidePannel.setOpenable(false);
        sidePannel.setOpen(true);
    }

    @Override
    public void registerPages(MOBaseContainer container,TileEntityMachineStarMap starMap)
    {
        ElementBaseGroup galaxyPage = new PageGalaxy(this,0,0,width,height,starMap);
        galaxyPage.setName("Galaxy");
        AddPage(galaxyPage, ClientProxy.holoIcons.getIcon("page_icon_galaxy"), MOStringHelper.translateToLocal("gui.tooltip.page.galaxy")).setIconColor(Reference.COLOR_MATTER);
        pageQuadrant = new PageQuadrant(this,0,0,width,height,starMap);
        pageQuadrant.setName("Quadrant");
        AddPage(pageQuadrant, ClientProxy.holoIcons.getIcon("page_icon_quadrant"), MOStringHelper.translateToLocal("gui.tooltip.page.quadrant")).setIconColor(Reference.COLOR_MATTER);
        pageStar = new PageStar(this,0,0,width,height,starMap);
        pageStar.setName("Star");
        AddPage(pageStar, ClientProxy.holoIcons.getIcon("page_icon_star"), MOStringHelper.translateToLocal("gui.tooltip.page.star")).setIconColor(Reference.COLOR_MATTER);
        planetPage = new PagePlanetMenu(this,0,0,width,height,(ContainerStarMap)container,starMap);
        planetPage.setName("Planet");
        AddPage(planetPage, ClientProxy.holoIcons.getIcon("page_icon_planet"), MOStringHelper.translateToLocal("gui.tooltip.page.planet")).setIconColor(Reference.COLOR_MATTER);
        pagePlanetStats = new PagePlanetStats(this,0,0,width,height,starMap);
        pagePlanetStats.setName("Planet Stats");
        AddPage(pagePlanetStats,ClientProxy.holoIcons.getIcon("icon_stats"),MOStringHelper.translateToLocal("gui.tooltip.page.planet_stats")).setIconColor(Reference.COLOR_MATTER);

        setPage(machine.getZoomLevel());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(partialTick,x,y);

        glPushMatrix();
        glTranslated(guiLeft, guiTop, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        List<ISpaceBodyHoloRenderer> renderers = ClientProxy.renderHandler.getRendererStarMap().getRenderers(machine.getZoomLevel());
        if (renderers != null) {
            for (ISpaceBodyHoloRenderer renderer : renderers) {
                glPushMatrix();
                glTranslated(xSize / 2, ySize, 0);
                if (machine.getActiveSpaceBody() != null) {
                    renderer.renderGUIInfo(GalaxyClient.getInstance().getTheGalaxy(), machine.getActiveSpaceBody(), machine, partialTick, 0.8f);
                }
                glPopMatrix();
            }
        }
        glPopMatrix();
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        super.drawScreen(x, y, partialTick);
    }

    public void drawWorldBackground(int p_146270_1_)
    {

    }

    public void onPageChange(int newPage)
    {
        pageQuadrant.init();
        pageStar.init();
        planetPage.init();
        pagePlanetStats.init();
        if (newPage != machine.getZoomLevel())
        {
            machine.setZoomLevel(newPage);
            machine.SyncCommandsToServer();
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        indicator.setVisible(false);

        if (renderer == null)
        {
            renderer = new StarMapEntityRenderer(Minecraft.getMinecraft());
        }
        if (mc.entityRenderer != renderer)
        {
            prevRenderer = mc.entityRenderer;
            mc.entityRenderer = renderer;
            renderer.setStarMap(machine);
        }

        AddMainPlayerSlots(inventorySlots, this,"holo_with_BG",new GuiColor(Reference.COLOR_HOLO.getIntR()/3,Reference.COLOR_HOLO.getIntG()/3,Reference.COLOR_HOLO.getIntB()/3),45,ySize - 104);
        AddHotbarPlayerSlots(inventorySlots, this,"holo_with_BG",new GuiColor(Reference.COLOR_HOLO.getIntR()/2,Reference.COLOR_HOLO.getIntG()/2,Reference.COLOR_HOLO.getIntB()/2),45,ySize - 25);
    }

    public void onGuiClosed()
    {
        if (prevRenderer != null && mc.entityRenderer != prevRenderer)
        {
            mc.entityRenderer = prevRenderer;
            prevRenderer = null;
        }

        machine.SyncCommandsToServer();
    }

    @Override
    protected void updateElementInformation()
    {
        if (machine.getZoomLevel() != currentPage)
        {
            setPage(machine.getZoomLevel());
        }
        super.updateElementInformation();
        pageButtons.get(4).setVisible(machine.getPlanet() != null);
        pageButtons.get(3).setVisible(machine.getPlanet() != null);
        pageButtons.get(2).setVisible(machine.getStar() != null);
        pageButtons.get(1).setVisible(machine.getQuadrant() != null);

    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }
}
