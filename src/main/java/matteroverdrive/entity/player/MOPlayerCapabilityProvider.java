package matteroverdrive.entity.player;

import matteroverdrive.api.android.IAndroid;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.EnumSet;

/**
 * Created by Simeon on 3/24/2016.
 */
public class MOPlayerCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
	AndroidPlayer androidPlayer;
	MOExtendedProperties extendedProperties;

	public MOPlayerCapabilityProvider(EntityPlayer player)
	{
		androidPlayer = new AndroidPlayer(player);
		extendedProperties = new MOExtendedProperties(player);
	}

	public static AndroidPlayer GetAndroidCapability(Entity entity)
	{
		return entity.getCapability(AndroidPlayer.CAPIBILITY, EnumFacing.DOWN);
	}

	public static MOExtendedProperties GetExtendedCapability(Entity entity)
	{
		return entity.getCapability(MOExtendedProperties.CAPIBILITY, EnumFacing.DOWN);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == AndroidPlayer.CAPIBILITY || capability == MOExtendedProperties.CAPIBILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == AndroidPlayer.CAPIBILITY)
		{
			return (T)androidPlayer;
		}
		else if (capability == MOExtendedProperties.CAPIBILITY)
		{
			return (T)extendedProperties;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagCompound androidTag = new NBTTagCompound();
		androidPlayer.writeToNBT(androidTag, EnumSet.allOf(IAndroid.DataType.class));
		tagCompound.setTag("Android", androidTag);
		extendedProperties.saveNBTData(tagCompound);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		extendedProperties.loadNBTData(nbt);
		NBTTagCompound androidNbt = nbt.getCompoundTag("Android");
		androidPlayer.readFromNBT(androidNbt, EnumSet.allOf(IAndroid.DataType.class));
	}
}
