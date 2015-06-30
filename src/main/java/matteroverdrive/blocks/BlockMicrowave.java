package matteroverdrive.blocks;

import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.client.render.block.MOBlockRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/17/2015.
 */
public class BlockMicrowave extends MOBlock
{
    private IIcon frontIcon;
    private IIcon backIcon;

    public BlockMicrowave(Material material, String name)
    {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {

        if (side == meta)
        {
            return frontIcon;
        }
        else if (ForgeDirection.OPPOSITES[side] == meta)
        {
            return backIcon;
        }

        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "microwave");
        this.frontIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "microwave_front");
        this.backIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "microwave_back");
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){return false;}

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x,y,z);
        float pixel = 1f/16f;
        ForgeDirection dir = ForgeDirection.getOrientation(meta);

        if (dir == ForgeDirection.WEST || dir == ForgeDirection.EAST)
        {
            setBlockBounds(3 * pixel,0,1 * pixel,13 * pixel,10 * pixel,15 * pixel);
        }
        else if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH)
        {
            setBlockBounds(1 * pixel,0,3 * pixel,15 * pixel,10 * pixel,13 * pixel);
        }
        else
        {
            setBlockBounds(1 * pixel,0,3 * pixel,15 * pixel,10 * pixel,13 * pixel);
        }
    }
}
