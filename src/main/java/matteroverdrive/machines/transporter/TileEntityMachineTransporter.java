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

package matteroverdrive.machines.transporter;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.transport.ITransportList;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.TeleportFlashDriveSlot;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.transporter.components.ComponentComputers;
import matteroverdrive.network.packet.client.PacketSyncTransportProgress;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.math.MOMathHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Simeon on 5/3/2015.
 */
@Optional.InterfaceList({
		@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.SimpleComponent"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.ManagedPeripheral")
})
public class TileEntityMachineTransporter extends MOTileEntityMachineMatter implements ITransportList, IWailaBodyProvider, IPeripheral, SimpleComponent, ManagedPeripheral
{
    public static final int MAX_ENTETIES_PRE_TRANSPORT = 3;
    public static final int TRANSPORT_TIME = 70;
    public static final int TRANSPORT_DELAY = 80;
    private static final int TRANSPORT_RANGE = 32;
    public static final int ENERGY_STORAGE = 1024000;
    public static final int MAX_ENERGY_EXTRACT = 32000;
    public static final int ENERGY_PER_UNIT = 16;
    public List<TransportLocation> locations;
    public int selectedLocation;
    public int usbSlotID;
    int transportTimer;
    long transportTracker;
    private ComponentComputers computerComponent;

