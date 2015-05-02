package com.MO.MatterOverdrive.tile;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.fx.ReplicatorParticle;
import com.MO.MatterOverdrive.fx.VentParticle;
import com.MO.MatterOverdrive.network.packet.client.PacketMatterUpdate;
import com.MO.MatterOverdrive.util.Vector3;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.MO.MatterOverdrive.api.matter.IMatterHandler;
import com.MO.MatterOverdrive.api.matter.IMatterStorage;
import com.MO.MatterOverdrive.data.MatterStorage;
import cofh.lib.util.helpers.MathHelper;
import org.lwjgl.util.vector.*;

import java.util.Random;

public abstract class MOTileEntityMachineMatter extends MOTileEntityMachineEnergy implements IMatterHandler
{
	protected MatterStorage matterStorage;
	
	public MOTileEntityMachineMatter(int upgradesCount)
	{
        super(upgradesCount);
		matterStorage = new MatterStorage();
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt)
	{
		super.writeCustomNBT(nbt);
		matterStorage.writeToNBT(nbt);

	}
	
	@Override
	public void readCustomNBT(NBTTagCompound nbt)
	{
		super.readCustomNBT(nbt);
		matterStorage.readFromNBT(nbt);

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
			worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
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
		MatterOverdrive.packetPipeline.sendToAllAround(new PacketMatterUpdate(this),this,64);
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
}
