package com.MO.MatterOverdrive.items;

import java.util.*;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.*;
import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeapon;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.sound.PhaserSound;
import com.MO.MatterOverdrive.util.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.items.includes.MOItemEnergyContainer;
import org.lwjgl.util.vector.Vector2f;

public class Phaser extends MOItemEnergyContainer implements IWeapon{

	private static final double ENERGY_MULTIPLY = 2.5;
    private static final int MAX_USE_TIME = 80;
    private static final int MIN_HEAT_FIRE = 40;
    public static final int MAX_LEVEL = 6;
    private static final int KILL_MODE_LEVEL = 3;
    private static final float KILL_DAMAGE_MULTIPLY = 2.5f;
    private static final int STUN_SLEEP_MULTIPLY = 5;
    public static final int RANGE = 24;

    Map<EntityPlayer,PhaserSound> soundMap;
	
	public Phaser(String name) {
		super(name,32000,128,128);
		this.bFull3D = true;
        soundMap = new HashMap<EntityPlayer, PhaserSound>();
	}
	
	public void registerIcons(IIconRegister iconRegistry)
    {

    }

    @Override
    public int getDamage(ItemStack stack)
    {
        return super.getDamage(stack);
    }

    @Override
    public int getDisplayDamage(ItemStack stack)
    {
        return super.getDamage(stack);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.bow;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack, player, infos);

