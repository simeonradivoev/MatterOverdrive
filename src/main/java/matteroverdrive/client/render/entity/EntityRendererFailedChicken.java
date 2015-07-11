package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedChicken extends RenderChicken
{
    private static final ResourceLocation chickenTextures = new ResourceLocation(Reference.PATH_ENTITIES + "failed_chicken.png");

    public EntityRendererFailedChicken(ModelBase p_i1252_1_, float p_i1252_2_) {
        super(p_i1252_1_, p_i1252_2_);
    }

    protected ResourceLocation getEntityTexture(EntityChicken p_110775_1_)
    {
        return chickenTextures;
    }
}
