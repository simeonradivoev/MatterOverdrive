package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import com.MO.MatterOverdrive.util.RenderUtils;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Random;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TileEntityRendererWeaponStation extends TileEntitySpecialRenderer {
    EntityItem itemEntity;
    ResourceLocation glowTexture = new ResourceLocation(Reference.PATH_FX + "blueshaft1.png");
    ResourceLocation holo_shader_vert_loc = new ResourceLocation(Reference.PATH_SHADERS + "holo_shader.vert");
    ResourceLocation holo_shader_frag_loc = new ResourceLocation(Reference.PATH_SHADERS + "holo_shader.frag");
    String holo_shader_vert;
    String holo_shader_frag;
    int shaderProgram;
    int vertexShader;
    int fragmentShader;
    Random fliker;

    public TileEntityRendererWeaponStation() {
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
        }

        glValidateProgram(shaderProgram);
        if (glGetProgrami(vertexShader, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.out.println("Could not validate shader");
            System.out.println(glGetProgramInfoLog(vertexShader, glGetProgrami(vertexShader, GL_INFO_LOG_LENGTH)));
        }

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("Could not compile shader");
        }
        //glShaderSource(fragmentShader,holo_shader_frag);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks) {
        if (tileEntity instanceof TileEntityWeaponStation) {
            TileEntityWeaponStation weaponStation = (TileEntityWeaponStation) tileEntity;
            double t = MOMathHelper.noise(weaponStation.xCoord * 0.3, weaponStation.yCoord * 0.3, weaponStation.zCoord * 0.3);

            glPushMatrix();
            glUseProgram(shaderProgram);
            glEnable(GL_BLEND);
            glEnable(GL_ALPHA_TEST);
            glBlendFunc(GL_ONE, GL_ONE);
            glDisable(GL_ALPHA_TEST);
            glUniform4f(0, Reference.COLOR_HOLO.getFloatR() * 0.3f, Reference.COLOR_HOLO.getFloatG() * 0.3f, Reference.COLOR_HOLO.getFloatB() * 0.3f, 0);

            renderItem(weaponStation, x, y, z,ticks,t);
            glDisable(GL_BLEND);
            glEnable(GL_ALPHA_TEST);
            glPopMatrix();
            glUseProgram(0);

            drawHoloLights(weaponStation, x, y, z,t);
        }
    }

    private void drawHoloLights(TileEntityWeaponStation weaponStation, double x, double y, double z,double t) {
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        RenderUtils.disableLightmap();
        glDisable(GL_CULL_FACE);
        double mul = Math.sin(weaponStation.getWorldObj().getWorldTime() * 0.5 + t * 10);
        double flikkerInt = 0.01;

        glColor3d(0.1 + flikkerInt * mul, 0.1 + flikkerInt * mul, 0.1 + flikkerInt * mul);
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

    private void renderItem(TileEntityWeaponStation weaponStation, double x, double y, double z,float ticks,double t)
    {
        ItemStack stack = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT);
        if (stack != null) {
            if (itemEntity == null) {
                itemEntity = new EntityItem(weaponStation.getWorldObj(), weaponStation.xCoord, weaponStation.yCoord, weaponStation.zCoord, stack);
            } else if (!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack)) {
                itemEntity.setEntityItemStack(stack);
            }

            int metadata = weaponStation.getWorldObj().getBlockMetadata(weaponStation.xCoord, weaponStation.yCoord, weaponStation.zCoord);
            ForgeDirection direction = ForgeDirection.values()[metadata];

            itemEntity.hoverStart = weaponStation.getWorldObj().getWorldTime() * 0.05f + (float)t * 10;
            RenderManager.instance.func_147939_a(itemEntity, x + 0.5f, y + 0.8f, z + 0.5f, 0, 0, true);
        }
    }
}

