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

package matteroverdrive.entity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyStorage;
import cofh.lib.audio.SoundBase;
import cofh.lib.util.helpers.MathHelper;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.gui.GuiAndroidHud;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.client.PacketSendMinimapInfo;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketAndroidChangeAbility;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Simeon on 5/26/2015.
 */
public class AndroidPlayer implements IExtendedEntityProperties, IEnergyStorage, IInventory
{
    public final static int BUILTIN_ENERGY_TRANSFER = 1024;
    public final static int TRANSFORM_TIME = 20 * 34;
    public final static String EFFECT_KEY_TURNING = "Turning";
    public final static String EXT_PROP_NAME = "AndroidPlayer";
    public final static int ENERGY_WATCHER_DEFAULT = 29;
    private static int energyWatchID;
    public final static int ENERGY_FOOD_MULTIPLY = 256;
    public final static int ENERGY_PER_JUMP = 512;
    public final static float FALL_NEGATE = 0.5f;
    public final static int MINIMAP_SEND_TIMEOUT = 20*2;
    public static boolean TRANSFORMATION_DEATH = true;
    public static boolean REMOVE_POTION_EFFECTS = true;
    public final static AttributeModifier outOfPowerSpeedModifier = new AttributeModifier(UUID.fromString("ec778ddc-9711-498b-b9aa-8e5adc436e00"), "Android Out of Power", -0.5, 2).setSaved(false);
    private static List<IBionicStat> wheelStats = new ArrayList<>();
    private static Map<Integer,MinimapEntityInfo> entityInfoMap = new HashMap<>();
    private ItemStack[] previousBionicPatts = new ItemStack[5];


    public final int ENERGY_SLOT;
    private final EntityPlayer player;
    private Inventory inventory;
    private IBionicStat activeStat;
    NBTTagCompound unlocked;
    NBTTagCompound effects;
    int maxEnergy;
    boolean isAndroid;

