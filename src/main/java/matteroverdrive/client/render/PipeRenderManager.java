package matteroverdrive.client.render;

import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 1/20/2016.
 */
public class PipeRenderManager
{
    @SubscribeEvent
    public void drawSelectionBox(DrawBlockHighlightEvent e)
    {
        BlockPos blockPos = e.target.getBlockPos();
        if (blockPos != null)
        {
            World world = e.player.worldObj;
            TileEntity tileEntity = world.getTileEntity(blockPos);


            if (tileEntity instanceof TileEntityPipe)
            {
                if (e.isCancelable())
                    e.setCanceled(true);

                IBlockState blockState = world.getBlockState(blockPos);
                Vec3 hitFrom = e.target.hitVec;
                AxisAlignedBB mask = new AxisAlignedBB(blockPos, blockPos.add(1, 1, 1));

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                float f = 0.1F;
                Block block = world.getBlockState(blockPos).getBlock();

                if (block.getMaterial() != Material.air && world.getWorldBorder().contains(blockPos))
                {
                    block.setBlockBoundsBasedOnState(world, blockPos);
                    double d0 = e.player.lastTickPosX + (e.player.posX - e.player.lastTickPosX) * (double) e.partialTicks;
                    double d1 = e.player.lastTickPosY + (e.player.posY - e.player.lastTickPosY) * (double) e.partialTicks;
                    double d2 = e.player.lastTickPosZ + (e.player.posZ - e.player.lastTickPosZ) * (double) e.partialTicks;
                    List<AxisAlignedBB> bbs = new ArrayList<>();
                    blockState.getBlock().addCollisionBoxesToList(world, blockPos, blockState, mask, bbs, e.player);
                    for (AxisAlignedBB bb : bbs)
                    {
                        AxisAlignedBB changed = bb.expand(f, f, f).offset(-d0, -d1, -d2);
                        Vec3 look = e.player.getLook(e.partialTicks);
                        look = new Vec3(look.xCoord * 10, look.yCoord * 10 + e.player.getEyeHeight(), look.zCoord * 10);
                        MovingObjectPosition position = changed.calculateIntercept(new Vec3(0, e.player.getEyeHeight(), 0), look);
                        if (position != null)
                        {
                            RenderGlobal.drawSelectionBoundingBox(changed);
                            //MOLog.info(changed.toString());
                        }
                    }
                }

                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();

                //MOLog.info("SubId: " + e.target.hitInfo);
            }
        }
    }
}
