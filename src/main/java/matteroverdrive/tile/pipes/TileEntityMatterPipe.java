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

package matteroverdrive.tile.pipes;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.data.MatterStorage;
import matteroverdrive.data.transport.FluidPipeNetwork;
import matteroverdrive.data.transport.IFluidPipe;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.client.PacketMatterUpdate;
import matteroverdrive.util.TimeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityMatterPipe extends TileEntityPipe implements IFluidPipe
{
	public static Random rand = new Random();
	protected final MatterStorage storage;
	protected FluidPipeNetwork fluidPipeNetwork;
	protected int transferSpeed;
	TimeTracker t;

	public TileEntityMatterPipe()
	{
		t = new TimeTracker();
		storage = new MatterStorage(32);
		this.transferSpeed = 10;
	}

	@Override
	public void update()
	{
		super.update();
		if (!worldObj.isRemote)
		{
			manageTransfer();
			manageNetwork();
		}
	}

	public void manageNetwork()
	{
		if (fluidPipeNetwork == null)
		{
			if (!tryConnectToNeighborNetworks(worldObj))
			{
				FluidPipeNetwork network = MatterOverdrive.fluidNetworkHandler.getNetwork(this);
				network.addNode(this);
			}
		}
	}

	public void manageTransfer()
	{
		if (storage.getMatterStored() > 0 && getNetwork() != null)
		{
			for (IFluidPipe pipe : getNetwork().getNodes())
			{
				for (EnumFacing direction : EnumFacing.VALUES)
				{
					TileEntity handler = pipe.getTile().getWorld().getTileEntity(pipe.getTile().getPos().offset(direction));
					if (handler != null && handler.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()) && !(handler instanceof IFluidPipe))
					{
						int amount = storage.extractMatter(handler.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).fill(new FluidStack(MatterOverdriveFluids.matterPlasma, storage.getMatterStored()), true), false);
						if (amount != 0)
						{
							MatterOverdrive.packetPipeline.sendToAllAround(new PacketMatterUpdate(handler), handler, 64);
						}
						if (storage.getMatterStored() <= 0)
						{
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean canConnectToPipe(TileEntity entity, EnumFacing direction)
	{
		if (entity != null)
		{
			if (entity instanceof TileEntityMatterPipe)
			{
				if (this.getBlockType() != entity.getBlockType())
				{
					return false;
				}
			}
			return entity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
		}
		return false;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		if (!worldObj.isRemote && categories.contains(MachineNBTCategory.DATA) && toDisk)
		{
			storage.writeToNBT(comp);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories)
	{
		if (categories.contains(MachineNBTCategory.DATA))
		{
			storage.readFromNBT(comp);
		}
	}

	@Override
	protected void onAwake(Side side)
	{

	}

//	@Override
//	public int getMatterStored()
//	{
//		return storage.getMatterStored();
//	}
//
//	@Override
//	public int getCapacity()
//	{
//		return storage.getCapacity();
//	}
//
//	@Override
//	public int receiveMatter(int amount, boolean simulate)
//	{
//		return storage.receiveMatter(amount, simulate);
//	}
//
//	@Override
//	public int extractMatter(int amount, boolean simulate)
//	{
//		return storage.extractMatter(amount, simulate);
//	}

	@Override
	public void onPlaced(World world, EntityLivingBase entityLiving)
	{

	}

	@Override
	public void onAdded(World world, BlockPos pos, IBlockState state)
	{

	}

	public boolean tryConnectToNeighborNetworks(World world)
	{
		boolean hasConnected = false;
		for (EnumFacing side : EnumFacing.VALUES)
		{
			TileEntity neighborEntity = world.getTileEntity(pos.offset(side));
			if (neighborEntity instanceof TileEntityMatterPipe && this.getBlockType() == neighborEntity.getBlockType())
			{
				if (((TileEntityMatterPipe)neighborEntity).getNetwork() != null && ((TileEntityMatterPipe)neighborEntity).getNetwork() != this.fluidPipeNetwork)
				{
					((TileEntityMatterPipe)neighborEntity).getNetwork().addNode(this);
					hasConnected = true;
				}
			}
		}
		return hasConnected;
	}

	@Override
	public void onDestroyed(World worldIn, BlockPos pos, IBlockState state)
	{
		if (fluidPipeNetwork != null)
		{
			fluidPipeNetwork.onNodeDestroy(state, this);
		}
	}

	@Override
	public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		updateSides(true);
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{

	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{

	}

	@Override
	public TileEntity getTile()
	{
		return this;
	}

	@Override
	public FluidPipeNetwork getNetwork()
	{
		return fluidPipeNetwork;
	}

	@Override
	public void setNetwork(FluidPipeNetwork network)
	{
		this.fluidPipeNetwork = network;
	}

	@Override
	public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction)
	{
		return toNode instanceof TileEntityMatterPipe;
	}

	@Override
	public boolean canConnectFromSide(IBlockState blockState, EnumFacing side)
	{
		return true;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == MatterOverdriveCapabilities.MATTER_HANDLER) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == MatterOverdriveCapabilities.MATTER_HANDLER) {
			return (T)storage;
		}
		return super.getCapability(capability, facing);
	}
}
