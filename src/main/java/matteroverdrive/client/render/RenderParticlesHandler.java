package matteroverdrive.client.render;

import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.data.TextureAtlasSpriteParticle;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
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
public class RenderParticlesHandler implements IWorldLastRenderer, ITextureMapPopulator
{
	public static ResourceLocation star = new ResourceLocation(Reference.MOD_ID, "sprite_star");
	public static ResourceLocation shockwave = new ResourceLocation(Reference.MOD_ID, "sprite_shockwave");
	public static ResourceLocation selection = new ResourceLocation(Reference.MOD_ID, "sprite_selection");
	public static ResourceLocation marker = new ResourceLocation(Reference.MOD_ID, "sprite_marker");
	public static ResourceLocation sparks = new ResourceLocation(Reference.MOD_ID, "sprite_sparks");
	public static ResourceLocation blood = new ResourceLocation(Reference.MOD_ID, "sprite_blood");
	public static ResourceLocation smoke = new ResourceLocation(Reference.MOD_ID, "sprite_smoke");
	public static ResourceLocation explosion = new ResourceLocation(Reference.MOD_ID, "sprite_explosion");
	protected final World worldObj;
	final List<Particle>[] particles;
	private final TextureMap textureMap;
	private final ResourceLocation sheet = new ResourceLocation(Reference.MOD_ID, "textures/particle/mo_particles.png");
	private final TextureManager renderer;
	private Random rand = new Random();

	public RenderParticlesHandler(World world, TextureManager renderer)
	{
		this.worldObj = world;
		this.renderer = renderer;
		particles = new List[Blending.values().length];
		textureMap = new TextureMap("textures/particle", this);
		Minecraft.getMinecraft().renderEngine.loadTickableTexture(sheet, textureMap);
		for (int i = 0; i < Blending.values().length; i++)
		{
			particles[i] = new ArrayList<>();
		}
		textureMap.loadTextureAtlas(Minecraft.getMinecraft().getResourceManager());
	}

	public void onRenderWorldLast(RenderHandler renderHandler, RenderWorldLastEvent event)
	{
		renderParticles(Minecraft.getMinecraft().thePlayer, event.getPartialTicks());
	}

	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (!Minecraft.getMinecraft().isGamePaused())
		{
			updateEffects();
		}
	}

	public void addEffect(Particle particle, Blending blendingLayer)
	{
		particles[blendingLayer.ordinal()].add(particle);
	}

	private void updateEffects()
	{
		for (List<Particle> particleList : particles)
		{

			for (int j = 0; j < particleList.size(); ++j)
			{
				final Particle particle = particleList.get(j);

				try
				{
					if (particle != null)
					{
						particle.onUpdate();
					}
				}
				catch (Throwable throwable)
				{
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
					crashreportcategory.setDetail("Particle", particle::toString);
					throw new ReportedException(crashreport);
				}

				if (particle == null || !particle.isAlive())
				{
					particleList.remove(j--);
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
		Particle.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
		Particle.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
		Particle.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		ICamera camera = new Frustum();
		camera.setPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
		renderer.bindTexture(sheet);

		for (int k = 0; k < particles.length; ++k)
		{
			if (!this.particles[k].isEmpty())
			{
				VertexBuffer wr = Tessellator.getInstance().getBuffer();
				Blending blending = Blending.values()[k];

				switch (blending)
				{
					case Additive:
						GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE, 0, 1);
						wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
						break;
					case Additive2Sided:
						GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE, 0, 1);
						GlStateManager.disableCull();
						wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
						break;
					case LinesAdditive:
						GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE, GL11.GL_ONE, 0, 1);
						GlStateManager.disableTexture2D();
						GL11.glLineWidth(2);
						wr.begin(GL11.GL_LINES, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
						break;
					case Transparent:
						GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
						wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
						break;
				}

				for (int j = 0; j < this.particles[k].size(); ++j)
				{
					final Particle particle = this.particles[k].get(j);
					if (particle == null)
					{
						continue;
					}
					//int brightness = particle.getBrightnessForRender(f);
					//wr.putBrightness4(brightness,brightness,brightness,brightness);

					try
					{
						AxisAlignedBB bb = particle.getEntityBoundingBox();
						if (bb != null && !camera.isBoundingBoxInFrustum(bb))
						{
							continue;
						}

						// TODO: 3/25/2016 Add my own distance check for effects
						//if (!particle.isInRangeToRender3d(entity.posX,entity.posY,entity.posZ))
						//continue;

						particle.renderParticle(wr, null, f, f1, f5, f2, f3, f4);
					}
					catch (Throwable throwable)
					{
						CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
						CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
						crashreportcategory.setDetail("Particle", particle::toString);
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
		reg(iconRegistry, sparks);
		reg(iconRegistry, star);
		reg(iconRegistry, shockwave);
		reg(iconRegistry, marker);
		reg(iconRegistry, selection);
		reg(iconRegistry, blood, 64, 2);
		reg(iconRegistry, smoke, 64, 2);
		reg(iconRegistry, explosion, 21, 2);
	}

	public void reg(TextureMap textureMap, ResourceLocation resourceLocation)
	{
		textureMap.registerSprite(resourceLocation);
	}

	public void reg(TextureMap textureMap, ResourceLocation resourceLocation, int frameSize, int speed)
	{
		TextureAtlasSpriteParticle spriteParticle = new TextureAtlasSpriteParticle(resourceLocation.toString(), frameSize, speed);
		textureMap.setTextureEntry(spriteParticle);
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
