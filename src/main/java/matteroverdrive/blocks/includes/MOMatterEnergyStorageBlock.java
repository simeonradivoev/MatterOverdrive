package matteroverdrive.blocks.includes;

import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MOMatterEnergyStorageBlock<TE extends TileEntity> extends MOBlockMachine<TE>
{
	protected boolean dropsItself;
	private boolean keepsMatter;
	private boolean keepsEnergy;

	public MOMatterEnergyStorageBlock(Material material, String name, boolean keepsEnergy, boolean keepsMatter)
	{
		super(material, name);
		this.keepsEnergy = keepsEnergy;
		this.keepsMatter = keepsMatter;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (stack.hasTagCompound())
		{
			TileEntity entity = worldIn.getTileEntity(pos);

			if (entity instanceof MOTileEntityMachineEnergy)
			{
				if (this.keepsEnergy)
				{
					((MOTileEntityMachineEnergy)entity).setEnergyStored(stack.getTagCompound().getInteger("Energy"));
				}
			}
			if (entity.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null)) {
				if (this.keepsMatter) {
					entity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).setMatterStored(stack.getTagCompound().getInteger("Matter"));
				}
			}
		}
	}

    /*@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if(dropsItself)
        {
            MOTileEntityMachineMatter tile = (MOTileEntityMachineMatter)world.getTileEntity(x,y,z);

            if (tile != null && !world.isRemote && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
            {
                ItemStack item = new ItemStack(this);

                if(tile.getMatterStored() > 0 && this.keepsMatter)
                {
                    if(!item.hasTagCompound())
                        item.setTagCompound(new NBTTagCompound());

                    item.getTagCompound().setInteger("Matter", tile.getMatterStored());
                }
                if(tile.getEnergyStored(EnumFacing.DOWN) > 0 && this.keepsEnergy)
                {
                    if(!item.hasTagCompound())
                        item.setTagCompound(new NBTTagCompound());

                    item.getTagCompound().setInteger("Energy", tile.getEnergyStored(EnumFacing.DOWN));
                    item.getTagCompound().setInteger("MaxEnergy", tile.getMaxEnergyStored(EnumFacing.DOWN));
                }

                this.dropBlockAsItem(world, x, y, z, item);
            }
        }
        return super.removedByPlayer(world,player,x,y,z);
    }*/

}
