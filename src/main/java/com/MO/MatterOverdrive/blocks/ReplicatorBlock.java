package com.MO.MatterOverdrive.blocks;

import java.util.Random;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.items.includes.MOEnergyMatterBlockItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ReplicatorBlock extends MOMatterEnergyStorageBlock
{
	@SideOnly(Side.CLIENT)
	private IIcon iconFront;
	
	private static boolean keepInventory;
	
	public ReplicatorBlock(Material material,String name)
	{
		super(material, name, true, true);
		setHardness(2.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
        super.registerBlockIcons(iconRegister);
		this.iconFront = iconRegister.registerIcon(Reference.MOD_ID + ":" + "replicator_front");
        this.blockIcon = MatterOverdriveIcons.Base;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        //System.out.println("Metadata " + world.getBlockMetadata(x,y,z));

        if(side == metadata)
        {
            return MatterOverdriveIcons.Transperant;
        }
        else if(side == BlockHelper.getOppositeSide(metadata))
        {
            return GetIconBasedOnMatter(world,x,y,z);
        }
        else if(side == BlockHelper.getLeftSide(metadata) || side == BlockHelper.getRightSide(metadata))
        {
            return MatterOverdriveIcons.Transperant;
        }

        return this.blockIcon;
    }
    
    @Override
    public void onBlockPlacedBy(World World, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {
    	super.onBlockPlacedBy(World, x, y, z, player, item);
    	TileEntityMachineReplicator rep = (TileEntityMachineReplicator)World.getTileEntity(x, y, z);
    }
    
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
    	if(!world.isRemote)
    	{
    		FMLNetworkHandler.openGui(player, MatterOverdrive.instance, MatterOverdrive.guiIDReplicator, world, x, y, z);
    	}
    	
    	return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) 
    {
    	
    }
	 
	 @Override
	 public boolean isOpaqueCube()
	    {
	        return false;
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
		return new TileEntityMachineReplicator();
	}


    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return -1;
    }
}
