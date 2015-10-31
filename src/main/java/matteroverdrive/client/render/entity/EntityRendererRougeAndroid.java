package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRendererRougeAndroid extends RendererLivingEntity
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "android.png");
    public static final ResourceLocation texture_hologram = new ResourceLocation(Reference.PATH_ENTITIES + "android_holo.png");

    public EntityRendererRougeAndroid(ModelBase modelBase, float f)
    {
        super(modelBase, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (entity.getEntityData().getBoolean("Hologram"))
            return texture_hologram;
        else
            return texture;
    }
}
