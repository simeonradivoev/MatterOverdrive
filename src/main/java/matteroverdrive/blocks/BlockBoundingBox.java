package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlock;
import net.minecraft.block.material.Material;

/**
 * @author shadowfacts
 */
public class BlockBoundingBox extends MOBlock {

	public BlockBoundingBox(Material material, String name) {
		super(material, name);
		setBlockUnbreakable();
	}

	@Override
	public void register() {
		super.register();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
