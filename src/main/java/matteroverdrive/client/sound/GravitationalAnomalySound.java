package matteroverdrive.client.sound;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 5/12/2015.
 */
public class GravitationalAnomalySound extends PositionedSound implements ITickableSound {

    boolean donePlaying = false;
    double range;

    public GravitationalAnomalySound(ResourceLocation sound, int x, int y, int z, float volume, double range)
    {
        super(sound);
        setPosition(x, y, z);
        this.volume = volume;
        this.range = range;
        field_147666_i = AttenuationType.NONE;
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
        EntityClientPlayerMP mp =  FMLClientHandler.instance().getClient().thePlayer;
        double distance = Vec3.createVectorHelper(xPosF, yPosF, zPosF).distanceTo(mp.getPosition(0));
        volume = 1 - (float)(distance / range);
    }
}
