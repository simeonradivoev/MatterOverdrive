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

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import li.cil.oc.common.block.Item;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterConnection;
import matteroverdrive.api.network.*;
import matteroverdrive.blocks.BlockReplicator;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.inventory.ShieldingSlot;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentReplicator;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketReplicationComplete;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.math.MOMathHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.util.vector.Vector3f;

import java.util.EnumSet;
import java.util.List;


public class TileEntityMachineReplicator extends MOTileEntityMachineMatter implements IMatterConnection, IMatterNetworkClient, IMatterNetworkHandler, IMatterNetworkDispatcher<MatterNetworkTaskReplicatePattern>,IMatterNetworkBroadcaster,IWailaBodyProvider
{
	public static int MATTER_STORAGE = 1024;
	public static int ENERGY_STORAGE = 512000;
	public static final int MATTER_TRANSFER = 128;
    public static final int PATTERN_SEARCH_DELAY = 60;
    public static final int REPLICATION_ANIMATION_TIME = 60;

    public int OUTPUT_SLOT_ID = 0;
    public int SECOUND_OUTPUT_SLOT_ID = 1;
    public int DATABASE_SLOT_ID = 2;
    public int SHIELDING_SLOT_ID = 3;

    public static int REPLICATE_SPEED_PER_MATTER = 120;
	public static int REPLICATE_ENERGY_PER_MATTER = 16000;
    public static final int RADIATION_DAMAGE_DELAY = 5;
    public static final int RADIATION_RANGE = 8;
    public static final double FAIL_CHANCE = 0.005;

    @SideOnly(Side.CLIENT)
    private boolean isPlayingReplicateAnimation;
    @SideOnly(Side.CLIENT)
    private int replicateAnimationCounter;
	
	public int replicateTime;
    private float replicateProgress;

    private MatterNetworkComponentReplicator networkComponent;
    private ComponentMatterNetworkConfigs componentMatterNetworkConfigs;
    private MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> taskQueueProcessing;
    private TimeTracker timeTracker;
    private NBTTagCompound internalPatternStorage;
	
