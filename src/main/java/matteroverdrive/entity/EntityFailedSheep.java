package matteroverdrive.entity;

import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/29/2015.
 */
public class EntityFailedSheep extends EntitySheep
{
	public EntityFailedSheep(World world, EntitySheep sheep)
	{
		super(world);
		setFleeceColor(sheep.getFleeceColor());
	}

	public EntityFailedSheep(World world)
	{
		super(world);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return MatterOverdriveSounds.failedAnimalIdleSheep;
	}

	protected SoundEvent getHurtSound()
	{
		return MatterOverdriveSounds.failedAnimalIdleSheep;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return MatterOverdriveSounds.failedAnimalDie;
	}

	public EntitySheep createChild(EntityAgeable entity)
	{
		return new EntityFailedSheep(worldObj, super.createChild(entity));
	}
}
