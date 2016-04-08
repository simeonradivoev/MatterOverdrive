package matteroverdrive.entity;

import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityFailedPig extends EntityPig
{
    public EntityFailedPig(World world)
    {
        super(world);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return MatterOverdriveSounds.failedAnimalIdlePig;
    }

    protected SoundEvent getHurtSound()
    {
        return MatterOverdriveSounds.failedAnimalIdlePig;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return MatterOverdriveSounds.failedAnimalDie;
    }

    @Override
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    public EntityPig createChild(EntityAgeable entity)
    {
        return new EntityFailedPig(worldObj);
    }
}
