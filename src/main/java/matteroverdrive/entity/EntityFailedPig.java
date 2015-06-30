package matteroverdrive.entity;

import matteroverdrive.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityFailedPig extends EntityPig
{
    public EntityFailedPig(World p_i1689_1_)
    {
        super(p_i1689_1_);
    }

    @Override
    protected String getLivingSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_idle_pig";
    }

    protected String getHurtSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_idle_pig";
    }

    @Override
    protected String getDeathSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_die_" + rand.nextInt(2);
    }

    @Override
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    public EntityPig createChild(EntityAgeable p_90011_1_)
    {
        return new EntityFailedPig(this.worldObj);
    }
}
