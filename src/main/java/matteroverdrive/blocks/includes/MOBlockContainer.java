package matteroverdrive.blocks.includes;

import matteroverdrive.tile.IMOTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class MOBlockContainer extends MOBlock implements ITileEntityProvider
{

	public MOBlockContainer(Material material, String name)
    {
        super(material,name);
        this.isBlockContainer = true;
    }

    @Override
    public void Register()
    {
        super.Register();
        TileEntity e = createNewTileEntity(null,0);
        if(e != null)
        {
            GameRegistry.registerTileEntity(e.getClass(), this.getUnlocalizedName().substring(5));
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
    {
        super.onBlockPreDestroy(world,x,y,z,meta);
        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x,y,z);
        if(tileEntity != null)
            tileEntity.onDestroyed();
    }
}
