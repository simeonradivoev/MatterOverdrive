package com.MO.MatterOverdrive.items;

import java.util.*;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeapon;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.api.weapon.WeaponStat;
import com.MO.MatterOverdrive.network.PacketPipeline;
import com.MO.MatterOverdrive.network.packet.PacketPhaserUpdate;
import com.MO.MatterOverdrive.sound.PhaserSound;
import com.MO.MatterOverdrive.util.EntityDamageSourcePhaser;
import com.MO.MatterOverdrive.util.MOEnergyHelper;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.WeaponHelper;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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

    @SideOnly(Side.CLIENT)
    protected PhaserSound phaserSound;
	
	public Phaser(String name) {
		super(name,32000,128,128);
		this.bFull3D = true;
	}
	
	public void registerIcons(IIconRegister iconRegistry)
    {

    }

    @Override
    public int getDamage(ItemStack stack)
    {
        return 0;
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

        infos.add(EnumChatFormatting.DARK_RED + "Power Use: " + MOEnergyHelper.formatEnergy(GetEneryUse(itemstack)));
        infos.add(EnumChatFormatting.DARK_GREEN + "Damage: " + GetPhaserDamage(itemstack));
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
                for (final Map.Entry<WeaponStat, Double> entry : ((Map<WeaponStat,Double>) statsObject).entrySet())
                {
                    infos.add("    " + MOStringHelper.toInfo(entry.getKey(),entry.getValue()));
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

    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
        if (entityItem.worldObj.isRemote)
        {
            stopPhaserSounds();
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack itemStack,World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.worldObj.isRemote)
            {
                if (player.isUsingItem() && player.getItemInUse() == itemStack) {

                }
                else
                {
                    setFiring(itemStack, false);
                    stopPhaserSounds();
                }
            }
        }
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
		if(!w.isRemote)
		{
            Shoot(item, w, player);
		}
	}
	
	private void Shoot(ItemStack item, World w, EntityPlayer player)
	{
		float penetration = 1F;
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f + 1.62D - player.yOffset;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 32.0D;

		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		Vec3 direction = Vec3.createVectorHelper(f7 * d3, f6 * d3, f8 * d3);
		direction.normalize();
		
		if(getEnergyStored(item) >= GetEneryUse(item))
		{
			this.shoot(item,penetration, w, player, vec3, vec31, direction, d0, d1, d2);
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
                if (world.isRemote)
                {
                    PlaySound(player.posX, player.posY, player.posZ);
                    setFiring(item, true);
                }

                if (!world.isRemote)
                {
                    MatterOverdrive.packetPipeline.sendToAll(new PacketPhaserUpdate(player.inventory.currentItem, player.getEntityId(), true));
                }

                player.setItemInUse(item, getMaxItemUseDuration(item));
            }
            return item;
		}
		
		return item;
	}

    @SideOnly(Side.CLIENT)
    private void PlaySound(double x,double y,double z)
    {
        if(phaserSound == null)
        {
            phaserSound = new PhaserSound(new ResourceLocation(Reference.MOD_ID + ":" +"phaser_beam"),(float)x,(float)y,(float)z,itemRand.nextFloat() * 0.1f + 0.3f,1);
            Minecraft.getMinecraft().getSoundHandler().playSound(phaserSound);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopPhaserSounds()
    {
        if(phaserSound != null)
        {
            phaserSound.stopPlaying();
            phaserSound = null;
        }
    }

    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (world.isRemote) {
            stopPhaserSounds();
        }

        setFiring(itemStack, false);
        DrainEnergy(itemStack, getMaxItemUseDuration(itemStack), false);
        setHeat(itemStack, world, getMaxItemUseDuration(itemStack) + 40);

        return itemStack;
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int count)
    {
        if (DrainEnergy(itemStack,getMaxItemUseDuration(itemStack) - count,true))
        {
            ManageShooting(itemStack, player.worldObj, player);
        }
        else {
            DrainEnergy(itemStack, (getMaxItemUseDuration(itemStack) - count) - 1, false);
            if (player.worldObj.isRemote)
            {
                stopPhaserSounds();
            }
            player.stopUsingItem();
            setFiring(itemStack, false);
        }
    }

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int count)
    {
        if (world.isRemote)
        {
            stopPhaserSounds();
        }
        setHeat(itemStack, world, getMaxItemUseDuration(itemStack) - count);
        DrainEnergy(itemStack, getMaxItemUseDuration(itemStack) - count, false);
        setFiring(itemStack, false);
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

    private int GetEneryUse(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        return (int)Math.floor(Math.pow(ENERGY_MULTIPLY,level + 1));
    }
	
	private float GetPhaserDamage(ItemStack item)
	{
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level >= KILL_MODE_LEVEL)
        {
            return (float)Math.pow(KILL_DAMAGE_MULTIPLY,level - (KILL_MODE_LEVEL-1));
        }
        return 0;
	}

    private int GetSleepTime(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level < KILL_MODE_LEVEL)
        {
            return (int)Math.pow(level+1,STUN_SLEEP_MULTIPLY);
        }
        return 0;
    }

    public void setHeat(ItemStack item,World world,long amount)
    {
        if (item.hasTagCompound())
        {
            item.getTagCompound().setLong("heat",world.getTotalWorldTime() + amount);
        }
    }

    public long getHeat(ItemStack item,World world)
    {
        if (item.hasTagCompound())
        {
            return Math.max(0,item.getTagCompound().getLong("heat") - world.getTotalWorldTime());
        }
        return 0;
    }

    public static void setFiring(ItemStack item,boolean firing)
    {
        if (item.hasTagCompound())
        {
            item.getTagCompound().setBoolean("firing",firing);
        }
    }

    public static boolean getFiring(ItemStack item)
    {
        if (item.hasTagCompound())
        {
            return item.getTagCompound().getBoolean("firing");
        }
        return false;
    }

	private void shoot(ItemStack item,float penetration, World w, EntityPlayer p, Vec3 vec3,
			Vec3 vec31, Vec3 direction, double d0, double d1, double d2) 
	{
        Vec3 vec = Vec3.createVectorHelper(d0, d1, d2);
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
                Math.min(vec3.xCoord, vec31.xCoord),
                Math.min(vec3.yCoord, vec31.yCoord),
                Math.min(vec3.zCoord, vec31.zCoord),
                Math.max(vec3.xCoord, vec31.xCoord),
                Math.max(vec3.yCoord, vec31.yCoord),
                Math.max(vec3.zCoord, vec31.zCoord)).expand(16, 16, 16);


        Entity entity = null;
        List list = w.getEntitiesWithinAABBExcludingEntity(p, bb);
        double closest = 9999999.0D;

        for (int l = 0; l < list.size(); ++l)
        {
            Entity entity1 = (Entity) list.get(l);
            if (!entity1.isDead && entity1 != p && !(entity1 instanceof EntityItem)) {
                if (entity1.isEntityAlive()) {
                    // prevent killing / flying of mounts.
                    if (entity1.riddenByEntity == p)
                        continue;

                    float f1 = 0.3F;

                    AxisAlignedBB boundingBox = entity1.boundingBox.expand(f1, f1, f1);
                    MovingObjectPosition movingObjectPosition = boundingBox.calculateIntercept(vec3, vec31);

                    if (movingObjectPosition != null) {
                        double nd = vec3.squareDistanceTo(movingObjectPosition.hitVec);

                        if (nd < closest) {
                            entity = entity1;
                            closest = nd;
                        }
                    }
                }
            }
        }

        DamageSource damageInfo = new EntityDamageSourcePhaser(p);
        float damage = GetPhaserDamage(item);

        MovingObjectPosition pos = w.rayTraceBlocks(vec3, vec31, true);
        if (entity != null && pos != null && pos.hitVec.squareDistanceTo(vec) > closest) {
            pos = new MovingObjectPosition(entity);
        }

        else if (entity != null && pos == null)
        {
            pos = new MovingObjectPosition(entity);
        }

        if(pos != null && pos.entityHit != null)
        {
            if (pos.entityHit instanceof EntityLivingBase)
            {
                EntityLivingBase el = (EntityLivingBase) pos.entityHit;
                el.attackEntityFrom(damageInfo, damage);
                el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, GetSleepTime(item), 10));
                el.addPotionEffect(new PotionEffect(Potion.digSlowdown.id,GetSleepTime(item),10));
                el.addPotionEffect(new PotionEffect(Potion.jump.id,GetSleepTime(item),-10));
            }
        }
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
            return e.getEnergyStored(container);
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
