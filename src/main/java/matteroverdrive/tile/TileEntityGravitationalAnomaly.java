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

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.IScannable;
import matteroverdrive.client.sound.GravitationalAnomalySound;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.fx.GravitationalAnomalyParticle;
import matteroverdrive.items.SpacetimeEqualizer;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.util.vector.Vector3f;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Simeon on 5/11/2015.
 */
public class TileEntityGravitationalAnomaly extends MOTileEntity implements IScannable, IMOTickable
{
    public static boolean FALLING_BLOCKS = true;
    public static boolean BLOCK_ENTETIES = true;
    public static boolean VANILLA_FLUIDS = true;
    public static boolean FORGE_FLUIDS = true;
    public static final float MAX_VOLUME = 0.5f;
    public static final int BLOCK_DESTORY_DELAY = 6;
    public static final int MAX_BLOCKS_PER_HARVEST = 6;
    public static final int MAX_LIQUIDS_PER_HARVEST = 32;
    public static final double STREHGTH_MULTIPLYER = 0.00001;
    public static final double G = 6.67384;
    public static final double G2 = G * 2;
    public static final double C = 2.99792458;
    public static final double CC = C * C;
    public static final int ENTITY_DAMAGE = 6;

    @SideOnly(Side.CLIENT)
    private GravitationalAnomalySound sound;
    private TimeTracker blockDestoryTimer;
    private long mass;
    @SideOnly(Side.CLIENT)
    public int consumedCount;
    PriorityQueue<PositionWrapper> blocks;
    private float tmpSuppression;
    private float suppression;

    public TileEntityGravitationalAnomaly()
    {
        blockDestoryTimer = new TimeTracker();
        this.mass = 2048 + MathHelper.round(Math.random() * 8192);
    }

