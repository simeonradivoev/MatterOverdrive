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

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.weapon.MOEventPlasmaBlotHit;
import matteroverdrive.api.gravity.IGravitationalAnomaly;
import matteroverdrive.api.gravity.IGravityEntity;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.client.PacketUpdatePlasmaBolt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 7/25/2015.
 */
public class PlasmaBolt extends Entity implements IProjectile, IGravityEntity, IEntityAdditionalSpawnData
{
    private BlockPos blockPos;
    private int distanceTraveled;
    private float damage;
    public Entity shootingEntity;
    private IBlockState blockState;
    private int life;
    private int color;
    private float fireDamageMultiply;
    private ItemStack weapon;
    private float renderSize = 2;
    private boolean canHurtCaster = false;
    private float knockback;
    private boolean canRicoche;

    public PlasmaBolt(World world)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public PlasmaBolt(World world, EntityLivingBase entityLivingBase, Vec3 position, Vec3 dir, WeaponShot shot,float speed) {
        super(world);
        rand.setSeed(shot.getSeed());
        setEntityId(shot.getSeed());
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entityLivingBase;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(position.xCoord, position.yCoord, position.zCoord, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
        this.motionX = dir.xCoord;
        this.motionY = dir.yCoord;
        this.motionZ = dir.zCoord;
        this.height = 0.0F;
        this.life = shot.getRange();
        this.damage = shot.getDamage();
        this.color = shot.getColor();
        this.blockPos = new BlockPos(0,0,0);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ,speed * 1.5F, shot.getAccuracy());
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float speed, float accuracy)
    {
        float dirLength = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)dirLength;
        y /= (double)dirLength;
        z /= (double)dirLength;
        x += this.rand.nextGaussian() * 0.007499999832361937D * (double)accuracy;
        y += this.rand.nextGaussian() * 0.007499999832361937D * (double)accuracy;
        z += this.rand.nextGaussian() * 0.007499999832361937D * (double)accuracy;
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

    @Override
    protected void entityInit()
    {

    }

    public void simulateDelay(int delay)
    {
        if (delay > 0) {
            double lastMotionX = motionX;
            double lastMotionY = motionY;
            double lastMotionZ = motionZ;
            motionX *= delay;
            motionY *= delay;
            motionZ *= delay;
            onUpdate();
            motionX = lastMotionX;
            motionY = lastMotionY;
            motionZ = lastMotionZ;
        }
    }

    @Override
    public void onUpdate()
    {
        if (!worldObj.isRemote && shootingEntity instanceof EntityPlayerMP)
        {
            MatterOverdrive.packetPipeline.sendTo(new PacketUpdatePlasmaBolt(getEntityId(),posX,posY,posZ),(EntityPlayerMP) shootingEntity);
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        IBlockState blockState = worldObj.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (block != null  && block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj,blockPos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockPos,blockState);

            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                setDead();
            }
        }

        if (this.distanceTraveled > this.life)
        {
            setDead();
            return;
        }

