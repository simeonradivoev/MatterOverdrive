package com.MO.MatterOverdrive.blocks;

import cofh.api.block.IDismantleable;
import com.MO.MatterOverdrive.blocks.includes.MOBlock;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Simeon on 5/14/2015.
 */
public class BlockFusionReactorCoil extends MOBlock implements IDismantleable {

    public BlockFusionReactorCoil(Material material, String name) {
        super(material, name);
        setHardness(30.0F);
        this.setResistance(10.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return MatterOverdriveIcons.YellowStripes;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops)
    {
        if (!returnDrops)
        {
            world.setBlockToAir(x,y,z);
            dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);
        }

        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }
}
