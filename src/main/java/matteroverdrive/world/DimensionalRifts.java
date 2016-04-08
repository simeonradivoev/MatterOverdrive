package matteroverdrive.world;

import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Simeon on 2/10/2016.
 */
public class DimensionalRifts
{
    private double noiseScale;

    public DimensionalRifts(double noiseScale)
    {
        this.noiseScale = noiseScale;
    }

    public float getValueAt(BlockPos pos)
    {
        return this.getValueAt(new Vec3d(pos));
    }

    public float getValueAt(Vec3d pos)
    {
        if (Minecraft.getMinecraft().theWorld != null)
        {
            float yPos = (float) MOMathHelper.noise(pos.xCoord * noiseScale, Minecraft.getMinecraft().theWorld.provider.getSeed(), pos.zCoord * noiseScale);
            yPos = MathHelper.clamp_float((float) Math.pow((yPos - 0.45f), 5) * 180, 0, 1);
            return yPos;
        }
        return 0;
    }
}
