package com.MO.MatterOverdrive.client.render.entity;

import com.MO.MatterOverdrive.Reference;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityRendererMadScientist extends RenderVillager
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "mad_scientist.png");

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return texture;
    }
}
