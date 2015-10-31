package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedCow extends RenderCow {

    private static final ResourceLocation cowTextures = new ResourceLocation(Reference.PATH_ENTITIES + "failed_cow.png");

    public EntityRendererFailedCow(ModelBase model, float f)
    {
        super(model, f);
    }

    protected ResourceLocation getEntityTexture(EntityCow entity)
    {
        return cowTextures;
    }
}
