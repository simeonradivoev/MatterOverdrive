package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import com.MO.MatterOverdrive.tile.TileEntityMachineDecomposer;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DecomposerBlock extends MOMatterEnergyStorageBlock
{
	private IIcon iconTop;
	private static boolean keepInventory;
	
	public DecomposerBlock(Material material, String name) 
	{
		super(material, name, true, true);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
	}

	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_side");
		this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_top");
	}


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if(side == metadata)
        {
            return GetIconBasedOnMatter(world,x,y,z);
        }

        return getIcon(side,metadata);
    }

    @Override
    public IIcon getIcon(int side,int metadata)
    {
        if(side == 1)
        {
            return this.iconTop;
        }
        else if(side == metadata)
        {
            return MatterOverdriveIcons.Matter_tank_empty;
        }

        return this.blockIcon;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        this.dropsItself = !keepInventory;
        if(!keepInventory)
        {
            MatterHelper.DropInventory(world,(IInventory)world.getTileEntity(x, y, z),x,y,z);
        }
        return super.removedByPlayer(world,player,x,y,z);
    }
    
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
    	if(!world.isRemote)
    	{
    		FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiIDDecomposer, world, x, y, z);
    	}
    	
    	return true;
    }
    
    @Override
	 public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	 {
		 return true;
	 }
	 
	 public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	 {
		 return true;
	 }
	 
	 @Override
		public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) 
		{
			return new TileEntityMachineDecomposer();
		}

    @Override
    public int getRenderType()
    {
        return BlockRendererReplicator.renderID;
    }

}
