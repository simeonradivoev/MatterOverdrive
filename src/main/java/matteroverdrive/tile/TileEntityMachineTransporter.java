package matteroverdrive.tile;

import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import li.cil.oc.common.tileentity.traits.Computer;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Simeon on 5/3/2015.
 */
@Optional.InterfaceList({
		@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.SimpleComponent"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.ManagedPeripheral")
})
public class TileEntityMachineTransporter extends MOTileEntityMachineMatter implements IMatterConnection, ITransportList, IWailaBodyProvider, IPeripheral, SimpleComponent, ManagedPeripheral
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

//	All Computers

	private enum ComputerMethod {
		getLocations,
		getSelectedLocation,
		getLocation,
		addLocation,
		setSelectedLocation,
		setName,
		setX,
		setY,
		setZ;

		Function<Object[], Object[]> handler;
	}

	{
		ComputerMethod.getLocations.handler = this::computerGetLocations;
		ComputerMethod.getSelectedLocation.handler = this::computerGetSelectedLocation;
		ComputerMethod.getLocation.handler = this::computerGetLocation;
		ComputerMethod.addLocation.handler = this::computerAddLocation;
		ComputerMethod.setSelectedLocation.handler = this::computerSetSelectedLocation;
		ComputerMethod.setName.handler = this::computerSetName;
		ComputerMethod.setX.handler = this::computerSetX;
		ComputerMethod.setY.handler = this::computerSetY;
		ComputerMethod.setZ.handler = this::computerSetZ;
	}

	private String[] methodNames = null;

	public String[] genMethodNames() {
		if (methodNames == null) {
			methodNames = new String[ComputerMethod.values().length];
			for (ComputerMethod m : ComputerMethod.values()) {
				methodNames[m.ordinal()] = m.name();
			}
		}

		return methodNames;
	}

	private Object[] callMethod(int method, Object[] args) {
		switch (method) {
			case 0:
				return callMethod(ComputerMethod.getLocations, args);
			case 1:
				return callMethod(ComputerMethod.getSelectedLocation, args);
			case 2:
				return callMethod(ComputerMethod.getLocation, args);
			case 3:
				return callMethod(ComputerMethod.addLocation, args);
			case 4:
				return callMethod(ComputerMethod.setSelectedLocation, args);
			case 5:
				return callMethod(ComputerMethod.setName, args);
			case 6:
				return callMethod(ComputerMethod.setX, args);
			case 7:
				return callMethod(ComputerMethod.setY, args);
			case 8:
				return callMethod(ComputerMethod.setZ, args);
			default:
				throw new IllegalArgumentException("Invalid method id");
		}
	}

	private Object[] callMethod(ComputerMethod method, Object[] args) {
		return method.handler.apply(args);
	}


//	Computer methods
	private Object[] computerGetLocations(Object[] args) {

		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		for (TransportLocation loc : locations) {
			HashMap<String, Object> map = new HashMap<>();

			map.put("name", loc.name);
			map.put("selected", selectedLocation == locations.indexOf(loc));
			map.put("x", loc.x);
			map.put("y", loc.y);
			map.put("z", loc.z);

			list.add(map);
		}

		return list.toArray();
	}

	private Object[] computerGetSelectedLocation(Object[] args) {
		return computerGetLocation(new Object[]{1.0});
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (First location has index 0)
	 */
	private Object[] computerGetLocation(Object[] args) {

		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("First argument must be the numerical id of the transport location");
		}

		int locNum = (int)Math.floor((Double)args[0]);

		HashMap<String, Object> map = new HashMap<>();

		TransportLocation loc = locations.get(locNum);
		map.put("name", loc.name);
		map.put("x", loc.x);
		map.put("y", loc.y);
		map.put("z", loc.z);

		return new Object[]{ map };
	}

	/**
	 * args:
	 * name (string) name of the new location
	 * x (number) x coord of the new location
	 * y (number) y coord of the new location
	 * z (number) z coord of the new location
	 */
	private Object[] computerAddLocation(Object[] args) {
		if (!(args[0] instanceof String)) {
			throw new IllegalArgumentException("First argument must be a string containing the name of the transport location");
		}
		for (int i = 1; i <= 4; i++) {
			if (!(args[i] instanceof Double)) {
				throw new IllegalArgumentException("Argument " + i + 1 + " must be an integer");
			}
		}
		String name = (String)args[0];
		int x = (int)Math.floor((Double)args[1]);
		int y = (int)Math.floor((Double)args[2]);
		int z = (int)Math.floor((Double)args[3]);
		addNewLocation(x, y, z, name);
		return null;
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (First location has index 0)
	 */
	private Object[] computerSetSelectedLocation(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		selectedLocation = (int)Math.floor((Double)args[0]);
		return null;
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (First location has index 0)
	 * name (string) the new name
	 */
	private Object[] computerSetName(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof String)) {
			throw new IllegalArgumentException("Argument 2 must be a string");
		}

		int locNum = (int)Math.floor((Double)args[0]);

		locations.get(locNum).name = (String)args[1];

		return null;
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (first location has index 0)
	 * x (number) the new X coordinate
	 */
	private Object[] computerSetX(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int)Math.floor((Double)args[0]);
		int newX = (int)Math.floor((Double)args[1]);

		locations.get(locNum).x = newX;

		return null;
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (first location has index 0)
	 * y (number) the new Y coordinate
	 */
	private Object[] computerSetY(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int)Math.floor((Double)args[0]);
		int newY = (int)Math.floor((Double)args[1]);

		locations.get(locNum).y = newY;

		return null;
	}

	/**
	 * args:
	 * id (number) numeric index of the location to select (first location has index 0)
	 * z (number) the new Z coordinate
	 */
	private Object[] computerSetZ(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int)Math.floor((Double)args[0]);
		int newZ = (int)Math.floor((Double)args[1]);

		locations.get(locNum).z = newZ;

		return null;
	}


//	ComputerCraft
	@Override
	public String getType() {
		return "mo_transporter";
	}

	@Override
	public String[] getMethodNames() {
		return genMethodNames();
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return callMethod(method, arguments);
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


//	Open Computers
	@Override
	public String getComponentName() {
		return "mo_transporter";
	}

	@Override
	public String[] methods() {
		return genMethodNames();
	}

	@Override
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		int methodId = Arrays.asList(genMethodNames()).indexOf(method);

		if (methodId == -1) {
			throw new NoSuchMethodException("The method " + method + " does not exist");
		}

		return callMethod(methodId, args.toArray());
	}

}