	public TileEntityMachineReplicator()
	{
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_STORAGE);
        this.energyStorage.setMaxReceive(ENERGY_STORAGE);
        this.matterStorage.setCapacity(MATTER_STORAGE);
        this.matterStorage.setMaxReceive(MATTER_TRANSFER);
        this.matterStorage.setMaxExtract(MATTER_TRANSFER);
        taskQueueProcessing = new MatterNetworkTaskQueue<>(this,1);
        networkComponent = new MatterNetworkComponentReplicator(this);
        timeTracker = new TimeTracker();
        playerSlotsMain = true;
        playerSlotsHotbar = true;
	}

    protected void RegisterSlots(Inventory inventory)
    {
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false));
        SECOUND_OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false));
        DATABASE_SLOT_ID = inventory.AddSlot(new DatabaseSlot(true));
        SHIELDING_SLOT_ID = inventory.AddSlot(new ShieldingSlot(true));
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        componentMatterNetworkConfigs = new ComponentMatterNetworkConfigs(this);
        addComponent(componentMatterNetworkConfigs);
    }

    @Override
	public void updateEntity()
	{
        super.updateEntity();
		this.manageReplicate();
        if (worldObj.isRemote)
        {
            manageSpawnParticles();
        }
	}

    protected void manageReplicate() {

        if (this.isActive()) {

            ItemStack newItem = MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage);
            int time = getSpeed(newItem);

            if (!worldObj.isRemote) {
                if (taskQueueProcessing.peek().isValid(worldObj)) {
                    if (energyStorage.getEnergyStored() >= getEnergyDrainPerTick()) {


                        taskQueueProcessing.peek().setState(MatterNetworkTaskState.PROCESSING);
                        this.replicateTime++;
                        this.extractEnergy(ForgeDirection.DOWN, getEnergyDrainPerTick(), false);

                        if (this.replicateTime >= time) {
                            this.replicateTime = 0;
                            this.replicateItem(internalPatternStorage, newItem);
                            MatterOverdrive.packetPipeline.sendToDimention(new PacketReplicationComplete(this), worldObj);
                            SoundHandler.PlaySoundAt(worldObj, "replicate_success", this.xCoord, this.yCoord, this.zCoord, 0.25F * getBlockType(BlockReplicator.class).replication_volume, 1.0F, 0.2F, 0.8F);
                        }
                        if (timeTracker.hasDelayPassed(worldObj, RADIATION_DAMAGE_DELAY)) {
                            manageRadiation();
                        }

                        replicateProgress = (float) replicateTime / (float) time;
                    }
                } else {
                    taskQueueProcessing.dequeue();
                }
            }
            else
            {
                if (getBlockType(BlockReplicator.class).hasVentParticles) {
                    SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getLeftSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
                    SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getRightSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
                }

            }
        } else {
            this.replicateTime = 0;
            replicateProgress = 0;
        }

    }

    @SideOnly(Side.CLIENT)
    public void beginSpawnParticles()
    {
        replicateAnimationCounter = REPLICATION_ANIMATION_TIME;
    }

    @SideOnly(Side.CLIENT)
    public void manageSpawnParticles()
    {
        if (replicateAnimationCounter > 0)
        {
            isPlayingReplicateAnimation = true;
            SpawnReplicateParticles(REPLICATION_ANIMATION_TIME - replicateAnimationCounter);
            replicateAnimationCounter--;
        }
        else
        {
            if (isPlayingReplicateAnimation)
            {
                //sync with server so that the replicated item will be seen
                isPlayingReplicateAnimation = false;
                ForceSync();
            }
        }
    }
	
	private void replicateItem(NBTTagCompound itemAsNBT,ItemStack newItem)
	{
		if(isActive())
        {
            int matterAmount = MatterHelper.getMatterAmountFromItem(newItem);

            float chance = random.nextFloat();

            if(chance <  getFailChance(itemAsNBT))
            {
                if(failReplicate(MatterHelper.getMatterAmountFromItem(newItem)))
                {
                    int matter = this.matterStorage.getMatterStored();
                    setMatterStored(matter - matterAmount);
                }
            }
            else
            {
                if(putInOutput(newItem))
                {
                    int matter = this.matterStorage.getMatterStored();
                    setMatterStored(matter - matterAmount);
                    MatterNetworkTaskReplicatePattern task = taskQueueProcessing.peek();
                    task.setAmount(task.getAmount()-1);
                    if (task.getAmount() <= 0)
                    {
                        task.setState(MatterNetworkTaskState.FINISHED);
                        taskQueueProcessing.dequeue();
                    }
                }
            }
		}
	}
	
	private boolean putInOutput(ItemStack item)
	{
		if(getStackInSlot(OUTPUT_SLOT_ID) == null)
		{
			setInventorySlotContents(OUTPUT_SLOT_ID, item);
			return true;
		}
		else
		{
			if(getStackInSlot(OUTPUT_SLOT_ID).isStackable() && getStackInSlot(OUTPUT_SLOT_ID).getItemDamage() == item.getItemDamage() && getStackInSlot(OUTPUT_SLOT_ID).getItem() == item.getItem())
			{
				int newStackSize = getStackInSlot(OUTPUT_SLOT_ID).stackSize + 1;
				
				if(newStackSize <= getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize())
				{
                    getStackInSlot(OUTPUT_SLOT_ID).stackSize = newStackSize;
					return true;
				}
			}
		}
		
		return false;
	}

    private boolean failReplicate(int amount)
    {
        ItemStack stack = getStackInSlot(SECOUND_OUTPUT_SLOT_ID);

        if(stack == null)
        {
            stack = new ItemStack(MatterOverdriveItems.matter_dust);
            MatterOverdriveItems.matter_dust.setMatter(stack,amount);
            setInventorySlotContents(SECOUND_OUTPUT_SLOT_ID, stack);
            return true;
        }
        else
        {
            if (canReplicateIntoSecoundOutput(amount))
            {
                stack.stackSize++;
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
	public void SpawnReplicateParticles(int startTime)
	{
        double time = (double)(startTime) / (double)(REPLICATION_ANIMATION_TIME);
        double gravity = MOMathHelper.easeIn(time, 0.02, 0.2, 1);
        int age = (int)Math.round(MOMathHelper.easeIn(time, 2, 10, 1));
        int count = (int)Math.round(MOMathHelper.easeIn(time, 1, 20, 1));

        for(int i = 0;i < count;i++)
        {
            float speed = 0.05f;

            Vector3f pos = MOMathHelper.randomSpherePoint(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, Vec3.createVectorHelper(0.5,0.5,0.5), this.worldObj.rand);
            Vector3f dir = new Vector3f(random.nextFloat() * 2 - 1,(random.nextFloat()* 2 - 1) * 0.05f,random.nextFloat()* 2 - 1);
            dir.scale(speed);
            ReplicatorParticle replicatorParticle = new ReplicatorParticle(this.worldObj,pos.getX(),pos.getY() ,pos.getZ(),dir.getX(), dir.getY(), dir.getZ());
            replicatorParticle.setCenter(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D);

            replicatorParticle.setParticleAge(age);
            replicatorParticle.setPointGravityScale(gravity);
            Minecraft.getMinecraft().effectRenderer.addEffect(replicatorParticle);
        }
	}

    public boolean canCompleteTask()
    {
        MatterNetworkTaskReplicatePattern task = taskQueueProcessing.peek();
        if (task != null && internalPatternStorage != null && task.getItemID() == internalPatternStorage.getShort("id") && internalPatternStorage.getShort("Damage") == task.getItemMetadata())
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean getServerActive()
    {
		if(getRedstoneActive() && taskQueueProcessing.size() > 0 && internalPatternStorage != null && canCompleteTask())
		{
			ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage);
			int matter = MatterHelper.getMatterAmountFromItem(item);
			return this.getMatterStored() >= matter && canReplicateIntoOutput(item) && canReplicateIntoSecoundOutput(matter);
		}
		return false;
    }

    public void manageRadiation()
    {
        int shielding = getShielding();

        if(shielding >= 5)
            return;             //has full shielding

        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - RADIATION_RANGE,yCoord - RADIATION_RANGE,zCoord - RADIATION_RANGE,xCoord + RADIATION_RANGE,yCoord + RADIATION_RANGE,zCoord + RADIATION_RANGE);
        List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class,bb);
        for (Object e : entities)
        {
            if (e instanceof EntityLivingBase)
            {
                EntityLivingBase l = (EntityLivingBase) e;

                double distance = l.getDistance(xCoord,yCoord,zCoord) / RADIATION_RANGE;
                distance = net.minecraft.util.MathHelper.clamp_double(distance,0,1);
                distance = 1.0 - distance;
                distance *= 5 - shielding;

                int value = 0;
                PotionEffect[] effects = new PotionEffect[4];
                //confusion
                effects[0] = new PotionEffect(9, MathHelper.round(Math.pow(5,distance)), 0);
                //weakness
                effects[1] = new PotionEffect(18, MathHelper.round(Math.pow(10,distance)), 0);
                //hunger
                effects[2] = new PotionEffect(17, MathHelper.round(Math.pow(12,distance)), 0);
                //poison
                effects[3] = new PotionEffect(19, MathHelper.round(Math.pow(5,distance)), 0);

                for (PotionEffect effect : effects)
                {
                    if(effect.getDuration() > 0)
                        l.addPotionEffect(effect);
                }
            }
        }
    }

	private boolean canReplicateIntoOutput(ItemStack itemStack)
	{
        if (itemStack == null)
            return false;

		if(getStackInSlot(OUTPUT_SLOT_ID) == null)
		{
			return true;
		}
		else
		{
			if(itemStack.isItemEqual(getStackInSlot(OUTPUT_SLOT_ID))
                    && ItemStack.areItemStackTagsEqual(itemStack,getStackInSlot(OUTPUT_SLOT_ID))
                    && getStackInSlot(OUTPUT_SLOT_ID).stackSize < getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize())
			{
				return true;
			}
		}
		
		return false;
	}

    private boolean canReplicateIntoSecoundOutput(int matter)
    {
        ItemStack stack = getStackInSlot(SECOUND_OUTPUT_SLOT_ID);

        if (stack == null)
        {
            return true;
        }else
        {
            if (stack.getItem() == MatterOverdriveItems.matter_dust && stack.getItemDamage() == matter && stack.stackSize < stack.getMaxStackSize())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return true;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerStorage || type == UpgradeTypes.Speed || type == UpgradeTypes.Fail || type == UpgradeTypes.PowerUsage;
    }

    //region NBT
    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            this.replicateTime = nbt.getShort("ReplicateTime");
            taskQueueProcessing.readFromNBT(nbt);
            internalPatternStorage = nbt.getCompoundTag("InternalPattern");
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.writeCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setShort("ReplicateTime", (short) this.replicateTime);
            taskQueueProcessing.writeToNBT(nbt);

            if (internalPatternStorage != null)
                nbt.setTag("InternalPattern", internalPatternStorage);
        }
    }
    //endregion

    //region Inventory Functions
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[]{OUTPUT_SLOT_ID,SECOUND_OUTPUT_SLOT_ID};
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        ItemStack s = super.decrStackSize(slot, size);
        ForceSync();
        return s;
    }
    //endregion

    //region Matter Network functions
    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return networkComponent.canPreform(packet);
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        networkComponent.queuePacket(packet, from);
    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return BlockHelper.getOppositeSide(meta) == side.ordinal();
    }

    @Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        return networkComponent.onNetworkTick(world, phase);
    }

    @Override
    public MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> getTaskQueue(int queueID)
    {
        return taskQueueProcessing;
    }
    //endregion

    //region Events
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
    protected void onAwake(Side side)
    {
        if (side.isServer()) {
            MatterNetworkHelper.broadcastConnection(worldObj, this);
        }
    }

    @Override
    protected void onActiveChange() {

    }
    //endregion

    //region Waila
    @Override
    @Optional.Method(modid = "Waila")
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        List<String> list = super.getWailaBody(itemStack, currenttip, accessor, config);
        if (accessor.getTileEntity() instanceof TileEntityMachineReplicator) {

            MatterNetworkTaskReplicatePattern task = ((TileEntityMachineReplicator) accessor.getTileEntity()).getTaskQueue(0).peek();
            if (task != null) {
                ItemStack pattern = new ItemStack(Item.getItemById(task.getItemID()), task.getAmount(), task.getItemMetadata());
                list.add(EnumChatFormatting.YELLOW + String.format("Replicating %s", pattern.getDisplayName()));
            }
        }
        return list;
    }
    //endregion

    //region Getters and Setters
    public NBTTagCompound getInternalPatternStorage()
    {
        return internalPatternStorage;
    }
    public void setInternalPatternStorage(NBTTagCompound internalPatternStorage){this.internalPatternStorage = internalPatternStorage;}
    private int getShielding()
    {
        if(getStackInSlot(SHIELDING_SLOT_ID) != null && getStackInSlot(SHIELDING_SLOT_ID).getItem() == MatterOverdriveItems.tritanium_plate)
        {
            return getStackInSlot(SHIELDING_SLOT_ID).stackSize;
        }
        return 0;
    }

	@Override
    public String getSound() {
        return "machine";
    }

	@Override
    public boolean hasSound() {
        return true;
    }

	@Override
    public float soundVolume() { return 1;}

	public int getSpeed(ItemStack itemStack)
    {
        double matter = Math.log1p(MatterHelper.getMatterAmountFromItem(itemStack));
        matter *= matter;
        return MathHelper.round(((REPLICATE_SPEED_PER_MATTER * matter) - 60) * getUpgradeMultiply(UpgradeTypes.Speed)) + 60;
    }

	public double getFailChance(NBTTagCompound itemAsNBT)
    {
        double progressChance = 1.0 - ((double) MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) / (double)MatterDatabaseHelper.MAX_ITEM_PROGRESS);
        double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.Fail);
        //this does not nagate all fail chance if item is not fully scanned
        return FAIL_CHANCE * upgradeMultiply + progressChance * 0.5 + (progressChance * 0.5) * upgradeMultiply;
    }

	public int getEnergyDrainPerTick()
    {
        int maxEnergy = getEnergyDrainMax();
        return maxEnergy / getSpeed(MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage));
    }

	public int getEnergyDrainMax()
    {
        int matter = MatterHelper.getMatterAmountFromItem(MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage));
        double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.PowerUsage);
        return MathHelper.round((matter * REPLICATE_ENERGY_PER_MATTER) * upgradeMultiply);
    }

    public boolean canCompleteTask(MatterNetworkTaskReplicatePattern taskReplicatePattern)
    {
        return taskReplicatePattern != null
                && internalPatternStorage != null
                && taskReplicatePattern.getItemID() == internalPatternStorage.getShort("id")
                && internalPatternStorage.getShort("Damage") == taskReplicatePattern.getItemMetadata()
                && taskReplicatePattern.isValid(worldObj);
    }

    @Override
    public NBTTagCompound getFilter() {
        return componentMatterNetworkConfigs.getFilter();
    }
    @Override
    public float getProgress()
    {
        return replicateProgress;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }
    //endregion
}
