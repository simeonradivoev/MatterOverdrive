package com.MO.MatterOverdrive.tile;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.IUpgrade;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.blocks.includes.MOBlock;
import com.MO.MatterOverdrive.blocks.includes.MOBlockMachine;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.fx.VentParticle;
import com.MO.MatterOverdrive.sound.MachineSound;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.Map;
import java.util.Random;

/**
 * Created by Simeon on 3/11/2015.
 */
public abstract class MOTileEntityMachine extends MOTileEntity implements IMOTileEntity, ISidedInventory
{

    protected static Random random = new Random();

    //client syncs
    protected boolean lastActive;

    protected MachineSound sound;
    private final ResourceLocation soundRes;

    protected boolean redstoneState;
    protected boolean redstoneStateDirty = true;

    protected boolean forceClientUpdate;

    protected Inventory inventory;
    private int upgradeSlotCount;
    private int[] upgrade_slots;

    public MOTileEntityMachine(int upgradeGount)
    {
        soundRes = getSoundFor(getSound());
        this.upgradeSlotCount = upgradeGount;
        upgrade_slots = new int[upgradeGount];
        inventory = new Inventory(this,"");
        RegisterSlots(inventory);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if(worldObj.isRemote)
        {
            manageSound();

            if(forceClientUpdate)
            {
                updateBlock();
                forceClientUpdate = false;
            }

            return;
        }

        manageRedstoneState();
        manageClientSync();

        if(lastActive != isActive())
        {
            onActiveChange();
            lastActive = isActive();
        }
    }

    protected void RegisterSlots(Inventory inventory)
    {
        for (int i = 0;i < upgrade_slots.length; i++)
        {
            upgrade_slots[i] = inventory.AddSlot(new UpgradeSlot(false));
        }
    }

    private static ResourceLocation getSoundFor(String sound)
    {
        return sound == null ? null : new ResourceLocation(Reference.MOD_ID + ":" + sound);
    }
    public abstract String getSound();
    public abstract boolean hasSound();
    public abstract boolean isActive();
    public abstract float soundVolume();
    public void onContainerOpen(Side side)
    {

    }

