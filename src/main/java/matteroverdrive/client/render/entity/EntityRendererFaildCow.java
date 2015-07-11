package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFaildCow extends RenderCow {

    private static final ResourceLocation cowTextures = new ResourceLocation(Reference.PATH_ENTITIES + "failed_cow.png");
    public EntityRendererFaildCow(ModelBase p_i1253_1_, float p_i1253_2_)
    {
        super(p_i1253_1_, p_i1253_2_);
    }

    protected ResourceLocation getEntityTexture(EntityCow p_110775_1_)
    {
        return cowTextures;
    }
}
