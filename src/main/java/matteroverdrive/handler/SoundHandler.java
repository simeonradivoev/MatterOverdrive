package matteroverdrive.handler;

import matteroverdrive.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Random;

public class SoundHandler
{
	protected static final Random soundRand = new Random();

	public static void PlaySoundAt(World world,String name,Entity entity)
	{
		PlaySoundAt(world,name,entity,1.0f,1.0f,0.1f,0.1f);
	}

	public static void PlaySoundAt(World world,String name,Entity entity,float maxVolume)
	{
		PlaySoundAt(world,name,entity,maxVolume,1f,0.1f,0.1f);
	}

	public static void PlaySoundAt(World world,String name,Entity entity,float maxVolume,float maxPitch)
	{
		PlaySoundAt(world,name,entity,maxVolume,maxPitch,0.1f,0.1f);
	}

	public static void PlaySoundAt(World world,String name,Entity entity,float maxVolume,float maxPitch,float maxVolumeDeviation)
	{
		PlaySoundAt(world,name,entity,maxVolume,maxPitch,maxVolumeDeviation,0.1f);
	}

	public static void PlaySoundAt(World world,String name,Entity entity,float maxVolume,float maxPitch,float maxVolumeDeviation,float maxPitchDeviation)
	{
		float PitchDeviation = maxPitch * maxPitchDeviation  * soundRand.nextFloat();
		float VolumeDeviation = maxVolume * maxVolumeDeviation  * soundRand.nextFloat();
		float volume = (maxVolume - maxVolumeDeviation) + VolumeDeviation;
		float pitch = (maxPitch - maxPitchDeviation) + PitchDeviation;
		world.playSoundAtEntity(entity, Reference.MOD_ID + ":" + name, volume , pitch);
	}

	public static void PlaySoundAt(World world,String name,float x,float y,float z)
	{
		PlaySoundAt(world,name,x,y,z,1.0f,1.0f,0.1f,0.1f);
	}
	public static void PlaySoundAt(World world,String name,float x,float y,float z,float maxVolume)
	{
		PlaySoundAt(world,name,x,y,z,maxVolume,1.0f,0.1f,0.1f);
	}
	public static void PlaySoundAt(World world,String name,float x,float y,float z,float maxVolume,float maxPitch)
	{
		PlaySoundAt(world,name,x,y,z,maxVolume,maxPitch,0.1f,0.1f);
	}
	public static void PlaySoundAt(World world,String name,float x,float y,float z,float maxVolume,float maxPitch,float maxVolumeDeviation)
	{
		PlaySoundAt(world,name,x,y,z,maxVolume,maxPitch,maxVolumeDeviation,0.1f);
	}
	public static void PlaySoundAt(World world,String name,float x,float y,float z,float maxVolume,float maxPitch,float maxVolumeDeviation,float maxPitchDeviation)
	{
		float PitchDeviation = maxPitch * maxPitchDeviation  * soundRand.nextFloat();
		float VolumeDeviation = maxVolume * maxVolumeDeviation  * soundRand.nextFloat();
		float volume = (maxVolume - VolumeDeviation) + VolumeDeviation;
		float pitch = (maxPitch - PitchDeviation) + PitchDeviation;
		world.playSoundEffect(x, y, z, Reference.MOD_ID + ":" + name, volume, pitch);
	}
}
