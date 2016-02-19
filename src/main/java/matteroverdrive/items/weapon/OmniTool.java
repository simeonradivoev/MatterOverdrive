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

package matteroverdrive.items.weapon;/* Created by Simeon on 10/17/2015. */

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.server.PacketDigBlock;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class OmniTool extends EnergyWeapon
{
    private static float BLOCK_DAMAGE,STEP_SOUND_COUNTER,LAST_BRAKE_TIME;
    private static BlockPos CURRENT_BLOCK;
    private static EnumFacing LAST_SIDE;
    public static final int RANGE = 24;
    private static final int MAX_USE_TIME = 240;
    private static final int ENERGY_PER_SHOT = 512;
    private static final float DIG_POWER_MULTIPLY = 0.03f;

    public OmniTool(String name)
    {
        super(name,32000,128,128,RANGE);
        setHarvestLevel("pickaxe",3);
        setHarvestLevel("axe",3);
        this.bFull3D = true;
        this.leftClickFire = true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
    }

    public int getItemEnchantability()
    {
        return 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
        this.TagCompountCheck(item);

        if (canDig(item, world))
        {
            player.setItemInUse(item, getMaxItemUseDuration(item));
            if (world.isRemote) {
                stopMiningLastBlock();
            }
        }
        if (needsRecharge(item))
        {
            chargeFromEnergyPack(item,player);
        }
        return item;
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int count)
    {
        if (player.worldObj.isRemote && player.equals(Minecraft.getMinecraft().thePlayer)) {
            if (canDig(itemStack, player.worldObj))
            {
                MovingObjectPosition hit = player.rayTrace(getRange(itemStack),1);

                if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK )
                {
                    IBlockState state = player.worldObj.getBlockState(hit.getBlockPos());
                    boolean canMine = state.getBlock().canHarvestBlock(player.worldObj,hit.getBlockPos(),player) && player.capabilities.allowEdit;

                    if (state.getBlock() != null && state.getBlock().getMaterial() != Material.air && canMine) {

                        ++STEP_SOUND_COUNTER;
                        LAST_SIDE = hit.sideHit;

                        if (isSameBlock(hit.getBlockPos()))
                        {
                            if (BLOCK_DAMAGE >= 1.0F)
                            {
                                //this.isHittingBlock = false;
                                MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(hit.getBlockPos(),2,hit.sideHit));
                                Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(hit.getBlockPos(), hit.sideHit);
                                BLOCK_DAMAGE = 0.0F;
                                STEP_SOUND_COUNTER = 0.0F;
                                //this.blockHitDelay = 5;
                            }else if (BLOCK_DAMAGE == 0)
                            {
                                MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(hit.getBlockPos(),0,hit.sideHit));
                            }

                            BLOCK_DAMAGE = MathHelper.clamp_float(modifyStatFromModules(Reference.WS_DAMAGE,itemStack,BLOCK_DAMAGE+state.getBlock().getPlayerRelativeBlockHardness(player, player.worldObj, hit.getBlockPos())),0,1);
                            player.worldObj.sendBlockBreakProgress(player.getEntityId(), hit.getBlockPos(), (int)(BLOCK_DAMAGE * 10));
                        }else
                        {
                            stopMiningLastBlock();
                            setLastBlock(hit.getBlockPos());
                        }
                    }
                }

            }else
            {
                player.stopUsingItem();
            }
        }else
        {
            DrainEnergy(itemStack,DIG_POWER_MULTIPLY,false);
        }
    }

    boolean isSameBlock(BlockPos pos)
    {
        return CURRENT_BLOCK != null && CURRENT_BLOCK.equals(pos);
    }

    @SideOnly(Side.CLIENT)
    void setLastBlock(BlockPos pos)
    {
        CURRENT_BLOCK = pos;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int count)
    {
        super.onPlayerStoppedUsing(itemStack,world,entityPlayer,count);
        if (world.isRemote)
        {
            stopMiningLastBlock();
        }else
        {
            int ticks = getMaxItemUseDuration(itemStack) - count;
            //DrainEnergy(itemStack, ticks, false);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopMiningLastBlock()
    {
        if (CURRENT_BLOCK != null)
        {
            BLOCK_DAMAGE = 0;
            STEP_SOUND_COUNTER = 0.0F;
            MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(CURRENT_BLOCK, 1, LAST_SIDE));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public PlasmaBolt getDefaultProjectile(ItemStack weapon,EntityLivingBase shooter,Vec3 position,Vec3 dir,WeaponShot shot)
    {
        PlasmaBolt bolt = super.getDefaultProjectile(weapon,shooter,position,dir,shot);
        bolt.setKnockBack(0.05f);
        return bolt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3 position, Vec3 dir,WeaponShot shot)
    {
        //ClientProxy.weaponHandler.addShootDelay(this);
        MOPositionedSound sound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "laser_fire_0"),0.5f + itemRand.nextFloat() * 0.2f, 1.2f + itemRand.nextFloat() * 0.2f);
        sound.setPosition((float) position.xCoord,(float)position.yCoord,(float)position.zCoord);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        spawnProjectile(weapon,shooter,position,dir,shot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onProjectileHit(MovingObjectPosition hit, ItemStack weapon, World world,float amount)
    {
        if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && amount == 1) {

            if (itemRand.nextFloat() < 0.8f)
            {
                Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(world, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, new Color(255, 255, 255)));
            }
        }
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        return 0.3f + getHeat(weapon)/getMaxHeat(weapon)*5;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos) {

    }

    @Override
    protected int getBaseEnergyUse(ItemStack item) {
        return ENERGY_PER_SHOT / getShootCooldown(item);
    }

    @Override
    protected int getBaseMaxHeat(ItemStack item) {
        return 80;
    }

    @Override
    public float getWeaponBaseDamage(ItemStack weapon) {
        return 7;
    }

    @Override
    public boolean canFire(ItemStack itemStack, World world,EntityLivingBase shooter) {
        return !isOverheated(itemStack) && DrainEnergy(itemStack,getShootCooldown(itemStack),true);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 3;
    }

    public boolean canDig(ItemStack itemStack,World world)
    {
        return !isOverheated(itemStack) && DrainEnergy(itemStack,DIG_POWER_MULTIPLY,true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vector2f getSlotPosition(int slot, ItemStack weapon) {
        switch (slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(170,115);
            case Reference.MODULE_COLOR:
                return new Vector2f(80,40);
            case Reference.MODULE_BARREL:
                return new Vector2f(60,115);
            default:
                return new Vector2f(200,60 + ((slot - Reference.MODULE_OTHER) * 22));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vector2f getModuleScreenPosition(int slot, ItemStack weapon) {
        switch(slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(173,90);
            case Reference.MODULE_COLOR:
                return new Vector2f(125,72);
            case Reference.MODULE_BARREL:
                return new Vector2f(85,105);
        }
        return getSlotPosition(slot,weapon);
    }

    @Override
    public boolean supportsModule(int slot, ItemStack weapon) {
        return slot != Reference.MODULE_SIGHTS;
    }

    @Override
    public boolean supportsModule(ItemStack weapon, ItemStack module)
    {
        if (module != null)
        {
            return module.getItem() == MatterOverdriveItems.weapon_module_color || (module.getItem() == MatterOverdriveItems.weapon_module_barrel && module.getItemDamage() != WeaponModuleBarrel.EXPLOSION_BARREL_ID && module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID);
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer, boolean sendServerTick)
    {
        if (Mouse.isButtonDown(0) && hasShootDelayPassed())
        {
            if (canFire(itemStack, world, entityPlayer)) {
                itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
                Vec3 dir = entityPlayer.getLook(1);
                Vec3 pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
                WeaponShot shot = createClientShot(itemStack, entityPlayer, Mouse.isButtonDown(1));
                onClientShot(itemStack, entityPlayer, pos, dir, shot);
                addShootDelay(itemStack);
                sendShootTickToServer(world, shot, dir, pos);
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                    ClientProxy.instance().getClientWeaponHandler().setRecoil(6 + getAccuracy(itemStack, entityPlayer, isWeaponZoomed(entityPlayer, itemStack)) * 2,1,0.05f);
                    ClientProxy.instance().getClientWeaponHandler().setCameraRecoil(1 + getAccuracy(itemStack, entityPlayer, true) * 0.08f,1);
                }
                return;
            } else if (needsRecharge(itemStack)) {
                chargeFromEnergyPack(itemStack, entityPlayer);
            }
        }

        super.onShooterClientUpdate(itemStack,world,entityPlayer,sendServerTick);
    }

    @SideOnly(Side.CLIENT)
    private Vec3 getFirePosition(EntityPlayer entityPlayer,Vec3 dir,boolean isAiming)
    {
        Vec3 pos = entityPlayer.getPositionEyes(1);
        pos = pos.subtract((double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F),0,(double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F));
        pos = pos.addVector(dir.xCoord,dir.yCoord,dir.zCoord);
        return pos;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3 position, Vec3 dir,int delay) {
        DrainEnergy(weapon, getShootCooldown(weapon), false);
        float newHeat =  (getHeat(weapon)+4) * 2.7f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, shooter.worldObj, shooter);
        PlasmaBolt fire = spawnProjectile(weapon,shooter,position,dir,shot);
        fire.simulateDelay(delay);
        weapon.getTagCompound().setLong("LastShot", shooter.worldObj.getTotalWorldTime());
        return true;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return false;
    }

    @Override
    public int getBaseShootCooldown(ItemStack itemStack) {
        return 18;
    }

    @Override
    public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isWeaponZoomed(EntityPlayer entityPlayer,ItemStack weapon)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity)
    {
        //return Reference.MOD_ID + ":" +"omni_tool_hum";
        return new WeaponSound(new ResourceLocation(Reference.MOD_ID + ":" +"omni_tool_hum"),(float)entity.posX,(float)entity.posY,(float)entity.posZ,itemRand.nextFloat() * 0.04f + 0.06f,itemRand.nextFloat() * 0.1f + 0.95f);
    }
}
