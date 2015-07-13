package matteroverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.tile.TileEntityMachineMatterRecycler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/15/2015.
 */
public class BlockMatterRecycler extends MOMatterEnergyStorageBlock {

    private IIcon iconTop;
    private IIcon iconSideAnim;

    public BlockMatterRecycler(Material material, String name)
    {
        super(material, name,true,true);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
        setHasGui(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_top");
        this.iconSideAnim = iconRegister.registerIcon(Reference.MOD_ID + ":" + "recycler_side_anim");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == BlockHelper.getAboveSide(meta))
        {
            return iconTop;
        }
        return MatterOverdriveIcons.Recycler;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (side != BlockHelper.getAboveSide(blockAccess.getBlockMetadata(x, y, z)))
        {
            if (blockAccess.getTileEntity(x,y,z) instanceof TileEntityMachineMatterRecycler)
            {
                if (((TileEntityMachineMatterRecycler) blockAccess.getTileEntity(x,y,z)).isActive())
                {
                    return iconSideAnim;
                }
            }
        }
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineMatterRecycler();
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }
}
