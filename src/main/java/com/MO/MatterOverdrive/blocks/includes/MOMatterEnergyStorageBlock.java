package com.MO.MatterOverdrive.blocks.includes;

import com.MO.MatterOverdrive.api.matter.IMatterHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.MOTileEntityMachineEnergy;
import com.MO.MatterOverdrive.tile.MOTileEntityMachineMatter;
import com.MO.MatterOverdrive.items.includes.MOEnergyMatterBlockItem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class MOMatterEnergyStorageBlock extends MOBlockMachine
{
	private boolean keepsMatter;
	private boolean keepsEnergy;
	protected boolean dropsItself;
	
	public MOMatterEnergyStorageBlock(Material material, String name,boolean keepsEnergy,boolean keepsMatter) 
	{
		super(material, name);
		this.keepsEnergy = keepsEnergy;
		this.keepsMatter = keepsMatter;
	}

    protected IIcon GetIconBasedOnMatter(IBlockAccess world, int x, int y, int z)
    {
        TileEntity entity = world.getTileEntity(x,y,z);

        if(entity instanceof IMatterHandler && entity != null)
        {
            if(((IMatterHandler) entity).getMatterStored() > 0)
            {
                return MatterOverdriveIcons.Matter_tank_full;
            }
        }
        return MatterOverdriveIcons.Matter_tank_empty;
    }
	
	@Override
	 public void onBlockPlacedBy(World World, int x, int y, int z, EntityLivingBase player, ItemStack item)
	    {
	    	super.onBlockPlacedBy(World, x, y, z, player, item);
            if(item.hasTagCompound()) {
                TileEntity entity = World.getTileEntity(x,y,z);

                if (entity instanceof MOTileEntityMachineEnergy) {
                    if(this.keepsEnergy)
                        ((MOTileEntityMachineEnergy)entity).setEnergyStored(item.getTagCompound().getInteger("Energy"));
                }
                if (entity instanceof MOTileEntityMachineMatter) {
                    if(this.keepsMatter)
                        ((MOTileEntityMachineMatter)entity).setMatterStored(item.getTagCompound().getInteger("Matter"));
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
                if(tile.getEnergyStored(ForgeDirection.DOWN) > 0 && this.keepsEnergy)
                {
                    if(!item.hasTagCompound())
                        item.setTagCompound(new NBTTagCompound());

                    item.getTagCompound().setInteger("Energy", tile.getEnergyStored(ForgeDirection.DOWN));
                    item.getTagCompound().setInteger("MaxEnergy", tile.getMaxEnergyStored(ForgeDirection.DOWN));
                }

                this.dropBlockAsItem(world, x, y, z, item);
            }
        }
        return super.removedByPlayer(world,player,x,y,z);
    }*/

}
