package matteroverdrive.entity.android_player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Simeon on 2/8/2016.
 */
public class AndroidEffects
{
	private static final Map<Class<?>, Integer> typeMap = Maps.newHashMap();

	static
	{
		typeMap.put(Byte.class, 0);
		typeMap.put(Short.class, 1);
		typeMap.put(Integer.class, 2);
		typeMap.put(Long.class, 3);
		typeMap.put(Float.class, 4);
		typeMap.put(Boolean.class, 5);
		typeMap.put(String.class, 6);
		typeMap.put(ItemStack.class, 7);
		typeMap.put(BlockPos.class, 8);
		typeMap.put(Rotations.class, 9);
	}

	private final AndroidPlayer androidPlayer;
	private Map<Integer, Effect> effectMap = Maps.newHashMap();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private boolean effectsChanged;

	public AndroidEffects(AndroidPlayer androidPlayer)
	{
		this.androidPlayer = androidPlayer;
	}

	public static void writeEffectsListToPacketBuffer(List<Effect> objectsList, ByteBuf buf) throws IOException
	{
		if (objectsList != null)
		{
			for (Effect effect : objectsList)
			{
				writeEffectToBuffer(buf, effect);
			}
		}

		buf.writeByte(127);
	}

	private static void writeEffectToBuffer(ByteBuf buf, Effect effect) throws IOException
	{
		int i = (effect.typeId << 5 | effect.id & 31) & 255;
		buf.writeByte(i);

		switch (effect.typeId)
		{
			case 0:
				buf.writeByte((Byte)effect.value);
				break;
			case 1:
				buf.writeShort((Short)effect.value);
				break;
			case 2:
				buf.writeInt((Integer)effect.value);
				break;
			case 3:
				buf.writeLong((Long)effect.value);
				break;
			case 4:
				buf.writeFloat((Float)effect.value);
				break;
			case 5:
				buf.writeBoolean((Boolean)effect.value);
				break;
			case 6:
				ByteBufUtils.writeUTF8String(buf, (String)effect.value);
				break;
			case 7:
				ItemStack itemstack = (ItemStack)effect.value;
				ByteBufUtils.writeItemStack(buf, itemstack);
				break;
			case 8:
				BlockPos blockpos = (BlockPos)effect.value;
				buf.writeInt(blockpos.getX());
				buf.writeInt(blockpos.getY());
				buf.writeInt(blockpos.getZ());
				break;
			case 9:
				Rotations rotations = (Rotations)effect.value;
				buf.writeFloat(rotations.getX());
				buf.writeFloat(rotations.getY());
				buf.writeFloat(rotations.getZ());
		}
	}

	private static void writeEffectToNBT(NBTTagCompound tagCompound, Effect effect)
	{
		switch (effect.typeId)
		{
			case 0:
				tagCompound.setByte("value", (Byte)effect.value);
				break;
			case 1:
				tagCompound.setShort("value", (Short)effect.value);
				break;
			case 2:
				tagCompound.setInteger("value", (Integer)effect.value);
				break;
			case 3:
				tagCompound.setLong("value", (Long)effect.value);
				break;
			case 4:
				tagCompound.setFloat("value", (Float)effect.value);
				break;
			case 5:
				tagCompound.setBoolean("value", (Boolean)effect.value);
				break;
			case 6:
				tagCompound.setString("value", (String)effect.value);
				break;
			case 7:
				NBTTagCompound itemTag = new NBTTagCompound();
				ItemStack itemstack = (ItemStack)effect.value;
				itemstack.writeToNBT(itemTag);
				tagCompound.setTag("value", itemTag);
				break;
			case 8:
				NBTTagList blockTag = new NBTTagList();
				BlockPos blockpos = (BlockPos)effect.value;
				blockTag.appendTag(new NBTTagInt(blockpos.getX()));
				blockTag.appendTag(new NBTTagInt(blockpos.getY()));
				blockTag.appendTag(new NBTTagInt(blockpos.getZ()));
				tagCompound.setTag("value", blockTag);
				break;
			case 9:
				Rotations rotations = (Rotations)effect.value;
				tagCompound.setTag("value", rotations.writeToNBT());
		}
	}

