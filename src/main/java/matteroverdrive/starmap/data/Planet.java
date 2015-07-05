package matteroverdrive.starmap.data;

import cofh.lib.gui.GuiColor;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import matteroverdrive.starmap.GalaxyGenerator;
import matteroverdrive.starmap.gen.ISpaceBodyGen;
import cpw.mods.fml.common.FMLLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Planet extends SpaceBody implements IInventory
{
    //region Static Vars
    public static final int SLOT_COUNT = 4;
    //endregion
    //region Private Vars
    private Star star;
    private float size,orbit;
    private byte type;
    private UUID ownerUUID;
    private ItemStack[] inventory;
    private List<ItemStack> buildings;
    private List<ItemStack> fleet;
    private boolean isDirty,homeworld,generated,needsUpdate;
    private int buildingSpaces,fleetSpaces,seed;
    //endregion

    //region Constructors
    public Planet()
    {
        super();
        init();
    }

    public Planet(String name,int id)
    {
        super(name,id);
        init();
    }
    //endregion

    private void init()
    {
        inventory = new ItemStack[SLOT_COUNT];
        buildings = new ArrayList();
        fleet = new ArrayList();
    }

    //region Updates
    public void update(World world)
    {
        if (!world.isRemote)
        {
            if (needsUpdate)
            {
                needsUpdate = false;
                MatterOverdrive.packetPipeline.sendToDimention(new PacketUpdatePlanet(this), world);
            }

            for (int i = 0;i < SLOT_COUNT;i++)
            {
                ItemStack ship = getStackInSlot(i);
                if (ship != null)
                {
                    if (ship.getItem() instanceof IBuilding && canBuild((IBuilding)ship.getItem(),ship)) {
                        int buildTime = ((IBuilding) ship.getItem()).getBuildTime(ship);
                        int maxBuildTime = ((IBuilding) ship.getItem()).maxBuildTime(ship, this);
                        if (buildTime < maxBuildTime) {
                            ((IBuilding) ship.getItem()).setBuildTime(ship, buildTime + 1);
                            markDirty();
                        } else {
                            buildings.add(ship);
                            setInventorySlotContents(i, null);
                            markDirty();
                        }
                    }else if (ship.getItem() instanceof IShip && canBuild((IShip)ship.getItem(),ship))
                    {
                        int buildTime = ((IShip) ship.getItem()).getBuildTime(ship);
                        int maxBuildTime = ((IShip) ship.getItem()).maxBuildTime(ship, this);
                        if (buildTime < maxBuildTime) {
                            ((IShip) ship.getItem()).setBuildTime(ship, buildTime + 1);
                            markDirty();
                        } else {
                            fleet.add(ship);
                            if (getOwnerUUID() != null) {
                                ((IShip) ship.getItem()).setOwner(ship, getOwnerUUID());
                            }
                            setInventorySlotContents(i, null);
                            markDirty();
                        }
                    }
                }
            }
        }
    }
    //endregion

    //region Events
    public void onSave(File file,World world)
    {
        isDirty = false;
    }

    public void onTravelEvent(ItemStack ship,GalacticPosition from,World world)
    {
        if (!world.isRemote)
        {
            if (ship.getItem() instanceof IShip)
            {
                UUID ownerID = ((IShip) ship.getItem()).getOwnerID(ship);
                if (ownerID != null)
                {
                    world.func_152378_a(ownerID).addChatMessage(
                            new ChatComponentText(
                                    EnumChatFormatting.GOLD + "["+Reference.MOD_NAME+"]" +
                                    EnumChatFormatting.RESET + String.format(MOStringHelper.translateToLocal("alert.starmap.ship_arrive"),ship.getDisplayName(),name)
                            )
                    );
                }

                ((IShip) ship.getItem()).onTravel(ship,this);
                if (ship.stackSize <= 0)
                {
                    removeShip(ship);
                }

                markDirty();
                markForUpdate();
            }
        }
    }
    //endregion

    //region Read - Write
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        for (int i = 0;i < getSizeInventory();i++)
        {
            if (getStackInSlot(i) != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                getStackInSlot(i).writeToNBT(itemTag);
                tagCompound.setTag("Slot" + i, itemTag);
            }
        }
        for (int i = 0;i < buildings.size();i++)
        {
            NBTTagCompound buiildingTAG = new NBTTagCompound();
            buildings.get(i).writeToNBT(buiildingTAG);
            tagCompound.setTag("Building"+i,buiildingTAG);
        }
        for (int i = 0;i < fleet.size();i++)
        {
            NBTTagCompound shipTag = new NBTTagCompound();
            fleet.get(i).writeToNBT(shipTag);
            tagCompound.setTag("Ship"+i,shipTag);
        }
        if (ownerUUID != null)
            tagCompound.setString("OwnerUUID",ownerUUID.toString());

        tagCompound.setBoolean("Homeworld", homeworld);
        tagCompound.setFloat("Size", size);
        tagCompound.setByte("Type", type);
        tagCompound.setFloat("Orbit", orbit);
        tagCompound.setInteger("BuildingSpaces", buildingSpaces);
        tagCompound.setInteger("FleetSpaces",fleetSpaces);
        tagCompound.setInteger("Seed", seed);
    }

    public void readFromNBT(NBTTagCompound tagCompound,GalaxyGenerator generator)
    {
        super.readFromNBT(tagCompound,generator);
        buildings.clear();
        fleet.clear();
        for (int i = 0;i < getSizeInventory();i++)
        {
            if (tagCompound.hasKey("Slot" + i,10))
            {
                setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Slot" + i)));
            }
        }
        buildingSpaces = tagCompound.getInteger("BuildingSpaces");
        for (int i = 0;i < buildingSpaces;i++)
        {
            if (tagCompound.hasKey("Building" + i,10))
            {
                buildings.add(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Building"+i)));
            }
        }
        fleetSpaces = tagCompound.getInteger("FleetSpaces");
        for (int i = 0;i < fleetSpaces;i++)
        {
            if (tagCompound.hasKey("Ship"+i,10))
            {
                fleet.add(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Ship" + i)));
            }
        }
        if (tagCompound.hasKey("OwnerUUID",8))
        {
            try {
                ownerUUID = UUID.fromString(tagCompound.getString("OwnerUUID"));
            }
            catch (IllegalArgumentException e)
            {
                FMLLog.log(Level.ERROR,e,"Invalid planet owner UUID '" + tagCompound.getString("OwnerUUID") +"'",this);
            }

        }
        homeworld = tagCompound.getBoolean("Homeworld");
        size = tagCompound.getFloat("Size");
        type = tagCompound.getByte("Type");
        orbit = tagCompound.getFloat("Orbit");
        seed = tagCompound.getInteger("Seed");

        generateMissing(tagCompound, generator);
    }

    public void generateMissing(NBTTagCompound tagCompound,GalaxyGenerator galaxyGenerator)
    {
        if (galaxyGenerator != null) {
            for (ISpaceBodyGen<Planet> starGen : galaxyGenerator.getPlanetGen().getGens()) {
                galaxyGenerator.getStarRandom().setSeed(seed);
                if (starGen.generateMissing(tagCompound, this, galaxyGenerator.getStarRandom())) {
                    break;
                }
            }
        }
    }
    //endregion

    //region Getters and Setters
    @Override
    public SpaceBody getParent() {
        return star;
    }
    public void setStar(Star star)
    {
        this.star = star;
    }
    public Star getStar()
    {
        return star;
    }
    public UUID getOwnerUUID(){return ownerUUID;}
    public void setOwner(EntityPlayer player){ownerUUID = EntityPlayer.func_146094_a(player.getGameProfile());}
    public void setOwnerUUID(UUID ownerUUID){this.ownerUUID = ownerUUID;}
    public boolean hasOwner(){return ownerUUID != null;}
    public boolean isOwner(EntityPlayer player){if (hasOwner()) return getOwnerUUID().equals(EntityPlayer.func_146094_a(player.getGameProfile())); return false; }
    public void setHomeworld(boolean homeworld){this.homeworld = homeworld;}
    public boolean isHomeworld(){return homeworld;}
    public boolean isHomeworld(EntityPlayer player){if (isOwner(player)) return isHomeworld(); else return false;}
    public float getSize(){return size;}
    public void setSize(float size){this.size = size;}
    public byte getType(){return type;}
    public void setType(byte type){this.type = type;}
    public float getOrbit(){return orbit;}
    public void setOrbit(float orbit){this.orbit = orbit;}
    public int getBuildingSpaces(){return buildingSpaces;}
    public void setBuildingSpaces(int buildingSpaces){this.buildingSpaces = buildingSpaces;}
    public List<ItemStack> getBuildings(){return buildings;}
    public List<ItemStack> getFleet(){return fleet;}
    public int getFleetSpaces(){return fleetSpaces;}
    public void setFleetSpaces(int fleetSpaces){this.fleetSpaces = fleetSpaces;}
    public void setSeed(int seed){this.seed = seed;}
    public int getSeed(){return seed;}
    public boolean isGenerated(){return generated;}
    public void setGenerated(boolean generated){this.generated = generated;}
    public ItemStack getShip(int at){return fleet.get(at);}
    public void addShip(ItemStack ship){if (ship != null && ship.getItem() instanceof IShip) fleet.add(ship);}
    public boolean canAddShip(ItemStack ship,@Nullable EntityPlayer player)
    {
        if (ship != null && ship.getItem() instanceof IShip)
        {
            if (player != null && hasOwner() && isHomeworld())
            {
                return isOwner(player);
            }
            else
            {
                if (fleetCount() < fleetSpaces) {
                    return true;
                }
            }
        }
        return false;
    }
    public ItemStack removeShip(int at){if (at < fleet.size()) return fleet.remove(at); else return null;}
    public boolean removeShip(ItemStack ship){return fleet.remove(ship);}
    public void addBuilding(ItemStack building){this.buildings.add(building);}
    public int fleetCount(){return fleet.size();}
    public static GuiColor getGuiColor(Planet planet)
    {
        if (planet.hasOwner()) {
            if (planet.getOwnerUUID().equals(EntityPlayer.func_146094_a(Minecraft.getMinecraft().thePlayer.getGameProfile()))) {
                if (planet.isHomeworld())
                {
                    return Reference.COLOR_HOLO_GREEN;
                }else
                {
                    return Reference.COLOR_HOLO_YELLOW;
                }
            }else
            {
                return Reference.COLOR_HOLO_RED;
            }
        }else
        {
            return Reference.COLOR_HOLO;
        }
    }
    public void markForUpdate(){
        needsUpdate = true;
    }
    public boolean canBuild(IBuilding building,ItemStack stack)
    {
        if (buildings.size() < buildingSpaces)
        {
            return building.canBuild(stack,this);
        }
        return false;
    }

    public boolean canBuild(IShip ship,ItemStack stack)
    {
        if (fleet.size() < fleetSpaces)
        {
            return ship.canBuild(stack,this);
        }
        return false;
    }
    public boolean isDirty(){return this.isDirty;}
    //endregion

    //region Inventory
    @Override
    public int getSizeInventory()
    {
        return SLOT_COUNT;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if(slot >= 0 && slot < getSizeInventory())
        {
            return inventory[slot];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        if (inventory[slot] != null)
        {
            ItemStack itemstack;

            if (inventory[slot].stackSize <= size)
            {
                itemstack = inventory[slot];
                inventory[slot] = null;

                return itemstack;
            }
            else
            {
                itemstack = inventory[slot].splitStack(size);

                if (inventory[slot].stackSize == 0)
                {
                    inventory[slot] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if(inventory[slot] != null)
        {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }

        return null;
    }

    public int getFactoryCount()
    {
        return buildings.size();
    }

    public int getFleetCount()
    {
        return fleet.size();
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;

        if(stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return getName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty()
    {
        isDirty = true;
        markForUpdate();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if (hasOwner())
        {
            return getOwnerUUID().equals(EntityPlayer.func_146094_a(player.getGameProfile()));
        }

        return true;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot < SLOT_COUNT/2)
        {
            return stack.getItem() instanceof IBuilding;
        }else
        {
            return stack.getItem() instanceof IShip;
        }
    }
    //endregion
}
