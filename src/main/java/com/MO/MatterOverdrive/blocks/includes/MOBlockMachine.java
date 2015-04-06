package com.MO.MatterOverdrive.blocks.includes;

import com.MO.MatterOverdrive.items.includes.MOEnergyMatterBlockItem;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Simeon on 4/5/2015.
 */
public class MOBlockMachine extends MOBlockContainer
{
    public MOBlockMachine(Material material, String name)
    {
        super(material, name);
    }

    @Override
    protected void RegisterBlock()
    {
        GameRegistry.registerBlock(this, MOEnergyMatterBlockItem.class, this.getUnlocalizedName().substring(5));
    }

    public boolean doNormalDrops(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        try
        {
            IMOTileEntity entity = (IMOTileEntity)world.getTileEntity(x,y,z);
            if(entity != null)
            {
                entity.readFromPlaceItem(itemStack);
            }
        }
        catch (Exception e)
        {
            FMLLog.warning("Could not load settings from placing item");
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        try
        {
            if (!world.isRemote && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
            {
                //drops inventory
                MOTileEntityMachine machine = (MOTileEntityMachine) world.getTileEntity(x, y, z);

                if (machine != null && machine.getInventory() != null) {
                    for (int i = 0; i < machine.getInventory().getSizeInventory(); i++) {
                        if (machine.getInventory().getSlot(i) != null && machine.getInventory().getSlot(i).drops()) {
                            if (machine.getInventory().getSlot(i).getItem() != null) {
                                dropBlockAsItem(world, x, y, z, machine.getInventory().getSlot(i).getItem());
                            }

                        }
                    }
                }
            }
        }catch (Exception e)
        {
            FMLLog.severe("Could not drop Items from Machine", e);
        }

        ItemStack machineBlock = getNBTDrop(world,x,y,z,(IMOTileEntity)world.getTileEntity(x,y,z));
        dropBlockAsItem(world, x, y, z, machineBlock);

        super.breakBlock(world,x,y,z,block,p_149749_6_);
    }

    //drop and write to items from existing IMOTileEntity
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        /*if(doNormalDrops(world, x, y, z))
        {
            return super.getDrops(world, x, y, z, metadata, fortune);
        }
        return Lists.newArrayList(getNBTDrop(world, x, y, z, (IMOTileEntity) world.getTileEntity(x, y, z)));*/
        return new ArrayList<ItemStack>();
    }

    public ItemStack getNBTDrop(World world, int x, int y, int z, IMOTileEntity te)
    {
        int meta = damageDropped(world.getBlockMetadata(x,y,z));
        ItemStack itemStack = new ItemStack(this, 1, meta);
        if(te != null)
            te.writeToDropItem(itemStack);
        return itemStack;
    }
}
