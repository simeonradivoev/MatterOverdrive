package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.TileEntityMachineReplicator;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
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
    private ResourceLocation portTexture;
    EntityItem itemEntity;

    public TileEntityRendererReplicator()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "replicator.png");
        ventTexture = new ResourceLocation(Reference.PATH_BLOCKS + "vent.png");
        baseTexture = new ResourceLocation(Reference.PATH_BLOCKS + "base.png");
        portTexture = new ResourceLocation(Reference.PATH_BLOCKS + "network_port.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL_PATH));
    }



    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float ticks)
    {
        TileEntityMachineReplicator replicator = (TileEntityMachineReplicator)entity;
        if(replicator != null)
        {
            GL11.glPushMatrix();

            renderItem(replicator, x, y, z);

            GL11.glColor3f(1,1,1);
            GL11.glTranslated(x + 0.5f, y + 0.5f, z + 0.5f);
            RenderUtils.rotateFromBlock(replicator.getWorldObj(), replicator.xCoord, replicator.yCoord, replicator.zCoord);
            bindTexture(texture);
            model.renderPart("Front");
            model.renderPart("Inside");
            bindTexture(ventTexture);
            model.renderPart("Vents");
            bindTexture(baseTexture);
            model.renderPart("Shell");
            bindTexture(portTexture);
            model.renderPart("Back");
            GL11.glPopMatrix();
        }
    }

    private void renderItem(TileEntityMachineReplicator replicator,double x,double y,double z)
    {
        ItemStack stack = replicator.getStackInSlot(replicator.OUTPUT_SLOT_ID);
        if(stack != null)
        {
            if(itemEntity == null)
            {
                itemEntity = new EntityItem(replicator.getWorldObj(),x,y,z,stack);
            }
            else if(!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack))
            {
                itemEntity.setEntityItemStack(stack);
            }

            itemEntity.hoverStart = (float)(Math.PI / 2);

            RenderManager.instance.renderEntityWithPosYaw(itemEntity,x + 0.5d,y + 0.25,z + 0.5,0,0);
        }
    }
}
