package matteroverdrive.client.render.tileentity.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRendererPlanet extends StarMapRendererAbstract {

    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float viewerDistance)
    {
        if (spaceBody instanceof Planet) {

            Planet planet = (Planet)spaceBody;
            float size = Math.min(Math.max(planet.getSize(),1f),2.2f) * 0.5f;
            glPushMatrix();
            glRotated(10, 1, 0, 0);

            //Minecraft.getMinecraft().renderEngine.bindTexture(BiostatRendererShield.forcefield_plasma_tex);
            glRotated(Minecraft.getMinecraft().theWorld.getWorldTime() * 0.1, 0, 1, 0);
            glPolygonMode(GL_FRONT, GL_LINE);

            glEnable(GL_CULL_FACE);
            glPushMatrix();
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glDisable(GL_TEXTURE_2D);
            //RenderUtils.applyColorWithMultipy(getPlanetColor(planet), 0.8f * (1f / viewerDistance));
            //glPushMatrix();
            //glScaled(size * 2, size * 2, size * 2);
            //sphere_model.renderAll();
            glDepthMask(true);
            glColor3f(0,0,0);
            sphere.draw(size * 0.99f, 64, 32);
            glDepthMask(false);
            //glPopMatrix();

            glPushMatrix();
            glRotated(90, 1, 0, 0);

            RenderUtils.applyColorWithMultipy(getPlanetColor(planet), 0.2f * (1f / viewerDistance));
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            sphere.draw(size, 64, 32);
            glPopMatrix();

            //disk.draw(1.5f,1.9f,30,1);
            drawBuildings(planet,size,viewerDistance);
            drawShips(planet,size,viewerDistance);
            glPopMatrix();
            glDisable(GL_CULL_FACE);

            glPopMatrix();

            glPushMatrix();
            drawPlanetInfoClose(planet);
            glPopMatrix();

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glEnable(GL_TEXTURE_2D);
        }
    }

    private void drawBuildings(Planet planet,float planetSize,float viewerDistance)
    {
        random.setSeed(planet.getId() + planet.getStar().getId() + planet.getStar().getQuadrant().getId());
        for (int i = 0;i < planet.getBuildings().size();i++)
        {
            glPushMatrix();
            glRotated(random.nextDouble() * 360,0,1,0);
            glRotated(random.nextDouble() * 360, 0, 0, 1);
            glTranslated(planetSize - 0.04, 0, 0);
            RenderUtils.drawCube(0.1, 0.1, 0.1, Reference.COLOR_HOLO, (1f / viewerDistance));
            glPopMatrix();
        }
    }

    private void drawShips(Planet planet,float planetSize,float viewerDistance)
    {
        RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, (1f / viewerDistance));
        for (int i = 0; i < planet.getFleet().size();i++)
        {
            glPushMatrix();
            double startingAngle = random.nextDouble() * 320;
            double direction = random.nextDouble() * 2 - 1;
            double radius = random.nextDouble() * 0.3 + 0.1;
            glRotated(random.nextDouble() * 360,0,0,1);
            glRotated(startingAngle + Math.copySign(Minecraft.getMinecraft().theWorld.getWorldTime() * 0.2, direction), 0, 1, 0);
            glTranslated(planetSize + radius, 0, 0);
            glRotated(90, 0, -Math.copySign(1, direction), 0);
            RenderUtils.drawShip(0,0,0,0.03);
            glPopMatrix();
        }
    }

    private void drawPlanetInfoClose(Planet planet)
    {
        RenderUtils.rotateTo(Minecraft.getMinecraft().renderViewEntity);
        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        if (planet.isOwner(Minecraft.getMinecraft().thePlayer))
        {
            glPushMatrix();
            glScaled(0.01, 0.01, 0.01);
            glRotated(180, 0, 0, 1);
            for (int i = 0;i < planet.getBuildings().size();i++)
            {
                double angle =  14 * i - 7 * planet.getBuildings().size();
                angle *= (Math.PI / 180);
                int x = (int) (Math.cos(angle) * 180 * planet.getSize()) - 10;
                int y = (int) (Math.sin(angle) * 180 * planet.getSize()) - 10;
                RenderUtils.renderStack(x, y, planet.getBuildings().get(i));
                Minecraft.getMinecraft().fontRenderer.drawString(planet.getBuildings().get(i).getDisplayName(), x + 21, y + 6, Reference.COLOR_HOLO.getColor());

            }
            glPopMatrix();
        }
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks, float opacity)
    {
        if (spaceBody instanceof Planet) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE,GL_ONE);

            Planet planet = (Planet)spaceBody;
            int itemCount = 0;
            for (int i = 0; i < planet.getSizeInventory(); i++)
            {
                if (planet.getStackInSlot(i) != null) {
                    ItemStack stack = planet.getStackInSlot(i);
                    if (stack.getItem() instanceof IBuilding) {
                        RenderUtils.renderStack(0, 0 - itemCount * 18 - 21, stack);
                        glEnable(GL_BLEND);
                        glBlendFunc(GL_ONE, GL_ONE);

                        RenderUtils.drawString(String.format("%1$s - %2$s", stack.getDisplayName(), DecimalFormat.getPercentInstance().format((double)((IBuilding) stack.getItem()).getBuildTime(stack) / (double)((IBuilding) stack.getItem()).maxBuildTime(stack,planet))), 0 + 18, 5 - itemCount * 18 - 21, Reference.COLOR_HOLO, opacity);
                    }
                    itemCount++;
                }
            }

            int x = 0;
            int factoryCount = planet.getFactoryCount();
            GuiColor color = Reference.COLOR_HOLO;
            if (factoryCount <= 0)
                color = Reference.COLOR_HOLO_RED;

            RenderUtils.applyColorWithMultipy(color, opacity);
            ClientProxy.holoIcons.renderIcon("holo_factory", x, 0);
            String factoryInfo = String.format("%1$s/%2$s", factoryCount, planet.getBuildingSpaces());
            x += 18;
            RenderUtils.drawString(factoryInfo, x, 6, color, opacity);

            int fleetCount = planet.getFleetCount();
            color = Reference.COLOR_HOLO;
            if (fleetCount <= 0)
                color = Reference.COLOR_HOLO_RED;

            String fleetInfo = String.format("%1$s/%2$s", fleetCount, planet.getFleetSpaces());
            RenderUtils.applyColorWithMultipy(color, opacity);
            x += fontRenderer.getStringWidth(factoryInfo) + 8;
            ClientProxy.holoIcons.renderIcon("icon_shuttle", x, 0);
            x+= 18;
            RenderUtils.drawString(fleetInfo, x, 6, color, opacity);

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
            x += fontRenderer.getStringWidth(fleetInfo) + 8;
            ClientProxy.holoIcons.renderIcon("icon_size", x, 0);
            x+=18;
            RenderUtils.drawString(DecimalFormat.getPercentInstance().format(planet.getSize()), x, 6, Reference.COLOR_HOLO, opacity);
        }
    }

    @Override
    public double getHologramHeight() {
        return 1.5;
    }

    public GuiColor getPlanetColor(Planet planet)
    {
        if (planet.hasOwner()) {
            if (planet.getOwnerUUID().equals(EntityPlayer.func_146094_a(Minecraft.getMinecraft().thePlayer.getGameProfile()))) {
                if (planet.isHomeworld())
                {
                    return Reference.COLOR_HOLO_GREEN;
                }else
                {
                    return Reference.COLOR_HOLO_YELLOW;
                }
            }else
            {
                return Reference.COLOR_HOLO_RED;
            }
        }else
        {
            return Reference.COLOR_HOLO;
        }
    }
}
