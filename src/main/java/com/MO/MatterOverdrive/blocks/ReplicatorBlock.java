package com.MO.MatterOverdrive.blocks;

import java.util.Random;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
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
		super(material,name,true,true);
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
    
    public Item getItemDropped(int metadata, Random rand, int fortune)
    {
        return Item.getItemFromBlock(MatterOverdriveBlocks.replicator);
    }
    
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
    	this.dropsItself = !keepInventory;
    	
    	if(!keepInventory)
    	{
	    	TileEntityMachineReplicator tileentityReplicator = (TileEntityMachineReplicator)world.getTileEntity(x, y, z);
	    	
	    	if(tileentityReplicator != null)
	    	{
	    		for (int i1 = 0; i1 < tileentityReplicator.getSizeInventory(); ++i1)
	            {
	                ItemStack itemstack = tileentityReplicator.getStackInSlot(i1);
	
	                if (itemstack != null)
	                {
	                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
	                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
	                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
	
	                    while (itemstack.stackSize > 0)
	                    {
	                        int j1 = world.rand.nextInt(21) + 10;
	
	                        if (j1 > itemstack.stackSize)
	                        {
	                            j1 = itemstack.stackSize;
	                        }
	
	                        itemstack.stackSize -= j1;
	                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
	
	                        if (itemstack.hasTagCompound())
	                        {
	                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	                        }
	
	                        float f3 = 0.05F;
	                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
	                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
	                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
	                        world.spawnEntityInWorld(entityitem);
	                    }
	                }
	            }
	            world.func_147453_f(x, y, z, block);
	    	}
    	}
    	super.breakBlock(world, x, y, z, block, p_149749_6_);
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
