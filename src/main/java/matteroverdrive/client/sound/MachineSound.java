package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 3/18/2015.
 */
public class MachineSound extends PositionedSound implements ITickableSound
{
    private boolean donePlaying;

    public MachineSound(SoundEvent sound, SoundCategory category, BlockPos pos, float volume, float pitch) {
        super(sound,category);
        setPosition(pos);
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = true;
        this.repeatDelay = 0;
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

    public void setPosition(BlockPos position)
    {
        this.xPosF = position.getX();
        this.yPosF = position.getY();
        this.zPosF = position.getZ();
    }

    @Override
    public void update()
    {
    }
}
