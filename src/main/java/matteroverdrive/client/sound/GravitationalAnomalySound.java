package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Created by Simeon on 5/12/2015.
 */
public class GravitationalAnomalySound extends PositionedSound implements ITickableSound {

    boolean donePlaying = false;
    double range;

    public GravitationalAnomalySound(ResourceLocation sound, BlockPos pos, float volume, double range)
    {
        super(sound);
        setPosition(pos.getX(),pos.getY(),pos.getZ());
        this.volume = volume;
        this.range = range;
        this.attenuationType = AttenuationType.NONE;
        this.repeat = true;
    }

    @Override
    public boolean isDonePlaying()
    {
        return donePlaying;
    }

    public void stopPlaying()
    {
        donePlaying = true;
    }

    public void startPlaying()
    {
        donePlaying = false;
    }

    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
    }

    public void setRange(double range)
    {
        this.range = range;
    }

    public void setPosition(float x,float y,float z)
    {
        this.xPosF = x;
        this.yPosF = y;
        this.zPosF = z;
    }

    @Override
    public void update()
    {
        EntityPlayerSP mp =  FMLClientHandler.instance().getClient().thePlayer;
        double distance = new Vec3(xPosF, yPosF, zPosF).distanceTo(mp.getPositionVector());
        volume = 1 - (float)(distance / range);
    }
}
