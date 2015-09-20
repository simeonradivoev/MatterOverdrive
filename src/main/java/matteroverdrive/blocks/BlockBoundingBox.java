package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.init.MatterOverdriveIcons;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

/**
 * @author shadowfacts
 */
public class BlockBoundingBox extends MOBlock {

	public BlockBoundingBox(Material material, String name) {
		super(material, name);
		setBlockUnbreakable();
		setCreativeTab(null);
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return MatterOverdriveIcons.Base;
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
