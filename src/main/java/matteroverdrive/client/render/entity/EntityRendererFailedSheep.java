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

    public EntityRendererFailedSheep(ModelBase p_i1266_1_, ModelBase p_i1266_2_, float p_i1266_3_)
    {
        super(p_i1266_1_, p_i1266_2_, p_i1266_3_);
    }

    protected ResourceLocation getEntityTexture(EntitySheep p_110775_1_)
    {
        return shearedSheepTextures;
    }
}
