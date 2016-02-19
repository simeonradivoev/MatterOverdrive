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

package matteroverdrive.tile;

import matteroverdrive.Reference;
import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.weapon.IWeaponColor;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.ModuleSlot;
import matteroverdrive.data.inventory.TeleportFlashDriveSlot;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import matteroverdrive.items.TransportFlashDrive;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.configs.ConfigPropertyInteger;
import matteroverdrive.machines.configs.ConfigPropertyString;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

/**
 * Created by Simeon on 12/10/2015.
 */
public class TileEntityAndroidSpawner extends MOTileEntityMachine
{
    public int FLASH_DRIVE_SLOT_START;
    public static final int FLASH_DRIVE_COUNT = 6;
    public int COLOR_MODULE_SLOT;
    private final Set<EntityRougeAndroidMob> spawnedAndroids;

    public TileEntityAndroidSpawner()
    {
        super(0);
        spawnedAndroids = new HashSet<>();
        playerSlotsMain = true;
        playerSlotsHotbar = true;
    }

    protected void RegisterSlots(Inventory inventory)
    {
        COLOR_MODULE_SLOT = inventory.AddSlot(new ModuleSlot(true,Reference.MODULE_COLOR,null));
        TeleportFlashDriveSlot slot = new TeleportFlashDriveSlot(false);
        slot.setKeepOnDismante(true);
        FLASH_DRIVE_SLOT_START = inventory.AddSlot(slot);

        for (int i = 0;i < FLASH_DRIVE_COUNT-1;i++)
        {
            slot = new TeleportFlashDriveSlot(false);
            slot.setKeepOnDismante(true);
            inventory.AddSlot(slot);
        }
        super.RegisterSlots(inventory);
    }

    @Override
    public void update()
    {
        super.update();

        if (!worldObj.isRemote)
        {
            if (isActive())
            {
                if (getSpawnDelay() == 0 || worldObj.getTotalWorldTime() % getSpawnDelay() == 0)
                {
                    for (int i = spawnedAndroids.size(); i < getMaxSpawnCount(); ++i)
                    {
                        EntityRougeAndroidMob entity;

                        if (random.nextInt(10) < 3)
                        {
                            entity = new EntityMeleeRougeAndroidMob(worldObj);
                        }else
                        {
                            entity = new EntityRangedRogueAndroidMob(worldObj);
                        }


                        double spawnRange = getSpawnRange();

                        double x = (double) getPos().getX() + MathHelper.clamp_double(worldObj.rand.nextGaussian(),0,1) * spawnRange;
                        double y = (double) (getPos().getY() + worldObj.rand.nextInt(3) - 1);
                        double z = (double) getPos().getZ() + MathHelper.clamp_double(worldObj.rand.nextGaussian(),0,1) * spawnRange;
                        int topY = worldObj.getHeight(new BlockPos(x,y,z)).getY();
                        topY = Math.min(topY,getPos().getY()+3);
                        entity.setLocationAndAngles(x, topY, z, worldObj.rand.nextFloat() * 360.0F, 0.0F);

                        if (entity.getCanSpawnHere(true,true,true))
                        {
                            entity.onInitialSpawn(worldObj.getDifficultyForLocation(getPos()), null);
                            entity.setSpawnerPosition(getPos());
                            entity.enablePersistence();
                            addSpawnedAndroid(entity);
                            worldObj.playAuxSFX(2004, getPos(), 0);
                            ScorePlayerTeam team = getTeam();
                            if (team != null)
                            {
                                entity.setTeam(team);
                                if (inventory.getStackInSlot(COLOR_MODULE_SLOT) != null && inventory.getStackInSlot(COLOR_MODULE_SLOT).getItem() instanceof IWeaponColor)
                                {
                                    entity.setVisorColor(((IWeaponColor) inventory.getStackInSlot(COLOR_MODULE_SLOT).getItem()).getColor(inventory.getStackInSlot(COLOR_MODULE_SLOT), null));
                                    if (entity.getHeldItem() != null)
                                    {
                                        WeaponHelper.setModuleAtSlot(Reference.MODULE_COLOR, entity.getHeldItem(), inventory.getStackInSlot(COLOR_MODULE_SLOT));
                                    }
                                }
                            }
                            this.spawnEntity(entity);
                            entity.spawnExplosionParticle();
                            forceSync();
                        }
                    }
                }
            }
        }
    }

