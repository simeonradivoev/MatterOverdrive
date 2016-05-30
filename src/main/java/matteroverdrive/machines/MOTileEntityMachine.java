/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.machines;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.api.IUpgradeable;
import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.client.sound.MachineSound;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.TileEntityInventory;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.fx.VentParticle;
import matteroverdrive.items.SecurityProtocol;
import matteroverdrive.machines.components.ComponentConfigs;
import matteroverdrive.machines.configs.ConfigPropertyStringList;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.network.packet.server.PacketSendMachineNBT;
import matteroverdrive.tile.MOTileEntity;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @autor Simeon
 * @since 3/11/2015
 */
public abstract class MOTileEntityMachine extends MOTileEntity implements IMOTileEntity, ISidedInventory, IUpgradeable, ITickable
{
	protected static final Random random = new Random();
	protected static final UpgradeHandlerGeneric basicUpgradeHandler = new UpgradeHandlerGeneric(0.05, Double.MAX_VALUE).addUpgradeMinimum(UpgradeTypes.Speed, 0.1);

	protected final List<IMachineWatcher> watchers;
	protected final Inventory inventory;
	protected final List<IMachineComponent> components;
	private final int[] upgrade_slots;
	@SideOnly(Side.CLIENT)
	protected MachineSound sound;

	protected boolean redstoneState;
	protected boolean redstoneStateDirty = true;
	protected UUID owner;
	protected boolean playerSlotsHotbar, playerSlotsMain;
	protected ComponentConfigs configs;
	//client syncs
	private boolean lastActive;
	private boolean activeState;
	private boolean awoken;
	private boolean forceClientUpdate;

	public MOTileEntityMachine(int upgradeCount)
	{
		components = new ArrayList<>();
		upgrade_slots = new int[upgradeCount];
		inventory = new TileEntityInventory(this, "");
		registerComponents();
		RegisterSlots(inventory);
		watchers = new ArrayList<>();
	}

	@Override
	public void update()
	{
		if (!awoken)
		{
			awoken = true;
			onAwake(worldObj.isRemote ? Side.CLIENT : Side.SERVER);
		}

		if (worldObj.isRemote)
		{
			manageSound();

			if (lastActive != isActive())
			{
				onActiveChange();
				lastActive = isActive();
			}
		}
		else
		{
			activeState = getServerActive();
			if (lastActive != activeState)
			{
				forceSync();
				onActiveChange();
				lastActive = activeState;
			}

			manageRedstoneState();
			manageClientSync();
		}

		components.stream().filter(component -> component instanceof ITickable).forEach(component -> {
			try
			{
				((ITickable)component).update();
			}
			catch (Exception e)
			{
				MOLog.log(Level.FATAL, e, "There was a problem while ticking %s component %s", this, component);
			}
		});
	}

	protected void RegisterSlots(Inventory inventory)
	{
		for (int i = 0; i < upgrade_slots.length; i++)
		{
			upgrade_slots[i] = inventory.AddSlot(new UpgradeSlot(false, this));
		}
		for (IMachineComponent component : components)
		{
			component.registerSlots(inventory);
		}
	}

	protected void registerComponents()
	{
		configs = new ComponentConfigs(this);
		configs.addProperty(new ConfigPropertyStringList(
				"redstoneMode",
				"gui.config.redstone",
				new String[] {
						MOStringHelper.translateToLocal("gui.redstone_mode.low"),
						MOStringHelper.translateToLocal("gui.redstone_mode.high"),
						MOStringHelper.translateToLocal("gui.redstone_mode.disabled")},
				0
		));
		addComponent(configs);
	}

	public abstract SoundEvent getSound();

	public abstract boolean hasSound();

	public abstract boolean getServerActive();

	public abstract float soundVolume();