    public TileEntityGravitationalAnomaly(int mass)
    {
        this();
        this.mass = mass;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setLong("Mass", mass);
            nbt.setFloat("Suppression", suppression);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA)) {
            mass = nbt.getLong("Mass");
            suppression = nbt.getFloat("Suppression");
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData, MachineNBTCategory.ALL_OPTS);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        //System.out.println("Receiving Packet From Server");
        NBTTagCompound syncData = pkt.func_148857_g();
        if(syncData != null)
        {
            readCustomNBT(syncData, MachineNBTCategory.ALL_OPTS);
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (worldObj.isRemote)
        {
            spawnParticles(worldObj);
            manageSound();
        }
    }

    public void invalidate()
    {
        super.invalidate();
        if (worldObj.isRemote)
        {
            stopSounds();
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticles(World world)
    {
        double radius = (float)getBlockBreakRange();
        Vector3f point = MOMathHelper.randomSpherePoint(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, Vec3.createVectorHelper(radius, radius, radius), world.rand);
        GravitationalAnomalyParticle particle = new GravitationalAnomalyParticle(world,point.x, point.y, point.z, Vec3.createVectorHelper(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f));
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public void manageEntityGravitation(World world,float ticks)
    {
        if (world.isRemote)
            consumedCount = 0;

        double range = getMaxRange() + 1;
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range, yCoord + range, zCoord + range);
        List entities = world.getEntitiesWithinAABB(Entity.class, bb);
        for (Object entityObject : entities)
        {
            if (entityObject instanceof EntityPlayer)
            {
                AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)entityObject);
                if (((EntityPlayer)entityObject).capabilities.isCreativeMode || androidPlayer.isUnlocked(MatterOverdrive.statRegistry.equalizer,1))
                    continue;
            }

            if (entityObject instanceof Entity)
            {
                Entity entity = (Entity)entityObject;
                Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);

                pos.yCoord += entity.getEyeHeight();
                Vec3 blockPos = Vec3.createVectorHelper(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                Vec3 dir = pos.subtract(blockPos).normalize();
                double distance = pos.distanceTo(blockPos);
                double eventHorizon = getEventHorizon();
                if (distance < eventHorizon)
                {
                    if (!world.isRemote)
                        consume(entity);
                    else
                        consumedCount++;
                }
                else
                {
                    if (entityObject instanceof EntityLivingBase)
                    {
                        ItemStack eq = ((EntityLivingBase) entityObject).getEquipmentInSlot(3);
                        if (eq != null && eq.getItem() instanceof SpacetimeEqualizer)
                            continue;
                    }

                    double acceleration = getAcceleration(distance);
                    entity.addVelocity(dir.xCoord * acceleration,dir.yCoord * acceleration,dir.zCoord * acceleration);
                    entity.velocityChanged = true;
                }
            }
        }
    }

    public void onChunkUnload()
    {
        if (worldObj.isRemote)
        {
            stopSounds();
        }
    }

    @SideOnly(Side.CLIENT)
    public void stopSounds()
    {
        if (sound != null)
        {
            sound.stopPlaying();
            FMLClientHandler.instance().getClient().getSoundHandler().stopSound(sound);
            sound = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void playSounds()
    {
        if (sound == null)
        {
            sound = new GravitationalAnomalySound(new ResourceLocation(Reference.MOD_ID + ":" + "windy"),xCoord,yCoord,zCoord,0.2f,getMaxRange());
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
        }
        else if (!FMLClientHandler.instance().getClient().getSoundHandler().isSoundPlaying(sound))
        {
            stopSounds();
            sound = new GravitationalAnomalySound(new ResourceLocation(Reference.MOD_ID + ":" + "windy"),xCoord,yCoord,zCoord,0.2f,getMaxRange());
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
        }
    }

    @SideOnly(Side.CLIENT)
    public void manageSound()
    {
        if (sound == null)
        {
            playSounds();
        }else
        {
            sound.setVolume(Math.min(MAX_VOLUME,getBreakStrength() * 0.1f));
            sound.setRange(getMaxRange());
        }
    }

    @Override
    public void onServerTick(TickEvent.WorldTickEvent event)
    {
        if (worldObj == null)
            return;

        if (event.phase.equals(TickEvent.Phase.END))
        {
            manageEntityGravitation(worldObj, 0);
            manageBlockDestory(worldObj);

            if (tmpSuppression != suppression)
            {
                suppression = tmpSuppression;
                worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            }

            tmpSuppression = 1;
        }
    }

    @Override
    public int getPhase()
    {
        return 1;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void onNeighborBlockChange() {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    public  static class BlockComparitor implements Comparator<PositionWrapper>
    {
        int posX,posY,posZ;

        public BlockComparitor(int x,int y,int z)
        {
            posX = x;
            posY = y;
            posZ = z;
        }

        @Override
        public int compare(PositionWrapper o1, PositionWrapper o2)
        {
            return Double.compare(MOMathHelper.distanceSqured(o1.x, o1.y, o1.z, posX, posY, posZ),MOMathHelper.distanceSqured(o2.x, o2.y, o2.z, posX, posY, posZ));
        }
    }

    public static class PositionWrapper
    {
        int x,y,z;

        public PositionWrapper(int x,int y,int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void manageBlockDestory(World world)
    {
        Vec3 pos = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
        int solidCount = 0;
        int liquidCount = 0;
        int range = MathHelper.floor(getBlockBreakRange());
        double distance;
        double eventHorizon = getEventHorizon();
        float strength = getBreakStrength();
        int blockPosX,blockPosY,blockPosZ;
        float hardness;
        Block block;

        blocks = new PriorityQueue<>(1,new BlockComparitor(xCoord,yCoord,zCoord));

        if (blockDestoryTimer.hasDelayPassed(world,BLOCK_DESTORY_DELAY))
        {
            for (int x = -range; x < range;x++)
            {
                for (int y = -range;y < range;y++)
                {
                    for (int z = -range;z < range;z++)
                    {
                        blockPosX = xCoord + x;
                        blockPosY = yCoord + y;
                        blockPosZ = zCoord + z;
                        block = getBlock(world,blockPosX,blockPosY,blockPosZ);
                        distance = MOMathHelper.distance(blockPosX,blockPosY,blockPosZ,xCoord,yCoord,zCoord);
                        hardness = block.getBlockHardness(world,blockPosX,blockPosY,blockPosZ);
                        if (block instanceof IFluidBlock || block instanceof BlockLiquid)
                        {
                            hardness = 1;
                        }

                        if (block != null && block != Blocks.air && distance <= range && hardness >= 0 && (distance < eventHorizon || hardness < strength))
                        {
                            blocks.add(new PositionWrapper(blockPosX,blockPosY,blockPosZ));
                        }
                    }
                }
            }
        }

        for (PositionWrapper position : blocks)
        {
            block = world.getBlock(position.x,position.y,position.z);

            if (!cleanFlowingLiquids(block,position.x,position.y,position.z))
            {
                if (liquidCount < MAX_LIQUIDS_PER_HARVEST) {
                    if (cleanLiquids(block, position.x, position.y, position.z)) {
                        liquidCount++;
                        continue;
                    }
                }
                if (solidCount < MAX_BLOCKS_PER_HARVEST) {
                    if (brakeBlock(world,position.x, position.y, position.z,strength,eventHorizon,range)) {
                        solidCount++;
                    }
                }
            }
        }
    }

    public Block getBlock(World world,int x,int y,int z)
    {
        return world.getBlock(x,y,z);
    }

    public double getEventHorizon()
    {
        return (G2 * getRealMass()) / CC;
    }

    public double getBlockBreakRange()
    {
        return getMaxRange() / 2;
    }

    public double getMaxRange()
    {
        return Math.sqrt((G * getRealMass() / 0.005));
    }

    public double getAcceleration(double range)
    {
        return (getRealMass() / range);
    }

    public double getRealMass() {
         return getRealMassUnsuppressed() * suppression;
    }

    public double getRealMassUnsuppressed()
    {
        return Math.log1p(mass * STREHGTH_MULTIPLYER);
    }

    public float getBreakStrength()
    {
        return (float)getRealMass() * 4 * suppression;
    }

    public void consume(Entity entity) {
        int matter = 1;

        if (!entity.isDead) {
            if (entity instanceof EntityItem) {
                ItemStack itemStack = ((EntityItem) entity).getEntityItem();
                if (itemStack != null) {
                    matter = MatterHelper.getMatterAmountFromItem(itemStack) * itemStack.stackSize;
                }
                entity.setDead();
                worldObj.removeEntity(entity);
            } else if (entity instanceof EntityFallingBlock) {
                ItemStack itemStack = new ItemStack(((EntityFallingBlock) entity).func_145805_f());
                matter = MatterHelper.getMatterAmountFromItem(itemStack) * itemStack.stackSize;
                entity.setDead();
                worldObj.removeEntity(entity);
            } else if (entity instanceof EntityPlayer) {
                matter += Math.min(((EntityLivingBase) entity).getHealth(), getBreakStrength());
                DamageSource damageSource = new DamageSource("blackHole");
                entity.attackEntityFrom(damageSource, getBreakStrength());
            } else if (entity instanceof EntityLivingBase) {
                matter += Math.min(((EntityLivingBase) entity).getHealth(), getBreakStrength());
                if (((EntityLivingBase) entity).getHealth() <= getBreakStrength()) {
                    entity.setDead();
                    worldObj.removeEntity(entity);
                }

                DamageSource damageSource = new DamageSource("blackHole");
                entity.attackEntityFrom(damageSource, getBreakStrength());
            }

            mass += matter;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Math.pow(getMaxRange(),3);
    }

    public boolean brakeBlock(World world,int x,int y,int z,float strength,double eventHorizon,int range)
    {
        Block block = world.getBlock(x, y, z);
        if (block == null)
            return true;

        float hardness = block.getBlockHardness(worldObj,x,y,z);
        double distance = MOMathHelper.distance(x,y,z,xCoord,yCoord,zCoord);
        if (distance <= range && hardness >= 0 && (distance < eventHorizon || hardness < strength))
        {
            int meta = worldObj.getBlockMetadata(x, y, z);
            if (BLOCK_ENTETIES) {

                if (FALLING_BLOCKS)
                {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, x + 0.5, y + 0.5, z + 0.5, block, world.getBlockMetadata(x, y, z));
                    fallingBlock.field_145812_b = 1;
                    fallingBlock.noClip = true;
                    world.spawnEntityInWorld(fallingBlock);
                }
                else {
                    ItemStack bStack = createStackedBlock(block, meta);
                    if (bStack != null)
                    {
                        EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, bStack);
                        world.spawnEntityInWorld(item);
                    }
                }

                block.breakBlock(world, x, y, z, block, 0);
                worldObj.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
                world.setBlock(x, y, z, Blocks.air, 0, 10);
                return true;
            }else
            {
                int matter = 0;

                if (block.canSilkHarvest(worldObj, null, x, y, z, meta)) {
                    matter += MatterHelper.getMatterAmountFromItem(createStackedBlock(block, meta));
                } else {
                    for (ItemStack stack : block.getDrops(worldObj, x, y, z, meta, 0))
                    {
                        matter+= MatterHelper.getMatterAmountFromItem(stack);
                    }
                }

                worldObj.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));

                List<EntityItem> result = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 2, y - 2, z - 2, x + 3, y + 3, z + 3));
                for (EntityItem entityItem : result) {
                    EntityItem entity = entityItem;
                    if (entity.isDead || entity.getEntityItem().stackSize <= 0) {
                        continue;
                    }
                    matter += MatterHelper.getMatterAmountFromItem(entity.getEntityItem());
                    entity.worldObj.removeEntity(entity);
                }

                mass += matter;
                world.setBlock(x, y, z, Blocks.air, 0, 10);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
        }

        return false;
    }

    protected ItemStack createStackedBlock(Block block,int meta)
    {
        if (block != null) {
            Item item = Item.getItemFromBlock(block);
            if (item != null) {
                if (item.getHasSubtypes()) {
                    return new ItemStack(item, 1, meta);
                }
                return new ItemStack(item, 1, 0);
            }
        }
        return null;
    }

    public boolean cleanLiquids(Block block,int x,int y,int z)
    {
        if (block instanceof IFluidBlock && FORGE_FLUIDS)
        {
            if(((IFluidBlock) block).canDrain(worldObj,x,y,z))
            {
                if (FALLING_BLOCKS)
                {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(worldObj, x + 0.5, y + 0.5, z + 0.5, block, worldObj.getBlockMetadata(x, y, z));
                    fallingBlock.field_145812_b = 1;
                    fallingBlock.noClip = true;
                    worldObj.spawnEntityInWorld(fallingBlock);
                }

                ((IFluidBlock) block).drain(worldObj,x,y,z,true);
                return true;
            }

        }else if (block instanceof BlockLiquid && VANILLA_FLUIDS)
        {
            if(worldObj.setBlock(x, y, z, Blocks.air, 0, 2)) {
                if (FALLING_BLOCKS)
                {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(worldObj, x + 0.5, y + 0.5, z + 0.5, block, worldObj.getBlockMetadata(x, y, z));
                    fallingBlock.field_145812_b = 1;
                    fallingBlock.noClip = true;
                    worldObj.spawnEntityInWorld(fallingBlock);
                }
                return true;
            }
        }

        return false;
    }

    public boolean cleanFlowingLiquids(Block block,int x,int y,int z) {
        if (VANILLA_FLUIDS) {
            if (block == Blocks.flowing_water || block == Blocks.flowing_lava) {
                return worldObj.setBlock(x, y, z, Blocks.air, 0, 2);
            }
        }
        return false;
    }

    @Override
    public void addInfo(World world, double x, double y, double z, List<String> infos)
    {
        DecimalFormat format = new DecimalFormat("#.##");
        infos.add("Mass: " + mass);
        infos.add("Range: " + format.format(getMaxRange()));
        infos.add("Brake Range: " + format.format(getBlockBreakRange()));
        infos.add("Horizon: " + format.format(getEventHorizon()));
        infos.add("Brake Lvl: " + format.format(getBreakStrength()));
    }

    @Override
    public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner) {

    }

    public void supress(double amount)
    {
        tmpSuppression -= amount;
        tmpSuppression = Math.max(0,tmpSuppression);
    }
}
