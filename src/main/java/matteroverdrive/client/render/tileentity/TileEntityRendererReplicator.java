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
    EntityItem itemEntity;

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float ticks)
    {
        TileEntityMachineReplicator replicator = (TileEntityMachineReplicator)entity;
        if(replicator != null)
        {
            GL11.glPushMatrix();
            renderItem(replicator, x, y, z);
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
                itemEntity = new EntityItem(replicator.getWorldObj(), x, y, z, stack);
            }
            else if(!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack))
            {
                itemEntity.setEntityItemStack(stack);
            }

            itemEntity.hoverStart = (float)(Math.PI / 2);

            RenderManager.instance.renderEntityWithPosYaw(itemEntity, x + 0.5d, y + 0.25, z + 0.5, 0, 0);
        }
    }
}
