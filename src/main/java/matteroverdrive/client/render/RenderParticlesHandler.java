package matteroverdrive.client.render;

import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.data.TextureAtlasSpriteParticle;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
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
public class RenderParticlesHandler implements IWorldLastRenderer, IIconCreator
{
    public static ResourceLocation star = new ResourceLocation(Reference.MOD_ID,"sprite_star");
    public static ResourceLocation shockwave = new ResourceLocation(Reference.MOD_ID,"sprite_shockwave");
    public static ResourceLocation selection = new ResourceLocation(Reference.MOD_ID,"sprite_selection");
    public static ResourceLocation marker = new ResourceLocation(Reference.MOD_ID,"sprite_marker");
    public static ResourceLocation sparks = new ResourceLocation(Reference.MOD_ID,"sprite_sparks");
    public static ResourceLocation blood = new ResourceLocation(Reference.MOD_ID,"sprite_blood");
    public static ResourceLocation smoke = new ResourceLocation(Reference.MOD_ID,"sprite_smoke");
    public static ResourceLocation explosion = new ResourceLocation(Reference.MOD_ID,"sprite_explosion");

    private final TextureMap textureMap;
    private final ResourceLocation sheet = new ResourceLocation(Reference.MOD_ID, "textures/particle/mo_particles.png");
    private final TextureManager renderer;
    protected final World worldObj;
    private Random rand = new Random();
    final List<EntityFX>[] fxes;

    public RenderParticlesHandler(World world, TextureManager renderer)
    {
        this.worldObj = world;
        this.renderer = renderer;
        fxes = new List[Blending.values().length];
        textureMap = new TextureMap("textures/particle",this);
        Minecraft.getMinecraft().renderEngine.loadTickableTexture(sheet,textureMap);
        for (int i = 0;i < Blending.values().length;i++)
        {
            fxes[i] = new ArrayList<>();
        }
        textureMap.loadTextureAtlas(Minecraft.getMinecraft().getResourceManager());
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
        renderer.bindTexture(sheet);

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
                        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                    case Additive2Sided:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE,GL11.GL_ONE,0,1);
                        GlStateManager.disableCull();
                        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                    case LinesAdditive:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE,0,1);
                        GlStateManager.disableTexture2D();
                        GL11.glLineWidth(2);
                        wr.begin(GL11.GL_LINES, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        break;
                    case Transparent:
                        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA,1,0);
                        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
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

    @Override
    public void registerSprites(TextureMap iconRegistry)
    {
        reg(iconRegistry,sparks);
        reg(iconRegistry,star);
        reg(iconRegistry,shockwave);
        reg(iconRegistry,marker);
        reg(iconRegistry,selection);
        reg(iconRegistry,blood,64,2);
        reg(iconRegistry,smoke,64,2);
        reg(iconRegistry,explosion,21,2);
    }

    public void reg(TextureMap textureMap,ResourceLocation resourceLocation)
    {
        textureMap.registerSprite(resourceLocation);
    }

    public void reg(TextureMap textureMap,ResourceLocation resourceLocation,int frameSize,int speed)
    {
        TextureAtlasSpriteParticle spriteParticle = new TextureAtlasSpriteParticle(resourceLocation.toString(),frameSize,speed);
        textureMap.setTextureEntry(resourceLocation.toString(),spriteParticle);
    }

    public TextureAtlasSprite getSprite(ResourceLocation location)
    {
        return textureMap.getAtlasSprite(location.toString());
    }

    public void bindSheet()
    {
        RenderUtils.bindTexture(sheet);
    }

    public enum Blending
    {
        Additive,
        Additive2Sided,
        LinesAdditive,
        Transparent
    }
}
