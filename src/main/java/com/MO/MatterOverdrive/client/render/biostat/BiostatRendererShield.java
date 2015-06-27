package com.MO.MatterOverdrive.client.render.biostat;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.client.RenderHandler;
import com.MO.MatterOverdrive.client.render.IWorldLastRenderer;
import com.MO.MatterOverdrive.data.biostats.BioticStatShield;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/7/2015.
 */
public class BiostatRendererShield implements IWorldLastRenderer
{
    public static final ResourceLocation forcefield_damage_tex = new ResourceLocation(Reference.PATH_FX + "shield_damage.png");
    public static final ResourceLocation forcefield_tex = new ResourceLocation(Reference.PATH_FX + "forcefield_plasma.png");
    public static final ResourceLocation forcefield_plasma_tex = new ResourceLocation(Reference.PATH_FX + "forcefield_plasma_2.png");
    public static final ResourceLocation shield_texture = new ResourceLocation(Reference.PATH_FX + "shield.png");
    public static final ResourceLocation model_path = new ResourceLocation(Reference.PATH_MODEL + "shield_sphere.obj");
    IModelCustom normal_sphere;
    IModelCustom shield_model;
    float opacityLerp;

    public BiostatRendererShield()
    {
        shield_model = AdvancedModelLoader.loadModel(model_path);
        normal_sphere = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        opacityLerp = 0;
    }

    public void onRenderWorldLast(RenderHandler handler,RenderWorldLastEvent event)
    {
        List playerEntities = Minecraft.getMinecraft().theWorld.playerEntities;
        for (int i = 0;i < playerEntities.size();i++)
        {
            renderPlayerShield(event,(EntityPlayer)playerEntities.get(i));
        }

    }

    private void renderPlayerShield(RenderWorldLastEvent event,EntityPlayer player)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(player);
        boolean isVisible = manageOpacityLerp(androidPlayer,event.partialTicks);

        if (isVisible)
        {
            double time = Minecraft.getMinecraft().theWorld.getWorldTime();

            glPushMatrix();
            glDepthMask(false);
            glEnable(GL_BLEND);
            glDisable(GL_ALPHA_TEST);
            glDisable(GL_CULL_FACE);
            glBlendFunc(GL_ONE, GL_ONE);
            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.2f * getOpacityLerp(player));
            Minecraft.getMinecraft().renderEngine.bindTexture(shield_texture);
            if (!isClient(player))
            {
                glTranslated(0, player.height - 0.5, 0);
                glEnable(GL_CULL_FACE);
            }else
            {
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
                    glEnable(GL_CULL_FACE);
                }
            }
            glTranslated(player.posX - Minecraft.getMinecraft().thePlayer.posX,player.posY - Minecraft.getMinecraft().thePlayer.posY,player.posZ - Minecraft.getMinecraft().thePlayer.posZ);
            glTranslated(0, -0.5, 0);
            glScaled(3, 3, 3);
            glRotated(player.motionZ * 45, -1, 0, 0);
            glRotated(player.motionX * 45, 0, 0, 1);
            shield_model.renderAll();

            glDisable(GL_CULL_FACE);
            renderAttacks(androidPlayer);

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.1f * getOpacityLerp(player));
            Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_tex);
            glScaled(1.02, 1.02, 1.02);
            normal_sphere.renderAll();
            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.05f * getOpacityLerp(player));
            glPushMatrix();
            glRotated(time * 0.005, Math.sin(time * 0.01), Math.cos(time * 0.01), 0);
            Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_plasma_tex);
            glScaled(1.01, 1.01, 1.01);
            normal_sphere.renderAll();
            glPopMatrix();
            glDisable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glEnable(GL_ALPHA_TEST);
            glDepthMask(true);
            glPopMatrix();
        }
    }

    private boolean manageOpacityLerp(AndroidPlayer androidPlayer,float partialTicks)
    {
        if (AndroidStatRegistry.shield.getShieldState(androidPlayer))
        {
            if (isClient(androidPlayer.getPlayer()))
            {
                if (opacityLerp < 1) {
                    opacityLerp = Math.min(1, opacityLerp + partialTicks * 0.1f);
                }
            }

            return true;
        }else
        {
            if (isClient(androidPlayer.getPlayer()) && opacityLerp > 0)
            {
                opacityLerp = Math.max(0,opacityLerp - partialTicks * 0.2f);
                return true;
            }else
            {
                return false;
            }
        }
    }

    private boolean isClient(EntityPlayer player)
    {
        return player == Minecraft.getMinecraft().thePlayer;
    }

    private float getOpacityLerp(EntityPlayer player)
    {
        if (Minecraft.getMinecraft().thePlayer == player)
        {
            return opacityLerp;
        }
        return 1;
    }

    private void renderAttacks(AndroidPlayer androidPlayer)
    {
        float opacity = getOpacityLerp(androidPlayer.getPlayer());
        if (androidPlayer.getEffects().hasKey(BioticStatShield.TAG_HITS))
        {
            NBTTagList hits = androidPlayer.getEffects().getTagList(BioticStatShield.TAG_HITS, 10);
            for (int i = 0; i < hits.tagCount(); i++) {
                renderAttack(new Vector3f(hits.getCompoundTagAt(i).getFloat("x"), -hits.getCompoundTagAt(i).getFloat("y"), -hits.getCompoundTagAt(i).getFloat("z")).normalise(null),(hits.getCompoundTagAt(i).getInteger("time") / 10f) * opacity);
            }
        }
    }

    private void renderAttack(Vector3f dir,float percent)
    {
        glPushMatrix();
        Vector3f front = new Vector3f(1, 0, 0);
        Vector3f c = Vector3f.cross(dir, front, null);
        double omega = Math.acos(Vector3f.dot(dir, front));
        glRotated(omega * (180 / Math.PI), c.x, c.y, c.z);
        RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 1 * percent);
        Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_damage_tex);
        normal_sphere.renderAll();
        glPopMatrix();
    }
}
