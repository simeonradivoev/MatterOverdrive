package matteroverdrive.tile;

import cofh.lib.util.helpers.MathHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterConnection;
import matteroverdrive.api.transport.ITransportList;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.network.packet.client.PacketSyncTransportProgress;
import matteroverdrive.util.math.MOMathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/3/2015.
 */
public class TileEntityMachineTransporter extends MOTileEntityMachineMatter implements IMatterConnection, ITransportList, IWailaBodyProvider, IPeripheral
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
    int transportTimer;
    long transportTracker;

    public TileEntityMachineTransporter()
    {
        super(4);
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxTransfer(MAX_ENERGY_EXTRACT);
        matterStorage.setCapacity(512);
        locations = new ArrayList<TransportLocation>();
        selectedLocation = 0;
        redstoneMode = Reference.MODE_REDSTONE_LOW;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        writeLocations(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        readLocations(nbt);
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
        for (int i = 0;i < locations.size();i++)
        {
            NBTTagCompound positionTag = new NBTTagCompound();
            locations.get(i).writeToNBT(positionTag);
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
    public void updateEntity()
    {
        super.updateEntity();
        manageTeleportation();
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

                        energyStorage.setEnergyStored(energyStorage.getEnergyStored() - getEnergyDrain());

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
            location.x = MathHelper.clampI(location.x,xCoord - range,xCoord + range);
            location.y = MathHelper.clampI(location.y,yCoord - range,yCoord + range);
            location.z = MathHelper.clampI(location.z,zCoord - range,zCoord + range);
            return location;
        }
        return new TransportLocation(xCoord,yCoord,zCoord,"Unknown");
    }

    public boolean isLocationValid(TransportLocation location)
    {
        return !(location.x == xCoord && location.y < yCoord + 4 && location.y > yCoord - 4 && location.z == zCoord);
    }

    public void setSelectedLocation(int x,int y,int z,String name)
    {
        if (selectedLocation < locations.size() && selectedLocation >= 0)
        {
            locations.set(selectedLocation,new TransportLocation(x,y,z,name));
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
        if (at < locations.size() && at >= 0)
            locations.remove(at);
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
        return MathHelper.round(getUpgradeMultiply(UpgradeTypes.PowerUsage) * (location.getDistance(xCoord, yCoord, zCoord) * ENERGY_PER_UNIT));
    }

    private int getSpeed()
    {
        return MathHelper.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_TIME);
    }

    private int getTransportDelay()
    {
        return MathHelper.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_DELAY);
    }

    public int getTransportRange()
    {
        return MathHelper.round(getUpgradeMultiply(UpgradeTypes.Range) * TRANSPORT_RANGE);
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
    public boolean isActive()
    {
        return transportTimer > getSpeed() - TRANSPORT_TIME;
    }

    @Override
    public float soundVolume() {
        return 0.5f;
    }

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return dir != ForgeDirection.UP;
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


//	WAILA
	@Override
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


//	ComputerCraft
	@Override
	public String getType() {
		return "MatterOverdrive_Transporter";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getLocations", "getActiveLocation", "setLocation", "setActiveLocation"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0: // getLocations
				return handleGetLocations(computer, context, arguments);
			case 1: // getActiveLocation
				return handleGetActiveLocation(computer, context, arguments);
			case 2: // setLocation
				return handleSetLocation(computer, context, arguments);
			case 3: // setActiveLocation
				return handleSetActiveLocation(computer, context, arguments);
			default:
				throw new RuntimeException(getMethodNames()[method] + " is not a valid method");
		}
	}

	@Override
	public void attach(IComputerAccess computer) {

	}

	@Override
	public void detach(IComputerAccess computer) {

	}

	@Override
	public boolean equals(IPeripheral other) { // Does this mean if it's the same type or if they're the same one?
		return false; // TODO: Implement
	}

	public Object[] handleGetLocations(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		return null;
	}

	public Object[] handleGetActiveLocation(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		return null;
	}

	public Object[] handleSetLocation(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		return null;
	}

	public Object[] handleSetActiveLocation(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		return null;
	}

}
