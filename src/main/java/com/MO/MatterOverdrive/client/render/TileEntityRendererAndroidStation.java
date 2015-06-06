package com.MO.MatterOverdrive.client.render;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.entity.EntityRougeAndroidMob;
import com.MO.MatterOverdrive.tile.TileEntityAndroidStation;
import com.MO.MatterOverdrive.util.RenderUtils;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.SerializationUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Created by Simeon on 5/27/2015.
 */
public class TileEntityRendererAndroidStation extends TileEntityRendererStation<TileEntityAndroidStation>
{
    EntityRougeAndroidMob mob;

    public TileEntityRendererAndroidStation()
    {
        super();
    }

    @Override
    protected void renderHologram(TileEntityAndroidStation station, double x, double y, double z, float partialTicks, double noise)
    {
        if ((station).isUseableByPlayer(Minecraft.getMinecraft().thePlayer)) {
            if (mob == null) {
                mob = new EntityRougeAndroidMob(Minecraft.getMinecraft().theWorld);
                mob.getEntityData().setBoolean("Hologram", true);
            }

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glPushMatrix();
            glTranslated(x + 0.5, y + 0.8, z + 0.5);
            rotate(station,noise);

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.3f);

            if (station.isUseableByPlayer(Minecraft.getMinecraft().thePlayer)) {
                RenderManager.instance.func_147939_a(mob, 0, 0, 0, 0, 0, true);
            }
            glPopMatrix();
        }else
        {
            super.renderHologram(station,x,y,z,partialTicks, noise);
        }
    }
}
