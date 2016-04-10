package matteroverdrive.entity;

import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityFailedChicken extends EntityChicken
{
	public EntityFailedChicken(World world)
	{
		super(world);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return MatterOverdriveSounds.failedAnimalUdleChicken;
	}

	protected SoundEvent getHurtSound()
	{
		return MatterOverdriveSounds.failedAnimalUdleChicken;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return MatterOverdriveSounds.failedAnimalDie;
	}

	@Override
	public EntityChicken createChild(EntityAgeable entity)
	{
		return new EntityFailedChicken(this.worldObj);
	}

}
