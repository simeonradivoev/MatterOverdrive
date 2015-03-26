package com.MO.MatterOverdrive.blocks.includes;

import java.util.Random;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x,y,z);
        if(tileEntity != null)
            tileEntity.onNeighborBlockChange();
    }


}