    public AndroidPlayer(EntityPlayer player)
    {
        this.player = player;
        this.maxEnergy = 512000;
        inventory = new Inventory("Android");
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_HEAD));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_ARMS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_LEGS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_CHEST));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_OTHER));
        ENERGY_SLOT = inventory.AddSlot(new EnergySlot(false));
        unlocked = new NBTTagCompound();
        effects = new NBTTagCompound();

        int energyWatchID = MatterOverdrive.configHandler.getInt(ConfigurationHandler.KEY_ANDROID_ENERGY_WATCH_ID, ConfigurationHandler.CATEGORY_ABILITIES,ENERGY_WATCHER_DEFAULT);
        try
        {
            player.getDataWatcher().addObject(energyWatchID, this.maxEnergy);
        }
        catch (IllegalArgumentException e)
        {
            energyWatchID = 8;
            MOLog.log(Level.ERROR,e,"Android Energy Watch ID taken. Starting id iteration.");
            while (energyWatchID <= 31)
            {
                try
                {
                    player.getDataWatcher().addObject(energyWatchID, this.maxEnergy);
                    break;
                }
                catch (IllegalArgumentException ex)
                {
                    MOLog.log(Level.ERROR,ex,"Android Energy Watch ID '%s' taken.", energyWatchID);
                    energyWatchID++;
                }
            }
        }

        MatterOverdrive.configHandler.setInt(ConfigurationHandler.KEY_ANDROID_ENERGY_WATCH_ID, ConfigurationHandler.CATEGORY_ABILITIES, energyWatchID);
        MatterOverdrive.configHandler.save();
        AndroidPlayer.energyWatchID = energyWatchID;

        registerAttributes(player);
    }

    public void registerAttributes(EntityPlayer player)
    {
        player.getAttributeMap().registerAttribute(AndroidAttributes.attributeGlitchTime);
        player.getAttributeMap().registerAttribute(AndroidAttributes.attributeBatteryUse);
    }

    public static void register(EntityPlayer player)
    {
        player.registerExtendedProperties(EXT_PROP_NAME, new AndroidPlayer(player));
    }

    public static AndroidPlayer get(EntityPlayer player)
    {
        return (AndroidPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    public static void loadConfigs(ConfigurationHandler configurationHandler)
    {
        TRANSFORMATION_DEATH = configurationHandler.getBool("transformation_death", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, true, "Should the player die after an Android transformation");
        REMOVE_POTION_EFFECTS = configurationHandler.getBool("remove_potion_effects", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, true, "Remove all potion effects while an Android");
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        NBTTagCompound prop = new NBTTagCompound();
        prop.setInteger("Energy", player.getDataWatcher().getWatchableObjectInt(energyWatchID));
        prop.setInteger("MaxEnergy", this.maxEnergy);
        prop.setBoolean("isAndroid", isAndroid);
        prop.setTag("Stats", unlocked);
        prop.setTag("Effects", effects);
        if (activeStat != null)
            prop.setString("ActiveAbility", activeStat.getUnlocalizedName());
        inventory.writeToNBT(prop);
        compound.setTag(EXT_PROP_NAME, prop);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        NBTTagCompound prop = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        player.getDataWatcher().updateObject(energyWatchID, prop.getInteger("Energy"));
        this.maxEnergy = prop.getInteger("MaxEnergy");
        this.isAndroid = prop.getBoolean("isAndroid");
        unlocked = prop.getCompoundTag("Stats");
        effects = prop.getCompoundTag("Effects");
        if (prop.hasKey("ActiveAbility"))
        {
            activeStat = MatterOverdrive.statRegistry.getStat(prop.getString("ActiveAbility"));
        }
        this.inventory.readFromNBT(prop);
    }

    @Override
    public void init(Entity entity, World world)
    {
        manageStatAttributeModifiers();
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
            int energy = this.player.getDataWatcher().getWatchableObjectInt(energyWatchID);
            energyExtracted = Math.min(Math.min(energy, amount),BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy -= energyExtracted;
                energy = MathHelper.clampI(energy,0,getMaxEnergyStored());
                this.player.getDataWatcher().updateObject(energyWatchID,energy);
            }
        }

        return energyExtracted;
    }

    public boolean isUnlocked(IBionicStat stat, int level)
    {
        return unlocked.hasKey(stat.getUnlocalizedName()) && unlocked.getInteger(stat.getUnlocalizedName()) >= level;
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
        if (stat.canBeUnlocked(this, level))
        {
            unlock(stat,level);
            return true;
        }

        return false;
    }

    public void unlock(IBionicStat stat,int level)
    {
        clearAllStatAttributeModifiers();
        this.unlocked.setInteger(stat.getUnlocalizedName(), level);
        stat.onUnlock(this,level);
        sync(PacketSyncAndroid.SYNC_STATS);
        manageStatAttributeModifiers();
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
            return this.player.getDataWatcher().getWatchableObjectInt(energyWatchID);
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
            int energy = this.player.getDataWatcher().getWatchableObjectInt(energyWatchID);
            energyReceived = Math.min(Math.min(getMaxEnergyStored() - energy, amount), BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy += energyReceived;
                energy = MathHelper.clampI(energy, 0, getMaxEnergyStored());
                this.player.getDataWatcher().updateObject(energyWatchID, energy);
            }
        }
        return energyReceived;
    }

    public void setAndroid(boolean isAndroid)
    {
        this.isAndroid = isAndroid;
        sync(PacketSyncAndroid.SYNC_ALL);
        if (isAndroid) {
            previousBionicPatts = new ItemStack[5];
            manageStatAttributeModifiers();
        }
        else {
            clearAllStatAttributeModifiers();
            clearAllEquipmentAttributeModifiers();
        }
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

    public void sync(EntityPlayer player,int syncPart,boolean toOthers)
    {
        if (player instanceof EntityPlayerMP)
        {
            if (toOthers)
            {
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketSyncAndroid(this, syncPart), player, 64);
            }
            else
            {
                MatterOverdrive.packetPipeline.sendTo(new PacketSyncAndroid(this, syncPart), (EntityPlayerMP) player);
            }
        }
    }

    public void setActionToServer(int action,boolean state)
    {
        setActionToServer(action, state, 0);
    }

    public void setActionToServer(int action,boolean state,int options)
    {
        MatterOverdrive.packetPipeline.sendToServer(new PacketSendAndroidAnction(action, state, options));
    }

    public void copy(AndroidPlayer player)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.saveNBTData(tagCompound);
        loadNBTData(tagCompound);
        manageStatAttributeModifiers();
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public NBTTagCompound getUnlocked()
    {
        return unlocked;
    }
    public void setUnlocked(NBTTagCompound unlocked)
    {
        this.unlocked = unlocked;
    }
    public int resetUnlocked()
    {
        int xp = getResetXPRequired();
        this.unlocked = new NBTTagCompound();
        sync(PacketSyncAndroid.SYNC_STATS);
        clearAllStatAttributeModifiers();
        return xp;
    }
    public int getResetXPRequired()
    {
        int calculatedXP = 0;
        for (Object key : this.unlocked.func_150296_c())
        {
            IBionicStat stat = MatterOverdrive.statRegistry.getStat(key.toString());
            int unlocked = this.unlocked.getInteger(key.toString());
            calculatedXP += stat.getXP(this,unlocked);
        }
        return calculatedXP / 2;
    }
    public void reset(IBionicStat stat)
    {
        if (getUnlocked().hasKey(stat.getUnlocalizedName()))
        {
            getUnlocked().removeTag(stat.getUnlocalizedName());
            sync(PacketSyncAndroid.SYNC_STATS);
            manageStatAttributeModifiers();
        }
    }

    public NBTTagCompound getEffects()
	{
		return effects;
	}
    public void setEffects(NBTTagCompound effects)
	{
		this.effects = effects;
	}

    public void onAndroidTick(Side side)
    {
        if (side.isServer())
        {
            if (isAndroid())
            {
                if (getEnergyStored() > 0) {
                    if (getPlayer().getFoodStats().needFood() && getEnergyStored() > 0) {
                        int foodNeeded = 20 - getPlayer().getFoodStats().getFoodLevel();
                        int extractedEnergy = extractEnergy(foodNeeded * ENERGY_FOOD_MULTIPLY, false);
                        getPlayer().getFoodStats().addStats(extractedEnergy / ENERGY_FOOD_MULTIPLY, 0);
                    }

                    manageHasPower();
                    managePotionEffects();
                } else if (getEnergyStored() <= 0) {
                    manageOutOfPower();
                }

                manageCharging();
                manageEquipmentAttributeModifiers();

                if (!getPlayer().worldObj.isRemote)
                {
                    manageMinimapInfo();
                }
            }

            manageTurning();
        }
        if (side.isClient() && isAndroid())
        {
            manageAbilityWheel();
        }

        if (isAndroid())
        {
            manageGlitch();
            manageSwimming();
            manageAir();

            for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
                int unlockedLevel = getUnlockedLevel(stat);
                if (unlockedLevel > 0) {
                    if (stat.isEnabled(this, unlockedLevel)) {
                        stat.changeAndroidStats(this, unlockedLevel, true);
                        stat.onAndroidUpdate(this, unlockedLevel);
                    } else {
                        stat.changeAndroidStats(this, unlockedLevel, false);
                    }
                }
            }
        }
    }

    public void onPlayerLoad(PlayerEvent.LoadFromFile event)
    {
        manageStatAttributeModifiers();
    }

    public void onPlayerDeath(LivingDeathEvent event)
    {

    }

    public void onPlayerRespawn()
    {
        while (getEnergyStored() < getMaxEnergyStored())
        {
            receiveEnergy(getMaxEnergyStored(),false);
        }
    }

    private void clearAllEquipmentAttributeModifiers()
    {
        for (int j = 0; j < 5; ++j)
        {
            ItemStack itemstack = this.previousBionicPatts[j];
            ItemStack itemstack1 = this.inventory.getStackInSlot(j);

            if (itemstack1 != null && itemstack1.getItem() instanceof IBionicPart)
            {
                Multimap multimap = ((IBionicPart) itemstack1.getItem()).getModifiers(this, itemstack1);
                if (multimap != null)
                    player.getAttributeMap().removeAttributeModifiers(multimap);
            }
        }
    }

    private void manageEquipmentAttributeModifiers()
    {
        boolean needsSync = false;

        for (int j = 0; j < 5; ++j)
        {
            ItemStack itemstack = this.previousBionicPatts[j];
            ItemStack itemstack1 = this.inventory.getStackInSlot(j);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                //((WorldServer)player.worldObj).getEntityTracker().func_151247_a(player, new S04PacketEntityEquipment(player.getEntityId(), j, itemstack1));

                if (itemstack != null && itemstack.getItem() instanceof IBionicPart)
                {
                    Multimap multimap = ((IBionicPart) itemstack.getItem()).getModifiers(this, itemstack);
                    if (multimap != null)
                        player.getAttributeMap().removeAttributeModifiers(multimap);
                }

                if (itemstack1 != null && itemstack1.getItem() instanceof IBionicPart)
                {
                    Multimap multimap = ((IBionicPart) itemstack1.getItem()).getModifiers(this, itemstack1);
                    if (multimap != null)
                        player.getAttributeMap().applyAttributeModifiers(multimap);
                }

                this.previousBionicPatts[j] = itemstack1 == null ? null : itemstack1.copy();
                needsSync = true;
            }
        }

        if (needsSync)
        {
            sync(PacketSyncAndroid.SYNC_INVENTORY,true);
        }
    }

    private void clearAllStatAttributeModifiers()
    {
        for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
            int unlockedLevel = getUnlockedLevel(stat);
            Multimap multimap = stat.attributes(this, unlockedLevel);
            if (multimap != null) {
                player.getAttributeMap().removeAttributeModifiers(multimap);
            }
        }
    }

    private void manageStatAttributeModifiers()
    {
        for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
            int unlockedLevel = getUnlockedLevel(stat);
            Multimap multimap = stat.attributes(this,unlockedLevel);
            if (multimap != null) {
                if (isAndroid()) {
                    if (unlockedLevel > 0) {
                        if (stat.isEnabled(this, unlockedLevel)) {
                            player.getAttributeMap().applyAttributeModifiers(multimap);
                        } else {
                            player.getAttributeMap().removeAttributeModifiers(multimap);
                        }
                    } else {
                        player.getAttributeMap().removeAttributeModifiers(multimap);
                    }
                } else {
                    player.getAttributeMap().removeAttributeModifiers(multimap);
                }
            }
        }
    }

    private void manageMinimapInfo()
    {
        if (getPlayer() instanceof EntityPlayerMP && getPlayer().worldObj.getWorldTime() % MINIMAP_SEND_TIMEOUT == 0)
        {
            List<MinimapEntityInfo> entityList = new ArrayList<>();
            for (Object entityObject : getPlayer().worldObj.loadedEntityList) {
                if (entityObject instanceof EntityLivingBase) {
                    if (isVisibleOnMinimap((EntityLivingBase) entityObject, player, Vec3.createVectorHelper(((EntityLivingBase) entityObject).posX, ((EntityLivingBase) entityObject).posY, ((EntityLivingBase) entityObject).posZ).subtract(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))) && MinimapEntityInfo.hasInfo((EntityLivingBase)entityObject, player)) {
                        entityList.add(new MinimapEntityInfo((EntityLivingBase)entityObject, getPlayer()));
                    }
                }
            }

            if (entityList.size() > 0)
                MatterOverdrive.packetPipeline.sendTo(new PacketSendMinimapInfo(entityList), (EntityPlayerMP)getPlayer());
        }
    }

    public static boolean isVisibleOnMinimap(EntityLivingBase entityLivingBase, EntityPlayer player, Vec3 relativePosition)
    {
        return !entityLivingBase.isInvisible() && Math.abs(relativePosition.yCoord) < 16 && isInRangeToRenderDist(entityLivingBase, 256);
    }

    private static boolean isInRangeToRenderDist(EntityLivingBase entity, double distance)
    {
        double d1 = entity.boundingBox.getAverageEdgeLength();
        d1 *= 64.0D * entity.renderDistanceWeight;
        return distance < d1 * d1;
    }

    public void manageOutOfPower()
    {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speed.getModifier(outOfPowerSpeedModifier.getID()) == null)
        {
            speed.applyModifier(outOfPowerSpeedModifier);
        }

        if (player.worldObj.isRemote)
        {
            if (player.worldObj.getWorldTime() % 60 == 0) {
                getEffects().setInteger("GlitchTime", 5);
                playGlitchSoundClient(player.worldObj.rand, 0.2f);
            }
        }
    }

    public void manageHasPower()
    {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speed.getModifier(outOfPowerSpeedModifier.getID()) != null)
        {
            speed.removeModifier(outOfPowerSpeedModifier);
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

    @SideOnly(Side.CLIENT)
    private void manageAbilityWheel()
    {
        GuiAndroidHud.showRadial = ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_SWITCH_KEY).getIsKeyPressed();

        if (GuiAndroidHud.showRadial)
        {
            double mag = Math.sqrt(GuiAndroidHud.radialDeltaX * GuiAndroidHud.radialDeltaX + GuiAndroidHud.radialDeltaY * GuiAndroidHud.radialDeltaY);
            double magAcceptance = 0.2D;

            double radialAngle = -720F;
            if (mag > magAcceptance)
            {
                double aSin = Math.toDegrees(Math.asin(GuiAndroidHud.radialDeltaX));

                if(GuiAndroidHud.radialDeltaY >= 0 && GuiAndroidHud.radialDeltaX >= 0)
                {
                    radialAngle = aSin;
                }
                else if(GuiAndroidHud.radialDeltaY < 0 && GuiAndroidHud.radialDeltaX >= 0)
                {
                    radialAngle = 90D + (90D - aSin);
                }
                else if(GuiAndroidHud.radialDeltaY < 0 && GuiAndroidHud.radialDeltaX < 0)
                {
                    radialAngle = 180D - aSin;
                }
                else if(GuiAndroidHud.radialDeltaY >= 0 && GuiAndroidHud.radialDeltaX < 0)
                {
                    radialAngle = 270D + (90D + aSin);
                }
            }

            if(mag > 0.9999999D)
            {
                mag = Math.round(mag);
            }

            wheelStats.clear();
			wheelStats.addAll(MatterOverdrive.statRegistry.getStats().stream()
					.filter(stat -> stat.showOnWheel(this, getUnlockedLevel(stat)) && isUnlocked(stat, 0))
					.collect(Collectors.toList()));

            if (mag > magAcceptance)
            {
                GuiAndroidHud.radialAngle = radialAngle;
            }

            if (wheelStats.size() <= 0)
            {
                GuiAndroidHud.showRadial = false;
                return;
            }

            int i = 0;
            for (IBionicStat stat : wheelStats)
            {
                float leeway = 360f / wheelStats.size();
                if (mag > magAcceptance && (i == 0 && (radialAngle < (leeway / 2) && radialAngle >= 0F || radialAngle > (360F) - (leeway / 2)) || i != 0 && radialAngle < (leeway * i) + (leeway / 2) && radialAngle > (leeway * i) - (leeway / 2)))
                {
                    if (activeStat != stat)
                    {
                        activeStat = stat;
                        MatterOverdrive.packetPipeline.sendToServer(new PacketAndroidChangeAbility(activeStat.getUnlocalizedName()));
                    }
                    break;
                }
                i++;
            }

        }
    }

    public void startConversion()
    {
        if (!isAndroid() && !isTurning()) {
            if (player.worldObj.isRemote) {
                playTransformMusic();
            } else {
                AndroidPlayer androidPlayer = AndroidPlayer.get(player);
                androidPlayer.startTurningToAndroid();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void playTransformMusic()
    {
        SoundBase transform_music = new SoundBase(Reference.MOD_ID + ":transformation_music", 1, 1, false, 0, 0, 0, 0, ISound.AttenuationType.NONE);

        if (!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(transform_music))
        {
            Minecraft.getMinecraft().getSoundHandler().playSound(transform_music);
        }
    }

    public void onEntityHurt(LivingHurtEvent event)
    {
        if (isAndroid() && !event.isCanceled()) {
            effects.setInteger("GlitchTime", modify(10, AndroidAttributes.attributeGlitchTime));
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
        if (isAndroid() && REMOVE_POTION_EFFECTS)
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

    public int modify(int amount, IAttribute attribute)
    {
        IAttributeInstance glitchAttribute = player.getEntityAttribute(attribute);
        if (glitchAttribute != null)
        {
            amount = (int)(amount * glitchAttribute.getAttributeValue());
        }
        return amount;
    }

    public float modify(float amount, IAttribute attribute)
    {
        IAttributeInstance glitchAttribute = player.getEntityAttribute(attribute);
        if (glitchAttribute != null)
        {
            amount = (int)(amount * glitchAttribute.getAttributeValue());
        }
        return amount;
    }

    private void manageSwimming()
    {
        if (player.isInWater())
        {
            player.motionY = player.motionY - 0.007;
        }
    }

    private void manageAir()
    {
        if (player.getAir() < 0)
        {
            player.setAir(0);
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
                        playGlitchSound(this, player.worldObj.rand, 0.2f);
                    }
                }
            }else
            {
                effects.removeTag(EFFECT_KEY_TURNING);
                setAndroid(true);
                playGlitchSound(this, player.worldObj.rand, 0.8f);
                if (!player.capabilities.isCreativeMode && !player.worldObj.getWorldInfo().isHardcoreModeEnabled() && TRANSFORMATION_DEATH)
                {
                    player.attackEntityFrom(fake, Integer.MAX_VALUE);
                    player.setDead();
                }
            }

            sync(PacketSyncAndroid.SYNC_EFFECTS);
        }
    }

    public void playGlitchSound(AndroidPlayer player,Random random,float amount) {
        player.getPlayer().worldObj.playSoundEffect(player.getPlayer().posX,player.getPlayer().posY,player.getPlayer().posY,Reference.MOD_ID + ":gui.glitch_" + random.nextInt(11), amount, 0.9f + random.nextFloat() * 0.2f);
    }

    @SideOnly(Side.CLIENT)
    public void playGlitchSoundClient(Random random,float amount)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SoundBase(Reference.MOD_ID + ":" + "gui.glitch_" + random.nextInt(11), amount, 0.9f + random.nextFloat() * 0.2f));
    }

    public boolean isTurning()
    {
        return effects.hasKey(EFFECT_KEY_TURNING) && effects.getInteger(EFFECT_KEY_TURNING) > 0;
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
    private void startTurningToAndroid()
    {
        effects.setInteger("Turning",TRANSFORM_TIME);
        sync(PacketSyncAndroid.SYNC_EFFECTS);
    }
    public long getEffectLong(String effect)
    {
        return getEffects().getLong(effect);
    }
    public IBionicStat getActiveStat()
	{
		return activeStat;
	}
    public void setActiveStat(IBionicStat stat)
	{
		this.activeStat = stat;
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
        return inventory.decrStackSize(slot, amount);
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
        //sync(PacketSyncAndroid.SYNC_INVENTORY,true);
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

    @SideOnly(Side.CLIENT)
    public static void setMinimapEntityInfo(List<MinimapEntityInfo> entityInfo)
    {
        entityInfoMap.clear();
        for (MinimapEntityInfo info : entityInfo)
        {
            entityInfoMap.put(info.getEntityID(),info);
        }
    }

    @SideOnly(Side.CLIENT)
    public static MinimapEntityInfo getMinimapEntityInfo(EntityLivingBase entityLivingBase)
    {
        return entityInfoMap.get(entityLivingBase.getEntityId());
    }

    //endregion
}
