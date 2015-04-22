package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityRendererNetworkPipe extends TileEntityRendererPipe
{
    public static final String MODEL_PATH = Reference.PATH_MODEL_BLOCKS + "network_cable.obj";
    private IModelCustom model;
    private static final float SIZE = 0.2258f;

    public TileEntityRendererNetworkPipe()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "network_pipe_tex.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL_PATH));
    }

    @Override
    protected void drawCore(TileEntityPipe tile,double x,
                            double y, double z, float f,int sides)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glScalef(SIZE, SIZE, SIZE);
        
        if(sides == 3)
        {
            GL11.glRotatef(180,1,0,0);
            model.renderPart("cable_straight");
        }
        else if(sides == 12)
        {
            GL11.glRotatef(-90,1,0,0);
            model.renderPart("cable_straight");
        }
        else if(sides == 48)
        {
            GL11.glRotatef(90,0,0,1);
            model.renderPart("cable_straight");
        }
        else
        {
            model.renderPart("cable_junction");
        }
        GL11.glPopMatrix();
    }

    @Override
    protected void drawSide(TileEntityPipe tile,ForgeDirection dir)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f,0.5f,0.5f);
        GL11.glScalef(SIZE, SIZE, SIZE);
        
        float translateScale = 1.475f;
        GL11.glTranslatef(dir.offsetX * translateScale,dir.offsetY * translateScale,dir.offsetZ * translateScale);
        
        
        
        if(dir == ForgeDirection.DOWN)
        {
            GL11.glRotatef(180,1,0,0);
        }
        else if(dir == ForgeDirection.NORTH)
        {
            GL11.glRotatef(-90,1,0,0);
        }
        else if(dir == ForgeDirection.SOUTH)
        {
            GL11.glRotatef(90,1,0,0);
        }
        else if(dir == ForgeDirection.WEST)
        {
            GL11.glRotatef(90,0,0,1);
        }
        else if(dir == ForgeDirection.EAST)
        {
            GL11.glRotatef(-90,0,0,1);
        }

        model.renderPart("cable_straight");
        
        GL11.glPopMatrix();
    }
}
