package com.MO.MatterOverdrive.client.render.biostat;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.client.RenderHandler;
import com.MO.MatterOverdrive.client.render.IWorldLastRenderer;
import com.MO.MatterOverdrive.client.render.tileentity.TileEntityRendererGravitationalAnomaly;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.handler.KeyHandler;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BiostatRendererTeleporter implements IWorldLastRenderer
{
    public void onRenderWorldLast(RenderHandler renderHandler,RenderWorldLastEvent event)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(Minecraft.getMinecraft().thePlayer);
        Vec3 playerPos = androidPlayer.getPlayer().getPosition(event.partialTicks);

        if (androidPlayer != null && androidPlayer.isAndroid() && androidPlayer.isUnlocked(AndroidStatRegistry.teleport,AndroidStatRegistry.teleport.maxLevel()) && AndroidStatRegistry.teleport.isEnabled(androidPlayer,0) && AndroidStatRegistry.teleport.getHasPressedKey())
        {
            if(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed())
            {
                glPushMatrix();
                glEnable(GL_BLEND);
                glBlendFunc(GL_ONE, GL_ONE);
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
                glTranslated(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);

                //mob.rotationYawHead = androidPlayer.getPlayer().rotationYawHead;

                Vec3 pos = AndroidStatRegistry.teleport.getPos(androidPlayer);
                if (pos != null)
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityRendererGravitationalAnomaly.glow);
                    glTranslated(pos.xCoord, pos.yCoord, pos.zCoord);
                    glRotated(androidPlayer.getPlayer().rotationYaw,0,-1,0);
                    glRotated(androidPlayer.getPlayer().rotationPitch,1,0,0);
                    glRotated(Minecraft.getMinecraft().theWorld.getWorldTime() * 10,0,0,1);
                    glTranslated(-0.5,-0.5,0);
                    RenderUtils.drawPlane(1);
                    //RenderManager.instance.func_147939_a(mob,0,0,0,0,0,true);
                }

                glDisable(GL_BLEND);
                glPopMatrix();
            }
        }
    }
}
