package matteroverdrive.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BlockStarMap extends MOBlockMachine
{
    private IIcon topIcon;
    private IIcon bottomIcon;
    public BlockStarMap(Material material, String name)
    {
        super(material, name);
        setBlockBounds(0, 0, 0, 1, 9 * (1 / 16f), 1);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        lightValue = 10;
        setHasGui(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        topIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_top");
        bottomIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_bottom");
        blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "starmap_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            return topIcon;
        }
        else if (side == 0)
        {
            return bottomIcon;
        }
        else
        {
            return blockIcon;
        }
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        if (player.isSneaking()) {
            TileEntityMachineStarMap starMap = (TileEntityMachineStarMap) world.getTileEntity(x, y, z);
            starMap.zoom();
            return true;
        }else
        {
            return super.onBlockActivated(world,x,y,z,player,side,hitX,hitY,hitZ);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMachineStarMap();
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
}
