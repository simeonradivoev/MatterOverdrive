package matteroverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.tile.TileEntityMachineFusionReactorController;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/14/2015.
 */
public class BlockFusionReactorController extends MOBlockMachine
{
    public BlockFusionReactorController(Material material, String name)
    {
        super(material, name);
        setHardness(30.0F);
        this.setResistance(10.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
        lightValue = 10;
        setRotationType(BlockHelper.RotationType.SIX_WAY);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            return MatterOverdriveIcons.Monitor_back;
        }
        else if (side == BlockHelper.getRightSide(meta) || side == BlockHelper.getLeftSide(meta))
        {
            return MatterOverdriveBlocks.decomposer.iconTop;
        }
        else
        {
            return MatterOverdriveIcons.YellowStripes;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityMachineFusionReactorController();
    }

    @Override
    public void onBlockPlacedBy(World World, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {
        int l = BlockPistonBase.determineOrientation(World, x, y, z, player);
        World.setBlockMetadataWithNotify(x,y,z, l,2);
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMachineFusionReactorController) {
            if (((TileEntityMachineFusionReactorController) tileEntity).isValidStructure())
            {
                return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        super.onConfigChanged(config);
        TileEntityMachineFusionReactorController.ENERGY_STORAGE = config.getInt(getUnlocalizedName(),"storage.energy",100000000,String.format("How much energy can the %s hold",getLocalizedName()));
        TileEntityMachineFusionReactorController.MATTER_STORAGE = config.getInt(getUnlocalizedName(),"storage.matter",2048,String.format("How much matter can the %s hold",getLocalizedName()));
        TileEntityMachineFusionReactorController.ENERGY_PER_TICK = config.getInt(getUnlocalizedName(),"output.energy",2048,"The Energy Output per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MATTER_DRAIN_PER_TICK = (float)config.getMachineDouble(getUnlocalizedName(),"drain.matter",1D / 80D,"How much matter is drained per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MAX_GRAVITATIONAL_ANOMALY_DISTANCE = config.getMachineInt(getUnlocalizedName(),"distance.anomaly",3,"The maximum distance of the anomaly");
        TileEntityMachineFusionReactorController.STRUCTURE_CHECK_DELAY = config.getMachineInt(getUnlocalizedName(),"check.delay",40,"The time delay between each structure check");
    }
}
