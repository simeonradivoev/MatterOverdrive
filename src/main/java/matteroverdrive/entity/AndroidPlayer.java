package matteroverdrive.entity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyStorage;
import cofh.lib.audio.SoundBase;
import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IBionicStat;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.handler.AndroidStatRegistry;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Simeon on 5/26/2015.
 */
public class AndroidPlayer implements IExtendedEntityProperties, IEnergyStorage, IInventory
{
    public final static int BUILTIN_ENERGY_TRANSFER = 1024;
    public final static int TRANSFORM_TIME = 20 * 34;
    public final static String EFFECT_KEY_TURNING = "Turning";
    public final static String EXT_PROP_NAME = "AndroidPlayer";
    public final static int ENERGY_WATCHER = 29;
    public final static int ENERGY_FOOD_MULTIPLY = 256;
    public final static int ENERGY_PER_JUMP = 512;
    public final static float FALL_NEGATE = 0.5f;
    public final float DEFAULT_WLAK_SPEED = 0.1f;
    public final float DEFAULT_FLY_SPEED = 0.05f;
    public final float DEFAULT_JUMP_MOVEMENT_FACTOR = 0.02F;
    public final static float SPEED_MULTIPLY = 1.25f;
    public final static float POERLESS_SPEED_MULTIPLY = 0.25f;
    public final static int ENERGY_PER_EXOST = 16;
    public final static AttributeModifier outOfPowerSpeedModifyer = new AttributeModifier(UUID.fromString("ec778ddc-9711-498b-b9aa-8e5adc436e00"),"Android Out of Power",-0.5,2).setSaved(false);

    public final int ENERGY_SLOT;
    private final EntityPlayer player;
    private Inventory inventory;
    NBTTagCompound unlocked;
    NBTTagCompound effects;
    int maxEnergy;
    boolean isAndroid;

