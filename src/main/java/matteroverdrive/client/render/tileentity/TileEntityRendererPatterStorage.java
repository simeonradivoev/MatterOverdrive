package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;

/**
 * Created by Simeon on 3/19/2015.
 */
public class TileEntityRendererPatterStorage extends TileEntitySpecialRenderer<TileEntityMachinePatternStorage>
{
	EntityItem itemEntity;
	private IModel model;
	private ResourceLocation texture;
	private ResourceLocation ventTexture;

	public TileEntityRendererPatterStorage()
	{
		texture = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_storage.png");
		ventTexture = new ResourceLocation(Reference.PATH_BLOCKS + "vent.png");
		try
		{
			model = OBJLoader.INSTANCE.loadModel(new ResourceLocation(Reference.MODEL_PATTERN_STORAGE));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntityMachinePatternStorage patternStorage, double x, double y, double z, float ticks, int destroyStage)
	{
		/*GL11.glPushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        RenderUtils.rotateFromBlock(patternStorage.getWorld(), patternStorage.getPos());
        bindTexture(texture);

        for (int i = 0; i < patternStorage.pattern_storage_slots.length; i++)
        {
            ItemStack drive = patternStorage.getStackInSlot(patternStorage.pattern_storage_slots[i]);
            if(drive != null)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i >= 3 ? -0.3f : 0.3f, 0.1f - 0.2f * (i % 3), -0.2f);
                //model.renderPart("drive");
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();*/
	}
}
