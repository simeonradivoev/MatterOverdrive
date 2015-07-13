package matteroverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterConnection;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkConnectionProxy;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.blocks.BlockReplicator;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.inventory.ShieldingSlot;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketReplicationComplete;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;


public class TileEntityMachineReplicator extends MOTileEntityMachineMatter implements IMatterConnection, IMatterNetworkClient, IMatterNetworkConnectionProxy, IMatterNetworkDispatcher
{
	public static int MATTER_STORAGE = 1024;
	public static int ENERGY_STORAGE = 512000;
	public static final  int MATTER_TRANSFER = 2;
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
    public static double FAIL_CHANCE = 0.05;

    @SideOnly(Side.CLIENT)
    private boolean isPlayingReplicateAnimation;
    @SideOnly(Side.CLIENT)
    private int replicateAnimationCounter;
	
	public int replicateTime;
    public int replicateProgress;

    TimeTracker timeTracker;
    TimeTracker patternSearchTracker;
    MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> taskQueueProcessing;

    NBTTagCompound internalPatternStorage;
	
	public TileEntityMachineReplicator()
	{
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_STORAGE);
        this.energyStorage.setMaxReceive(ENERGY_STORAGE);
        this.matterStorage.setCapacity(MATTER_STORAGE);
        this.matterStorage.setMaxReceive(MATTER_TRANSFER);
        this.matterStorage.setMaxExtract(MATTER_TRANSFER);
        timeTracker = new TimeTracker();
        patternSearchTracker = new TimeTracker();
        taskQueueProcessing = new MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern>(this,1,MatterNetworkTaskReplicatePattern.class);
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
	public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        this.replicateTime = nbt.getShort("ReplicateTime");
        taskQueueProcessing.readFromNBT(nbt);
        internalPatternStorage = nbt.getCompoundTag("InternalPattern");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        nbt.setShort("ReplicateTime", (short) this.replicateTime);
        taskQueueProcessing.writeToNBT(nbt);

