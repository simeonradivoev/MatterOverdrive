package matteroverdrive.client.render;

import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
		BlockPos blockPos = e.getTarget().getBlockPos();
		if (blockPos != null)
		{
			World world = e.getPlayer().worldObj;
			TileEntity tileEntity = world.getTileEntity(blockPos);


			if (tileEntity instanceof TileEntityPipe)
			{
				if (e.isCancelable())
				{
					e.setCanceled(true);
				}

				IBlockState blockState = world.getBlockState(blockPos);
				Vec3d hitFrom = e.getTarget().hitVec;
				AxisAlignedBB mask = new AxisAlignedBB(blockPos, blockPos.add(1, 1, 1));

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
				GL11.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				float f = 0.1F;
				IBlockState state = world.getBlockState(blockPos);
				Block block = state.getBlock();

				if (block.getMaterial(state) != Material.air && world.getWorldBorder().contains(blockPos))
				{
					//block.setBlockBoundsBasedOnState(world, blockPos);
					double d0 = e.getPlayer().lastTickPosX + (e.getPlayer().posX - e.getPlayer().lastTickPosX) * (double)e.getPartialTicks();
					double d1 = e.getPlayer().lastTickPosY + (e.getPlayer().posY - e.getPlayer().lastTickPosY) * (double)e.getPartialTicks();
					double d2 = e.getPlayer().lastTickPosZ + (e.getPlayer().posZ - e.getPlayer().lastTickPosZ) * (double)e.getPartialTicks();
					List<AxisAlignedBB> bbs = new ArrayList<>();
					blockState.getBlock().addCollisionBoxToList(state, world, blockPos, mask, bbs, e.getPlayer());
					for (AxisAlignedBB bb : bbs)
					{
						AxisAlignedBB changed = bb.expand(f, f, f).offset(-d0, -d1, -d2);
						Vec3d look = e.getPlayer().getLook(e.getPartialTicks());
						look = new Vec3d(look.xCoord * 10, look.yCoord * 10 + e.getPlayer().getEyeHeight(), look.zCoord * 10);
						RayTraceResult position = changed.calculateIntercept(new Vec3d(0, e.getPlayer().getEyeHeight(), 0), look);
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
