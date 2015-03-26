package com.MO.MatterOverdrive.client.texture;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class TmpFlippableIcon extends FlippableIcon
{

	private static final IIcon NULL_ICON = new MissingIcon( Blocks.diamond_block );

	public TmpFlippableIcon() {
		super( NULL_ICON );
	}

	public void setOriginal(IIcon i)
	{
		this.setFlip( false, false );

		while (i instanceof FlippableIcon)
		{
			FlippableIcon fi = (FlippableIcon) i;
			if ( fi.flip_u )
				this.flip_u = !this.flip_u;

			if ( fi.flip_v )
				this.flip_v = !this.flip_v;

			i = fi.getOriginal();
		}

		if ( i == null )
			this.original = NULL_ICON;
		else
			this.original = i;
	}

}