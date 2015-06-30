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

    public EntityRendererFailedPig(ModelBase p_i1265_1_, ModelBase p_i1265_2_, float p_i1265_3_)
    {
        super(p_i1265_1_, p_i1265_2_, p_i1265_3_);
    }

    protected ResourceLocation getEntityTexture(EntityPig p_110775_1_)
    {
        return pig_texture;
    }
}