        infos.add(EnumChatFormatting.DARK_RED + "Power Use: " + MOEnergyHelper.formatEnergy(GetEneryUse(itemstack)) + "/t");
        infos.add(EnumChatFormatting.DARK_GREEN + "Damage: " + GetPhaserDamage(itemstack));
        infos.add(EnumChatFormatting.BLUE + "Stun: " + (GetSleepTime(itemstack) / 20f)+ "s");
        infos.add(EnumChatFormatting.DARK_RED + "Heat: " + getHeat(itemstack,player.worldObj));
        AddModuleDetails(itemstack, infos);
    }

    private void AddModuleDetails(ItemStack weapon,List infos)
    {
        ItemStack module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BARREL, weapon);
        if (module != null)
        {
            infos.add(EnumChatFormatting.GRAY + "");
            infos.add(EnumChatFormatting.GRAY + "Barrel:");

            Object statsObject = ((IWeaponModule)module.getItem()).getValue(module);
            if (statsObject instanceof Map)
            {
                for (final Map.Entry<Integer, Double> entry : ((Map<Integer,Double>) statsObject).entrySet())
                {
                    infos.add("    " + MOStringHelper.weaponStatToInfo(entry.getKey(), entry.getValue()));
                }
            }
        }
    }

	@Override
	public int getItemStackLimit(ItemStack item) 
	{
		return 1;
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) 
	{
		super.onCreated(itemStack, world, player);
	}

	
	/**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
    }
	
	public static boolean isKillMode(ItemStack item)
	{
		if(!item.hasTagCompound())
		{
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound().getByte("power") >= KILL_MODE_LEVEL;
	}
	
	private void ManageShooting(ItemStack item, World w, EntityPlayer player)
	{
        if (w.isRemote)
            return;

        MovingObjectPosition hit = MOPhysicsHelper.rayTrace(player, w, getRange(item), 0, Vec3.createVectorHelper(0, player.getEyeHeight(),0));
        if (hit != null)
        {
            Vec3 hitVector = hit.hitVec;

            if (hit.entityHit != null && hit.entityHit instanceof EntityLivingBase)
            {
                DamageSource damageInfo = new EntityDamageSourcePhaser(player);
                float damage = GetPhaserDamage(item);

                EntityLivingBase el = (EntityLivingBase) hit.entityHit;
                double motionX = el.motionX;
                double motionY = el.motionY;
                double moutionZ = el.motionZ;
                el.attackEntityFrom(damageInfo, damage);
                el.motionX = motionX;
                el.motionY = motionY;
                el.motionZ = moutionZ;

                el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, GetSleepTime(item), 100));
                //el.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, GetSleepTime(item), -10));
                el.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, GetSleepTime(item), 100));
                el.addPotionEffect(new PotionEffect(Potion.jump.id, GetSleepTime(item), -10));

                if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,item))
                {
                    el.setFire(MathHelper.round(WeaponHelper.getStatMultiply(Reference.WS_FIRE_DAMAGE,item) * item.getTagCompound().getByte("power")));
                }
            }

            if (isKillMode(item))
            {
                if (WeaponHelper.hasStat(Reference.WS_EXPLOSION_DAMAGE, item))
                {
                    w.createExplosion(player, hitVector.xCoord, hitVector.yCoord, hitVector.zCoord, (float) WeaponHelper.getStatMultiply(Reference.WS_EXPLOSION_DAMAGE, item) * item.getTagCompound().getByte("power") - (MAX_LEVEL/2), true);
                }
            }
        }
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
	{
		this.TagCompountCheck(item);

		if(player.isSneaking())
		{
			SwitchModes(world,player,item);
		}
        else
        {
            if (getHeat(item,world) <= MIN_HEAT_FIRE && DrainEnergy(item,1,true))
            {
                player.setItemInUse(item, getMaxItemUseDuration(item));
            }
            return item;
		}
		
		return item;
	}

    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        DrainEnergy(itemStack, getMaxItemUseDuration(itemStack), false);
        itemStack.setItemDamage(getDamage(itemStack));
        setHeat(itemStack, world, getMaxItemUseDuration(itemStack) + 40);

        return itemStack;
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int count)
    {
        int duration = getMaxItemUseDuration(itemStack) - count;

        if (DrainEnergy(itemStack,getMaxItemUseDuration(itemStack) - count,true))
        {
            int fireRate = MathHelper.round(5 / WeaponHelper.getStatMultiply(Reference.WS_FIRE_RATE, itemStack));

            if (duration %  fireRate == (fireRate/2))
                ManageShooting(itemStack, player.worldObj, player);
        }
        else
        {
            DrainEnergy(itemStack, duration - 1, false);
            itemStack.setItemDamage(getDamage(itemStack));
            player.stopUsingItem();
        }
    }

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int count)
    {
        setHeat(itemStack, world, getMaxItemUseDuration(itemStack) - count);
        DrainEnergy(itemStack, getMaxItemUseDuration(itemStack) - count, false);
        itemStack.setItemDamage(getDamage(itemStack));
    }
	
	private void SwitchModes(World world,EntityPlayer player,ItemStack item)
	{
		this.TagCompountCheck(item);
		SoundHandler.PlaySoundAt(world, "phaser_switch_mode", player);
        byte level = item.getTagCompound().getByte("power");
        level++;
        if(level >= MAX_LEVEL)
        {
            level = 0;
        }
		item.getTagCompound().setByte("power", level);
	}
	
	
	private boolean DrainEnergy(ItemStack item,int ticks,boolean simulate)
	{
        return MOEnergyHelper.extractExactAmount(this, item, GetEneryUse(item) * ticks,simulate);
	}

    public int getRange(ItemStack phaser)
    {
        int range = RANGE;
        range = cofh.lib.util.helpers.MathHelper.round(range * getRangeMultiply(phaser));
        return  range;
    }

    private double getRangeMultiply(ItemStack phaser)
    {
        return WeaponHelper.getStatMultiply(Reference.WS_RANGE,phaser);
    }

    private int GetEneryUse(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        return (int)Math.floor(Math.pow(ENERGY_MULTIPLY,level + 1) / getPowerMultiply(item));
    }

    private double getPowerMultiply(ItemStack phaser)
    {
        return WeaponHelper.getStatMultiply(Reference.WS_AMMO,phaser);
    }
	
	private float GetPhaserDamage(ItemStack item)
	{
        float damage = 0;
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level >= KILL_MODE_LEVEL)
        {
            damage = (float)Math.pow(KILL_DAMAGE_MULTIPLY,level - (KILL_MODE_LEVEL-1));
        }

        damage *= getDamageMultiplay(item);

        return damage;
	}

    private float getDamageMultiplay(ItemStack phaser)
    {
        return (float)WeaponHelper.getStatMultiply(Reference.WS_DAMAGE, phaser);
    }

    private int GetSleepTime(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level < KILL_MODE_LEVEL)
        {
            return (int)(Math.pow(level+1,STUN_SLEEP_MULTIPLY) * sleepTimeMultipy(item));
        }
        return 0;
    }

    private double sleepTimeMultipy(ItemStack phaser)
    {
        return WeaponHelper.getStatMultiply(Reference.WS_DAMAGE,phaser);
    }

    public void setHeat(ItemStack item,World world,long amount)
    {
        if (item.hasTagCompound())
        {
            item.getTagCompound().setLong("heat",world.getTotalWorldTime() + amount);
        }
    }

    public long getHeat(ItemStack item,World world) {
        if (item.hasTagCompound()) {
            return Math.max(0,item.getTagCompound().getLong("heat") - world.getTotalWorldTime());
        }
        return 0;
    }

    //region Energy Functions
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            int energy = e.receiveEnergy(energy_module,maxReceive,simulate);
            if (!simulate)
                WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,container,energy_module);
            return energy;
        }
        else
        {
            return super.receiveEnergy(container,maxReceive,simulate);
        }
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
    {

        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            int energy = e.extractEnergy(energy_module, maxReceive, simulate);
            if (!simulate)
                WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,container,energy_module);
            return energy;
        }
        else
        {
            return super.extractEnergy(container, maxReceive, simulate);
        }
    }

    @Override
    protected void setEnergyStored(ItemStack container,int amount)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            EnergyHelper.setDefaultEnergyTag(energy_module, amount);
            WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,container,energy_module);
        }
        else
        {
            super.setEnergyStored(container,amount);
        }

    }

    @Override
    public int getEnergyStored(ItemStack container)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            return e.getEnergyStored(energy_module);
        }
        else
        {
            return super.getEnergyStored(container);
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack container)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            return e.getMaxEnergyStored(energy_module);
        }
        else
        {
            return capacity;
        }
    }

    //endregion

    @Override
    public Vector2f getSlotPosition(int slot, ItemStack weapon)
    {
        switch (slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(170,115);
            case Reference.MODULE_COLOR:
                return new Vector2f(60,45);
            case Reference.MODULE_BARREL:
                return new Vector2f(60,115);
            case Reference.MODULE_OTHER:
                return new Vector2f(200,45);
        }
        return new Vector2f(0,0);
    }

    @Override
    public Vector2f getModuleScreenPosition(int slot, ItemStack weapon)
    {
        switch(slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(165,85);
            case Reference.MODULE_COLOR:
                return new Vector2f(100,80);
            case Reference.MODULE_BARREL:
                return new Vector2f(85,100);
        }
        return getSlotPosition(slot,weapon);
    }

    @Override
    public boolean supportsModule(int slot,ItemStack weapon)
    {
        return slot != Reference.MODULE_SIGHTS;

    }
}
