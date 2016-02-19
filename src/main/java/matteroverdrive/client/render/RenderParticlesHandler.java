package matteroverdrive.client.render;

import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Simeon on 5/13/2015.
 */
public class RenderParticlesHandler implements IWorldLastRenderer
{
    final ResourceLocation additiveTextureSheet = new ResourceLocation(Reference.PATH_PARTICLE + "particles_additive.png");
    private final TextureManager renderer;
    protected final World worldObj;
    private Random rand = new Random();
    final List<EntityFX>[] fxes;

    public RenderParticlesHandler(World world, TextureManager renderer)
    {
        this.worldObj = world;
        this.renderer = renderer;
        fxes = new List[Blending.values().length];
        for (int i = 0;i < Blending.values().length;i++)
        {
            fxes[i] = new ArrayList<>();
        }
    }

    public void onRenderWorldLast(RenderHandler renderHandler, RenderWorldLastEvent event)
    {
        renderParticles(Minecraft.getMinecraft().thePlayer, event.partialTicks);
    }

    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            updateEffects();
        }
    }

    public void addEffect(EntityFX entityFX, Blending blendingLayer)
    {
        fxes[blendingLayer.ordinal()].add(entityFX);
    }

    private void updateEffects()
    {
        for (List<EntityFX> fxe : fxes)
        {

            for (int j = 0; j < fxe.size(); ++j)
            {
                final EntityFX entityfx = fxe.get(j);

                try
                {
                    if (entityfx != null)
                    {
                        entityfx.onUpdate();
                    }
                } catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
                    crashreportcategory.addCrashSectionCallable("Particle", new Callable()
                    {
                        private static final String __OBFID = "CL_00000916";

                        public String call()
                        {
                            return entityfx.toString();
                        }
                    });
                    throw new ReportedException(crashreport);
                }

                if (entityfx == null || entityfx.isDead)
                {
                    fxe.remove(j--);
                }
            }
        }
    }

    public void renderParticles(Entity entity, float f)
    {
        float f1 = ActiveRenderInfo.getRotationX();
        float f2 = ActiveRenderInfo.getRotationZ();
        float f3 = ActiveRenderInfo.getRotationYZ();
        float f4 = ActiveRenderInfo.getRotationXY();
        float f5 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;

        GlStateManager.color(1,1,1,1);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        ICamera camera = new Frustum();
        camera.setPosition(EntityFX.interpPosX,EntityFX.interpPosY,EntityFX.interpPosZ);

        for (int k = 0; k < fxes.length; ++k)
        {
            if (!this.fxes[k].isEmpty())
            {
                WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
                Blending blending = Blending.values()[k];
                switch (blending)
                {
                    case Additive:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE,0,1);
                        renderer.bindTexture(additiveTextureSheet);
                        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                    case Additive2Sided:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE,GL11.GL_ONE,0,1);
                        renderer.bindTexture(additiveTextureSheet);
                        GlStateManager.disableCull();
                        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                    case LinesAdditive:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE,0,1);
                        GlStateManager.disableTexture2D();
                        GL11.glLineWidth(2);
                        wr.begin(GL11.GL_LINES, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                }

                for (int j = 0; j < this.fxes[k].size(); ++j)
                {
                    final EntityFX entityfx = this.fxes[k].get(j);
                    if (entityfx == null) continue;
                    //int brightness = entityfx.getBrightnessForRender(f);
                    //wr.putBrightness4(brightness,brightness,brightness,brightness);

                    try
                    {
                        AxisAlignedBB bb = entityfx.getEntityBoundingBox();
                        if (bb != null && !camera.isBoundingBoxInFrustum(bb))
                            continue;

                        if (!entityfx.isInRangeToRender3d(entity.posX,entity.posY,entity.posZ))
                            continue;

                        entityfx.renderParticle(wr,entityfx, f, f1, f5, f2, f3, f4);
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                        crashreportcategory.addCrashSectionCallable("Particle", entityfx::toString);
                        throw new ReportedException(crashreport);
                    }
                }

                Tessellator.getInstance().draw();

                switch (blending)
                {
                    case Additive2Sided:
                        GlStateManager.enableCull();
                        break;
                    case LinesAdditive:
                        GlStateManager.enableTexture2D();
                        break;
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
    }

    public enum Blending
    {
        Additive,
        Additive2Sided,
        LinesAdditive,
    }

    public ResourceLocation getAdditiveTextureSheet()
    {
        return additiveTextureSheet;
    }
}