	public static List<Effect> readEffectsListFromBuffer(ByteBuf buf) throws IOException
	{
		List<Effect> list = null;

		for (int i = buf.readByte(); i != 127; i = buf.readByte())
		{
			if (list == null)
			{
				list = Lists.<Effect>newArrayList();
			}

			int typeId = (i & 224) >> 5;
			int id = i & 31;
			Effect effect = null;

			switch (typeId)
			{
				case 0:
					effect = new Effect(id, buf.readByte(), typeId);
					break;
				case 1:
					effect = new Effect(id, buf.readShort(), typeId);
					break;
				case 2:
					effect = new Effect(id, buf.readInt(), typeId);
					break;
				case 3:
					effect = new Effect(id, buf.readLong(), typeId);
					break;
				case 4:
					effect = new Effect(id, buf.readFloat(), typeId);
					break;
				case 5:
					effect = new Effect(id, buf.readBoolean(), typeId);
					break;
				case 6:
					effect = new Effect(id, ByteBufUtils.readUTF8String(buf), typeId);
					break;
				case 7:
					effect = new Effect(id, ByteBufUtils.readItemStack(buf), typeId);
					break;
				case 8:
					int l = buf.readInt();
					int i1 = buf.readInt();
					int j1 = buf.readInt();
					effect = new Effect(id, new BlockPos(l, i1, j1), typeId);
					break;
				case 9:
					float f = buf.readFloat();
					float f1 = buf.readFloat();
					float f2 = buf.readFloat();
					effect = new Effect(id, new Rotations(f, f1, f2), typeId);
			}

			list.add(effect);
		}

		return list;
	}

	public static void readEffectFromNBT(NBTTagCompound tagCompound, Effect effect)
	{
		switch (effect.typeId)
		{
			case 0:
				effect.value = tagCompound.getByte("value");
				break;
			case 1:
				effect.value = tagCompound.getShort("value");
				break;
			case 2:
				effect.value = tagCompound.getInteger("value");
				break;
			case 3:
				effect.value = tagCompound.getLong("value");
				break;
			case 4:
				effect.value = tagCompound.getFloat("value");
				break;
			case 5:
				effect.value = tagCompound.getBoolean("value");
				break;
			case 6:
				effect.value = tagCompound.getString("value");
				break;
			case 7:
				NBTTagCompound itemTag = tagCompound.getCompoundTag("value");
				effect.value = ItemStack.loadItemStackFromNBT(itemTag);
				break;
			case 8:
				NBTTagList blockTag = tagCompound.getTagList("value", Constants.NBT.TAG_INT);
				effect.value = new BlockPos(((NBTTagInt)blockTag.get(0)).getInt(), ((NBTTagInt)blockTag.get(1)).getInt(), ((NBTTagInt)blockTag.get(2)).getInt());
				break;
			case 9:
				effect.value = new Rotations(tagCompound.getTagList("value", Constants.NBT.TAG_FLOAT));
		}
	}

	public Effect registerEffect(int id, Object value)
	{
		return registerEffect(id, value, false, false);
	}

	public Effect registerEffect(int id, Object value, boolean sendToOwner, boolean sendToOthers)
	{
		Integer dataType = typeMap.get(value.getClass());
		if (dataType == null)
		{
			throw new IllegalArgumentException("Unknown data type: " + value.getClass());
		}
		else if (id > 31)
		{
			throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
		}
		else if (this.effectMap.containsKey(id))
		{
			throw new IllegalArgumentException("Duplicate id value for " + id + "!");
		}
		return effectMap.put(id, new Effect(id, value, dataType, sendToOwner, sendToOthers));
	}

	public byte getEffectByte(int id)
	{
		return (Byte)this.getEffect(id).value;
	}

	public short getEffectShort(int id)
	{
		return (Short)this.getEffect(id).value;
	}

	public int getEffectInt(int id)
	{
		return (Integer)this.getEffect(id).value;
	}

	public boolean getEffectBool(int id)
	{
		return (Boolean)this.getEffect(id).value;
	}

	public float getEffectFloat(int id)
	{
		return (Float)this.getEffect(id).value;
	}

	public long getEffectLong(int id)
	{
		return (Long)this.getEffect(id).value;
	}

	public String getEffectString(int id)
	{
		return (String)this.getEffect(id).value;
	}

	public ItemStack getEffectItemStack(int id)
	{
		return (ItemStack)this.getEffect(id).value;
	}

	public Rotations getWatchableObjectRotations(int id)
	{
		return (Rotations)this.getEffect(id).value;
	}

