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

package matteroverdrive.client.model;

import matteroverdrive.client.data.Color;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 11/26/2015.
 */
public class MOModelRenderColored extends ModelRenderer
{
    boolean disableLighting;
    Color color;
    public MOModelRenderColored(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_)
    {
        super(p_i1174_1_, p_i1174_2_, p_i1174_3_);
    }

    @SideOnly(Side.CLIENT)
    public void render(float p_78785_1_)
    {
        if (disableLighting)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderUtils.disableLightmap();
        }

        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        RenderUtils.applyColor(color);
        super.render(p_78785_1_);
        GL11.glPopAttrib();
        if (disableLighting)
        {
            GL11.glEnable(GL11.GL_LIGHTING);
            RenderUtils.enableLightmap();
        }
    }

    public void setDisableLighting(boolean disableLighting)
    {
        this.disableLighting = disableLighting;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }
}