	public boolean getRedstoneActive()
	{
		if (getRedstoneMode() == Reference.MODE_REDSTONE_HIGH)
		{
			return redstoneState;
		}
		else if (getRedstoneMode() == Reference.MODE_REDSTONE_LOW)
		{
			return !redstoneState;
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	protected void manageSound()
	{
		float soundVolume = soundVolume();

		if (hasSound() && soundVolume > 0)
		{
			if (isActive() && !isInvalid())
			{
				if (sound == null)
				{
					float soundMultiply = 1;
					if (getBlockType() instanceof MOBlockMachine)
					{
						soundMultiply = ((MOBlockMachine)getBlockType()).volume;
					}
					if (soundMultiply > 0)
					{
						sound = new MachineSound(getSound(), SoundCategory.BLOCKS, getPos(), soundVolume() * soundMultiply, 1);
						FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
					}
				}
				else if (FMLClientHandler.instance().getClient().getSoundHandler().isSoundPlaying(sound))
				{
					sound.setVolume(soundVolume());
				}
				else
				{
					sound = null;
				}
			}
			else if (sound != null)
			{
				stopSound();
			}
		}
		else if (hasSound() && soundVolume <= 0)
		{
			stopSound();
		}
	}

	@SideOnly(Side.CLIENT)
	void stopSound()
	{
		if (sound != null)
		{
			sound.stopPlaying();
			FMLClientHandler.instance().getClient().getSoundHandler().stopSound(sound);
			sound = null;
		}
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();

		if (worldObj.isRemote)
		{
			stopSound();
		}

		MachineEvent.Unload unload = new MachineEvent.Unload();
		onMachineEvent(unload);
		onMachineEventCompoments(unload);
	}

	//region NBT
	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		if (categories.contains(MachineNBTCategory.DATA))
		{
			redstoneState = nbt.getBoolean("redstoneState");
			activeState = nbt.getBoolean("activeState");
			if (nbt.hasKey("Owner", 8) && !nbt.getString("Owner").isEmpty())
			{
				try
				{
					owner = UUID.fromString(nbt.getString("Owner"));
				}
				catch (Exception e)
				{
					MOLog.log(Level.ERROR, "Invalid Owner ID: " + nbt.getString("Owner"));
				}
			}
		}
		if (categories.contains(MachineNBTCategory.INVENTORY))
		{
			inventory.readFromNBT(nbt);
		}
		for (IMachineComponent component : components)
		{
			component.readFromNBT(nbt, categories);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		if (categories.contains(MachineNBTCategory.DATA))
		{
			nbt.setBoolean("redstoneState", redstoneState);
			nbt.setBoolean("activeState", activeState);
			if (toDisk)
			{
				if (owner != null)
				{
					nbt.setString("Owner", owner.toString());
				}
				else if (nbt.hasKey("Owner", 6))
				{
					nbt.removeTag("Owner");
				}
			}
		}
		if (categories.contains(MachineNBTCategory.INVENTORY))
		{
			inventory.writeToNBT(nbt, toDisk);
		}
		for (IMachineComponent component : components)
		{
			component.writeToNBT(nbt, categories, toDisk);
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		boolean saveTagFlag = false;

		if (!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound machineTag = new NBTTagCompound();
		NBTTagList itemTagList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (inventory.getSlot(i).keepOnDismantle() && inventory.getStackInSlot(i) != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				getStackInSlot(i).writeToNBT(itemTag);
				itemTagList.appendTag(itemTag);
				saveTagFlag = true;
			}
		}
		if (saveTagFlag)
		{
			machineTag.setTag("Items", itemTagList);
		}

		writeCustomNBT(machineTag, EnumSet.of(MachineNBTCategory.CONFIGS, MachineNBTCategory.DATA), true);
		if (hasOwner())
		{
			machineTag.setString("Owner", owner.toString());
		}

		itemStack.getTagCompound().setTag("Machine", machineTag);
	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		if (itemStack.hasTagCompound())
		{
			NBTTagCompound machineTag = itemStack.getTagCompound().getCompoundTag("Machine");
			NBTTagList itemTagList = machineTag.getTagList("Items", 10);
			for (int i = 0; i < itemTagList.tagCount(); ++i)
			{
				NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
				byte b0 = itemTag.getByte("Slot");
				inventory.setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(itemTag));
			}
			readCustomNBT(machineTag, EnumSet.of(MachineNBTCategory.CONFIGS, MachineNBTCategory.DATA));
			if (machineTag.hasKey("Owner", 8))
			{
				try
				{
					this.owner = UUID.fromString(machineTag.getString("Owner"));
				}
				catch (Exception e)
				{
					MOLog.log(Level.ERROR, e, "Invalid Owner ID: " + machineTag.getString("Owner"));
				}
			}
		}
	}
	//endregion


	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		writeCustomNBT(syncData, MachineNBTCategory.ALL_OPTS, false);
		return new SPacketUpdateTileEntity(getPos(), 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		//System.out.println("Receiving Packet From Server");
		NBTTagCompound syncData = pkt.getNbtCompound();
		if (syncData != null)
		{
			readCustomNBT(syncData, MachineNBTCategory.ALL_OPTS);
		}
	}

	protected void manageRedstoneState()
	{
		if (redstoneStateDirty)
		{
			boolean flag = redstoneState;
			for (int i = 0; i < EnumFacing.VALUES.length; i++)
			{
				if (getWorld().getRedstonePower(getPos(), EnumFacing.VALUES[i]) > 0)
				{
					redstoneState = true;
					return;
				}
			}
			redstoneStateDirty = false;
			if (flag != redstoneState)
			{
				forceClientUpdate = true;
			}

		}
	}

	protected void manageClientSync()
	{
		if (forceClientUpdate)
		{
			forceClientUpdate = false;
			MatterOverdrive.packetPipeline.sendToAllAround(new PacketSendMachineNBT(MachineNBTCategory.ALL_OPTS, this, false, false), this, 64);
			markDirty();
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if (worldObj.isRemote)
		{
			manageSound();
		}
	}

	//region Events
	protected abstract void onMachineEvent(MachineEvent event);

	protected void onMachineEventCompoments(MachineEvent event)
	{
		for (IMachineComponent component : components)
		{
			component.onMachineEvent(event);
		}
	}

	@Override
	public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		MachineEvent event = new MachineEvent.NeighborChange(world, pos, state, neighborBlock);
		onMachineEvent(event);
		onMachineEventCompoments(event);
		redstoneStateDirty = true;

	}

	@Override
	public void onDestroyed(World worldIn, BlockPos pos, IBlockState state)
	{
		MachineEvent event = new MachineEvent.Destroyed(worldIn, pos, state);
		onMachineEvent(event);
		onMachineEventCompoments(event);
	}

	@Override
	public void onPlaced(World world, EntityLivingBase entityLiving)
	{
		MachineEvent event = new MachineEvent.Placed(world, entityLiving);
		onMachineEvent(event);
		onMachineEventCompoments(event);
	}

	@Override
	public void onAdded(World world, BlockPos pos, IBlockState state)
	{
		MachineEvent event = new MachineEvent.Added(world, pos, state);
		onMachineEvent(event);
		onMachineEventCompoments(event);
	}

	protected void onActiveChange()
	{
		MachineEvent event = new MachineEvent.ActiveChange();
		onMachineEvent(event);
		onMachineEventCompoments(event);
	}

	@Override
	protected void onAwake(Side side)
	{
		MachineEvent machineEvent = new MachineEvent.Awake(side);
		onMachineEvent(machineEvent);
		onMachineEventCompoments(machineEvent);
	}

	public void onContainerOpen(Side side)
	{
		MachineEvent event = new MachineEvent.OpenContainer(side);
		onMachineEvent(event);
		onMachineEventCompoments(event);
	}
	//endregion

	//region Inventory Methods
	public boolean isItemValidForSlot(int slot, ItemStack item)
	{
		return getInventory() != null && getInventory().isItemValidForSlot(slot, item);
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{

	}

	@Override
	public int getSizeInventory()
	{
		if (getInventory() != null)
		{
			return getInventory().getSizeInventory();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (getInventory() != null)
		{
			return getInventory().getStackInSlot(slot);
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int size)
	{
		if (getInventory() != null)
		{
			return getInventory().decrStackSize(slot, size);
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (getInventory() != null)
		{
			return getInventory().removeStackFromSlot(index);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		if (getInventory() != null)
		{
			getInventory().setInventorySlotContents(slot, itemStack);
		}
	}

	@Override
	public ITextComponent getDisplayName()
	{
		if (getInventory() != null && getInventory().getDisplayName() != null)
		{
			return getInventory().getDisplayName();
		}
		else if (getBlockType() != null)
		{
			return new TextComponentString(getBlockType().getLocalizedName());
		}
		else
		{
			return new TextComponentString("");
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		if (getInventory() != null)
		{
			return getInventory().getInventoryStackLimit();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (hasOwner())
		{
			if (player.getGameProfile().getId().equals(owner) || player.capabilities.isCreativeMode)
			{
				return true;
			}
			else
			{
				for (int i = 0; i < player.inventory.getSizeInventory(); i++)
				{
					ItemStack itemStack = player.inventory.getStackInSlot(i);
					if (itemStack != null && itemStack.getItem() instanceof SecurityProtocol)
					{
						if (itemStack.hasTagCompound() && itemStack.getItemDamage() == 2 && UUID.fromString(itemStack.getTagCompound().getString("Owner")).equals(owner))
						{
							return true;
						}
					}
				}
			}
		}
		else
		{
			return true;
		}

		return false;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		if (getInventory() != null)
		{
			getInventory().openInventory(player);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		if (getInventory() != null)
		{
			getInventory().closeInventory(player);
		}
	}

	public IInventory getInventory()
	{
		return inventory;
	}

	public Inventory getInventoryContainer()
	{
		return inventory;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	//endregion

	//region Watcher Methods
	public void addWatcher(IMachineWatcher watcher)
	{
		if (!watchers.contains(watcher))
		{
			watchers.add(watcher);
			watcher.onWatcherAdded(this);
		}
	}

	public void removeWatcher(IMachineWatcher watcher)
	{
		watchers.remove(watcher);
	}
	//endregion

	public void forceSync()
	{
		forceClientUpdate = true;
	}

	@SideOnly(Side.CLIENT)
	public void sendConfigsToServer(boolean forceUpdate)
	{
		sendNBTToServer(EnumSet.of(MachineNBTCategory.CONFIGS), forceUpdate, true);
	}

	//region Upgrades
	public double getUpgradeMultiply(UpgradeTypes type)
	{
		double multiply = 1;

		//check to see if the machine is affected by this type of Update
		if (isAffectedByUpgrade(type))
		{
			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				if (inventory.getSlot(i) instanceof UpgradeSlot)
				{
					ItemStack upgradeItem = inventory.getStackInSlot(i);
					if (upgradeItem != null && MatterHelper.isUpgrade(upgradeItem))
					{
						Map<UpgradeTypes, Double> upgrades = ((IUpgrade)upgradeItem.getItem()).getUpgrades(upgradeItem);

						if (upgrades.containsKey(type))
						{
							multiply *= upgrades.get(type);
						}
					}
				}
			}

			if (getUpgradeHandler() != null)
			{
				multiply = getUpgradeHandler().affectUpgrade(type, multiply);
			}
		}
		return multiply;
	}
	//endregion

	@SideOnly(Side.CLIENT)
	public void SpawnVentParticles(float speed, EnumFacing side, int count)
	{
		for (int i = 0; i < count; i++)
		{
			Matrix4f rotation = new Matrix4f();
			Vector3f offset = new Vector3f();

			if (side == EnumFacing.UP)
			{
				rotation.rotate((float)Math.PI / 2f, new Vector3f(0, 0, 1));
				offset = new Vector3f(0.5f, 0.7f, 0.5f);
			}
			else if (side == EnumFacing.WEST)
			{
				rotation.rotate((float)Math.PI / 2f, new Vector3f(0, 0, 1));
				offset = new Vector3f(-0.2f, 0.5f, 0.5f);
			}
			else if (side == EnumFacing.EAST)
			{
				rotation.rotate((float)Math.PI / 2f, new Vector3f(0, 0, -1));
				offset = new Vector3f(1.2f, 0.5f, 0.5f);
			}
			else if (side == EnumFacing.SOUTH)
			{
				rotation.rotate((float)Math.PI / 2f, new Vector3f(1, 0, 0));
				offset = new Vector3f(0.5f, 0.5f, 1.2f);
			}
			else if (side == EnumFacing.NORTH)
			{
				rotation.rotate((float)Math.PI / 2f, new Vector3f(-1, 0, 0));
				offset = new Vector3f(0.5f, 0.5f, -0.2f);
			}


			Vector3f circle = MOMathHelper.randomCirclePoint(random.nextFloat(), random);
			circle.scale(0.4f);
			Vector4f circleTransformed = new Vector4f(circle.x, circle.y, circle.z, 1);
			Matrix4f.transform(rotation, circleTransformed, circleTransformed);

			float scale = 3f;

			VentParticle ventParticle = new VentParticle(this.worldObj, this.getPos().getX() + offset.x + circleTransformed.x, this.getPos().getY() + offset.y + circleTransformed.y, this.getPos().getZ() + offset.z + circleTransformed.z, side.getDirectionVec().getX() * speed, side.getDirectionVec().getY() * speed, side.getDirectionVec().getZ() * speed, scale);
			ventParticle.setAlphaF(0.05f);
			Minecraft.getMinecraft().effectRenderer.addEffect(ventParticle);
		}
	}

	//region Getters and settrs
	public <T extends MOBlock> T getBlockType(Class<T> type)
	{
		if (this.blockType == null)
		{
			this.blockType = this.worldObj.getBlockState(getPos()).getBlock();
		}
		if (type.isInstance(this.blockType))
		{
			return type.cast(this.blockType);
		}
		return null;
	}

	public int getRedstoneMode()
	{
		return getConfigs().getEnum("redstoneMode", 0);
	}

	public UUID getOwner()
	{
		return owner;
	}

	public boolean hasOwner()
	{
		return owner != null;
	}

	public boolean claim(ItemStack security_protocol)
	{
		try
		{
			if (owner == null)
			{
				if (security_protocol.hasTagCompound() && security_protocol.getTagCompound().hasKey("Owner", 8))
				{
					owner = UUID.fromString(security_protocol.getTagCompound().getString("Owner"));
					forceSync();
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean unclaim(ItemStack security_protocol)
	{
		try
		{
			if (owner != null)
			{
				if (security_protocol.hasTagCompound() && security_protocol.getTagCompound().hasKey("Owner", 8) && owner.equals(UUID.fromString(security_protocol.getTagCompound().getString("Owner"))))
				{
					owner = null;
					forceSync();
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void addComponent(IMachineComponent component)
	{
		components.add(component);
	}

	public boolean removeComponent(IMachineComponent component)
	{
		return components.remove(component);
	}

	public IMachineComponent removeComponent(int index)
	{
		return components.remove(index);
	}

	public IMachineComponent getComponent(int index)
	{
		return components.get(index);
	}

	public <C extends IMachineComponent> C getComponent(Class<C> componentClasss)
	{
		for (IMachineComponent component : components)
		{
			if (componentClasss.isInstance(component))
			{
				return componentClasss.cast(component);
			}
		}
		return null;
	}

	public boolean hasPlayerSlotsHotbar()
	{
		return playerSlotsHotbar;
	}

	public boolean hasPlayerSlotsMain()
	{
		return playerSlotsMain;
	}

	public float getProgress()
	{
		return 0;
	}

	public boolean isActive()
	{
		return activeState;
	}

	public void setActive(boolean active)
	{
		activeState = active;
	}

	public ComponentConfigs getConfigs()
	{
		return configs;
	}

	public IUpgradeHandler getUpgradeHandler()
	{
		return basicUpgradeHandler;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return false;
	}

	public List<IMachineWatcher> getWatchers()
	{
		return watchers;
	}
	//endregion
}