        if (internalPatternStorage != null)
            nbt.setTag("InternalPattern", internalPatternStorage);
    }

    @Override
    protected void onActiveChange() {

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

    protected void manageReplicate() {

        if (this.isReplicating()) {

            ItemStack newItem = MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage);
            int time = getSpeed(newItem);

            if (!worldObj.isRemote)
            {
                if (energyStorage.getEnergyStored() >= getEnergyDrainPerTick()) {

                    taskQueueProcessing.peek().setState(Reference.TASK_STATE_PROCESSING);
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

                    replicateProgress = (int) (((float) replicateTime / (float) time) * 100f);
                }
            }
            else
            {
                if (getBlockType(BlockReplicator.class).hasVentParticles) {
                    SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getLeftSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
                    SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getRightSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
                }

            }
        }


        if (!this.isReplicating()) {
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

    public int getSpeed(ItemStack itemStack)
    {
        int matter = MatterHelper.getMatterAmountFromItem(itemStack);
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
                        task.setState(Reference.TASK_STATE_FINISHED);
                        taskQueueProcessing.dequeueTask();
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


	public boolean isReplicating()
	{
		if(getRedstoneActive() && taskQueueProcessing.size() > 0 && internalPatternStorage != null && canCompleteTask())
        {
            ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(internalPatternStorage);
            int matter = MatterHelper.getMatterAmountFromItem(item);
            return this.getMatterStored() >= matter && canReplicateIntoOutput(item) && canReplicateIntoSecoundOutput(matter);
        }
        return false;
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
    public boolean isActive()
    {
        return this.isReplicating() && energyStorage.getEnergyStored() > getEnergyDrainPerTick();
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

    private int getShielding()
    {
        if(getStackInSlot(SHIELDING_SLOT_ID) != null && getStackInSlot(SHIELDING_SLOT_ID).getItem() == MatterOverdriveItems.tritanium_plate)
        {
            return getStackInSlot(SHIELDING_SLOT_ID).stackSize;
        }
        return 0;
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

    //region Inventory Functions
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(side == 1)
        {
            //top
            return new int[]{DATABASE_SLOT_ID};
        }
        else
        {
            return new int[]{OUTPUT_SLOT_ID,SECOUND_OUTPUT_SLOT_ID};
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }
    //endregion

    //region Matter Network functions
    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (packet instanceof MatterNetworkTaskPacket)
        {
            if (((MatterNetworkTaskPacket) packet).getTask(worldObj) instanceof MatterNetworkTaskReplicatePattern)
            {
                return taskQueueProcessing.remaintingCapacity() > 0;
            }
        }else if (packet instanceof MatterNetworkRequestPacket)
        {
            return ((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_CONNECTION || ((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION;
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        packet.addToPath(this, from);

        if (packet instanceof MatterNetworkTaskPacket) {
            if (((MatterNetworkTaskPacket) packet).getTask(worldObj) instanceof MatterNetworkTaskReplicatePattern)
            {
                MatterNetworkTaskReplicatePattern task = (MatterNetworkTaskReplicatePattern)((MatterNetworkTaskPacket) packet).getTask(worldObj);
                if (taskQueueProcessing.queueTask(task))
                {
                    task.setState(Reference.TASK_STATE_QUEUED);
                    task.setAlive(true);
                    ForceSync();
                }
            }
        }else if (packet instanceof MatterNetwrokResponcePacket)
        {
            manageResponces((MatterNetwrokResponcePacket)packet);
        }
        else if (packet instanceof MatterNetworkRequestPacket)
        {
            manageRequests((MatterNetworkRequestPacket)packet,from);
        }
    }

    private void manageRequests(MatterNetworkRequestPacket packet,ForgeDirection direction)
    {
        MatterNetworkHelper.handleConnectionRequestPacket(worldObj, this, packet, direction);
    }

    private void manageResponces(MatterNetwrokResponcePacket packet)
    {
        if (packet.getRequestType() == Reference.PACKET_REQUEST_PATTERN_SEARCH && packet.getResponceType() == Reference.PACKET_RESPONCE_VALID)
        {
            NBTTagCompound responceTag = packet.getResponce();
            MatterNetworkTaskReplicatePattern task = taskQueueProcessing.peek();
            if (responceTag != null && responceTag.getShort("id") == task.getItemID() && responceTag.getShort("Damage") == task.getItemMetadata())
            {
                if (internalPatternStorage != null)
                {
                    //if the previous tag is the same but has a higher progress, then continue
                    if (internalPatternStorage.getShort("id") == responceTag.getShort("id") && internalPatternStorage.getShort("Damage") == responceTag.getShort("Damage") && MatterDatabaseHelper.GetProgressFromNBT(internalPatternStorage) > MatterDatabaseHelper.GetProgressFromNBT(responceTag))
                    {
                        return;
                    }
                }

                internalPatternStorage = responceTag;
                ForceSync();
            }
        }
    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        int meta = worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
        return BlockHelper.getOppositeSide(meta) == side.ordinal();
    }

    @Override
    public IMatterNetworkConnection getMatterNetworkConnection()
    {
        return this;
    }

    @Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.END))
        {
            return managePatternSearch(world);
        }
        return 0;
    }

    public int managePatternSearch(World world)
    {
        int broadcasts = 0;

        if (getRedstoneActive() && !canCompleteTask() && patternSearchTracker.hasDelayPassed(worldObj,PATTERN_SEARCH_DELAY)) {

                MatterNetworkTaskReplicatePattern task = taskQueueProcessing.peek();
            if (task != null) {
                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket requestPacket = new MatterNetworkRequestPacket(this, Reference.PACKET_REQUEST_PATTERN_SEARCH,ForgeDirection.getOrientation(i), new int[]{task.getItemID(), task.getItemMetadata()});
                    if (MatterNetworkHelper.broadcastTaskInDirection(world, requestPacket, this, ForgeDirection.getOrientation(i))) {
                        broadcasts++;
                    }
                }
            }
        }
        return broadcasts;
    }

    @Override
    protected void onAwake(Side side)
    {
        if (side.isServer()) {
            MatterNetworkHelper.broadcastConnection(worldObj, this);
        }
    }

    @Override
    public MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> getQueue(byte queueID)
    {
        return taskQueueProcessing;
    }
    //endregion

    public NBTTagCompound getInternalPatternStorage()
    {
        return internalPatternStorage;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        ItemStack s = super.decrStackSize(slot, size);
        ForceSync();
        return s;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerStorage || type == UpgradeTypes.Speed || type == UpgradeTypes.Fail || type == UpgradeTypes.PowerUsage;
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
}
