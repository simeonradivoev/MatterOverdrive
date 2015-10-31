package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedPig extends RenderPig
{
    public static final ResourceLocation pig_texture = new ResourceLocation(Reference.PATH_ENTITIES + "failed_pig.png");

    public EntityRendererFailedPig(ModelBase model1, ModelBase model2, float f)
    {
        super(model1, model2, f);
    }

    protected ResourceLocation getEntityTexture(EntityPig entity)
    {
        return pig_texture;
    }
}
