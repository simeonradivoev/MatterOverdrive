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

package matteroverdrive.gui;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.client.data.Color;
import matteroverdrive.container.ContainerStarMap;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.pages.starmap.*;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.starmap.data.TravelEvent;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import org.lwjgl.util.glu.Project;

import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/12/2015.
 */
public class GuiStarMap extends MOGuiMachine<TileEntityMachineStarMap>
{
    public static ScaleTexture BG = new ScaleTexture(new ResourceLocation(Reference.PATH_GUI + "star_map.png"),255,141).setOffsets(213,34,42,94);
    Minecraft mc;
    IModel sphere;
    PagePlanetMenu planetPage;
    PageGalaxy pageGalaxy;
    PageQuadrant pageQuadrant;
    PageStar pageStar;
    PagePlanetStats pagePlanetStats;

    public GuiStarMap(InventoryPlayer inventoryPlayer, TileEntityMachineStarMap machine)
    {
        super(new ContainerStarMap(inventoryPlayer,machine),machine,480,360);
        mc = Minecraft.getMinecraft();
        try
        {
            sphere = OBJLoader.INSTANCE.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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
        pageGalaxy = new PageGalaxy(this,0,0,width,height,starMap);
        pageGalaxy.setName("Galaxy");
        AddPage(pageGalaxy, ClientProxy.holoIcons.getIcon("page_icon_galaxy"), MOStringHelper.translateToLocal("gui.tooltip.page.galaxy")).setIconColor(Reference.COLOR_MATTER);
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
        super.drawGuiContainerBackgroundLayer(partialTick, x, y);

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);
        if (machine.getActiveSpaceBody() != null)
        {
            Collection<ISpaceBodyHoloRenderer> renderers = ClientProxy.renderHandler.getStarmapRenderRegistry().getStarmapRendererCollection(machine.getActiveSpaceBody().getClass());
            if (renderers != null)
            {
                for (ISpaceBodyHoloRenderer renderer : renderers)
                {
                    if (renderer.displayOnZoom(machine.getZoomLevel(), machine.getActiveSpaceBody()))
                    {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(xSize / 1.9, ySize - 16, 0);
                        if (machine.getActiveSpaceBody() != null)
                        {
                            renderer.renderGUIInfo(GalaxyClient.getInstance().getTheGalaxy(), machine.getActiveSpaceBody(), machine, partialTick, 0.8f);
                        }
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
        GlStateManager.popMatrix();
    }

    public void drawWorldBackground(int p_146270_1_)
    {
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.color(0,0,0);
        RenderUtils.drawPlane(0,0,-1000,width,height);
        GlStateManager.enableTexture2D();
        GlStateManager.pushMatrix();

        //GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        //GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);

        GlStateManager.matrixMode(GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(75f, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, 20);
        //RenderUtil.loadMatrix(camera.getTransposeProjectionMatrix());
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        //RenderUtil.loadMatrix(camera.getTransposeViewMatrix());
        GlStateManager.rotate(15,1,0,0);
        GlStateManager.translate(0, -0.8f, 0);

        float lastRotationYaw = Minecraft.getMinecraft().getRenderViewEntity().rotationYaw;
        float lastRotationPitch = Minecraft.getMinecraft().getRenderViewEntity().rotationPitch;
        float rotation = 0;
        if (machine.getZoomLevel() <= 2)
        {
            rotation = mc.theWorld.getWorldTime() * 0.1f;
        }
        Minecraft.getMinecraft().getRenderViewEntity().rotationYaw = 180 + rotation;
        Minecraft.getMinecraft().getRenderViewEntity().prevRotationPitch = Minecraft.getMinecraft().getRenderViewEntity().rotationPitch = 15;


        //bindTexture(ClientProxy.renderHandler.getRendererOmniTool().getWeaponTexture());
        //ClientProxy.renderHandler.getRendererOmniTool().getModel().renderAll();
        switch (machine.getZoomLevel())
        {
            case 0:
                GlStateManager.translate(0,-1.1,-4f);
                break;
            case 1:
                GlStateManager.translate(0,-0.6,-4f);
                break;
            case 2:
                Star star = machine.getStar();
                float maxDistance = 0;
                if (star != null)
                {
                    for (Planet planet : star.getPlanets())
                    {
                        if (maxDistance < planet.getOrbit())
                        {
                            maxDistance = planet.getOrbit();
                        }
                    }
                }
                GlStateManager.translate(0,0,-maxDistance * 3 - 1.5f);
                break;
            default:
                GlStateManager.translate(0, 0.1f, -3f);
                break;
        }

        GlStateManager.rotate(rotation,0,1,0);
        //GlStateManager.translate(0.5f,2.5f,3);
        ClientProxy.renderHandler.getTileEntityRendererStarMap().render(machine,-0.5f,-1.8f,-0.5f,0);
        GlStateManager.popMatrix();
        //fontRendererObj.drawString("Test",100,100,0xffffff);

        GlStateManager.matrixMode(GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.popMatrix();

        Minecraft.getMinecraft().getRenderViewEntity().rotationYaw = lastRotationYaw;
        Minecraft.getMinecraft().getRenderViewEntity().prevRotationPitch = Minecraft.getMinecraft().getRenderViewEntity().rotationPitch = lastRotationPitch;

        GlStateManager.popAttrib();
    }

    public void onPageChange(int newPage)
    {
        pageQuadrant.init();
        pageStar.init();
        planetPage.init();
        pagePlanetStats.init();
        if (newPage != machine.getZoomLevel())
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketStarMapClientCommands(machine,newPage,machine.getGalaxyPosition(),machine.getDestination()));
        }
    }

    public void onPlanetChange(Planet planet)
    {
        pageStar.init();
        pagePlanetStats.loadShips();
        planetPage.init();
    }

    public void onTravelEventsChange(List<TravelEvent> travelEvents)
    {
        pagePlanetStats.init();
    }

    @Override
    public void initGui()
    {
        this.xSize = width;
        this.ySize = height;
        pageGalaxy.setSize(width,height);
        pageQuadrant.setSize(width,height);
        pageStar.setSize(width,height);
        sidePannel.setPosition(xSize-42,16);

        super.initGui();
        indicator.setVisible(false);

        AddMainPlayerSlots(inventorySlots, this,"holo_with_BG",new Color(Reference.COLOR_HOLO.getIntR()/3,Reference.COLOR_HOLO.getIntG()/3,Reference.COLOR_HOLO.getIntB()/3),45,ySize - 104);
        AddHotbarPlayerSlots(inventorySlots, this,"holo_with_BG",new Color(Reference.COLOR_HOLO.getIntR()/2,Reference.COLOR_HOLO.getIntG()/2,Reference.COLOR_HOLO.getIntB()/2),45,ySize - 25);
    }

    public void onGuiClosed()
    {
        //machine.SyncCommandsToServer();
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
}
