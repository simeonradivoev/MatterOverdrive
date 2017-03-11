/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.client.model.ModelHulkingScientist;
import matteroverdrive.entity.monster.EntityMutantScientist;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 11/27/2015.
 */
public class EntityRendererMutantScientist extends RenderBiped<EntityMutantScientist>
{

	private final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "hulking_scinetist.png");

	public EntityRendererMutantScientist(RenderManager renderManager)
	{
		super(renderManager, new ModelHulkingScientist(), 0, 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMutantScientist entity)
	{
		return texture;
	}

}
