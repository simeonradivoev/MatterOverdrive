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

package matteroverdrive.entity.weapon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.IGravityEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simeon on 7/25/2015.
 */
public class PhaserFire extends Entity implements IProjectile, IGravityEntity
{
    private int field_145791_d = -1;
    private int field_145792_e = -1;
    private int field_145789_f = -1;
    private int ticksInAir;
    private float damage;
    public Entity shootingEntity;
    private Block block;
    private int life;
    private int color;
    private float fireDamageMultiply;

    public PhaserFire(World p_i1753_1_)
    {
        super(p_i1753_1_);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public PhaserFire(World world, EntityLivingBase entityLivingBase,Vec3 position,Vec3 dir,float damage, float speed,float accuracy,int color,boolean isAiming,int seed)
    {
        this(world, entityLivingBase,position,dir,damage, speed, accuracy, 60, color, isAiming,seed);
    }

    public PhaserFire(World world, EntityLivingBase entityLivingBase,Vec3 position,Vec3 dir,float damage, float speed,float accuracy,int life,int color,boolean isAiming,int seed) {
        super(world);
        rand.setSeed(seed);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entityLivingBase;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(position.xCoord, position.yCoord, position.zCoord, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
        this.motionX = dir.xCoord;
        this.motionY = dir.yCoord;
        this.motionZ = dir.zCoord;
        this.yOffset = 0.0F;
        this.life = life;
        this.damage = damage;
        this.color = color;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, accuracy);
    }

    public void setThrowableHeading(double x, double y, double z, float speed, float accuracy)
    {
        float dirLength = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)dirLength;
        y /= (double)dirLength;
        z /= (double)dirLength;
        x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)accuracy;
        y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)accuracy;
        z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)accuracy;
        x *= (double)speed;
        y *= (double)speed;
        z *= (double)speed;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void onUpdate() {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                setDead();
            }
        }

        if (this.ticksInAir > this.life)
        {
            setDead();
            return;
        }

        ++this.ticksInAir;
        Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
        vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        int i;
        float f1;

        for (i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1.canBeCollidedWith() && entity1 != this.shootingEntity) {
                f1 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                if (movingobjectposition1 != null) {
                    double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null && entity != this.shootingEntity) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

            if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                movingobjectposition = null;
            }
        }

        float f2;
        float f4;

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null) {
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                int k = MathHelper.ceiling_double_int((double) f2 * this.damage);

                DamageSource damagesource = null;

                if (this.shootingEntity == null) {
                    damagesource = DamageSource.causeThrownDamage(this, this);
                } else {
                    damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
                }

                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) k))
                {
                    if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                        EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                        if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                            EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                            EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entitylivingbase);
                        }

                        if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                    }

                    if (fireDamageMultiply > 0)
                    {
                        movingobjectposition.entityHit.setFire((int)(80 * fireDamageMultiply));
                    }

                    if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        this.setDead();
                    }
                }
                else
                {
                    //client hit
                    for (int s = 0;s < 10;s++)
                    {
                        worldObj.spawnParticle("reddust", posX,posY,posZ, 0, 0, 0);
                    }
                    this.setDead();
                }
            } else {
                this.field_145791_d = movingobjectposition.blockX;
                this.field_145792_e = movingobjectposition.blockY;
                this.field_145789_f = movingobjectposition.blockZ;
                this.block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);

                if (this.block.getMaterial() != Material.air) {
                    this.block.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f, this);
                }
                for (int s = 0;s < 10;s++)
                {
                    worldObj.spawnParticle("smoke", movingobjectposition.hitVec.xCoord,movingobjectposition.hitVec.yCoord,movingobjectposition.hitVec.zCoord, 0, 0, 0);
                }
                this.setDead();
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.func_145775_I();
    }

    public void simulate(long delay)
    {
        if (!worldObj.isRemote)
        {
            long averageTickTime = TimeUnit.NANOSECONDS.toMillis(MatterOverdrive.tickHandler.getLastTickLength());
            if (averageTickTime > 0) {
                int simulationCount = (int) (delay / averageTickTime);
                for (int i = 0; i < simulationCount; i++) {
                    onUpdate();
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short) this.field_145791_d);
        tagCompound.setShort("yTile", (short) this.field_145792_e);
        tagCompound.setShort("zTile", (short) this.field_145789_f);
        tagCompound.setFloat("damage", this.damage);
        tagCompound.setInteger("ticksInAir", this.ticksInAir);
        tagCompound.setInteger("life", this.life);
        tagCompound.setInteger("color", this.color);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        this.field_145791_d = tagCompound.getShort("xTile");
        this.field_145792_e = tagCompound.getShort("yTile");
        this.field_145789_f = tagCompound.getShort("zTile");

        if (tagCompound.hasKey("damage", 99))
        {
            this.damage = tagCompound.getFloat("damage");
        }
        if (tagCompound.hasKey("ticksInAir", 99))
        {
            this.ticksInAir = tagCompound.getInteger("ticksInAir");
        }
        if (tagCompound.hasKey("life", 99))
        {
            this.life = tagCompound.getInteger("life");
        }
        if (tagCompound.hasKey("color", 99))
        {
            this.color = tagCompound.getInteger("color");
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer p_70100_1_)
    {

    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setDamage(float p_70239_1_)
    {
        this.damage = p_70239_1_;
    }

    public double getDamage()
    {
        return this.damage;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }


    public int getColor()
    {
        return color;
    }
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    @Override
    public boolean isAffectedByAnomaly() {
        return false;
    }

    public void setFireDamageMultiply(float fiery)
    {
        this.fireDamageMultiply = fiery;
    }
}
