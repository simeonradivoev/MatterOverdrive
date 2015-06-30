package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.entity.EntityRougeAndroidMob;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;

import static org.lwjgl.opengl.GL11.*;

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
