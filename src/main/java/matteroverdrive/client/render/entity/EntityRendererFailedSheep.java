package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/29/2015.
 */
public class EntityRendererFailedSheep extends RenderSheep {

    private static final ResourceLocation shearedSheepTextures = new ResourceLocation(Reference.PATH_ENTITIES + "failed_sheep.png");

    public EntityRendererFailedSheep(ModelBase model1, ModelBase model2, float f)
    {
        super(model1, model2, f);
    }

    protected ResourceLocation getEntityTexture(EntitySheep entity)
    {
        return shearedSheepTextures;
    }
}
