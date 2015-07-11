package matteroverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityMachineNetworkRouter;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/11/2015.
 */
public class BlockNetworkRouter extends MOBlockMachine
{
    IIcon activeIcon;

    public BlockNetworkRouter(Material material, String name)
    {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
        setRotationType(BlockHelper.RotationType.PREVENT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        activeIcon = p_149651_1_.registerIcon(this.getTextureName() + "_active");
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
    {
        TileEntity entity = world.getTileEntity(x,y,z);
        if (entity instanceof TileEntityMachineNetworkRouter)
        {
            if (((TileEntityMachineNetworkRouter) entity).isActive())
            {
                return activeIcon;
            }
        }
        return blockIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineNetworkRouter();
    }
}
