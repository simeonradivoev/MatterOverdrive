package matteroverdrive.client.render;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IBlockScanner;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.data.Color;
import matteroverdrive.fx.MOEntityFX;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Simeon on 2/3/2016.
 */
public class DimensionalRiftsRender implements IWorldLastRenderer
{
    Random random = new Random();
    double lastY;
    float[][] points = new float[128][128];

    @Override
    public void onRenderWorldLast(RenderHandler handler, RenderWorldLastEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null)
        {
            ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (heldItem.getItem() instanceof  IBlockScanner && ((IBlockScanner) heldItem.getItem()).showsGravitationalWaves(heldItem))
            {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                GlStateManager.depthMask(true);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glPointSize(6);
                GL11.glLineWidth(1);
                Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
                Vec3 viewEntityPos = renderViewEntity.getPositionEyes(event.partialTicks).subtract(0, renderViewEntity.getEyeHeight(), 0);
                if (lastY == 0)
                    lastY = 64;
                if (renderViewEntity.onGround)
                    lastY = MOMathHelper.Lerp(lastY, viewEntityPos.yCoord, 0.05);
                Vec3 viewEntityPosRound = new Vec3(Math.floor(viewEntityPos.xCoord), lastY, Math.floor(viewEntityPos.zCoord));


                GlStateManager.translate(-viewEntityPos.xCoord, -viewEntityPos.yCoord, -viewEntityPos.zCoord);
                RenderUtils.bindTexture(ClientProxy.renderHandler.getRenderParticlesHandler().additiveTextureSheet);
                WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

                int vewDistance = 128;
                double height = 5;

                random.setSeed(Minecraft.getMinecraft().theWorld.getSeed());

                for (int x = 0; x < vewDistance; x++)
                {
                    for (int z = 0; z < vewDistance; z++)
                    {
                        float yPos = MatterOverdrive.moWorld.getDimensionalRifts().getValueAt(new Vec3(viewEntityPosRound.xCoord + x - vewDistance / 2,0,viewEntityPosRound.zCoord + z - vewDistance / 2));
                        yPos *= Math.sin((x / (double) vewDistance) * Math.PI) * Math.sin((z / (double) vewDistance) * Math.PI);
                        points[x][z] = yPos;
                    }
                }

                GlStateManager.translate(0, viewEntityPosRound.yCoord, 0);

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                for (int z = 0; z < vewDistance; z++)
                {
                    for (int x = 0; x < vewDistance; x++)
                    {
                        if (points[x][z] > 0.01)
                        {
                            double xPos = viewEntityPosRound.xCoord + x - vewDistance / 2;
                            double zPos = viewEntityPosRound.zCoord + z - vewDistance / 2;

                            float r = Reference.COLOR_HOLO.getFloatR() * points[x][z];
                            float g = Reference.COLOR_HOLO.getFloatG() * points[x][z];
                            float b = Reference.COLOR_HOLO.getFloatB() * points[x][z];
                            worldRenderer.pos(xPos, getPointSafe(x, z) * height, zPos).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos, getPointSafe(x, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z) * height, zPos).color(r, g, b, 1).endVertex();
                        }
                    }
                }
                Tessellator.getInstance().draw();

                worldRenderer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
                for (int z = 0; z < vewDistance; z++)
                {
                    for (int x = 0; x < vewDistance; x++)
                    {
                        if (points[x][z] > 0.01)
                        {
                            double xPos = viewEntityPosRound.xCoord + x - vewDistance / 2;
                            double zPos = viewEntityPosRound.zCoord + z - vewDistance / 2;

                            float r = Reference.COLOR_HOLO.getFloatR() * points[x][z];
                            float g = Reference.COLOR_HOLO.getFloatG() * points[x][z];
                            float b = Reference.COLOR_HOLO.getFloatB() * points[x][z];
                            worldRenderer.pos(xPos, getPointSafe(x, z) * height, zPos).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos, getPointSafe(x, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                            worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z) * height, zPos).color(r, g, b, 1).endVertex();
                        }
                    }
                }
                Tessellator.getInstance().draw();

                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public double getPointSafe(int x,int y)
    {
        return points[MathHelper.clamp_int(x,0,127)][MathHelper.clamp_int(y,0,127)];
    }
}
