package matteroverdrive.client.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

public class MissingIcon implements IIcon
{

	final boolean isBlock;

	public MissingIcon(Object forWhat) {
		this.isBlock = forWhat instanceof Block;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getMissing()
	{
		return ((TextureMap) Minecraft.getMinecraft().getTextureManager()
				.getTexture(this.isBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture)).getAtlasSprite("missingno");
	}

	@Override
	public int getIconWidth()
	{
		return this.getMissing().getIconWidth();
	}

	@Override
	public int getIconHeight()
	{
		return this.getMissing().getIconHeight();
	}

	@Override
	public float getMinU()
	{
		return this.getMissing().getMinU();
	}

	@Override
	public float getMaxU()
	{
		return this.getMissing().getMaxU();
	}

	@Override
	public float getInterpolatedU(double var1)
	{
		return this.getMissing().getInterpolatedU(var1);
	}

	@Override
	public float getMinV()
	{
		return this.getMissing().getMinV();
	}

	@Override
	public float getMaxV()
	{
		return this.getMissing().getMaxV();
	}

	@Override
	public float getInterpolatedV(double var1)
	{
		return this.getMissing().getInterpolatedV(var1);
	}

	@Override
	public String getIconName()
	{
		return this.getMissing().getIconName();
	}

}