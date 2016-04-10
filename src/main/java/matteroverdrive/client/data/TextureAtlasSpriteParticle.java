package matteroverdrive.client.data;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Created by Simeon on 2/20/2016.
 */
public class TextureAtlasSpriteParticle extends TextureAtlasSprite
{
	private int frameSize;
	private int speed;
	private int animationCounter;
	private int tickCount;
	private int frameCountPerRow;
	private float frameSizeDeltaWidth;
	private float frameSizeDeltaHeight;

	public TextureAtlasSpriteParticle(String spriteName, int frameSize, int speed)
	{
		super(spriteName);
		this.frameSize = frameSize;
		this.speed = speed;
	}

	public TextureAtlasSpriteParticle copy()
	{
		TextureAtlasSpriteParticle sprite = new TextureAtlasSpriteParticle(this.getIconName(), frameSize, speed);
		sprite.copyFrom(this);
		sprite.calculate();
		return sprite;
	}

	private void calculate()
	{
		frameCountPerRow = getIconWidth() / frameSize;
		frameSizeDeltaWidth = (super.getMaxU() - super.getMinU()) / (float)frameCountPerRow;
		frameSizeDeltaHeight = (super.getMaxV() - super.getMinV()) / (float)frameCountPerRow;
	}

	public void updateParticleAnimation()
	{
		++this.tickCount;
		if (tickCount >= speed)
		{
			tickCount = 0;
			if (animationCounter < (frameCountPerRow * frameCountPerRow) - 1)
			{
				animationCounter++;
			}
		}
	}

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	public float getMinU()
	{
		if (frameCountPerRow > 0)
		{
			return super.getMinU() + ((animationCounter % frameCountPerRow) * frameSizeDeltaWidth);
		}
		return super.getMinU();
	}

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	public float getMaxU()
	{
		if (frameCountPerRow > 0)
		{
			return super.getMinU() + ((animationCounter % frameCountPerRow) + 1) * frameSizeDeltaWidth;
		}
		return super.getMaxU();
	}

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	public float getMinV()
	{
		if (frameCountPerRow > 0)
		{
			float vOffset = ((float)Math.floor((animationCounter) / frameCountPerRow) * frameSizeDeltaHeight);
			return super.getMinV() + vOffset;
		}
		return super.getMinV();
	}

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	public float getMaxV()
	{
		if (frameCountPerRow > 0)
		{
			float vOffset = (((float)Math.floor((animationCounter) / frameCountPerRow) + 1) * frameSizeDeltaHeight);
			return super.getMinV() + vOffset;
		}
		return super.getMaxV();
	}
}
