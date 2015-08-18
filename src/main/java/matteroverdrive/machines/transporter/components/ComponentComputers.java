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

package matteroverdrive.machines.transporter.components;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.configs.IConfigProperty;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Simeon on 7/21/2015.
 */
@Optional.InterfaceList({
        @Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
})
public class ComponentComputers extends MachineComponentAbstract<TileEntityMachineTransporter> implements IPeripheral
{
    private String[] methodNames = new String[] {
            "getLocations",
            "getSelectedLocation",
            "getLocation",
            "addLocation",
            "setSelectedLocation",
            "setName",
            "setX",
            "setY",
            "setZ",
            "setRedstoneMode"
    };
    private String peripheralName = "mo_transporter";

    public ComponentComputers(TileEntityMachineTransporter machine)
    {
        super(machine);
    }

    //region Computer methods
    private Object[] callMethod(int method, Object[] args) {
        switch (method) {
            case 0:
                return computerGetLocations(args);
            case 1:
                return computerGetSelectedLocation(args);
            case 2:
                return computerGetLocation(args);
            case 3:
                return computerAddLocation(args);
            case 4:
                return computerSetSelectedLocation(args);
            case 5:
                return computerSetName(args);
            case 6:
                return computerSetX(args);
            case 7:
                return computerSetY(args);
            case 8:
                return computerSetZ(args);
            case 9:
                return computerSetRedstoneMode(args);
            default:
                throw new IllegalArgumentException("Invalid method id");
        }
    }

    private Object[] computerGetLocations(Object[] args) {

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (TransportLocation loc : machine.getPositions()) {
            HashMap<String, Object> map = new HashMap<>();

            map.put("name", loc.name);
            map.put("selected", machine.selectedLocation == machine.getPositions().indexOf(loc));
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

        TransportLocation loc = machine.getPositions().get(locNum);
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
        machine.addNewLocation(x, y, z, name);
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
        machine.selectedLocation = (int)Math.floor((Double)args[0]);
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

        machine.getPositions().get(locNum).name = (String)args[1];

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
        machine.getPositions().get(locNum).x = (int)Math.floor((Double)args[1]);

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
        machine.getPositions().get(locNum).y = (int)Math.floor((Double)args[1]);

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
        machine.getPositions().get(locNum).z = (int)Math.floor((Double)args[1]);

        return null;
    }

    /**
     * args:
     * mode (number) the redstone mode of the transporter
     * 		0: High redstone signal
     * 		1: Low redstone signal
     * 		2: Redstone disabled
     */
    private Object[] computerSetRedstoneMode(Object[] args) {
        if (!(args[0] instanceof Double)) {
            throw new IllegalArgumentException("Argument 1 must be a number from 0 to 2");
        }

        int i = (int)Math.floor((Double)args[0]);

        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("Argument 1 must be a number from 0 to 2");
        }

        IConfigProperty property = machine.getConfigs().getProperty(Reference.CONFIG_KEY_REDSTONE_MODE);
        if (property != null)
        {
            property.setValue(i);
        }else
        {
            throw new IllegalArgumentException("No redstone mode config found for machine");
        }

        return null;
    }

    //endregion

    //region ComputerCraft
    @Override
    @Optional.Method(modid = "ComputerCraft")
    public String getType() {
        return peripheralName;
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public String[] getMethodNames() {
        return getMachine().getMethodNames();
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        try {
            return callMethod(method, arguments);
        } catch (Exception e) {
            throw new LuaException(e.getMessage());
        }
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void attach(IComputerAccess computer) {

    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void detach(IComputerAccess computer) {

    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public boolean equals(IPeripheral other) {
        return false; // TODO: Implement
    }
    //endregion

    //region Open Computers
    @Optional.Method(modid = "OpenComputers")
    public String[] methods() {
        return methodNames;
    }

    @Optional.Method(modid = "OpenComputers")
    public Object[] invoke(String method, Context context, Arguments args) throws Exception {
        int methodId = Arrays.asList(methodNames).indexOf(method);

        if (methodId == -1) {
            throw new RuntimeException("The method " + method + " does not exist");
        }

        return callMethod(methodId, args.toArray());
    }

    @Optional.Method(modid = "OpenComputers")
    public String getComponentName() {
        return peripheralName;
    }
    //endregion

    //region Component Functions
    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    public void registerSlots(Inventory inventory) {

    }

    @Override
    public void update(TileEntityMachineTransporter machine) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onActiveChange(TileEntityMachineTransporter machine) {

    }

    @Override
    public void onAwake(TileEntityMachineTransporter machine, Side side) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving, TileEntityMachineTransporter machine) {

    }
    //endregion
}