        distanceTraveled+=new Vec3(motionX,motionY,motionZ).lengthVector();
        float motionLeway = 0.0f;
        Vec3 vec31 = new Vec3(this.posX - this.motionX*motionLeway, this.posY - this.motionY*motionLeway, this.posZ - this.motionZ*motionLeway);
        Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
        vec31 = new Vec3(this.posX - this.motionX*motionLeway, this.posY - this.motionY*motionLeway, this.posZ - this.motionZ*motionLeway);
        vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        Vec3 hit = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        for (Object aList : list)
        {
            Entity entity1 = (Entity) aList;

            if (entity1 != null && entity1.canBeCollidedWith() && !entity1.isDead && entity1 instanceof EntityLivingBase && ((EntityLivingBase) entity1).deathTime == 0)
            {
                float f1 = 0.3f;
                if (this.shootingEntity != null)
                {
                    if (this.shootingEntity instanceof EntityLivingBase)
                    {
                        if (!canAttackTeammate((EntityLivingBase) entity1, (EntityLivingBase) this.shootingEntity))
                            continue;
                    }
                    if (!canHurtCaster && (entity1 == this.shootingEntity || entity1 == this.shootingEntity.ridingEntity))
                        continue;
                }

                AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                if (movingobjectposition1 != null)
                {
                    double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        hit = movingobjectposition1.hitVec;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity,hit);
        }

        if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

            if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                movingobjectposition = null;
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null) {
                DamageSource damagesource;

                if (this.shootingEntity == null) {
                    damagesource = getDamageSource(this);
                } else {
                    damagesource = getDamageSource(this.shootingEntity);
                }

                movingobjectposition.entityHit.hurtResistantTime = 0;
                double lastMotionX = movingobjectposition.entityHit.motionX;
                double lastMotionY = movingobjectposition.entityHit.motionY;
                double lastMotionZ = movingobjectposition.entityHit.motionZ;

                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, this.damage))
                {
                    movingobjectposition.entityHit.motionX = lastMotionX + (movingobjectposition.entityHit.motionX - lastMotionX) * knockback;
                    movingobjectposition.entityHit.motionY = lastMotionY + (movingobjectposition.entityHit.motionY - lastMotionY) * knockback;
                    movingobjectposition.entityHit.motionZ = lastMotionZ + (movingobjectposition.entityHit.motionZ - lastMotionZ) * knockback;

                    if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                        EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                        if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                            EnchantmentHelper.applyArthropodEnchantments(entitylivingbase, this.shootingEntity);
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                        }

                        if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                    }

                    if (fireDamageMultiply > 0)
                    {
                        movingobjectposition.entityHit.setFire((int)(10 * fireDamageMultiply));
                    }

                    if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        this.setDead();
                    }
                }
                else
                {
                    if (movingobjectposition.entityHit instanceof EntityLivingBase)
                    {
                        //client hit
                        this.setDead();
                    }
                }

                if (weapon != null && weapon.getItem() instanceof EnergyWeapon)
                {
                    if (worldObj.isRemote) {
                        ((EnergyWeapon) weapon.getItem()).onProjectileHit(movingobjectposition, weapon, worldObj, 5);
                        onHit(movingobjectposition);
                    }

                    MinecraftForge.EVENT_BUS.post(new MOEventPlasmaBlotHit(weapon,movingobjectposition,this,worldObj.isRemote ? Side.CLIENT : Side.SERVER));
                }
            } else {
                this.blockPos = movingobjectposition.getBlockPos();
                this.blockState = this.worldObj.getBlockState(blockPos);

                if (this.blockState.getBlock().getMaterial() != Material.air) {
                    this.blockState.getBlock().onEntityCollidedWithBlock(this.worldObj, blockPos, this);
                    if (this.blockState instanceof BlockTNT)
                    {
                        worldObj.setBlockToAir(blockPos);
                        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldObj, (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 0.5F), (double)((float)blockPos.getZ() + 0.5F), shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) shootingEntity : null);
                        entitytntprimed.fuse = 0;
                        worldObj.spawnEntityInWorld(entitytntprimed);
                    }
                }
                if (weapon != null && weapon.getItem() instanceof EnergyWeapon)
                {
                    if (worldObj.isRemote) {
                        ((EnergyWeapon) weapon.getItem()).onProjectileHit(movingobjectposition, weapon, worldObj, 5);
                        onHit(movingobjectposition);
                    }

                    MinecraftForge.EVENT_BUS.post(new MOEventPlasmaBlotHit(weapon,movingobjectposition,this,worldObj.isRemote ? Side.CLIENT : Side.SERVER));
                }
                if (canRicoche)
                {
                    handleRicochets(movingobjectposition);
                }else
                {
                    setDead();
                }
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        //this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ);
        //this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
        //this.posY = this.getEntityBoundingBox().minY + this.getYOffset() - (double)this.height;
        //this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        this.setPositionAndRotation(this.posX,this.posY,this.posZ,(float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI),(float)(Math.atan2(motionY, (double)f3) * 180.0D / Math.PI));
        this.doBlockCollisions();
        super.onUpdate();
    }

    protected void handleRicochets(MovingObjectPosition hit)
    {
        Vec3 motion = new Vec3(this.motionX, this.motionY, this.motionZ);
        Vec3 surfaceNormal = new Vec3(hit.sideHit.getDirectionVec());
        double deflectDot = surfaceNormal.dotProduct(motion) * 2;
        Vec3 projection = new Vec3(surfaceNormal.xCoord * deflectDot, surfaceNormal.yCoord * deflectDot, surfaceNormal.zCoord * deflectDot);
        Vec3 deflectDir = motion.subtract(projection);
        this.motionX = deflectDir.xCoord;
        this.motionY = deflectDir.yCoord;
        this.motionZ = deflectDir.zCoord;
        this.distanceTraveled += 2;
        canHurtCaster = true;
    }

    @Override
    public void setDead()
    {
        if (worldObj.isRemote)
        {
            ((ClientWeaponHandler)MatterOverdrive.proxy.getWeaponHandler()).removePlasmaBolt(this);
        }
        super.setDead();
    }

    @SideOnly(Side.CLIENT)
    protected void onHit(MovingObjectPosition hit) {
        Vec3 sideHit;
        if (hit.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
            sideHit = new Vec3(hit.sideHit.getDirectionVec());
        } else {
            sideHit = new Vec3(-motionX, -motionY, -motionZ);
        }
        Color c = new Color(color);
        //EntityExplodeFX explodeFX = new EntityExplodeFX(worldObj, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, 0, 0, 0);
        //explodeFX.setRBGColorF(c.getFloatR(),c.getFloatG(),c.getFloatB());
        //Minecraft.getMinecraft().effectRenderer.addEffect(explodeFX);
        if (rand.nextFloat() < 0.8f) {
            int hitPraticles = Math.max(0, (int) (16*renderSize) - ((int) (8 * renderSize) * Minecraft.getMinecraft().gameSettings.particleSetting));
            for (int i = 0; i < hitPraticles; i++) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(worldObj, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, c, sideHit.xCoord * 30, sideHit.yCoord * 30, sideHit.zCoord * 30));
            }

            if (getRenderSize() > 0.5)
            {
                MOPositionedSound sizzleSound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "sizzle"), rand.nextFloat() * 0.2f + 0.4f, rand.nextFloat() * 0.6f + 0.7f);
                sizzleSound.setPosition((float) hit.hitVec.xCoord, (float) hit.hitVec.yCoord, (float) hit.hitVec.zCoord);
                Minecraft.getMinecraft().getSoundHandler().playSound(sizzleSound);
                if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    MOPositionedSound ricochetSound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "laser_ricochet"), rand.nextFloat() * 0.2f + 0.6f, rand.nextFloat() * 0.2f + 1f);
                    ricochetSound.setPosition((float) hit.hitVec.xCoord, (float) hit.hitVec.yCoord, (float) hit.hitVec.zCoord);
                    Minecraft.getMinecraft().getSoundHandler().playSound(ricochetSound);
                }
            }
        }

        if (hit.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY))
        {
            if (hit.entityHit instanceof EntityLivingBase)
            {
                for (int s = 0; s < Math.max(0,10 - (5 * Minecraft.getMinecraft().gameSettings.particleSetting)); s++) {
                    worldObj.spawnParticle(EnumParticleTypes.REDSTONE, hit.hitVec.xCoord+rand.nextDouble()*0.4-0.2, hit.hitVec.yCoord+rand.nextDouble()*0.4-0.2, hit.hitVec.zCoord+rand.nextDouble()*0.4-0.2, 0,0,0);
                }
            }
        }
    }

    private boolean canAttackTeammate(EntityLivingBase shooter,EntityLivingBase target)
    {
        if (shooter != null && target != null)
        {
            if (shooter.getTeam() != null && shooter.isOnSameTeam(target))
            {
                return shooter.getTeam().getAllowFriendlyFire();
            }
            return true;
        }
        return true;
    }

    public DamageSource getDamageSource(Entity shootingEntity)
    {
        EntityDamageSourceIndirect dmg = new EntityDamageSourceIndirect("plasmaBolt",this,shootingEntity);
        dmg.setProjectile();
        return dmg;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short) this.blockPos.getX());
        tagCompound.setShort("yTile", (short) this.blockPos.getY());
        tagCompound.setShort("zTile", (short) this.blockPos.getZ());
        tagCompound.setFloat("damage", this.damage);
        tagCompound.setInteger("distanceTraveled", this.distanceTraveled);
        tagCompound.setInteger("life", this.life);
        tagCompound.setInteger("color", this.color);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        this.blockPos = new BlockPos(tagCompound.getShort("xTile"),tagCompound.getShort("yTile"),tagCompound.getShort("zTile"));
        if (tagCompound.hasKey("damage", 99))
        {
            this.damage = tagCompound.getFloat("damage");
        }
        if (tagCompound.hasKey("ticksInAir", 99))
        {
            this.distanceTraveled = tagCompound.getInteger("distanceTraveled");
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

    public void setWeapon(ItemStack weapon)
    {
        this.weapon = weapon;
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
    public boolean isAffectedByAnomaly(IGravitationalAnomaly anomaly) {
        return false;
    }

    @Override
    public void onEntityConsumed(IGravitationalAnomaly anomaly)
    {

    }

    public void setFireDamageMultiply(float fiery)
    {
        this.fireDamageMultiply = fiery;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeFloat(damage);
        buffer.writeInt(color);
        buffer.writeFloat(fireDamageMultiply);
        buffer.writeInt(life);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        damage = additionalData.readFloat();
        color = additionalData.readInt();
        fireDamageMultiply = additionalData.readFloat();
        life = additionalData.readInt();
    }

    public float getLife()
    {
        return 1f-((float)distanceTraveled/(float)life);
    }

    public float setLife(float life)
    {
        return life;
    }

    public void setRenderSize(float size)
    {
        this.renderSize = size;
    }

    public float getRenderSize(){return renderSize;}

    public void setKnockBack(float knockback)
    {
        this.knockback = knockback;
    }

    public void markRicochetable()
    {
        this.canRicoche = true;
    }
}
