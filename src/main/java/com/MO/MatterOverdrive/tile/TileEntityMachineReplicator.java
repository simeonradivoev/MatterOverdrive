package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.api.matter.IMatterConnection;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.RemoveOnlySlot;
import com.MO.MatterOverdrive.data.inventory.ShieldingSlot;
import com.MO.MatterOverdrive.fx.ReplicatorParticle;
import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.Vector3;

import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Random;

public class TileEntityMachineReplicator extends MOTileEntityMachineMatter implements IMatterConnection, IMatterNetworkConnection
{
	public static final int MATTER_STORAGE = 256;
	public static final int ENERGY_STORAGE = 512000;
	public  static final  int MATTER_TRANSFER = 1;

    public int OUTPUT_SLOT_ID = 0;
    public int SECOUND_OUTPUT_SLOT_ID = 1;
    public int DATABASE_SLOT_ID = 2;
    public int SHIELDING_SLOT_ID = 3;

    public static final int REPLICATE_SPEED_PER_MATTER = 120;
	public static final int REPLICATE_ENERGY_PER_MATTER = 32000;
    public static final int RADIATION_DAMAGE_DELAY = 5;
    public static final int RADIATION_RANGE = 8;
    public static final double FAIL_CHANGE = 0.05;
	
	public int replicateTime;
    public int replicateProgress;

