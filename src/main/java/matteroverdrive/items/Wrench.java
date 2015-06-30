package matteroverdrive.items;

import cofh.api.block.IDismantleable;
import matteroverdrive.items.includes.MOBaseItem;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Simeon on 5/19/2015.
 */
public class Wrench extends MOBaseItem
{
    public Wrench(String name)
    {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlock(x,y,z);
        boolean result = false;

        if (block != null) {
            PlayerInteractEvent e = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world);
            if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY || e.useBlock == Event.Result.DENY || e.useItem == Event.Result.DENY) {
                return false;
            }
            if (player.isSneaking() && block instanceof IDismantleable && ((IDismantleable) block).canDismantle(player,world,x,y,z)) {
                if (!world.isRemote) {
                    ((IDismantleable) block).dismantleBlock(player, world, x, y, z, false);
                }
                result = true;
            }else if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
            {
                if (block == Blocks.chest)
                {
                    TileEntityChest te = (TileEntityChest)world.getTileEntity(x,y,z);
                    if (te.adjacentChestXNeg != null || te.adjacentChestXPos != null || te.adjacentChestZNeg != null || te.adjacentChestZPos != null)
                    {
                        TileEntityChest masterChest = te.adjacentChestXNeg == null && te.adjacentChestZNeg == null ? te : te.adjacentChestXNeg == null ? te.adjacentChestZNeg : te.adjacentChestXNeg;
                        if (masterChest != te)
                        {
                            int meta = world.getBlockMetadata(masterChest.xCoord,masterChest.yCoord,masterChest.zCoord);
                            world.setBlockMetadataWithNotify(masterChest.xCoord,masterChest.yCoord,masterChest.zCoord,meta ^ 1,3);
                        }else
                        {
                            block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                        }
                    }
                }

                result = true;
            }
        }

        if (result)
        {
            player.swingItem();
        }

        return result && !world.isRemote;
    }

    @Override
    public boolean hasDetails(ItemStack stack){return true;}
}
