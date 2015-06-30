package matteroverdrive.client.render.tileentity.starmap;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import matteroverdrive.Reference;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.starmap.data.SpaceBody;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.util.glu.Sphere;

import java.text.DecimalFormat;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRendererStar extends StarMapRendererAbstract {

    public StarMapRendererStar()
    {

        sphere = new Sphere();
        random = new Random();
    }

    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float distance)
    {
        if (spaceBody instanceof Star) {

            Star star = (Star)spaceBody;
            random.setSeed(star.getSeed());

            double time = Minecraft.getMinecraft().theWorld.getWorldTime();

            glPushMatrix();
            glScaled(star.getSize(), star.getSize(), star.getSize());

            bindTexture(ClientProxy.renderHandler.getRenderParticlesHandler().getAdditiveTextureSheet());
            Tessellator.instance.startDrawingQuads();
            RenderUtils.tessalateParticle(Minecraft.getMinecraft().renderViewEntity, star_icon, star.getSize(), Vec3.createVectorHelper(0, 0, 0), Reference.COLOR_HOLO_YELLOW.getFloatR() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatG() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatB() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatA() * 0.1f);
            Tessellator.instance.draw();

            RenderUtils.applyColorWithMultipy(new GuiColor(star.getColor()), 0.25f * (1f / distance));
            glPolygonMode(GL_FRONT, GL_LINE);
            glDisable(GL_TEXTURE_2D);
            double s = 0.9 + Math.sin(time * 0.01) * 0.1;
            glScaled(s, s, s);
            sphere_model.renderAll();
            glPolygonMode(GL_FRONT, GL_POINT);
            glPointSize(10 / (float) Math.max(0.1, distance));

            sphere_model.renderAll();
            if (Minecraft.getMinecraft().theWorld.getWorldTime() % 120 > 80) {
                double t = ((Minecraft.getMinecraft().theWorld.getWorldTime() % 120) - 80) / 40d;
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO_YELLOW, (float) MOMathHelper.easeIn(1 - t, 0, 0.1, 1));
                s = MOMathHelper.easeIn(t, 0.0, 10, 1);
                glScaled(1 + s, 1 + s, 1 + s);
                sphere_model.renderAll();
            }
            glPopMatrix();
            glPolygonMode(GL_FRONT, GL_LINE);

            int planetID = 0;
            for (Planet planet : star.getPlanets())
            {
                float sizeMultiply = 1;
                if (starMap.getDestination().equals(planet))
                {
                    sizeMultiply = 1.2f;
                }

                glDisable(GL_ALPHA_TEST);
                GuiColor planetColor = Planet.getGuiColor(planet);
                random.setSeed(planet.getSeed());

                glPushMatrix();
                double axisRotation = random.nextInt(30) - 15;
                glRotated(axisRotation, 1, 0, 0);
                double radius = planet.getOrbit() * 2 + (star.getSize()/2 + 0.1);
                float planetSize = planet.getSize();
                drawPlanetOrbit(planet, radius);

                glTranslated(Math.sin(time * 0.001 + 10 * planetID) * radius, 0, Math.cos(time * 0.001 + 10 * planetID) * radius);

                glPolygonMode(GL_FRONT, GL_FILL);
                glEnable(GL_TEXTURE_2D);

                if (starMap.getDestination().equals(planet))
                {
                    bindTexture(ClientProxy.renderHandler.getRenderParticlesHandler().getAdditiveTextureSheet());
                    Tessellator.instance.startDrawingQuads();
                    RenderUtils.tessalateParticle(Minecraft.getMinecraft().renderViewEntity, selectedIcon, planet.getSize() * 0.15f * sizeMultiply, Vec3.createVectorHelper(0, 0, 0), planetColor);
                    Tessellator.instance.draw();
                }

                if (starMap.getGalaxyPosition().equals(planet))
                {
                    bindTexture(ClientProxy.renderHandler.getRenderParticlesHandler().getAdditiveTextureSheet());
                    Tessellator.instance.startDrawingQuads();
                    RenderUtils.tessalateParticle(Minecraft.getMinecraft().renderViewEntity, currentIcon, planet.getSize() * 0.25f, Vec3.createVectorHelper(0, 0, 0), planetColor);
                    Tessellator.instance.draw();
                }

                glPushMatrix();
                glRotated(-axisRotation, 1, 0, 0);
                RenderUtils.rotateTo(Minecraft.getMinecraft().renderViewEntity);
                drawPlanetInfo(planet);
                glPopMatrix();
                glPolygonMode(GL_FRONT, GL_LINE);
                glDisable(GL_TEXTURE_2D);

                RenderUtils.applyColorWithMultipy(planetColor, 0.3f * (1f / distance));
                glRotated(100, 1, 0, 0);
                glRotated(time * 2, 0, 0, 1);
                sphere.draw(planetSize * 0.1f * sizeMultiply, (int)(16 + planetSize * 2), (int)(8 + planetSize * 2));
                planetID++;
                glPopMatrix();
            }
            glEnable(GL_TEXTURE_2D);
            glPolygonMode(GL_FRONT, GL_FILL);
        }
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks,float opacity)
    {
        if (spaceBody instanceof Star) {
            Star star = (Star)spaceBody;
            ClientProxy.holoIcons.bindSheet();
            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
            glEnable(GL_ALPHA_TEST);
            RenderHelper.renderIcon(82,-30,0,ClientProxy.holoIcons.getIcon("icon_size"),16,16);
            //RenderUtils.drawString(star.getName(), 82, -30, Reference.COLOR_HOLO,opacity);
            RenderUtils.drawString(DecimalFormat.getPercentInstance().format(star.getSize()), 102, -25, Reference.COLOR_HOLO,opacity);

            glDisable(GL_TEXTURE_2D);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            Tessellator.instance.startDrawingQuads();
            for (int i = 0; i < 10; i++) {
                double step = 64d / 10d;
                double x = step * i;
                double y = -10;
                double height = 64 * (0.5 * random.nextGaussian() + 1d) / 2d;
                Tessellator.instance.addVertex(x, y, 0);
                Tessellator.instance.addVertex(x + step - 1, y, 0);
                Tessellator.instance.addVertex(x + step - 1, y - height, 0);
                Tessellator.instance.addVertex(x, y - height, 0);
            }
            Tessellator.instance.draw();
            glEnable(GL_TEXTURE_2D);
        }
    }

    private void drawPlanetInfo(Planet planet)
    {
        glTranslated(0, planet.getSize() * 0.13f + 0.05f, 0);
        glScaled(0.005, 0.005, 0.005);
        glRotated(180, 0, 0, 1);

        if (planet.hasOwner())
        {
            if (planet.isOwner(Minecraft.getMinecraft().thePlayer))
            {
                int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(planet.getName());
                Minecraft.getMinecraft().fontRenderer.drawString(planet.getName(), -width / 2, 0, Planet.getGuiColor(planet).getColor());

                if (planet.isHomeworld())
                {
                    width = Minecraft.getMinecraft().fontRenderer.getStringWidth("[Home]");
                    Minecraft.getMinecraft().fontRenderer.drawString(EnumChatFormatting.GOLD + "[Home]", -width / 2, -10, 0xFFFFFF);
                }
            }
            else
            {
                int width = Minecraft.getMinecraft().standardGalacticFontRenderer.getStringWidth(planet.getName());
                Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(planet.getName(), -width / 2, 0, Planet.getGuiColor(planet).getColor());

                EntityPlayer owner = Minecraft.getMinecraft().theWorld.func_152378_a(planet.getOwnerUUID());
                if (owner != null)
                {
                    String info = String.format("[%s]",owner.getDisplayName());
                    width = Minecraft.getMinecraft().fontRenderer.getStringWidth(info);
                    Minecraft.getMinecraft().fontRenderer.drawString(EnumChatFormatting.GOLD + info, -width / 2, -10, 0xFFFFFF);
                }
            }
        }else
        {
            int width = Minecraft.getMinecraft().standardGalacticFontRenderer.getStringWidth(planet.getName());
            Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(planet.getName(), -width / 2, 0, Planet.getGuiColor(planet).getColor());
        }
    }

    private void drawPlanetOrbit(Planet planet,double radius)
    {
        glDisable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_LINE);
        Tessellator.instance.startDrawing(GL_LINES);
        Tessellator.instance.setColorRGBA_F(Planet.getGuiColor(planet).getFloatR() * 0.1f,Planet.getGuiColor(planet).getFloatG()* 0.1f,Planet.getGuiColor(planet).getFloatB()* 0.1f,Planet.getGuiColor(planet).getFloatA()* 0.1f);
        for (int i = 0;i < 32;i++)
        {
            double angleStep = (Math.PI*2) / 32;
            Tessellator.instance.addVertex(Math.sin(angleStep * i) * radius,0,Math.cos(angleStep * i) * radius);
            Tessellator.instance.addVertex(Math.sin(angleStep * (i+1)) * radius,0,Math.cos(angleStep * (i+1)) * radius);
        }
        Tessellator.instance.draw();
    }



    private void bindTexture(ResourceLocation location)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(location);
    }

    @Override
    public double getHologramHeight() {
        return 1.5;
    }
}
