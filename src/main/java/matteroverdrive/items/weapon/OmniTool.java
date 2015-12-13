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

import cofh.lib.gui.GuiColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.render.item.ItemRendererOmniTool;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.server.PacketDigBlock;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class OmniTool extends EnergyWeapon
{
    private static float BLOCK_DAMAGE,STEP_SOUND_COUNTER,LAST_BRAKE_TIME;
    private static int CURRENT_BLOCK_X,CURRENT_BLOCK_Y,CURRENT_BLOCK_Z,LAST_SIDE;
    public static final int RANGE = 24;
    private static final int MAX_USE_TIME = 240;
    private static final int ENERGY_PER_SHOT = 512;
    private static final float DIG_POWER_MULTIPLY = 0.007f;

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
                MovingObjectPosition movingObjectPosition = player.rayTrace(getRange(itemStack),1);

                if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK )
                {
                    int x = movingObjectPosition.blockX;
                    int y = movingObjectPosition.blockY;
                    int z = movingObjectPosition.blockZ;
                    Block block = player.worldObj.getBlock(x, y, z);

                    if (block != null && block.getMaterial() != Material.air) {

                        float percent = (1-(float)count/(float)getMaxItemUseDuration(itemStack));
                        //MatterOverdrive.log.info("Percent %s",percent);

                        ++STEP_SOUND_COUNTER;
                        LAST_SIDE = movingObjectPosition.sideHit;

                        if (isSameBlock(x,y,z))
                        {
                            if (BLOCK_DAMAGE >= 1.0F)
                            {
                                //this.isHittingBlock = false;
                                MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(x,y,z,2,movingObjectPosition.sideHit));
                                Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(x, y, z, movingObjectPosition.sideHit);
                                BLOCK_DAMAGE = 0.0F;
                                STEP_SOUND_COUNTER = 0.0F;
                                //this.blockHitDelay = 5;
                            }else if (BLOCK_DAMAGE == 0)
                            {
                                MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(x,y,z,0,movingObjectPosition.sideHit));
                            }

                            BLOCK_DAMAGE = MathHelper.clamp_float(modifyStatFromModules(Reference.WS_DAMAGE,itemStack,BLOCK_DAMAGE+block.getPlayerRelativeBlockHardness(player, player.worldObj, x, y, z)),0,1);
                            player.worldObj.destroyBlockInWorldPartially(player.getEntityId(), x, y, z, (int)(BLOCK_DAMAGE * 10));
                        }else
                        {
                            stopMiningLastBlock();
                            setLastBlock(x,y,z);
                        }
                    }
                }

            }
        }else
        {
            DrainEnergy(itemStack,DIG_POWER_MULTIPLY,false);
        }
    }

    boolean isSameBlock(int x,int y,int z)
    {
        return x == CURRENT_BLOCK_X && y == CURRENT_BLOCK_Y && z == CURRENT_BLOCK_Z;
    }

    @SideOnly(Side.CLIENT)
    void setLastBlock(int x,int y,int z)
    {
        CURRENT_BLOCK_X = x;
        CURRENT_BLOCK_Y = y;
        CURRENT_BLOCK_Z = z;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int count)
    {
        super.onPlayerStoppedUsing(itemStack,world,entityPlayer,count);
        if (world.isRemote)
        {
            stopMiningLastBlock();
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopMiningLastBlock()
    {
        BLOCK_DAMAGE = 0;
        STEP_SOUND_COUNTER = 0.0F;
        MatterOverdrive.packetPipeline.sendToServer(new PacketDigBlock(CURRENT_BLOCK_X, CURRENT_BLOCK_Y, CURRENT_BLOCK_Z, 1, LAST_SIDE));
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }

    public PlasmaBolt spawnProjectile(ItemStack weapon,EntityLivingBase shooter,Vec3 position,Vec3 dir,WeaponShot shot)
    {
        PlasmaBolt fire = new PlasmaBolt(shooter.worldObj, shooter,position,dir, shot,getShotSpeed(weapon,shooter));
        fire.setWeapon(weapon);
        fire.setFireDamageMultiply(WeaponHelper.modifyStat(Reference.WS_FIRE_DAMAGE,weapon,0));
        fire.setKnockBack(0.05f);
        shooter.worldObj.spawnEntityInWorld(fire);
        return fire;
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
                Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(world, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, new GuiColor(255, 255, 255)));
            }
        }
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        return 0.3f + getHeat(weapon)/getMaxHeat(weapon)*5;
    }

    //Dig speed
    public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
    {
        return 8.0F;
    }

    //Can harvest block ?
    @Override
    public boolean func_150897_b(Block p_150897_1_)
    {
        return true;
    }

    private boolean sameToolAndBlock(int x, int y, int z)
    {
        return x == CURRENT_BLOCK_X && y == CURRENT_BLOCK_Y && z == CURRENT_BLOCK_Z;
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
            case Reference.MODULE_OTHER:
                return new Vector2f(200,45);
        }
        return new Vector2f(0,0);
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
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                    ItemRendererOmniTool.RECOIL_AMOUNT = 6 + getAccuracy(itemStack, entityPlayer, isWeaponZoomed(entityPlayer, itemStack)) * 2;
                    ItemRendererOmniTool.RECOIL_TIME = 1;
                    Minecraft.getMinecraft().renderViewEntity.hurtTime = 8;
                    Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 20;
                }
                Vec3 dir = entityPlayer.getLook(1);
                Vec3 pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
                WeaponShot shot = createShot(itemStack, entityPlayer, Mouse.isButtonDown(1));
                onClientShot(itemStack, entityPlayer, pos, dir, shot);
                addShootDelay(itemStack);
                sendShootTickToServer(world, shot, dir, pos);
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
        Vec3 pos = Vec3.createVectorHelper(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        if (!isAiming) {
            pos.xCoord -= (double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
            pos.zCoord -= (double)(MathHelper.sin(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
            pos.yCoord -= (double)(MathHelper.cos(entityPlayer.rotationPitch / 180.0F * (float) Math.PI) * 0.16F);
        }
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