    public TileEntityMachineTransporter()
    {
        super(5);
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxExtract(MAX_ENERGY_EXTRACT);
        matterStorage.setCapacity(512);
        locations = new ArrayList<>();
        selectedLocation = 0;
        playerSlotsHotbar = true;
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        super.RegisterSlots(inventory);
        usbSlotID = inventory.AddSlot(new TeleportFlashDriveSlot(true));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.CONFIGS))
        {
            writeLocations(nbt);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.CONFIGS)) {
            readLocations(nbt);
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    public void readLocations(NBTTagCompound nbt)
    {
        locations.clear();
        NBTTagList locationsList = nbt.getTagList("transportLocations",10);
        for (int i = 0;i < locationsList.tagCount();i++)
        {
            locations.add(new TransportLocation(locationsList.getCompoundTagAt(i)));
        }
        selectedLocation = nbt.getInteger("selectedTransport");
    }

    public void writeLocations(NBTTagCompound nbt)
    {
        NBTTagList locationsList = new NBTTagList();
        for (TransportLocation location : locations)
        {
            NBTTagCompound positionTag = new NBTTagCompound();
            location.writeToNBT(positionTag);
            locationsList.appendTag(positionTag);
        }
        nbt.setTag("transportLocations", locationsList);
        nbt.setInteger("selectedTransport", selectedLocation);
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack)
    {
        super.writeToDropItem(itemStack);
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        writeLocations(itemStack.getTagCompound());
    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack)
    {
        super.readFromPlaceItem(itemStack);
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

            readLocations(itemStack.getTagCompound());
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        manageTeleportation();
    }

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        computerComponent = new ComponentComputers(this);
        addComponent(computerComponent);
    }

    void manageTeleportation()
    {
        List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        TransportLocation position = getSelectedLocation();

        if (!worldObj.isRemote) {
            if (getEnergyStorage().getEnergyStored() > getEnergyDrain() && entities.size() > 0 && isLocationValid(getSelectedLocation()))
            {
                if (transportTracker < worldObj.getTotalWorldTime())
                {
                    transportTimer++;

                    if (transportTimer >= getSpeed())
                    {
                        for (int i = 0;i < Math.min(entities.size(),MAX_ENTETIES_PRE_TRANSPORT);i++)
                        {
                            Teleport(entities.get(i),position);
                            transportTracker = worldObj.getTotalWorldTime() + getTransportDelay();
                        }

                        energyStorage.modifyEnergyStored(-getEnergyDrain());

                        transportTimer = 0;
                        MatterOverdrive.packetPipeline.sendToDimention(new PacketSyncTransportProgress(this),worldObj);
                    }
                    else
                    {
                        MatterOverdrive.packetPipeline.sendToAllAround(new PacketSyncTransportProgress(this), this, TRANSPORT_RANGE);
                    }
                }
            }
            else
            {
                if (transportTimer != 0)
                {
                    transportTimer = 0;
                    MatterOverdrive.packetPipeline.sendToDimention(new PacketSyncTransportProgress(this),worldObj);
                }
            }

        }
        else {
            if (transportTimer > 0) {
                for (Entity entity : entities)
                {
                    SpawnReplicateParticles(entity,new Vector3f((float)entity.posX,yCoord,(float)entity.posZ));
                }


                for (Entity entity : entities)
                {
                    SpawnReplicateParticles(entity,new Vector3f(position.x,position.y-1,position.z));
                }
            }
        }
    }

    public void Teleport(Entity entity,TransportLocation position)
    {
        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase) entity).setPositionAndUpdate(position.x,position.y,position.z);
        }
        else
        {
            entity.setPosition(position.x,position.y,position.z);
        }

    }

    public TransportLocation getSelectedLocation()
    {
        if (selectedLocation < locations.size() && selectedLocation >= 0)
        {
            TransportLocation location = locations.get(selectedLocation);
            int range = getTransportRange();
            //location.x = MathHelper.clampI(location.x,xCoord - range,xCoord + range);
            //location.y = MathHelper.clampI(location.y,yCoord - range,yCoord + range);
            //location.z = MathHelper.clampI(location.z,zCoord - range,zCoord + range);
            return location;
        }
        return new TransportLocation(xCoord,yCoord,zCoord,"Unknown");
    }

    public boolean isLocationValid(TransportLocation location)
    {
        return !(location.x == xCoord && location.y < yCoord + 4 && location.y > yCoord - 4 && location.z == zCoord) && location.getDistance(xCoord,yCoord,zCoord) < getTransportRange();
    }

    public void setSelectedLocation(int x,int y,int z,String name)
    {
        if (selectedLocation < locations.size() && selectedLocation >= 0)
        {
            TransportLocation location = locations.get(selectedLocation);
            if (location != null)
            {
                location.setPosition(x,y,z);
                location.setName(name);
            }else
            {
                locations.set(selectedLocation,new TransportLocation(x,y,z,name));
            }

        }else
        {
            selectedLocation = 0;
            locations.add(new TransportLocation(x,y,z,name));
        }
    }

    public void addNewLocation(int x,int y,int z,String name)
    {
        locations.add(new TransportLocation(x,y,z,name));
    }

    public void removeLocation(int at)
    {
        if (at < locations.size() && at >= 0) {
            locations.remove(at);
            selectedLocation = net.minecraft.util.MathHelper.clamp_int(selectedLocation,0,locations.size()-1);
        }
    }

    @SideOnly(Side.CLIENT)
    public void SpawnReplicateParticles(Entity entity,Vector3f p)
    {
        double entityRadius = entity.width;
        double entityArea = Math.max(entityRadius * entity.height,0.3);

        double radiusX = entityRadius + random.nextDouble() * 0.2f;
        double radiusZ = entityRadius + random.nextDouble() * 0.2f;
        double time = Math.min((double) (transportTimer) / (double) (getTransportDelay()), 1);
        double gravity = 0.015f;
        int age = (int)Math.round(MOMathHelper.easeIn(time, 5, 15, 1));
        int count = (int)Math.round(MOMathHelper.easeIn(time, 2, entityArea * 15, 1));

        for(int i = 0;i < count;i++)
        {
            float speed = random.nextFloat() * 0.05f + 0.15f;
            float height = p.y + 1 + random.nextFloat() * entity.height;

            Vector3f origin = new Vector3f(p.x ,height, p.z);
            Vector3f pos = MOMathHelper.randomSpherePoint(origin.x,origin.y,origin.z, Vec3.createVectorHelper(radiusX, 0,radiusZ), random);
            Vector3f dir = Vector3f.cross(Vector3f.sub(origin, pos,null), new Vector3f(0,1,0),null);
            dir.scale(speed);
            ReplicatorParticle replicatorParticle = new ReplicatorParticle(this.worldObj,pos.x,pos.y ,pos.z,dir.x,dir.y,dir.z);
            replicatorParticle.setCenter(origin.x,origin.y,origin.z);

            replicatorParticle.setParticleAge(age);
            replicatorParticle.setPointGravityScale(gravity);

            Minecraft.getMinecraft().effectRenderer.addEffect(replicatorParticle);
        }
    }

    public int getEnergyDrain()
    {
        TransportLocation location = getSelectedLocation();
        return (int)Math.round(getUpgradeMultiply(UpgradeTypes.PowerUsage) * (location.getDistance(xCoord, yCoord, zCoord) * ENERGY_PER_UNIT));
    }

    private int getSpeed()
    {
        return (int)Math.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_TIME);
    }

    private int getTransportDelay()
    {
        return (int)Math.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_DELAY);
    }

    public int getTransportRange()
    {
        return (int)Math.round(getUpgradeMultiply(UpgradeTypes.Range) * TRANSPORT_RANGE);
    }

    @Override
    public String getSound()
    {
        return "transporter";
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public boolean getServerActive()
    {
        return  transportTimer > 0;
    }

    @Override
    public float soundVolume() {
        return 0.5f;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return from != ForgeDirection.UP && super.canFill(from,fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return from != ForgeDirection.UP && super.canDrain(from,fluid);
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerUsage || type == UpgradeTypes.Speed || type == UpgradeTypes.Range || type == UpgradeTypes.PowerStorage;
    }

    public void setTransportTime(int time)
    {
        transportTimer = time;
    }

    public int getTransportTime()
    {
        return transportTimer;
    }

    @Override
    public List<TransportLocation> getPositions() {
        return locations;
    }


    //region WAILA
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof TileEntityMachineTransporter) {
			TileEntityMachineTransporter transporter = (TileEntityMachineTransporter)te;

			TransportLocation location = transporter.getSelectedLocation();

			currenttip.add(String.format("%sSelected Location: %s%s", EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, location.name));
			currenttip.add(String.format("%sDestination Coords: %s X:%d Y:%d Z:%d", EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, location.x, location.y, location.z));

		} else {
			throw new RuntimeException("Transporter WAILA provider is being used for something that is not a Transporter: " + te.getClass());
		}



		return currenttip;
	}
    //endregion

    //region All Computers
    //region ComputerCraft
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String getType() {
		return computerComponent.getType();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String[] getMethodNames() {
		return computerComponent.getMethodNames();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        return computerComponent.callMethod(computer,context,method,arguments);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void attach(IComputerAccess computer) {
        computerComponent.attach(computer);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void detach(IComputerAccess computer) {
        computerComponent.attach(computer);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public boolean equals(IPeripheral other) { // Does this mean if it's the same type or if they're the same one?
		return computerComponent.equals(other);
	}
    //endregion
    //region Open Computers
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName() {
		return computerComponent.getComponentName();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public String[] methods() {
		return computerComponent.methods();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		return computerComponent.invoke(method,context,args);
	}
    //endregion
    //endregion
}
