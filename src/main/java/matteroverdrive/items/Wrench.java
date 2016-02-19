package matteroverdrive.items;

import cofh.api.block.IDismantleable;
import matteroverdrive.blocks.includes.IMultiBlock;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

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
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        boolean result = false;

        if (state != null) {
            PlayerInteractEvent e = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, pos, side, world);
            if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY || e.useBlock == Event.Result.DENY || e.useItem == Event.Result.DENY) {
                return false;
            }
            if (player.isSneaking() && state.getBlock() instanceof IDismantleable && ((IDismantleable) state.getBlock()).canDismantle(player,world,pos)) {
                if (!world.isRemote) {
                    ((IDismantleable) state.getBlock()).dismantleBlock(player, world, pos, false);
                }
                result = true;
            }if (state.getBlock() instanceof IMultiBlock && !world.isRemote)
            {
                result = ((IMultiBlock) state.getBlock()).onWrenchHit(stack,player,world,pos,side,hitX,hitY,hitZ);
            }
            else if (!player.isSneaking() && state.getBlock().rotateBlock(world, pos, side))
            {
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
