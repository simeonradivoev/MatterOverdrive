package matteroverdrive.blocks.includes;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.tile.IMOTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class MOBlockContainer extends MOBlock implements ITileEntityProvider
{

	public MOBlockContainer(Material material, String name)
    {
        super(material,name);
        this.isBlockContainer = true;
    }

    @Override
    public void register()
    {
        super.register();
		GameRegistry.registerTileEntity(createNewTileEntity(null, 0).getClass(), this.getUnlocalizedName().substring(5));
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