    TimeTracker timeTracker;
	
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
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        nbt.setShort("ReplicateTime", (short) this.replicateTime);
    }
	
	@Override
	public void updateEntity()
	{
        super.updateEntity();
		this.manageReplicate();
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

            NBTTagCompound itemAsNBT = GetNewItemNBT();
            ItemStack newItem = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);
            int time = getSpeed(newItem);

            if (!worldObj.isRemote)
            {
                if (energyStorage.getEnergyStored() >= getEnergyDrainPerTick(newItem))
                {
                    this.replicateTime++;
                    this.extractEnergy(ForgeDirection.DOWN, getEnergyDrainPerTick(newItem), false);

                    if (this.replicateTime >= time) {
                        this.replicateTime = 0;
                        this.replicateItem(itemAsNBT, newItem);

                    } else if (this.replicateTime == time - 30)
                        SoundHandler.PlaySoundAt(worldObj, "replicate_success", this.xCoord, this.yCoord, this.zCoord, 0.25F, 1.0F, 0.2F, 0.8F);
                }

                if (timeTracker.hasDelayPassed(worldObj, RADIATION_DAMAGE_DELAY)) {
                    manageRadiation();
                }

                replicateProgress = (int) (((float) replicateTime / (float) time) * 100f);
            }
            else
            {
                SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getLeftSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
                SpawnVentParticles(0.05f, ForgeDirection.getOrientation(BlockHelper.getRightSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);

                if (this.replicateTime >= time - 60) {
                    if (worldObj.isRemote)
                        SpawnReplicateParticles(time - 60, time);
                }
            }
        }


        if (!this.isReplicating()) {
            this.replicateTime = 0;
            replicateProgress = 0;
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
        return FAIL_CHANGE * upgradeMultiply + progressChance * 0.5 + (progressChance * 0.5) * upgradeMultiply;
    }

    public int getEnergyDrainPerTick(ItemStack itemStack)
    {
        int maxEnergy = getEnergyDrainMax(itemStack);
        return maxEnergy / getSpeed(itemStack);
    }

    public int getEnergyDrainMax(ItemStack itemStack)
    {
        int matter = MatterHelper.getMatterAmountFromItem(itemStack);
        double upgradeMultiply = getUpgradeMultiply(UpgradeTypes.PowerUsage);
        return MathHelper.round((matter * REPLICATE_ENERGY_PER_MATTER) * upgradeMultiply);
    }

    public NBTTagCompound GetNewItemNBT()
    {
        IMatterDatabase database = MatterScanner.getLink(worldObj, getStackInSlot(DATABASE_SLOT_ID));

        if (database != null)
        {
            NBTTagCompound itemAsNBT = database.getItemAsNBT(MatterScanner.getSelectedAsItem(getStackInSlot(DATABASE_SLOT_ID)));
            return itemAsNBT;
        }
        return null;
    }
	
	private void replicateItem(NBTTagCompound itemAsNBT,ItemStack newItem)
	{
		if(isActive())
        {
            int matterAmount = MatterHelper.getMatterAmountFromItem(newItem);

            if(random.nextFloat() <  getFailChance(itemAsNBT))
            {
                if(failReplicate(MatterHelper.getMatterAmountFromItem(newItem)))
                {
                    int matter = this.matterStorage.getMatterStored();
                    this.matterStorage.setMatterStored(matter - matterAmount);
                }
            }
            else
            {
                if(putInOutput(newItem))
                {
                    int matter = this.matterStorage.getMatterStored();
                    this.matterStorage.setMatterStored(matter - matterAmount);
                }
            }

            forceClientUpdate = true;
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
        if(getStackInSlot(SECOUND_OUTPUT_SLOT_ID) == null)
        {
            setInventorySlotContents(SECOUND_OUTPUT_SLOT_ID, new ItemStack(MatterOverdriveItems.matter_dust, amount));
            return true;
        }
        else
        {
            if(getStackInSlot(SECOUND_OUTPUT_SLOT_ID).getItem() == MatterOverdriveItems.matter_dust && getStackInSlot(SECOUND_OUTPUT_SLOT_ID).stackSize + amount < getStackInSlot(SECOUND_OUTPUT_SLOT_ID).getMaxStackSize())
            {
                getStackInSlot(SECOUND_OUTPUT_SLOT_ID).stackSize+=amount;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
	public void SpawnReplicateParticles(int startTime,int totalTime)
	{
        double time = (double)(this.replicateTime - startTime) / (double)(totalTime - startTime);
        double gravity = easeIn(time,0.02,0.2,1);
        int age = (int)Math.round(easeIn(time,2,10,1));
        int count = (int)Math.round(easeIn(time,1,20,1));

        for(int i = 0;i < count;i++)
        {
            double speed = 0.05D;
            double sphereX = Math.sin(this.worldObj.rand.nextDouble());
            double sphereY = Math.sin(this.worldObj.rand.nextDouble());

            Vector3 pos = MOMathHelper.randomSpherePoint(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D,new Vector3(0.5,0.5,0.5),this.worldObj.rand);
            Vector3 dir = new Vector3(this.worldObj.rand.nextDouble() * 2 - 1,(this.worldObj.rand.nextDouble()* 2 - 1) * 0.05,this.worldObj.rand.nextDouble()* 2 - 1).scale(speed);
            ReplicatorParticle replicatorParticle = new ReplicatorParticle(this.worldObj,pos.getX(),pos.getY() ,pos.getZ(),dir.getX(),dir.getY(),dir.getZ());
            replicatorParticle.setCenter(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D);

            replicatorParticle.setParticleAge(age);
            replicatorParticle.setPointGravityScale(gravity);
            Minecraft.getMinecraft().effectRenderer.addEffect(replicatorParticle);
        }
	}
	
	public static double easeIn(double t,double b , double c, double d) 
	{
		return c*(t/=d)*t*t*t + b;
	}


	public boolean isReplicating()
	{
		if(getStackInSlot(DATABASE_SLOT_ID) == null)
			return false;

        IMatterDatabase database = MatterScanner.getLink(worldObj,getStackInSlot(DATABASE_SLOT_ID));

        if(database == null)
            return false;       //no database

        NBTTagCompound itemAsNBT = database.getItemAsNBT(MatterScanner.getSelectedAsItem(getStackInSlot(DATABASE_SLOT_ID)));
        ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);

		
		return itemAsNBT != null
                && item != null
				&& MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) > 0
				&& this.getMatterStored() >= MatterHelper.getMatterAmountFromItem(item)
				&& canReplicateIntoOutput(item)
                && canReplicateIntoSecoundOutput()
                && redstoneState;
	}

    @Override
    public boolean isActive()
    {
        NBTTagCompound newItemNBT = GetNewItemNBT();
        ItemStack newItem = MatterDatabaseHelper.GetItemStackFromNBT(newItemNBT);
        return this.isReplicating() && energyStorage.getEnergyStored() >= getEnergyDrainPerTick(newItem);
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

    private boolean canReplicateIntoSecoundOutput()
    {
        IMatterDatabase database = MatterScanner.getLink(worldObj, getStackInSlot(DATABASE_SLOT_ID));
        NBTTagCompound itemAsNBT = null;

        if(database != null)
        {
            itemAsNBT = database.getItemAsNBT(MatterScanner.getSelectedAsItem(getStackInSlot(DATABASE_SLOT_ID)));
        }
        ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);

        if(getStackInSlot(SECOUND_OUTPUT_SLOT_ID) == null)
        {
            return true;
        }else
        {
            return getStackInSlot(SECOUND_OUTPUT_SLOT_ID).getItem() == MatterOverdriveItems.matter_dust && getStackInSlot(SECOUND_OUTPUT_SLOT_ID).stackSize + MatterHelper.getMatterAmountFromItem(item) < getStackInSlot(SECOUND_OUTPUT_SLOT_ID).getMaxStackSize();
        }
    }

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return true;
    }

    //region IMatterNetworkConnection
    @Override
    public boolean canConnectToNetwork(ForgeDirection direction) {
        return true;
    }
    //endregion

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
}
