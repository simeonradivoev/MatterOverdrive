package com.MO.MatterOverdrive.blocks;

import java.util.Random;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
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
	private static boolean keepInventory;
	public float replication_volume;
    public boolean hasVentParticles;
	
	public ReplicatorBlock(Material material,String name)
	{
		super(material, name, true, true);
		setHardness(2.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        return MatterOverdriveIcons.Base;
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
	public TileEntity createNewTileEntity(World world, int meta)
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

	@Override
	public void loadConfigs(MOConfigurationHandler configurationHandler)
	{
		super.loadConfigs(configurationHandler);
        replication_volume = configurationHandler.getMachineFloat(getUnlocalizedName() + ".volume.replicate",1f,0,2f,"The volume of the replication animation");
        hasVentParticles = configurationHandler.getMachineBool(getUnlocalizedName() + ".particles", true, "Sould vent particles be displayed");
	}
}
