package com.MO.MatterOverdrive.client.render.tileentity;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.TileEntityGravitationalAnomaly;
import com.MO.MatterOverdrive.tile.TileEntityMachineGravitationalStabilizer;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/12/2015.
 */
public class TileEntityRendererGravitationalStabilizer extends TileEntitySpecialRenderer {
    public static final ResourceLocation beam = new ResourceLocation(Reference.PATH_FX + "physbeam.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks) {
        TileEntityMachineGravitationalStabilizer stabilizer = (TileEntityMachineGravitationalStabilizer) tileEntity;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ForgeDirection f = ForgeDirection.getOrientation(tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord));

        if (stabilizer.getHit() != null) {
            MovingObjectPosition hit = stabilizer.getHit();
            TileEntity tileEntityHit = tileEntity.getWorldObj().getTileEntity(hit.blockX, hit.blockY, hit.blockZ);


            glPushMatrix();
            glTranslated(x + 0.5, y + 0.5, z + 0.5);

            long time = stabilizer.getWorldObj().getWorldTime();
            double pulseSize = Math.sin(time * 0.2) * 0.001;
            Vector3f source = new Vector3f(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            Vector3f destination = new Vector3f((float)hit.hitVec.xCoord, (float)hit.hitVec.yCoord, (float)hit.hitVec.zCoord);
            Vector3f dir = Vector3f.sub(destination, source, null);
            Vector3f dirC = Vector3f.cross(dir,new Vector3f(1,0,1),null);
            float distance = dir.length();
            dir.normalise(dir);
            Vector3f front = new Vector3f(0, 0, -1);
            Vector3f c = Vector3f.cross(dir, front, null);
            double omega = Math.acos(Vector3f.dot(dir, front));


            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glDisable(GL_LIGHTING);
            RenderUtils.disableLightmap();

            glBlendFunc(GL_ONE, GL_ONE);
            glColor3d(stabilizer.getBeamColorR(), stabilizer.getBeamColorG(), stabilizer.getBeamColorB());
            bindTexture(beam);

            glPushMatrix();
            glScaled(dirC.x * pulseSize + 1, dirC.y * pulseSize + 1, dirC.z * pulseSize + 1);
            glTranslated(dir.x * distance / 2, dir.y * distance / 2, dir.z * distance / 2);
            glScaled(dir.x * distance + 1, dir.y * distance + 1, dir.z * distance + 1);
            glRotated(omega * (180 / Math.PI), c.x, c.y, c.z);
            glRotated(90, 0, 1, 0);
            glTranslated(-0.5, -0.5, 0);
            RenderUtils.drawPlaneWithUV(1, 1, 0, 0, distance / 2, 1);
            glPopMatrix();

            glPushMatrix();
            glTranslated(dir.x * distance / 2, dir.y * distance / 2, dir.z * distance / 2);
            glScaled((destination.x - source.x) + 1, (destination.y - source.y) + 1, (destination.z - source.z) + 1);
            glRotated(omega * (180 / Math.PI), c.x, c.y, c.z);
            glRotated(90, 0, 0, 1);
            glRotated(90, 0, 1, 0);
            glTranslated(-0.5, -0.5, 0);
            RenderUtils.drawPlaneWithUV(1, 1, 0, 0, distance / 2, 1);
            glPopMatrix();

            glEnable(GL_CULL_FACE);
            glDisable(GL_BLEND);
            glEnable(GL_LIGHTING);
            glPopMatrix();

            if (tileEntityHit != null && tileEntityHit instanceof TileEntityGravitationalAnomaly)
                renderScreen(x, y, z, stabilizer, (TileEntityGravitationalAnomaly) tileEntityHit);
        }
    }


    public void renderScreen(double x, double y, double z, TileEntityMachineGravitationalStabilizer stabilizer, TileEntityGravitationalAnomaly anomaly) {

        int meta = stabilizer.getWorldObj().getBlockMetadata(stabilizer.xCoord,stabilizer.yCoord,stabilizer.zCoord);
        ForgeDirection side = ForgeDirection.getOrientation(BlockHelper.getOppositeSide(meta));

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side, Reference.COLOR_HOLO,stabilizer);

        List<String> infos = new ArrayList<String>();
        anomaly.addInfo(anomaly.getWorldObj(), anomaly.xCoord, anomaly.yCoord, anomaly.zCoord, infos);
        RenderUtils.drawScreenInfo(infos.toArray(new String[]{}), Reference.COLOR_HOLO,side);

        RenderUtils.endDrawinngBlockScreen();
    }

    private FontRenderer fontRenderer()
    {
        return Minecraft.getMinecraft().fontRenderer;
    }

}
