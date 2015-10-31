package matteroverdrive.entity;

import matteroverdrive.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/29/2015.
 */
public class EntityFailedSheep extends EntitySheep
{
    public EntityFailedSheep(World world,EntitySheep sheep)
    {
        super(world);
        setFleeceColor(sheep.getFleeceColor());
    }

    public EntityFailedSheep(World world) {
        super(world);
    }

    @Override
    protected String getLivingSound()
    {
        return Reference.MOD_ID + ":failed_animal_idle_sheep";
    }

    protected String getHurtSound()
    {
        return Reference.MOD_ID + ":failed_animal_idle_sheep";
    }

    @Override
    protected String getDeathSound()
    {
        return Reference.MOD_ID + ":failed_animal_die_" + rand.nextInt(2);
    }

    public EntitySheep createChild(EntityAgeable entity)
    {
        return new EntityFailedSheep(worldObj,super.createChild(entity));
    }
}
