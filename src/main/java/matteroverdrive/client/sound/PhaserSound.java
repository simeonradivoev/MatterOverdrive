package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/16/2015.
 */
public class PhaserSound extends PositionedSound implements ITickableSound
{
    private boolean donePlaying;

    public PhaserSound(ResourceLocation sound,float x,float y,float z,float volume,float pitch)
    {
        super(sound);
        setPosition(x,y,z);
        super.volume = volume;
        super.field_147663_c = pitch;
        super.repeat = false;
        super.field_147665_h = 0;
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

    public void setPosition(float x,float y,float z)
    {
        this.xPosF = x;
        this.yPosF = y;
        this.zPosF = z;
    }

    @Override
    public void update()
    {

    }
}
