package com.MO.MatterOverdrive.client.render;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.MO.MatterOverdrive.tile.TileEntityAndroidStation;
import com.MO.MatterOverdrive.util.RenderUtils;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Simeon on 5/27/2015.
 */
public abstract class TileEntityRendererStation<T extends MOTileEntityMachine> extends TileEntitySpecialRenderer
{
    public static ResourceLocation glowTexture = new ResourceLocation(Reference.PATH_FX + "blueshaft1.png");
    ResourceLocation holo_shader_vert_loc = new ResourceLocation(Reference.PATH_SHADERS + "holo_shader.vert");
    ResourceLocation holo_shader_frag_loc = new ResourceLocation(Reference.PATH_SHADERS + "holo_shader.frag");
    String holo_shader_vert;
    String holo_shader_frag;
    protected int shaderProgram;
    protected boolean validShader = true;
    int vertexShader;
    int fragmentShader;
    Random fliker;

    protected GuiColor holoColor;
    protected GuiColor red_holoColor;

    public TileEntityRendererStation()
    {
        holoColor = new GuiColor(Reference.COLOR_HOLO.getIntR() / 8, Reference.COLOR_HOLO.getIntG() / 8, Reference.COLOR_HOLO.getIntB() / 8);
        red_holoColor = new GuiColor(Reference.COLOR_HOLO_RED.getIntR() / 8,Reference.COLOR_HOLO_RED.getIntG() / 8,Reference.COLOR_HOLO_RED.getIntB() / 8);

        shaderProgram = glCreateProgram();
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        fliker = new Random();

        try {
            InputStream descriptionStream = Minecraft.getMinecraft().getResourceManager().getResource(holo_shader_vert_loc).getInputStream();
            holo_shader_vert = IOUtils.toString(descriptionStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream descriptionStream = Minecraft.getMinecraft().getResourceManager().getResource(holo_shader_frag_loc).getInputStream();
            holo_shader_frag = IOUtils.toString(descriptionStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(vertexShader, holo_shader_vert);
        glCompileShader(vertexShader);

        glShaderSource(fragmentShader, holo_shader_frag);
        glCompileShader(fragmentShader);

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);

        glLinkProgram(shaderProgram);
        if (glGetProgrami(vertexShader, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println("Could not link shader");
            System.out.println(glGetProgramInfoLog(vertexShader, glGetProgrami(vertexShader, GL_INFO_LOG_LENGTH)));
            validShader = false;
        }

        glValidateProgram(shaderProgram);
        if (glGetProgrami(vertexShader, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.out.println("Could not validate shader");
            System.out.println(glGetProgramInfoLog(vertexShader, glGetProgrami(vertexShader, GL_INFO_LOG_LENGTH)));
            validShader = false;
        }

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("Could not compile shader");
            validShader = false;
        }
    }

    private void drawHoloLights(TileEntity entity,World world, double x, double y, double z,double t) {
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        RenderUtils.disableLightmap();
        glDisable(GL_CULL_FACE);
        double mul = Math.sin(world.getWorldTime() * 0.5 + t * 10);
        double flikkerInt = 0.01;

        glColor3d(getHoloColor(entity).getFloatR() + flikkerInt * mul, getHoloColor(entity).getFloatG() + flikkerInt * mul, getHoloColor(entity).getFloatB() + flikkerInt * mul);
        Minecraft.getMinecraft().renderEngine.bindTexture(glowTexture);

        double rot = 8;
        double of = -0.0;
        double height = 9f * (1f / 16f);
        double size = 1.5;
        double halfSize = size / 2;

        glPushMatrix();
        glTranslated(x + 0.5, y + height, z);
        glRotated(180, 0, 0, 1);
        glRotated(rot, 1, 0, 0);
        glTranslated(-halfSize, -size, 0);
        RenderUtils.drawPlane(size);
        glPopMatrix();

        glPushMatrix();
        glTranslated(x + of, y + height, z + 0.5);
        glRotated(90, 0, 1, 0);
        glRotated(180, 0, 0, 1);
        glRotated(rot, 1, 0, 0);
        glTranslated(-halfSize, -size, 0);
        RenderUtils.drawPlane(size);
        glPopMatrix();

        glPushMatrix();
        glTranslated(x + 0.5, y + height, z + (1 - of));
        glRotated(180, 0, 1, 0);
        glRotated(180, 0, 0, 1);
        glRotated(rot, 1, 0, 0);
        glTranslated(-halfSize, -size, 0);
        RenderUtils.drawPlane(size);
        glPopMatrix();

        glPushMatrix();
        glTranslated(x + (1 - of), y + height, z + 0.5);
        glRotated(-90, 0, 1, 0);
        glRotated(180, 0, 0, 1);
        glRotated(rot, 1, 0, 0);
        glTranslated(-halfSize, -size, 0);
        RenderUtils.drawPlane(size);
        glPopMatrix();

        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
    }

    protected GuiColor getHoloColor(TileEntity entity)
    {
        if (((MOTileEntityMachine)entity).isUseableByPlayer(Minecraft.getMinecraft().thePlayer))
        {
            return holoColor;
        }
        return red_holoColor;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        double t = MOMathHelper.noise(tileEntity.xCoord * 0.3, tileEntity.yCoord * 0.3, tileEntity.zCoord * 0.3);

        try {
            glPushMatrix();


            glEnable(GL_BLEND);
            glEnable(GL_ALPHA_TEST);
            glBlendFunc(GL_ONE, GL_ONE);
            glDisable(GL_ALPHA_TEST);

            try {
                renderHologram((T) tileEntity, x, y, z, ticks, t);
            } catch (ClassCastException e) {
                System.out.println("Could not cast to desired station class");
                e.printStackTrace();
            }
            glDisable(GL_BLEND);
            glEnable(GL_ALPHA_TEST);
            glPopMatrix();

        }
        catch (Exception e)
        {

        }

        drawHoloLights(tileEntity,tileEntity.getWorldObj(), x, y, z,ticks);
    }

    protected void beginHolo(T tileEntity)
    {
        if (validShader) {
            glUseProgram(shaderProgram);
            glUniform4f(0, getHoloColor(tileEntity).getFloatR(),getHoloColor(tileEntity).getFloatG(),getHoloColor(tileEntity).getFloatB(), 0);
        }
    }

    protected void endHolo()
    {
        glUseProgram(0);
    }

    protected void rotate(T station,double noise)
    {
        glRotated((Minecraft.getMinecraft().theWorld.getWorldTime() * 0.5) + (1800 * noise),0,-1,0);
    }

    protected boolean isUsable(T station)
    {
        return (station).isUseableByPlayer(Minecraft.getMinecraft().thePlayer);
    }

    protected void renderHologram(T station, double x, double y, double z,float partialTicks,double noise)
    {
        if (!isUsable(station))
        {
            glPushMatrix();
            glTranslated(x + 0.5,y + 0.8,z + 0.5);
            rotate(station,noise);

            glDisable(GL_CULL_FACE);
            glScaled(0.02,0.02,0.02);
            glRotated(180,1,0,0);

            GuiColor color = new GuiColor(Reference.COLOR_HOLO_RED.getIntR() / 80,Reference.COLOR_HOLO_RED.getIntG() / 80,Reference.COLOR_HOLO_RED.getIntB() / 80);
            String info = "ACCESS";
            int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(info);
            glPushMatrix();
            glTranslated(-width/2,-32,0);
            Minecraft.getMinecraft().fontRenderer.drawString(info, 0, 0, color.getColor());
            glPopMatrix();
            glPushMatrix();
            info = "DENIED";
            width = Minecraft.getMinecraft().fontRenderer.getStringWidth(info);
            glTranslated(-width/2,-32,0);
            Minecraft.getMinecraft().fontRenderer.drawString(info, 0, 10, color.getColor());
            glPopMatrix();
            glEnable(GL_CULL_FACE);
            glPopMatrix();
        }
    }
}
