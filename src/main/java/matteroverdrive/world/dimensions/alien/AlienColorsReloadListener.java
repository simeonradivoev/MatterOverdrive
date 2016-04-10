package matteroverdrive.world.dimensions.alien;

import matteroverdrive.Reference;
import matteroverdrive.util.MOLog;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.IOException;

/**
 * Created by Simeon on 2/24/2016.
 */
public class AlienColorsReloadListener implements IResourceManagerReloadListener
{
	private static final ResourceLocation LOC_FOLIAGE_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_foliage.png");
	private static final ResourceLocation LOC_GRASS_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_grass.png");
	private static final ResourceLocation LOC_WATER_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_water.png");
	private static final ResourceLocation LOC_SKY_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_sky.png");
	private static final ResourceLocation LOC_FOG_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_fog.png");
	private static final ResourceLocation LOC_STONE_PNG = new ResourceLocation(Reference.MOD_ID, "textures/colormap/alien_stone.png");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		try
		{
			ColorizerAlien.setFoliageBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_FOLIAGE_PNG));
			ColorizerAlien.setGrassBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_GRASS_PNG));
			ColorizerAlien.setWaterBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_WATER_PNG));
			ColorizerAlien.setSkyBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_SKY_PNG));
			ColorizerAlien.setFogBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_FOG_PNG));
			ColorizerAlien.setStoneBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_STONE_PNG));
		}
		catch (IOException var3)
		{
			MOLog.log(Level.ERROR, var3, "There was a problem while loading Alien Biome Colors");
		}
	}
}