    public AndroidPlayer(EntityPlayer player)
    {
        this.player = player;
        this.maxEnergy = 512000;
        isAndroid = false;
        inventory = new Inventory("Android");
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_HEAD));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_ARMS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_LEGS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_CHEST));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_OTHER));
        ENERGY_SLOT = inventory.AddSlot(new EnergySlot(false));
        unlocked = new NBTTagCompound();
        effects = new NBTTagCompound();

        player.getDataWatcher().addObject(ENERGY_WATCHER,this.maxEnergy);
    }

    public static final void register(EntityPlayer player)
    {
        player.registerExtendedProperties(EXT_PROP_NAME,new AndroidPlayer(player));
    }

    public static final AndroidPlayer get(EntityPlayer player)
    {
        return (AndroidPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        NBTTagCompound prop = new NBTTagCompound();
        prop.setInteger("Energy",player.getDataWatcher().getWatchableObjectInt(ENERGY_WATCHER));
        prop.setInteger("MaxEnergy",this.maxEnergy);
        prop.setBoolean("isAndroid",isAndroid);
        prop.setTag("Stats",unlocked);
        prop.setTag("Effects",effects);
        inventory.writeToNBT(prop);
        compound.setTag(EXT_PROP_NAME,prop);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        NBTTagCompound prop = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        player.getDataWatcher().updateObject(ENERGY_WATCHER,prop.getInteger("Energy"));
        this.maxEnergy = prop.getInteger("MaxEnergy");
        this.isAndroid = prop.getBoolean("isAndroid");
        unlocked = prop.getCompoundTag("Stats");
        effects = prop.getCompoundTag("Effects");
        this.inventory.readFromNBT(prop);
    }

    @Override
    public void init(Entity entity, World world) {

    }

    public int extractEnergy(int amount,boolean simulate)
    {
        int energyExtracted;
        if (player.capabilities.isCreativeMode)
        {
            return amount;
        }

        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).getItem() instanceof IEnergyContainerItem)
        {
            ItemStack battery = getStackInSlot(ENERGY_SLOT);
            IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)battery.getItem();
            energyExtracted = energyContainerItem.extractEnergy(battery,amount,simulate);
            if (energyExtracted > 0 && !simulate)
                sync(PacketSyncAndroid.SYNC_BATTERY);
        }
        else
        {
            int energy = this.player.getDataWatcher().getWatchableObjectInt(ENERGY_WATCHER);
            energyExtracted = Math.min(Math.min(energy, amount),BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy -= energyExtracted;
                energy = MathHelper.clampI(energy,0,getMaxEnergyStored());
                this.player.getDataWatcher().updateObject(ENERGY_WATCHER,energy);
            }
        }

        return energyExtracted;
    }

    public boolean isUnlocked(IBionicStat stat,int level)
    {
        if (unlocked.hasKey(stat.getUnlocalizedName()))
        {
            return unlocked.getInteger(stat.getUnlocalizedName()) >= level;
        }
        return false;
    }

    public int getUnlockedLevel(IBionicStat stat)
    {
        if (unlocked.hasKey(stat.getUnlocalizedName()))
        {
            return unlocked.getInteger(stat.getUnlocalizedName());
        }
        return 0;
    }

    public boolean tryUnlock(IBionicStat stat, int level)
    {
        if (stat.canBeUnlocked(this,level))
        {
            unlock(stat,level);
            return true;
        }

        return false;
    }

    public void unlock(IBionicStat stat,int level)
    {
        this.unlocked.setInteger(stat.getUnlocalizedName(), level);
        stat.onUnlock(this,level);
        sync(PacketSyncAndroid.SYNC_STATS);
    }

    @Override
    public int getEnergyStored()
    {
        if (player.capabilities.isCreativeMode)
            return  getMaxEnergyStored();


        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) getStackInSlot(ENERGY_SLOT).getItem()).getEnergyStored(getStackInSlot(ENERGY_SLOT));
        }
        else
        {
            return this.player.getDataWatcher().getWatchableObjectInt(ENERGY_WATCHER);
        }
    }

    @Override
    public int getMaxEnergyStored()
    {
        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) getStackInSlot(ENERGY_SLOT).getItem()).getMaxEnergyStored(getStackInSlot(ENERGY_SLOT));
        }
        else
        {
            return maxEnergy;
        }
    }

    public int receiveEnergy(int amount,boolean simulate)
    {
        int energyReceived;
        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).getItem() instanceof IEnergyContainerItem)
        {
            ItemStack battery = getStackInSlot(ENERGY_SLOT);
            IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)battery.getItem();
            energyReceived = energyContainerItem.receiveEnergy(battery,amount,simulate);
            sync(PacketSyncAndroid.SYNC_BATTERY);
        }
        else
        {
            int energy = this.player.getDataWatcher().getWatchableObjectInt(ENERGY_WATCHER);
            energyReceived = Math.min(Math.min(getMaxEnergyStored() - energy, amount),BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy += energyReceived;
                energy = MathHelper.clampI(energy,0,getMaxEnergyStored());
                this.player.getDataWatcher().updateObject(ENERGY_WATCHER,energy);
            }
        }
        return energyReceived;
    }

    public void setAndroid(boolean isAndroid)
    {
        this.isAndroid = isAndroid;
        sync(PacketSyncAndroid.SYNC_ALL);
    }

    public boolean isAndroid()
    {
        return isAndroid;
    }

    public void sync(int part)
    {
        this.sync(player, part,false);
    }

    public void sync(int part,boolean others)
    {
        this.sync(player, part,others);
    }

    private void sync(EntityPlayer player,int syncPart,boolean toOthers)
    {
        if (player instanceof EntityPlayerMP)
        {
            if (toOthers)
            {
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketSyncAndroid(this, syncPart, toOthers), player, 64);
            }
            else
            {
                MatterOverdrive.packetPipeline.sendTo(new PacketSyncAndroid(this, syncPart, toOthers), (EntityPlayerMP) player);
            }
        }
    }

    public void setActionToServer(int action,boolean state,int options)
    {
        MatterOverdrive.packetPipeline.sendToServer(new PacketSendAndroidAnction(action,state,options));
    }

    public void copy(AndroidPlayer player)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.saveNBTData(tagCompound);
        loadNBTData(tagCompound);
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public NBTTagCompound getUnlocked()
    {
        return unlocked;
    }
    public void setUnlocked(NBTTagCompound unlocked){this.unlocked = unlocked;}
    public void resetUnlocked()
    {
        this.unlocked = new NBTTagCompound();
        sync(PacketSyncAndroid.SYNC_STATS);
        player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeAllModifiers();
    }
    public void reset(IBionicStat stat)
    {
        if (getUnlocked().hasKey(stat.getUnlocalizedName()))
        {
            getUnlocked().removeTag(stat.getUnlocalizedName());
            sync(PacketSyncAndroid.SYNC_STATS);
        }
    }

    public NBTTagCompound getEffects(){return effects;}
    public void setEffects(NBTTagCompound effects){this.effects = effects;}

    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(event.player);

        if (event.phase == TickEvent.Phase.START && androidPlayer != null)
        {
            if (androidPlayer.isAndroid())
            {
                if (getEnergyStored() > 0) {
                    if (event.player.getFoodStats().needFood() && androidPlayer.getEnergyStored() > 0) {
                        int foodNeeded = 20 - event.player.getFoodStats().getFoodLevel();
                        int extractedEnergy = androidPlayer.extractEnergy(foodNeeded * ENERGY_FOOD_MULTIPLY, false);
                        event.player.getFoodStats().addStats(extractedEnergy / ENERGY_FOOD_MULTIPLY, 0);
                    }

                    manageHasPower();
                    managePotionEffects();
                    manageSwimming();
                } else if (getEnergyStored() <= 0)
                {
                    manageOutOfPower();
                }

                for (IBionicStat stat : AndroidStatRegistry.stats.values())
                {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0)
                    {
                        if (stat.isEnabled(androidPlayer, unlockedLevel))
                        {
                            stat.changeAndroidStats(androidPlayer,unlockedLevel,true);
                            stat.onAndroidUpdate(androidPlayer, unlockedLevel);
                        }
                        else
                        {
                            stat.changeAndroidStats(androidPlayer,unlockedLevel,false);
                        }
                    }
                }
            }

            manageTurning();
            manageGlitch();
            manageCharging();
        }
    }

    public void manageOutOfPower()
    {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speed.getModifier(outOfPowerSpeedModifyer.getID()) == null)
        {
            speed.applyModifier(outOfPowerSpeedModifyer);
        }

        if (player.worldObj.isRemote)
        {
            if (player.worldObj.getWorldTime() % 60 == 0) {
                getEffects().setInteger("GlitchTime",5);
                playGlitchSoundClient(player.worldObj.rand, 0.2f);
            }
        }
    }

    public void manageHasPower()
    {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speed.getModifier(outOfPowerSpeedModifyer.getID()) != null)
        {
            speed.removeModifier(outOfPowerSpeedModifyer);
        }
    }

    public void manageCharging()
    {
        if (player.isSneaking() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IEnergyContainerItem) {
            int freeEnergy = getMaxEnergyStored() - getEnergyStored();
            int receivedAmount = ((IEnergyContainerItem) player.getHeldItem().getItem()).extractEnergy(player.getHeldItem(), freeEnergy, false);
            receiveEnergy(receivedAmount, false);
        }
    }

    public void onEntityHurt(LivingHurtEvent event)
    {
        if (isAndroid() && !event.isCanceled()) {
            effects.setInteger("GlitchTime", 10);
            sync(PacketSyncAndroid.SYNC_EFFECTS);
                player.worldObj.playSoundAtEntity(player, Reference.MOD_ID + ":" + "gui.glitch_" + player.worldObj.rand.nextInt(11), 0.2f, 0.9f + player.worldObj.rand.nextFloat() * 0.2f);
        }
    }

    public void onEntityJump(LivingEvent.LivingJumpEvent event)
    {
        if (isAndroid() && !event.entity.worldObj.isRemote)
        {
            extractEnergy(ENERGY_PER_JUMP, false);
        }
    }

    private void managePotionEffects()
    {
        if (isAndroid())
        {
            player.clearActivePotions();
        }
    }

    public double getSpeedMultiply()
    {
        return player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() / player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
    }

    private void manageGlitch()
    {
        if (effects.hasKey("GlitchTime"))
        {
            if (effects.getInteger("GlitchTime") > 0) {
                effects.setInteger("GlitchTime",effects.getInteger("GlitchTime")-1);
            }else {
                effects.removeTag("GlitchTime");
            }
        }
    }

    private void manageSwimming()
    {
        if (player.isInWater())
        {
            player.motionY = player.motionY - 0.017;
            player.setAir(300);
        }
    }

    private void manageTurning()
    {
        if (effects.hasKey(EFFECT_KEY_TURNING))
        {
            DamageSource fake = new DamageSource("android_transformation");
            fake.setDamageIsAbsolute();
            fake.setDamageBypassesArmor();

            if (effects.getInteger(EFFECT_KEY_TURNING) > 0) {
                effects.setInteger(EFFECT_KEY_TURNING, effects.getInteger(EFFECT_KEY_TURNING) - 1);
                getPlayer().addPotionEffect(new PotionEffect(9, AndroidPlayer.TRANSFORM_TIME));
                getPlayer().addPotionEffect(new PotionEffect(2, AndroidPlayer.TRANSFORM_TIME, 1));
                getPlayer().addPotionEffect(new PotionEffect(17, AndroidPlayer.TRANSFORM_TIME));
                getPlayer().addPotionEffect(new PotionEffect(18, AndroidPlayer.TRANSFORM_TIME));


                if (effects.getInteger(EFFECT_KEY_TURNING) % 40 == 0) {
                    player.attackEntityFrom(fake, 0.1f);
                    if (player.worldObj.isRemote) {
                        playGlitchSound(this,player.worldObj.rand, 0.2f);
                    }
                }
            }else
            {
                effects.removeTag(EFFECT_KEY_TURNING);
                setAndroid(true);
                playGlitchSound(this,player.worldObj.rand, 0.8f);
                player.attackEntityFrom(fake, player.getHealth() + 1);
                player.setDead();
            }

            sync(PacketSyncAndroid.SYNC_EFFECTS);
        }
    }

    public void playGlitchSound(AndroidPlayer player,Random random,float amount) {
        player.getPlayer().worldObj.playSoundEffect(player.getPlayer().posX,player.getPlayer().posY,player.getPlayer().posY,Reference.MOD_ID + ":" + "gui.glitch_" + random.nextInt(11), amount, 0.9f + random.nextFloat() * 0.2f);
    }

    @SideOnly(Side.CLIENT)
    public void  playGlitchSoundClient(Random random,float amount)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SoundBase(Reference.MOD_ID + ":" + "gui.glitch_" + random.nextInt(11), amount, 0.9f + random.nextFloat() * 0.2f));
    }

    public boolean isTurning()
    {
        if (effects.hasKey(EFFECT_KEY_TURNING))
        {
            return effects.getInteger(EFFECT_KEY_TURNING) > 0;
        }
        return false;
    }

    public static void onEntityFall(LivingFallEvent event)
    {
        AndroidPlayer props = AndroidPlayer.get((EntityPlayer) event.entity);
        if (props != null && props.isAndroid())
        {
            event.distance = (event.distance * FALL_NEGATE);
        }
    }

    //region getters and setters
    public EntityPlayer getPlayer()
    {
        return player;
    }
    public void startTurningToAndroid()
    {
        effects.setInteger("Turning",TRANSFORM_TIME);
        sync(PacketSyncAndroid.SYNC_EFFECTS);
    }
    public long getEffectLong(String effect)
    {
        return getEffects().getLong(effect);
    }
    //endregion

    //region inventory
    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot,amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot,stack);
    }

    @Override
    public String getInventoryName() {
        return player.getDisplayName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public void markDirty()
    {
        sync(PacketSyncAndroid.SYNC_ALL);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot,stack);
    }

    //endregion
}
