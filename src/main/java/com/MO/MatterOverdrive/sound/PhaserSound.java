package com.MO.MatterOverdrive.sound;

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
        super.xPosF = x;
        super.yPosF = y;
        super.zPosF = z;
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

    @Override
    public void update()
    {

    }
}
