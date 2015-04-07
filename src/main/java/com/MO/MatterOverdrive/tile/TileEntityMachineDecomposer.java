package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;

import com.MO.MatterOverdrive.api.matter.IMatterConnection;
import com.MO.MatterOverdrive.api.matter.IMatterHandler;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.MatterSlot;
import com.MO.MatterOverdrive.data.inventory.RemoveOnlySlot;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class TileEntityMachineDecomposer extends MOTileEntityMachineMatter implements ISidedInventory, IMatterConnection, IMatterNetworkConnection
{
	public static final int MATTER_STORAGE = 256;
	public static final int ENERGY_STORAGE = 512000;
    public  static final  int MATTER_TRANSFER = 1;
    public  static final  int MATTER_EXTRACT = 1;
    public  static  final  int MATTER_EXTRACT_SPEED = 100;
    public  static  final float MATTER_DECOMPOSE_FAIL_CHANGE = 0.1f;

    public static final int DECEOPOSE_SPEED_PER_MATTER = 80;
    public static final int DECOMPOSE_ENERGY_PER_TICK= 120;

    public int INPUT_SLOT_ID = 0;
    public int OUTPUT_SLOT_ID = 1;

    private TimeTracker time;
    private static Random random = new Random();
	public int decomposeTime;
    public int decomposeProgress;
	
	public TileEntityMachineDecomposer()
	{
		super(2);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_STORAGE);
        this.energyStorage.setMaxReceive(ENERGY_STORAGE);

        this.matterStorage.setCapacity(MATTER_STORAGE);
        this.matterStorage.setMaxReceive(MATTER_TRANSFER);
        this.matterStorage.setMaxExtract(MATTER_TRANSFER);
        time = new TimeTracker();
        network = null;
	}

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        INPUT_SLOT_ID = inventory.AddSlot(new MatterSlot());
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot());
    }
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		this.manageDecompose();
        this.manageExtract();
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

    private void  manageExtract()
    {
        if(!worldObj.isRemote)
        {
            if(time.hasDelayPassed(worldObj,MATTER_EXTRACT_SPEED))
            {
                for (int i = 0; i < 6; i++)
                {
                    ForgeDirection dir = ForgeDirection.values()[i];
                    TileEntity e = worldObj.getTileEntity(this.xCoord + dir.offsetX,this.yCoord + dir.offsetY,this.zCoord + dir.offsetZ);
                    if(e instanceof IMatterHandler)
                    {
                        MatterHelper.Transfer(dir,MATTER_EXTRACT,this,(IMatterHandler)e);
                    }
                }
            }
        }
    }

	protected void manageDecompose()
	{
		boolean isDirty = false;

        if(!worldObj.isRemote)
        {
            if (this.isDecomposing())
            {
                int itemMatter = MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));

                if(this.energyStorage.getEnergyStored() >= DECOMPOSE_ENERGY_PER_TICK)
                {
                    this.decomposeTime++;
                    this.extractEnergy(ForgeDirection.DOWN, DECOMPOSE_ENERGY_PER_TICK, false);

                    if (this.decomposeTime >= DECEOPOSE_SPEED_PER_MATTER * itemMatter)
                    {
                        this.decomposeTime = 0;
                        this.decomposeItem();
                    }

                    decomposeProgress = Math.round(((float) (decomposeTime) / (float) (DECEOPOSE_SPEED_PER_MATTER * itemMatter)) * 100);
                }
            }
        }

        if (!this.isDecomposing())
            {
			this.decomposeTime = 0;
                decomposeProgress = 0;
		}
	}

	public boolean isDecomposing()
    {
        return this.getStackInSlot(INPUT_SLOT_ID) != null
                && isItemValidForSlot(0, getStackInSlot(INPUT_SLOT_ID))
                && MatterHelper.getMatterAmountFromItem(this.getStackInSlot(INPUT_SLOT_ID)) <= this.getMatterCapacity() - this.getMatterStored()
                && canPutInOutput();
    }

    @Override
    public boolean isActive()
    {
        return isDecomposing() && this.energyStorage.getEnergyStored() >= MatterHelper.getEnergyFromMatter(DECOMPOSE_ENERGY_PER_TICK,getStackInSlot(INPUT_SLOT_ID));
    }


    private boolean canPutInOutput()
    {
        if(getStackInSlot(OUTPUT_SLOT_ID) == null)
        {
            return true;
        }
        else
        {
            if(getStackInSlot(OUTPUT_SLOT_ID).getItem() == MatterOverdriveItems.matter_dust)
            {
                if(getStackInSlot(INPUT_SLOT_ID) != null)
                {
                    return  getStackInSlot(OUTPUT_SLOT_ID).stackSize + MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID)) < getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize();
                }
                else
                {
                    return getStackInSlot(OUTPUT_SLOT_ID).stackSize < getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize();
                }
            }
        }

        return false;
    }

    private void failDecompose()
    {
        if(getStackInSlot(OUTPUT_SLOT_ID) != null)
        {
            getStackInSlot(OUTPUT_SLOT_ID).stackSize+=MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));
        }
        else
        {
            setInventorySlotContents(OUTPUT_SLOT_ID, new ItemStack(MatterOverdriveItems.matter_dust, MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID))));
        }
    }
	
	private void decomposeItem() 
	{
		if(getStackInSlot(INPUT_SLOT_ID) != null && canPutInOutput())
		{
            if(random.nextFloat() < MATTER_DECOMPOSE_FAIL_CHANGE)
            {
                failDecompose();
            }
            else
            {
                int matterAmount = MatterHelper.getMatterAmountFromItem(getStackInSlot(INPUT_SLOT_ID));
                int matter = this.matterStorage.getMatterStored();
                this.matterStorage.setMatterStored(matterAmount + matter);
            }

            this.decrStackSize(0, 1);
            forceClientUpdate = true;
		}
	}

    @Override
	public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        this.decomposeTime = nbt.getShort("DecomposeTime");
    }

    @Override
	public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        nbt.setShort("DecomposeTime", (short)this.decomposeTime);
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
        return new int[]{INPUT_SLOT_ID,OUTPUT_SLOT_ID};
	}

	@Override
	public boolean canExtractItem(int i, ItemStack item,
			int j) 
	{
		return j != 0 || i != INPUT_SLOT_ID;
	}

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return true;
    }

    @Override
    public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction)
    {
        return true;
    }
}
