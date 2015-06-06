package com.MO.MatterOverdrive.client;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.client.render.TileEntityRendererGravitationalAnomaly;
import com.MO.MatterOverdrive.client.render.TileEntityRendererStation;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.entity.EntityRougeAndroidMob;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.handler.KeyHandler;
import com.MO.MatterOverdrive.network.packet.server.PacketTeleportPlayer;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.util.MOPhysicsHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/1/2015.
 */
public class RenderBiostatEffects
{
    IModelCustom sphere_model;

    public RenderBiostatEffects()
    {
        sphere_model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
    }

    public void onRenderWorldLast(RenderWorldLastEvent event)
    {

        AndroidPlayer androidPlayer = AndroidPlayer.get(Minecraft.getMinecraft().thePlayer);
        Vec3 playerPos = androidPlayer.getPlayer().getPosition(event.partialTicks);

        if (androidPlayer != null && androidPlayer.isAndroid() && androidPlayer.isUnlocked(AndroidStatRegistry.teleport,AndroidStatRegistry.teleport.maxLevel()) && AndroidStatRegistry.teleport.isEnabled(androidPlayer,0))
        {
            if(ClientProxy.keyHandler.getBinding(KeyHandler.TELEPORT_KEY).getIsKeyPressed())
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
