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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IBuildable;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.server.starmap.PacketStarMapAttack;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.GalaxyServer;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.util.MOStringHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Simeon on 6/13/2015.
 */
public class TileEntityMachineStarMap extends MOTileEntityMachineEnergy implements IWailaBodyProvider
{
    GalacticPosition position;
    GalacticPosition destination;
    int zoomLevel;
    public TileEntityMachineStarMap()
    {
        super(0);
        position = new GalacticPosition();
        destination = new GalacticPosition();
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        for (int i = 0;i < Planet.SLOT_COUNT;i++)
        {
            inventory.AddSlot(new Slot(false));
        }
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents()
    {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        if (getInventory() != inventory)
        {
            getInventory().markDirty();
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.writeCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setByte("ZoomLevel", (byte) zoomLevel);
            NBTTagCompound positionTag = new NBTTagCompound();
            NBTTagCompound destinationTag = new NBTTagCompound();
            position.writeToNBT(positionTag);
            destination.writeToNBT(destinationTag);
            nbt.setTag("GalacticPosition", positionTag);
            nbt.setTag("GalacticDestination", destinationTag);
        }
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            zoomLevel = nbt.getByte("ZoomLevel");
            position = new GalacticPosition(nbt.getCompoundTag("GalacticPosition"));
            destination = new GalacticPosition(nbt.getCompoundTag("GalacticDestination"));
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    public void zoom()
    {
        if (getZoomLevel() < getMaxZoom())
        {
            setZoomLevel(getZoomLevel() + 1);
        } else {
            setZoomLevel(0);
        }
        ForceSync();
    }

    public void setZoomLevel(int zoomLevel)
    {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel()
    {
        return zoomLevel;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord - 3,yCoord,zCoord - 3,xCoord + 3,yCoord + 5,zCoord + 3);
    }

    @Override
    public IInventory getInventory()
    {
        if (getPlanet() != null)
        {
            return getPlanet();
        }else
        {
            return inventory;
        }
    }

    public Planet getPlanet()
    {
        if (worldObj.isRemote)
        {
            return GalaxyClient.getInstance().getPlanet(destination);
        }else
        {
            return GalaxyServer.getInstance().getPlanet(destination);
        }
    }

    public Star getStar()
    {
        if (worldObj.isRemote)
        {
            return  GalaxyClient.getInstance().getStar(destination);
        }else
        {
            return GalaxyServer.getInstance().getStar(destination);
        }
    }

    public Quadrant getQuadrant()
    {
        if (worldObj.isRemote)
        {
            return GalaxyClient.getInstance().getQuadrant(destination);
        }else
        {
            return GalaxyServer.getInstance().getQuadrant(destination);
        }
    }

    public int getMaxZoom()
    {
        if (getPlanet() != null)
        {
            return 4;
        }else
        {
            return 2;
        }
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world,EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer) {
            if (world.isRemote) {
                Planet homeworld = GalaxyClient.getInstance().getHomeworld((EntityPlayer)entityLiving);
                if (homeworld != null)
                    position = new GalacticPosition(homeworld);
            } else {
                Planet homeworld = GalaxyServer.getInstance().getHomeworld((EntityPlayer)entityLiving);
                if (homeworld != null)
                    position = new GalacticPosition(homeworld);
            }

            destination = new GalacticPosition(position);
            owner = ((EntityPlayer) entityLiving).getGameProfile().getId();
        }
    }

    @Override
    public void onDestroyed()
    {

    }

    public GalacticPosition getGalaxyPosition()
    {
        return position;
    }

    public void setGalaxticPosition(GalacticPosition position)
    {
        this.position = position;
    }

    public void setDestination(GalacticPosition position)
    {
        this.destination = position;
    }

    public GalacticPosition getDestination()
    {
        return this.destination;
    }

    public SpaceBody getActiveSpaceBody() {
        switch (getZoomLevel()) {
            case 0:
                return GalaxyClient.getInstance().getTheGalaxy();
            case 1:
                return GalaxyClient.getInstance().getQuadrant(destination);
            case 2:
                return GalaxyClient.getInstance().getStar(destination);
            default:
                return GalaxyClient.getInstance().getPlanet(destination);
        }
    }

    public void SyncCommandsToServer()
    {
        if (worldObj.isRemote)
            MatterOverdrive.packetPipeline.sendToServer(new PacketStarMapClientCommands(this));
    }

    public void Attack(GalacticPosition from,GalacticPosition to,int shipID)
    {
        MatterOverdrive.packetPipeline.sendToServer(new PacketStarMapAttack(from,to,shipID));
    }

    public boolean isItemValidForSlot(int slot, ItemStack item,EntityPlayer player)
    {
        return (getPlanet() == null || getPlanet().isOwner(player)) && getInventory().isItemValidForSlot(slot,item);
    }

    public void onItemPickup(EntityPlayer player, ItemStack itemStack)
    {
        if (!worldObj.isRemote) {
            if (itemStack != null && itemStack.getItem() instanceof IBuildable) {
                ((IBuildable) itemStack.getItem()).setBuildStart(itemStack, getWorldObj().getTotalWorldTime());
            }
        }
    }

    public void onItemPlaced(ItemStack itemStack)
    {
        if (!worldObj.isRemote) {
            if (itemStack != null && itemStack.getItem() instanceof IBuildable) {
                ((IBuildable) itemStack.getItem()).setBuildStart(itemStack, getWorldObj().getTotalWorldTime());
            }
        }
    }

//	WAILA
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();
		if (te instanceof TileEntityMachineStarMap) {
			TileEntityMachineStarMap starMap = (TileEntityMachineStarMap)te;

			String[] levels = new String[]{"gui.tooltip.page.galaxy", "gui.tooltip.page.quadrant", "gui.tooltip.page.star", "gui.tooltip.page.planet", "gui.tooltip.page.planet_stats"};

			currenttip.add(String.format("%sCurrent Mode: %s%s (%d)", EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, MOStringHelper.translateToLocal(levels[starMap.zoomLevel]), starMap.zoomLevel));

		} else {
			throw new RuntimeException("Star Map WAILA provider is being used for something that is not a Star Map: " + te.getClass());
		}

		return currenttip;
	}
}