    public ScorePlayerTeam getTeam()
    {
        String teamName= getTeamName();
        if (teamName != null && !teamName.isEmpty())
        {
            return worldObj.getScoreboard().getTeam(teamName);
        }
        return null;
    }

    public boolean isTeamValid()
    {
        String teamName= getTeamName();
        if (teamName != null && !teamName.isEmpty())
        {
            return worldObj.getScoreboard().getTeam(teamName) != null;
        }
        return true;
    }

    public void assignPath(EntityRougeAndroidMob androidMob)
    {
        List<Vec3> paths = new ArrayList<>();
        for (int i = FLASH_DRIVE_SLOT_START;i < FLASH_DRIVE_COUNT;i++)
        {
            ItemStack flashDrive = inventory.getSlot(i).getItem();
            if (flashDrive != null && flashDrive.getItem() instanceof TransportFlashDrive)
            {
                BlockPos position = ((TransportFlashDrive) flashDrive.getItem()).getTarget(flashDrive);
                if (position != null)
                    paths.add(new Vec3(position));
            }
        }

        if (paths.size() <= 0)
        {
            androidMob.setPath(new Vec3[]{new Vec3(getPos())},getSpawnRange());
        }else
        {
            androidMob.setPath(paths.toArray(new Vec3[paths.size()]),getSpawnRange());
        }
    }

    public int getMaxSpawnCount()
    {
        return configs.getInteger("max_spawn_amount",6);
    }

    public int getSpawnRange()
    {
        return configs.getInteger("spawn_range",4);
    }

    public String getTeamName()
    {
        return configs.getString("team",null);
    }

    public int getSpawnDelay(){return configs.getInteger("spawn_delay",300);}

    public int getSpawnedCount(){return spawnedAndroids.size();}

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        configs.addProperty(new ConfigPropertyInteger("max_spawn_amount","gui.config.spawn_amount",0,32,6));
        configs.addProperty(new ConfigPropertyInteger("spawn_range","gui.config.spawn_range",0,32,4));
        configs.addProperty(new ConfigPropertyInteger("spawn_delay","gui.config.spawn_delay",0,100000,300));
        configs.addProperty(new ConfigPropertyString("team","gui.config.team",""));
    }

    public EntityRougeAndroidMob spawnEntity(EntityRougeAndroidMob entity)
    {
        worldObj.spawnEntityInWorld(entity);
        return entity;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt,categories);
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        super.writeCustomNBT(nbt,categories, toDisk);
    }

    @Override
    public String getSound()
    {
        return null;
    }

    @Override
    public boolean hasSound()
    {
        return false;
    }

    @Override
    public boolean getServerActive()
    {
        return getRedstoneActive() && isTeamValid() && spawnedAndroids.size() <= getMaxSpawnCount();
    }

    @Override
    public float soundVolume()
    {
        return 0;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    @Override
    protected void onAwake(Side side)
    {
        if (side == Side.SERVER)
        {
            for (Entity entity : worldObj.loadedEntityList)
            {
                if (entity instanceof EntityRougeAndroidMob)
                {
                    if(((EntityRougeAndroidMob) entity).wasSpawnedFrom(this))
                    {
                        addSpawnedAndroid((EntityRougeAndroidMob)entity);
                        assignPath((EntityRougeAndroidMob)entity);
                    }
                }
            }
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event)
    {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    public void removeAllAndroids()
    {
        for (EntityRougeAndroidMob androidMob : spawnedAndroids)
        {
            androidMob.isDead = true;
        }
        spawnedAndroids.clear();
    }

    public void addSpawnedAndroid(EntityRougeAndroidMob androidMob)
    {
        if (!spawnedAndroids.contains(androidMob))
        {
            spawnedAndroids.add(androidMob);
            assignPath(androidMob);
        }
    }

    public void removeAndroid(EntityRougeAndroidMob androidMob)
    {
        if(spawnedAndroids.remove(androidMob))
        {

        }
    }
}
