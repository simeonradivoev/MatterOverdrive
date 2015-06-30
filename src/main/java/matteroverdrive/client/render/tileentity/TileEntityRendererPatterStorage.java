package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.TileEntityMachinePatternStorage;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/19/2015.
 */
public class TileEntityRendererPatterStorage extends TileEntitySpecialRenderer
{
    public static final String MODEL_PATH = Reference.PATH_MODEL_BLOCKS + "pattern_storage.obj";
    private IModelCustom model;
    private ResourceLocation texture;
    private ResourceLocation ventTexture;
    EntityItem itemEntity;

    public TileEntityRendererPatterStorage()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_storage.png");
        ventTexture = new ResourceLocation(Reference.PATH_BLOCKS + "vent.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL_PATH));
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float ticks)
    {
        TileEntityMachinePatternStorage patternStorage = (TileEntityMachinePatternStorage)entity;
        if(patternStorage != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5f, y + 0.5f, z + 0.5f);
            rotate(patternStorage.getWorldObj(), patternStorage.xCoord, patternStorage.yCoord, patternStorage.zCoord);
            bindTexture(texture);

            for (int i = 0; i < patternStorage.pattern_storage_slots.length;i++)
            {
                ItemStack drive = patternStorage.getStackInSlot(patternStorage.pattern_storage_slots[i]);
                if(drive != null)
                {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(i >= 3 ? -0.3f : 0.3f,0.1f - 0.2f * (i % 3),-0.2f);
                    model.renderPart("drive");
                    GL11.glPopMatrix();
                }
            }


            //model.renderPart("Front");
            model.renderPart("pattern_storage");
            bindTexture(ventTexture);
            model.renderPart("Vents");
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
    }
}
