package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.client.render.BlockRendererReplicator;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/27/2015.
 */
public class BlockPatternStorage extends MOBlockMachine
{
    public boolean hasVentParticles;

    public BlockPatternStorage(Material material, String name)
    {
        super(material, name);
        setHardness(2.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if(side == BlockHelper.getOppositeSide(metadata))
        {
            return MatterOverdriveIcons.Vent;
        }

        return MatterOverdriveIcons.Base;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMachinePatternStorage();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    public void loadConfigs(MOConfigurationHandler configurationHandler)
    {
        super.loadConfigs(configurationHandler);
        hasVentParticles = configurationHandler.getMachineBool(getUnlocalizedName() + ".particles", true, "Sould vent particles be displayed");
    }
}