    @SideOnly(Side.CLIENT)
    protected void manageSound()
    {
        if(hasSound())
        {
            if (isActive() && !isInvalid()) {
                if (sound == null) {
                    float soundMultiply = 1;
                    if (getBlockType() instanceof MOBlockMachine)
                    {
                        soundMultiply = ((MOBlockMachine) getBlockType()).volume;
                    }
                    sound = new MachineSound(soundRes, xCoord, yCoord, zCoord, soundVolume() * soundMultiply, 1);
                    FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);

                }
            } else if (sound != null) {
                sound.stopPlaying();
                sound = null;
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        redstoneState = nbt.getBoolean("redstoneState");
        forceClientUpdate = nbt.getBoolean("forceClientUpdate");
        inventory.readFromNBT(nbt);
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("redstoneState",redstoneState);
        nbt.setBoolean("forceClientUpdate", forceClientUpdate);
        forceClientUpdate = false;
        inventory.writeToNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        //System.out.println("Sending Packet To Client");
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        //System.out.println("Receiving Packet From Server");
        NBTTagCompound syncData = pkt.func_148857_g();
        if(syncData != null)
        {
            readCustomNBT(syncData);
        }
    }

    protected void manageRedstoneState()
    {
        if(redstoneStateDirty)
        {
            boolean flag = redstoneState;
            redstoneState = worldObj.getBlockPowerInput(xCoord,yCoord,zCoord) > 0;
            redstoneStateDirty = false;
            if(flag != redstoneState)
                forceClientUpdate = true;

        }
    }

    protected void manageClientSync()
    {
        if(forceClientUpdate)
        {
            updateBlock();
            markDirty();
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if(worldObj.isRemote)
        {
            manageSound();
        }
    }

    protected void onActiveChange()
    {

    }

    @Override
    public void onAdded()
    {

    }

    @Override
    public void onDestroyed()
    {

    }

    @Override
    public void onNeighborBlockChange()
    {
        redstoneStateDirty = true;
    }

    //region Inventory Methods
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        return inventory.isItemValidForSlot(slot, item);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return isItemValidForSlot(slot,item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        return inventory.decrStackSize(slot, size);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setInventorySlotContents(slot,itemStack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory()
    {
        System.out.println("Inventory Open");
    }

    @Override
    public void closeInventory() {

    }

    public Inventory getInventory()
    {
        return inventory;
    }
    //endregion

    public void ForceSync()
    {
        forceClientUpdate = true;
    }

    //region Upgrades
    public double getUpgradeMultiply(UpgradeTypes type)
    {
        double multiply = 1;

        //check to see if the machine is affected by this type of Update
        if (isAffectedBy(type)) {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                if (inventory.getSlot(i) instanceof UpgradeSlot) {
                    ItemStack upgradeItem = inventory.getStackInSlot(i);
                    if (upgradeItem != null && MatterHelper.isUpgrade(upgradeItem)) {
                        Map<UpgradeTypes, Double> upgrades = ((IUpgrade) upgradeItem.getItem()).getUpgrades(upgradeItem);

                        if (upgrades.containsKey(type)) {
                            multiply *= upgrades.get(type);
                        }
                    }
                }
            }
        }

        return multiply;
    }
    public boolean isAffectedBy(UpgradeTypes type)
    {
        return true;
    }
    //endregion

    @SideOnly(Side.CLIENT)
    protected void SpawnVentParticles(float speed,ForgeDirection side,int count)
    {
        for (int i = 0;i < count;i++)
        {
            Matrix4f rotation = new Matrix4f();
            Vector3f offset = new Vector3f();

            if (side == ForgeDirection.UP)
            {
                rotation.rotate((float) Math.PI / 2f, new Vector3f(0, 0, 1));
                offset = new Vector3f(0.5f,0.7f,0.5f);
            }
            else if (side == ForgeDirection.WEST)
            {
                rotation.rotate((float) Math.PI / 2f, new Vector3f(0, 0, 1));
                offset = new Vector3f(-0.2f,0.5f,0.5f);
            }
            else if (side == ForgeDirection.EAST)
            {
                rotation.rotate((float) Math.PI / 2f, new Vector3f(0, 0, -1));
                offset = new Vector3f(1.2f,0.5f,0.5f);
            }
            else if (side == ForgeDirection.SOUTH)
            {
                rotation.rotate((float) Math.PI / 2f, new Vector3f(1, 0, 0));
                offset = new Vector3f(0.5f,0.5f,1.2f);
            }
            else if (side == ForgeDirection.NORTH)
            {
                rotation.rotate((float) Math.PI / 2f, new Vector3f(-1, 0, 0));
                offset = new Vector3f(0.5f,0.5f,-0.2f);
            }


            Vector3f circle = MOMathHelper.randomCirclePoint(random.nextFloat(), random);
            circle.scale(0.4f);
            Vector4f circleTransformed = new Vector4f(circle.x,circle.y,circle.z,1);
            rotation.transform(rotation, circleTransformed, circleTransformed);

            float scale = 3f;

            VentParticle ventParticle = new VentParticle(this.worldObj,this.xCoord + offset.x + circleTransformed.x,this.yCoord + offset.y + circleTransformed.y,this.zCoord + offset.z + circleTransformed.z,side.offsetX * speed,side.offsetY * speed,side.offsetZ * speed,scale);
            ventParticle.setAlphaF(0.05f);
            Minecraft.getMinecraft().effectRenderer.addEffect(ventParticle);
        }
    }

    //region Getters and settrs
    public <T extends MOBlock> T getBlockType(Class<T> type)
    {
        if (this.blockType == null)
        {
            this.blockType = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        }
        if (type.isInstance(this.blockType))
        {
            return type.cast(this.blockType);
        }
        return null;
    }
    //endregion
}
