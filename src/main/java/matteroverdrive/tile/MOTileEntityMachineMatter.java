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

package matteroverdrive.tile;

import cpw.mods.fml.common.Optional;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.api.matter.IMatterStorage;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.MachineMatterStorage;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.client.PacketMatterUpdate;
import matteroverdrive.util.MatterHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;
import java.util.List;

public abstract class MOTileEntityMachineMatter extends MOTileEntityMachineEnergy implements IMatterHandler, IWailaBodyProvider
{
	protected MachineMatterStorage matterStorage;
	
	public MOTileEntityMachineMatter(int upgradesCount)
	{
        super(upgradesCount);
		matterStorage = new MachineMatterStorage(this,32768);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.writeCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA)) {
			matterStorage.writeToNBT(nbt);
		}

	}
	
	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA)) {
			matterStorage.readFromNBT(nbt);
		}

	}
	
	@Override
	public int getMatterStored() {
		return this.matterStorage.getMatterStored();
	}

	@Override
	public int getMatterCapacity() {
		return this.matterStorage.getCapacity();
	}

	@Override
	public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        int received = this.matterStorage.receiveMatter(side, amount, simulate);
		if (!simulate && received != 0)
		{
			updateClientMatter();
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
        return received;
	}

	@Override
	public int extractMatter(ForgeDirection direction, int amount,
			boolean simulate)
    {
		int extracted = this.matterStorage.extractMatter(direction, amount, simulate);
		if (!simulate && extracted != 0) {
			updateClientMatter();
			worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
		}
		return extracted;
	}
	
	public IMatterStorage getMatterStorage()
	{
		return this.matterStorage;
	}

	public void setMatterStored(int matter)
	{
		int lastMatter = getMatterStorage().getMatterStored();
		getMatterStorage().setMatterStored(matter);
		if (lastMatter != matter) {
			ForceSync();
			worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
		}
	}

	public void updateClientMatter()
	{
		MatterOverdrive.packetPipeline.sendToAllAround(new PacketMatterUpdate(this), this, 64);
	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		super.readFromPlaceItem(itemStack);

		if(itemStack != null)
		{
			if(itemStack.hasTagCompound())
			{
				matterStorage.readFromNBT(itemStack.getTagCompound());
			}
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		super.writeToDropItem(itemStack);

		if(itemStack != null)
		{
			if(matterStorage.getMatterStored() > 0) {
				if (!itemStack.hasTagCompound())
					itemStack.setTagCompound(new NBTTagCompound());

				matterStorage.writeToNBT(itemStack.getTagCompound());
				itemStack.getTagCompound().setInteger("MaxMatter", matterStorage.getCapacity());
				itemStack.getTagCompound().setInteger("MatterSend", matterStorage.getMaxExtract());
				itemStack.getTagCompound().setInteger("MatterReceive", matterStorage.getMaxReceive());
			}
		}
	}

//	WAILA
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof MOTileEntityMachineMatter) {
			MOTileEntityMachineMatter machine = (MOTileEntityMachineMatter)te;
			currenttip.add(EnumChatFormatting.AQUA + String.format("%s / %s %s",machine.getMatterStored(),machine.getMatterCapacity(), MatterHelper.MATTER_UNIT));

		} else {
			throw new RuntimeException("MOTileEntityMachineMatter WAILA provider is being used for something that is not a MOTileEntityMachineMatter: " + te.getClass());
		}

		return currenttip;
	}

}
