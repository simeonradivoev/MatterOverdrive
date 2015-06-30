package com.MO.MatterOverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/18/2015.
 */
public class MachineSound extends PositionedSound implements ITickableSound
{
    private boolean donePlaying;

    public MachineSound(ResourceLocation sound,float x,float y,float z,float volume,float pitch) {
        super(sound);
        super.xPosF = x;
        super.yPosF = y;
        super.zPosF = z;
        super.volume = volume;
        super.field_147663_c = pitch;
        super.repeat = true;
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

    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
    }

    @Override
    public void update()
    {

    }
}
