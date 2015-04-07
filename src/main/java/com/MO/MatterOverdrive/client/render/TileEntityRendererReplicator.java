package com.MO.MatterOverdrive.client.render;

import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;

import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/19/2015.
 */
public class TileEntityRendererReplicator extends TileEntitySpecialRenderer
{
    public static final String MODEL_PATH = Reference.PATH_MODEL_BLOCKS + "replicator.obj";
    private IModelCustom model;
    private ResourceLocation texture;
    private ResourceLocation ventTexture;
    private ResourceLocation baseTexture;
    EntityItem itemEntity;

    public TileEntityRendererReplicator()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "replicator.png");
        ventTexture = new ResourceLocation(Reference.PATH_BLOCKS + "vent.png");
        baseTexture = new ResourceLocation(Reference.PATH_BLOCKS + "base.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL_PATH));
    }



    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float ticks)
    {
        TileEntityMachineReplicator replicator = (TileEntityMachineReplicator)entity;
        if(replicator != null)
        {
            GL11.glPushMatrix();

            renderItem(replicator,x,y,z);

            GL11.glTranslated(x + 0.5f, y + 0.5f, z + 0.5f);
            rotate(replicator.getWorldObj(), replicator.xCoord, replicator.yCoord, replicator.zCoord);
            bindTexture(texture);
            model.renderPart("Front");
            model.renderPart("Inside");
            bindTexture(ventTexture);
            model.renderPart("Vents");
            bindTexture(baseTexture);
            model.renderPart("Shell");
            GL11.glPopMatrix();
        }
    }

    private void rotate(World world,int x,int y,int z)
    {
        if(world != null) {
            int metadata = world.getBlockMetadata(x, y, z);

            ForgeDirection direction = ForgeDirection.values()[metadata];

            if (direction == ForgeDirection.WEST) {
                GL11.glRotated(-90, 0, 1, 0);
            }
            if (direction == ForgeDirection.EAST) {
                GL11.glRotated(90, 0, 1, 0);
            }
            if (direction == ForgeDirection.NORTH) {
                GL11.glRotated(-180, 0, 1, 0);
            }
        }
        
        //System.out.println("Metadata " + metadata + "at [" + x +","+ y +","+ z + "]");
    }

    private void renderItem(TileEntityMachineReplicator replicator,double x,double y,double z)
    {
        ItemStack stack = replicator.getStackInSlot(replicator.OUTPUT_SLOT_ID);
        if(stack != null)
        {
            if(itemEntity == null || !itemEntity.getEntityItem().isItemEqual(stack))
            {
                itemEntity = new EntityItem(replicator.getWorldObj(),x,y,z,stack);
            }

            RenderManager.instance.renderEntityWithPosYaw(itemEntity,x + 0.5d,y + 0.5,z + 0.5,0,0);
        }
    }
}
