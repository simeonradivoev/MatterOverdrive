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

package matteroverdrive.items.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by Simeon on 12/6/2015.
 */
public class PlasmaShotgun extends EnergyWeapon
{
	public static final int RANGE = 16;
	private static final int MAX_CHARGE_TIME = 20;
	private static final int ENERGY_PER_SHOT = 2560;
	@SideOnly(Side.CLIENT)
	private MOPositionedSound lastChargingSound;

	public PlasmaShotgun(String name)
	{
		super(name, 32000, 128, 128, RANGE);
		this.setFull3D();
		leftClickFire = true;
	}

	@Override
	protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos)
	{

	}

	@Override
	public int getBaseEnergyUse(ItemStack item)
	{
		return ENERGY_PER_SHOT / getShootCooldown(item);
	}

	@Override
	protected int getBaseMaxHeat(ItemStack item)
	{
		return 80;
	}

	@Override
	public float getWeaponBaseDamage(ItemStack weapon)
	{
		return 16;
	}

	@Override
	public float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed)
	{
		return 5f + getHeat(weapon) * 0.3f;
	}

	@Override
	public boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter)
	{
		return DrainEnergy(itemStack, getShootCooldown(itemStack), true) && !isOverheated(itemStack);
	}

	@Override
	public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter)
	{
		return 3;
	}

	@Override
	public int getBaseShootCooldown(ItemStack weapon)
	{
		return 22;
	}

	@Override
	public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot)
	{
		MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.weaponsPlasmaShotgunShot, SoundCategory.PLAYERS, 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
		sound.setPosition((float)position.xCoord, (float)position.yCoord, (float)position.zCoord);
		Minecraft.getMinecraft().getSoundHandler().playDelayedSound(sound, 1);
		spawnProjectiles(weapon, shooter, position, dir, shot);
	}

	public PlasmaBolt[] spawnProjectiles(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot)
	{
		//PlasmaBolt fire = new PlasmaBolt(entityPlayer.worldObj, entityPlayer,position,dir, getWeaponScaledDamage(weapon), 2, getAccuracy(weapon, zoomed), getRange(weapon), WeaponHelper.getColor(weapon).getColor(), zoomed,seed);
		PlasmaBolt[] bolts = new PlasmaBolt[shot.getCount()];
		for (int i = 0; i < shot.getCount(); i++)
		{
			WeaponShot newShot = new WeaponShot(shot);
			if (shooter.worldObj.isRemote)
			{
				newShot.setSeed(((ClientWeaponHandler)MatterOverdrive.proxy.getWeaponHandler()).getNextShotID());
			}
			else
			{
				newShot.setSeed(shot.getSeed() + i);
			}
			newShot.setDamage(shot.getDamage() / shot.getCount());
			bolts[i] = new PlasmaBolt(shooter.worldObj, shooter, position, dir, newShot, getShotSpeed(weapon, shooter));
			bolts[i].setWeapon(weapon);
			bolts[i].setRenderSize((getShotCount(weapon, shooter) / shot.getCount()) * 0.5f);
			bolts[i].setFireDamageMultiply(WeaponHelper.modifyStat(Reference.WS_FIRE_DAMAGE, weapon, 0));
			float explosionMultiply = WeaponHelper.modifyStat(Reference.WS_EXPLOSION_DAMAGE, weapon, 0);
			if (explosionMultiply > 0)
			{
				bolts[i].setExplodeMultiply((getWeaponBaseDamage(weapon) * 0.3f * explosionMultiply) / shot.getCount());
			}
			bolts[i].setKnockBack(0.5f);
			if (WeaponHelper.modifyStat(Reference.WS_RICOCHET, weapon, 0) == 1)
			{
				bolts[i].markRicochetable();
			}
			shooter.worldObj.spawnEntityInWorld(bolts[i]);
		}
		return bolts;
	}

	public int getShotCount(ItemStack weapon, EntityLivingBase shooter)
	{
		return 10;
	}

	@Override
	public void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount)
	{

	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vector2f getSlotPosition(int slot, ItemStack weapon)
	{
		switch (slot)
		{
			case Reference.MODULE_BATTERY:
				return new Vector2f(170, 115);
			case Reference.MODULE_COLOR:
				return new Vector2f(60, 45);
			case Reference.MODULE_BARREL:
				return new Vector2f(60, 115);
			case Reference.MODULE_SIGHTS:
				return new Vector2f(150, 35);
			default:
				return new Vector2f(205, 80 + ((slot - Reference.MODULE_OTHER) * 22));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vector2f getModuleScreenPosition(int slot, ItemStack weapon)
	{
		switch (slot)
		{
			case Reference.MODULE_BATTERY:
				return new Vector2f(165, 80);
			case Reference.MODULE_COLOR:
				return new Vector2f(100, 80);
			case Reference.MODULE_BARREL:
				return new Vector2f(90, 90);
			case Reference.MODULE_SIGHTS:
				return new Vector2f(140, 72);
		}
		return getSlotPosition(slot, weapon);
	}

	@Override
	public boolean supportsModule(int slot, ItemStack weapon)
	{
		return true;

	}

	@Override
	public boolean supportsModule(ItemStack weapon, ItemStack module)
	{
		if (module != null)
		{
			return module.getItem() == MatterOverdrive.items.weapon_module_color || (module.getItem() == MatterOverdrive.items.weapon_module_barrel && module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID);
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (hand == EnumHand.OFF_HAND)
		{
			return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
		}
		if (worldIn.isRemote && canFire(itemStackIn, worldIn, playerIn) && hasShootDelayPassed() && playerIn.getActiveHand() != EnumHand.MAIN_HAND)
		{
			playerIn.setActiveHand(hand);
			playChargingSound(playerIn);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	public void playChargingSound(EntityPlayer entityPlayer)
	{
		lastChargingSound = new MOPositionedSound(MatterOverdriveSounds.weaponsPlasmaShotgunCharging, SoundCategory.PLAYERS, 3f + itemRand.nextFloat() * 0.2f, 0.9f * itemRand.nextFloat() * 0.2f);
		lastChargingSound.setPosition((float)entityPlayer.posX, (float)entityPlayer.posY, (float)entityPlayer.posZ);
		Minecraft.getMinecraft().getSoundHandler().playSound(lastChargingSound);
	}

	@SideOnly(Side.CLIENT)
	public void stopChargingSound()
	{
		Minecraft.getMinecraft().getSoundHandler().stopSound(lastChargingSound);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (worldIn.isRemote)
		{
			int maxCount = getShotCount(stack, entityLiving);
			int timeElapsed = (getMaxItemUseDuration(stack) - timeLeft);
			int count = Math.max(1, (int)((1f - (timeElapsed / (float)MAX_CHARGE_TIME)) * maxCount));
			float shotPercent = count / (float)getShotCount(stack, entityLiving);

			ClientProxy.instance().getClientWeaponHandler().setCameraRecoil(0.3f + getAccuracy(stack, entityLiving, true) * 0.1f, 1);
			Vec3d dir = entityLiving.getLook(1);
			Vec3d pos = getFirePosition(entityLiving, dir, Mouse.isButtonDown(1));
			WeaponShot shot = createShot(stack, entityLiving, Mouse.isButtonDown(1));
			shot.setCount(count);
			shot.setAccuracy(shot.getAccuracy() * shotPercent);
			shot.setRange(shot.getRange() + (int)(shot.getRange() * (1 - shotPercent)));
			onClientShot(stack, entityLiving, pos, dir, shot);
			MatterOverdrive.packetPipeline.sendToServer(new PacketFirePlasmaShot(entityLiving.getEntityId(), pos, dir, shot));
			addShootDelay(stack);
			ClientProxy.instance().getClientWeaponHandler().setRecoil(15 + (maxCount - count) * 2 + getAccuracy(stack, entityLiving, isWeaponZoomed(entityLiving, stack)) * 2, 1f + (maxCount - count) * 0.03f, 0.3f);
			stopChargingSound();
			entityLiving.resetActiveHand();
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase playerIn)
	{
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack weapon)
	{
		return 72000;
	}


	@SideOnly(Side.CLIENT)
	private Vec3d getFirePosition(EntityLivingBase entityPlayer, Vec3d dir, boolean isAiming)
	{
		Vec3d pos = entityPlayer.getPositionEyes(1);
		if (!isAiming)
		{
			//pos.xCoord -= (double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
			//pos.zCoord -= (double)(MathHelper.sin(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		}
		pos = pos.addVector(dir.xCoord, dir.yCoord, dir.zCoord);
		return pos;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer, boolean sendServerTick)
	{
		if (Mouse.isButtonDown(0) && hasShootDelayPassed())
		{
			if (canFire(itemStack, world, entityPlayer))
			{
				itemStack.setTagInfo("LastShot", new NBTTagLong(world.getTotalWorldTime()));
				Vec3d dir = entityPlayer.getLook(1);
				Vec3d pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
				WeaponShot shot = createShot(itemStack, entityPlayer, Mouse.isButtonDown(1));
				onClientShot(itemStack, entityPlayer, pos, dir, shot);
				addShootDelay(itemStack);
				sendShootTickToServer(world, shot, dir, pos);
				ClientProxy.instance().getClientWeaponHandler().setRecoil(12 + getAccuracy(itemStack, entityPlayer, isWeaponZoomed(entityPlayer, itemStack)) * 2, 1, 0.2f);
				Minecraft.getMinecraft().thePlayer.hurtTime = 15;
				Minecraft.getMinecraft().thePlayer.maxHurtTime = 30;
				return;
			}
			else if (needsRecharge(itemStack))
			{
				chargeFromEnergyPack(itemStack, entityPlayer);
			}
		}

		super.onShooterClientUpdate(itemStack, world, entityPlayer, sendServerTick);
	}

	public WeaponShot createShot(ItemStack weapon, EntityLivingBase shooter, boolean zoomed)
	{
		WeaponShot shot = new WeaponShot(itemRand.nextInt(), getWeaponScaledDamage(weapon, shooter), getAccuracy(weapon, shooter, zoomed), WeaponHelper.getColor(weapon), getRange(weapon));
		shot.setCount(getShotCount(weapon, shooter));
		return shot;
	}

	@Override
	public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3d position, Vec3d dir, int delay)
	{
		DrainEnergy(weapon, getShootCooldown(weapon), false);
		int heatAdd = (getShotCount(weapon, shooter) - shot.getCount()) * 2;
		float newHeat = (getHeat(weapon) + heatAdd + 6) * 4.2f;
		setHeat(weapon, Math.max(newHeat, 0));
		manageOverheat(weapon, shooter.worldObj, shooter);
		PlasmaBolt[] fires = spawnProjectiles(weapon, shooter, position, dir, shot);
		for (PlasmaBolt bolt : fires)
		{
			bolt.simulateDelay(delay);
		}
		return true;
	}

	@Override
	public boolean isAlwaysEquipped(ItemStack weapon)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isWeaponZoomed(EntityLivingBase entityPlayer, ItemStack weapon)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity)
	{
		return null;
	}
}
