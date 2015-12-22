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

package matteroverdrive.entity.monster;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import matteroverdrive.Reference;
import matteroverdrive.api.entity.IPathableMob;
import matteroverdrive.data.BlockPos;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.TileEntityAndroidSpawner;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRougeAndroidMob extends EntityMob implements IEntityAdditionalSpawnData, IPathableMob<EntityRougeAndroidMob>
{
    private static ResourceLocation androidNames = new ResourceLocation(Reference.PATH_INFO + "android_names.txt");
    private static String[] names = MOStringHelper.readTextFile(androidNames).split(",");
    boolean fromSpawner;
    private BlockPos spawnerPosition;
    private int currentPathIndex;
    private Vec3[] path;
    private int maxPathTargetRangeSq;
    private int visorColor;
    private ScorePlayerTeam team;
    private boolean legendary;
    private int level;

    public EntityRougeAndroidMob(World world)
    {
        super(world);
        if (!world.isRemote)
        {
            setAndroidLevel((int) (MathHelper.clamp_double(Math.abs(rand.nextGaussian()), 0, 3)));
            boolean isLegendary = rand.nextDouble() < 0.05 * getAndroidLevel();
            setLegendary(isLegendary);
            init();
            getNavigator().setAvoidsWater(true);
            getNavigator().setCanSwim(false);
        }
    }

    public EntityRougeAndroidMob(World world,int level,boolean legendary)
    {
        super(world);
        setAndroidLevel(level);
        setLegendary(legendary);
        init();
    }

    private void init()
    {
        String name = getIsLegendary() ? String.format("\u272a %s ",MOStringHelper.translateToLocal("rarity.legendary")) : "";
        name += String.format("[%s] ",getAndroidLevel());
        name += names[rand.nextInt(names.length)];
        setCustomNameTag(name);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getIsLegendary() ? 128 : getAndroidLevel() * 10 + 32);
        this.setHealth(this.getMaxHealth());

        if (fromSpawner)
        {
            if (!addToSpawner(spawnerPosition))
            {
                setDead();
            }
        }

        if (getIsLegendary())
        {
            setVisorColor(Reference.COLOR_HOLO_RED.getColor());
        }else
        {
            switch (getAndroidLevel())
            {
                case 0:
                    setVisorColor(Reference.COLOR_HOLO.getColor());
                    break;
                case 1:
                    setVisorColor(Reference.COLOR_HOLO_YELLOW.getColor());
                    break;
                case 2:
                    setVisorColor(Reference.COLOR_HOLO_PURPLE.getColor());
                    break;
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setLegendary(nbtTagCompound.getBoolean("Legendary"));
        setAndroidLevel(nbtTagCompound.getByte("Level"));
        setVisorColor(nbtTagCompound.getInteger("VisorColor"));
        if (nbtTagCompound.hasKey("Team", Constants.NBT.TAG_STRING))
        {
            ScorePlayerTeam team = worldObj.getScoreboard().getTeam(nbtTagCompound.getString("Team"));
            if (team != null)
            {
                setTeam(team);
            }else
            {
                setDead();
            }
        }
        if (nbtTagCompound.hasKey("SpawnerPos", Constants.NBT.TAG_COMPOUND))
        {
            spawnerPosition = new BlockPos(nbtTagCompound.getCompoundTag("SpawnerPos"));
            this.fromSpawner = true;
        }
        currentPathIndex = nbtTagCompound.getInteger("CurrentPathIndex");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setByte("Level",(byte)getAndroidLevel());
        nbtTagCompound.setBoolean("Legendary",getIsLegendary());
        nbtTagCompound.setInteger("VisorColor",getVisorColor());
        if (getTeam() != null)
        {
            nbtTagCompound.setString("Team",getTeam().getRegisteredName());
        }
        if (spawnerPosition != null)
        {
            NBTTagCompound spawnerPos = new NBTTagCompound();
            spawnerPosition.writeToNBT(spawnerPos);
            nbtTagCompound.setTag("SpawnerPos",spawnerPos);
        }
        nbtTagCompound.setInteger("CurrentPathIndex",currentPathIndex);
    }

    private boolean addToSpawner(BlockPos position)
    {
        this.spawnerPosition = position;
        TileEntityAndroidSpawner spawnerEntity = position.getTileEntity(worldObj,TileEntityAndroidSpawner.class);
        if (spawnerEntity != null)
        {
            spawnerEntity.addSpawnedAndroid(this);
            return true;
        }
        return false;
    }

    public void setAttackTarget(EntityLivingBase target)
    {
        if (target != null && target.getTeam() != null)
        {
            if (!target.getTeam().isSameTeam(getTeam()))
                super.setAttackTarget(target);
        }else
        {
            super.setAttackTarget(target);
        }
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potion)
    {
        return false;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return getCanSpawnHere(false,false,false);
    }

    public boolean getCanSpawnHere(boolean ignoreEntityCollision,boolean ignoreLight,boolean ignoreDimension)
    {
        if (!ignoreDimension)
        {
            if (EntityRogueAndroid.dimensionWhitelist.size() > 0)
            {
                return EntityRogueAndroid.dimensionWhitelist.contains(worldObj.provider.dimensionId) && inDimensionBlacklist();
            }
            if (inDimensionBlacklist())
            {
                return false;
            }
        }
        boolean light = ignoreLight ? true : isValidLightLevel();
        boolean entityCollison = ignoreEntityCollision ? true : this.worldObj.checkNoEntityCollision(this.boundingBox);
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && light && entityCollison && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
    }

    public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_)
    {
        float weight = 1-this.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_);
        weight *= this.worldObj.isSideSolid(p_70783_1_,p_70783_2_,p_70783_3_, ForgeDirection.UP) ? 0 : 1;
        weight /= Math.abs(p_70783_2_ - posX);
        return weight;
    }

    protected void addRandomArmor()
    {
        if (this.rand.nextFloat() < 0.15F)
        {
            int i = this.rand.nextInt(2);
            float f = this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            for (int j = 3; j >= 0; --j)
            {
                ItemStack itemstack = this.func_130225_q(j);

                if (j < 3 && this.rand.nextFloat() < f)
                {
                    break;
                }

                if (itemstack == null)
                {
                    Item item = null;

                    if (i == 3)
                    {
                        if (rand.nextBoolean())
                        {
                            switch (j + 1)
                            {
                                case 4:
                                    item = MatterOverdriveItems.tritaniumHelemet;
                                    break;
                                case 3:
                                    item = MatterOverdriveItems.tritaniumChestplate;
                                    break;
                                case 2:
                                    item = MatterOverdriveItems.tritaniumLeggings;
                                    break;
                                case 1:
                                    item = MatterOverdriveItems.tritaniumBoots;
                                    break;
                            }
                        }else
                        {
                            item = getArmorItemForSlot(j + 1, i);
                        }
                    }else
                    {
                        item = getArmorItemForSlot(j + 1, i);
                    }

                    if (item != null)
                    {
                        this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }

    @Override
    public boolean isWithinHomeDistance(int p_110176_1_, int p_110176_2_, int p_110176_3_)
    {
        return true;
    }

    @Override
    public boolean hasHome()
    {

        return getCurrentTarget() != null;
    }

    @Override
    public ChunkCoordinates getHomePosition()
    {
        Vec3 currentTarget = getCurrentTarget();
        return new ChunkCoordinates((int)currentTarget.xCoord,(int)currentTarget.yCoord,(int)currentTarget.zCoord);
    }

    private boolean inDimensionBlacklist() {
        return EntityRogueAndroid.dimensionBlacklist.contains(worldObj.provider.dimensionId);
    }

    public void setAndroidLevel(int level)
    {
        this.level = level;
    }

    public int getAndroidLevel()
    {
        return this.level;
    }

    public void setLegendary(boolean legendary)
    {
        this.legendary = legendary;
        this.height = 1.8f * 1.5f;
    }

    public boolean getIsLegendary()
    {
        return legendary;
    }

    public void setTeam(ScorePlayerTeam team)
    {
        this.team = team;
    }

    @Override
    public String getCustomNameTag()
    {
        if(hasTeam())
        {
            return getTeam().formatString(this.dataWatcher.getWatchableObjectString(10));
        }
        return this.dataWatcher.getWatchableObjectString(10);
    }

    @Override
    public ScorePlayerTeam getTeam()
    {
        return team;
    }

    public boolean hasTeam()
    {
        return getTeam() != null;
    }

    public boolean wasSpawnedFrom(TileEntityAndroidSpawner spawner)
    {
        if (spawnerPosition != null)
        {
            TileEntity tileEntity = spawnerPosition.getTileEntity(worldObj);
            return tileEntity == spawner;
        }
        return false;
    }

    public void setSpawnerPosition(BlockPos position)
    {
        this.spawnerPosition = position;
        this.fromSpawner = true;
    }

    @Override
    public void setDead()
    {
        if (spawnerPosition != null)
        {
            TileEntityAndroidSpawner spawner = spawnerPosition.getTileEntity(worldObj,TileEntityAndroidSpawner.class);
            if (spawner != null)
            {
                spawner.removeAndroid(this);
            }
        }
        this.isDead = true;
    }

    public int getVisorColor()
    {
        return visorColor;
    }

    public void setVisorColor(int color)
    {
        visorColor = color;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeByte(level);
        buffer.writeBoolean(legendary);
        buffer.writeInt(visorColor);
        buffer.writeBoolean(hasTeam());
        if (hasTeam())
        {
            ByteBufUtils.writeUTF8String(buffer, getTeam().getRegisteredName());
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        level = additionalData.readByte();
        legendary = additionalData.readBoolean();
        visorColor = additionalData.readInt();
        if (additionalData.readBoolean())
        {
            String teamName = ByteBufUtils.readUTF8String(additionalData);
            ScorePlayerTeam team = worldObj.getScoreboard().getTeam(teamName);
            if (team != null)
                setTeam(team);
        }
    }

    @Override
    public Vec3 getCurrentTarget()
    {
        if (path != null && currentPathIndex < path.length)
        {
            return path[currentPathIndex];
        }
        return null;
    }

    @Override
    public void onTargetReached(Vec3 pos)
    {
        if (currentPathIndex < path.length-1)
        {
            currentPathIndex++;
        }
    }

    @Override
    public boolean isNearTarget(Vec3 pos)
    {
        return pos.squareDistanceTo(posX, posY, posZ) < maxPathTargetRangeSq;
    }

    @Override
    public EntityRougeAndroidMob getEntity()
    {
        return this;
    }

    public void setPath(Vec3[] path,int range)
    {
        this.path = path;
        maxPathTargetRangeSq = range*range;
    }

    @Override
    protected String getLivingSound()
    {
        return Reference.MOD_ID + ":" + "rogue_android_say";
    }

    @Override
    protected String getDeathSound()
    {
        return Reference.MOD_ID + ":" + "rogue_android_death";
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.5f;
    }

    public int getTalkInterval()
    {
        return 20*24;
    }
}
