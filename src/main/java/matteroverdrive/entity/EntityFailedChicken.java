package matteroverdrive.entity;

import matteroverdrive.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityFailedChicken extends EntityChicken
{
    public EntityFailedChicken(World p_i1682_1_)
    {
        super(p_i1682_1_);
    }

    @Override
    protected String getLivingSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_idle_chicken";
    }

    protected String getHurtSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_idle_chicken";
    }

    @Override
    protected String getDeathSound()
    {
        return Reference.MOD_ID + ":" + "failed_animal_die_" + rand.nextInt(2);
    }

    @Override
    public EntityChicken createChild(EntityAgeable p_90011_1_)
    {
        return new EntityFailedChicken(this.worldObj);
    }

}
