package matteroverdrive.client.render.tileentity;

import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/19/2015.
 */
public class TileEntityRendererReplicator extends TileEntitySpecialRenderer<TileEntityMachineReplicator>
{
    EntityItem itemEntity;

    @Override
    public void renderTileEntityAt(TileEntityMachineReplicator replicator, double x, double y, double z, float ticks,int destoryStage)
    {
        GlStateManager.pushMatrix();
        renderItem(replicator, x, y, z);
        GlStateManager.popMatrix();
    }

    private void renderItem(TileEntityMachineReplicator replicator,double x,double y,double z)
    {
        ItemStack stack = replicator.getStackInSlot(replicator.OUTPUT_SLOT_ID);
        if(stack != null)
        {
            if(itemEntity == null)
            {
                itemEntity = new EntityItem(replicator.getWorld(), x, y, z, stack);
            }
            else if(!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack))
            {
                itemEntity.setEntityItemStack(stack);
            }

            itemEntity.hoverStart = (float) (Math.PI/2);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(itemEntity, x + 0.5d, y + 0.05, z + 0.5, 0, 0);
        }
    }
}