	/**
	 * is threadsafe, unless it throws an exception, then
	 */
	private Effect getEffect(int id)
	{
		this.lock.readLock().lock();
		Effect effect;

		try
		{
			effect = this.effectMap.get(id);
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched android effects data");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched android effects data");
			crashreportcategory.addCrashSection("Data ID", id);
			throw new ReportedException(crashreport);
		}

		this.lock.readLock().unlock();
		return effect;
	}

	public <T> void updateEffect(int id, T newData)
	{
		Effect effect = this.getEffect(id);

		if (!typeMap.get(newData.getClass()).equals(effect.typeId))
		{
			throw new ClassCastException(String.format("Class: %s of value not the same as in stored effect", newData.getClass().getName()));
		}

		if (ObjectUtils.notEqual(newData, effect.value))
		{
			effect.value = newData;
			this.androidPlayer.onEffectsUpdate(id);
			effect.setWatched(true);
			this.effectsChanged = true;
		}
	}

	public void setEffectWatched(int id)
	{
		this.getEffect(id).setWatched(true);
		this.effectsChanged = true;
	}

	public boolean haveEffectsChanged()
	{
		return this.effectsChanged;
	}

	public List<Effect> getChanged()
	{
		List<Effect> list = null;

		if (this.effectsChanged)
		{
			this.lock.readLock().lock();

			for (Effect effect : this.effectMap.values())
			{
				if (effect.isWatched())
				{
					effect.setWatched(false);

					if (list == null)
					{
						list = Lists.<Effect>newArrayList();
					}

					list.add(effect);
				}
			}

			this.lock.readLock().unlock();
		}

		this.effectsChanged = false;
		return list;
	}

	public void writeTo(ByteBuf buffer) throws IOException
	{
		this.lock.readLock().lock();

		for (Effect effect : this.effectMap.values())
		{
			writeEffectToBuffer(buffer, effect);
		}

		this.lock.readLock().unlock();
		buffer.writeByte(127);
	}

	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		for (Map.Entry<Integer, Effect> effectEntry : effectMap.entrySet())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			writeEffectToNBT(tagCompound, effectEntry.getValue());
			nbtTagCompound.setTag(String.valueOf(effectEntry.getKey()), tagCompound);
		}
	}

	public void readFromNBT(NBTTagCompound tagCompound)
	{
		for (Map.Entry<Integer, Effect> effectEntry : effectMap.entrySet())
		{
			if (tagCompound.hasKey(String.valueOf(effectEntry.getKey())))
			{
				NBTTagCompound tag = tagCompound.getCompoundTag(String.valueOf(effectEntry.getKey()));
				readEffectFromNBT(tag, effectEntry.getValue());
			}
		}
	}

	public List<Effect> getAllWatched()
	{
		List<Effect> list = null;
		this.lock.readLock().lock();

		for (Effect datawatcher$watchableobject : this.effectMap.values())
		{
			if (list == null)
			{
				list = Lists.<Effect>newArrayList();
			}

			list.add(datawatcher$watchableobject);
		}

		this.lock.readLock().unlock();
		return list;
	}

	@SideOnly(Side.CLIENT)
	public void updateEffectsFromList(List<Effect> effects)
	{
		this.lock.writeLock().lock();

		for (Effect effect : effects)
		{
			Effect existingEffect = this.effectMap.get(effect.id);

			if (existingEffect != null)
			{
				existingEffect.value = effect.value;
				this.androidPlayer.onEffectsUpdate(effect.id);
			}
		}

		this.lock.writeLock().unlock();
		this.effectsChanged = true;
	}

	public static class Effect
	{
		private final int id;
		private final boolean sendToOwner;
		private final boolean sendToOthers;
		private final int typeId;
		private Object value;
		private boolean watched;

		public Effect(int id, Object value, int typeId)
		{
			this(id, value, typeId, false, false);
		}

		public Effect(int id, Object value, int typeId, boolean sendToOwner, boolean sendToOthers)
		{
			this.id = id;
			this.value = value;
			this.typeId = typeId;
			this.sendToOwner = sendToOwner;
			this.sendToOthers = sendToOthers;
			this.watched = true;
		}

		public boolean isWatched()
		{
			return watched;
		}

		public void setWatched(boolean watched)
		{
			this.watched = watched;
		}

		public boolean isSendToOthers()
		{
			return sendToOthers;
		}
	}
}
