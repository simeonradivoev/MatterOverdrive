package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import com.MO.MatterOverdrive.api.matter.IMatterConnection;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.RemoveOnlySlot;
import com.MO.MatterOverdrive.fx.ReplicatorParticle;
import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.Vector3;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class TileEntityMachineReplicator extends MOTileEntityMachineMatter implements ISidedInventory, IMatterConnection, IMatterNetworkConnection
{
	public static final int MATTER_STORAGE = 256;
	public static final int ENERGY_STORAGE = 512000;
	public  static final  int MATTER_TRANSFER = 1;

    public int OUTPUT_SLOT_ID = 0;
    public int SECOUND_OUTPUT_SLOT_ID = 1;
    public int DATABASE_SLOT_ID = 2;

    public static final int REPLICATE_SPEED_PER_MATTER = 120;
	public static final int REPLICATE_ENERGY_PER_TICK = 160;
    public static final int RADIATION_DAMAGE_DELAY = 5;
    public static final int RADIATION_RANGE = 8;
    private static Random random = new Random();
	
	public int replicateTime;
    public int replicateProgress;

    TimeTracker timeTracker;
	
	public TileEntityMachineReplicator()
	{
        super(3);
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
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot());
        SECOUND_OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot());
        DATABASE_SLOT_ID = inventory.AddSlot(new DatabaseSlot());
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
        nbt.setShort("ReplicateTime",(short)this.replicateTime);
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


    protected void manageReplicate()
	{
		if(!this.worldObj.isRemote)
		{
			if(this.isReplicating())
			{
                NBTTagCompound itemAsNBT = MatterDatabaseHelper.GetItemAsNBTAt(getStackInSlot(DATABASE_SLOT_ID),MatterDatabaseHelper.GetSelectedIndex(getStackInSlot(DATABASE_SLOT_ID)));
                ItemStack newItem = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);

                int matter = MatterHelper.getMatterAmountFromItem(newItem);
                int lastTime = REPLICATE_SPEED_PER_MATTER * matter;

                if(energyStorage.getEnergyStored() >= REPLICATE_ENERGY_PER_TICK) {
                    this.replicateTime++;
                    this.extractEnergy(ForgeDirection.DOWN, REPLICATE_ENERGY_PER_TICK, false);

                    if (this.replicateTime >= lastTime) {
                        this.replicateTime = 0;
                        this.replicateItem(itemAsNBT, newItem);

                    } else if (this.replicateTime >= lastTime - 60) {
                        SpawnReplicateParticles(lastTime - 60,lastTime);
                        if (this.replicateTime == lastTime - 30)
                            SoundHandler.PlaySoundAt(worldObj, "replicate_success", this.xCoord, this.yCoord, this.zCoord, 0.25F, 1.0F, 0.2F, 0.8F);

                    }

                    if(timeTracker.hasDelayPassed(worldObj,RADIATION_DAMAGE_DELAY))
                    {
                        manageRadiation();
                    }

                    replicateProgress = (int)(((float)replicateTime / (float)lastTime) * 100f);
                }
			}
		}
		
		if(!this.isReplicating())
        {
			this.replicateTime = 0;
            replicateProgress = 0;
		}
		
	}
	
	private void replicateItem(NBTTagCompound itemAsNBT,ItemStack newItem)
	{
		if(isActive())
        {
            if(random.nextFloat() < 1f - ((float) MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) / (float)MatterDatabaseHelper.MAX_ITEM_PROGRESS) )
            {
                if(failReplicate(MatterHelper.getMatterAmountFromItem(newItem)))
                {
                    this.matterStorage.extractMatter(ForgeDirection.EAST, MatterHelper.getMatterAmountFromItem(newItem), false);
                }
            }
            else
            {
                if(putInOutput(newItem))
                {
                    this.matterStorage.extractMatter(ForgeDirection.EAST, MatterHelper.getMatterAmountFromItem(newItem), false);
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
	
	public void SpawnReplicateParticles(int startTime,int totalTime)
	{
		if(!this.worldObj.isRemote)
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
	}
	
	public static double easeIn(double t,double b , double c, double d) 
	{
		return c*(t/=d)*t*t*t + b;
	}


	public boolean isReplicating()
	{
		if(getStackInSlot(DATABASE_SLOT_ID) == null)
			return false;

		NBTTagCompound itemAsNBT =  MatterDatabaseHelper.GetItemAsNBTAt(getStackInSlot(DATABASE_SLOT_ID), MatterDatabaseHelper.GetSelectedIndex(getStackInSlot(DATABASE_SLOT_ID)));
		ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);
		
		return MatterDatabaseHelper.GetSelectedIndex(getStackInSlot(DATABASE_SLOT_ID)) >= 0
				&& MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) > 0
				&& this.getMatterStored() >= MatterHelper.getMatterAmountFromItem(item)
				&& canReplicateIntoOutput()
                && canReplicateIntoSecoundOutput()
                && redstoneState;
	}

    @Override
    public boolean isActive()
    {
        return this.isReplicating() && energyStorage.getEnergyStored() >= REPLICATE_ENERGY_PER_TICK;
    }

    public void manageRadiation() {
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
                distance *= 5;

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
	
	private boolean canReplicateIntoOutput()
	{
		if(getStackInSlot(OUTPUT_SLOT_ID) == null)
		{
			return true;
		}
		else
		{
			if(getStackInSlot(OUTPUT_SLOT_ID).stackSize < getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize())
			{
				return true;
			}
		}
		
		return false;
	}

    private boolean canReplicateIntoSecoundOutput()
    {
        NBTTagCompound itemAsNBT =  MatterDatabaseHelper.GetItemAsNBTAt(getStackInSlot(DATABASE_SLOT_ID), MatterDatabaseHelper.GetSelectedIndex(getStackInSlot(DATABASE_SLOT_ID)));
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

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return var1 == 0 ? new int[]{this.energySlotID,DATABASE_SLOT_ID} : new int[]{OUTPUT_SLOT_ID,SECOUND_OUTPUT_SLOT_ID};
    }
    //endregion
}
