package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.IScannable;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.fx.GravitationalAnomalyParticle;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.items.SpacetimeEqualizer;
import com.MO.MatterOverdrive.sound.GravitationalAnomalySound;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.util.vector.Vector3f;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Simeon on 5/11/2015.
 */
public class TileEntityGravitationalAnomaly extends MOTileEntity implements IScannable, IMOTickable
{
    public static boolean FALLING_BLOCKS = true;
    public static boolean BLOCK_ENTETIES = true;
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
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        nbt.setLong("Mass", mass);
        nbt.setFloat("Suppression", suppression);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        mass = nbt.getLong("Mass");
        suppression = nbt.getFloat("Suppression");
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        //System.out.println("Receiving Packet From Server");
        NBTTagCompound syncData = pkt.func_148857_g();
        if(syncData != null)
        {
            readCustomNBT(syncData);
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
        for (int i = 0;i < entities.size();i++)
        {
            if (entities.get(i) instanceof EntityPlayer)
            {
                if (((EntityPlayer) entities.get(i)).capabilities.isCreativeMode)
                    continue;
            }

            if (entities.get(i) instanceof Entity)
            {
                Entity entity = (Entity)entities.get(i);
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
                    if (entities.get(i) instanceof EntityLivingBase)
                    {
                        ItemStack eq = ((EntityLivingBase) entities.get(i)).getEquipmentInSlot(3);
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

        blocks = new PriorityQueue<PositionWrapper>(1,new BlockComparitor(xCoord,yCoord,zCoord));

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

                        if (block != Blocks.air && distance <= range && hardness >= 0 && (distance < eventHorizon || hardness < strength))
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
                if (itemStack != null) {
                    matter = MatterHelper.getMatterAmountFromItem(itemStack) * itemStack.stackSize;
                }
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
                    EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, BlockHelper.createStackedBlock(block, meta));
                    world.spawnEntityInWorld(item);
                }

                block.breakBlock(world, x, y, z, block, 0);
                worldObj.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
                world.setBlock(x, y, z, Blocks.air, 0, 10);
                return true;
            }else
            {
                int matter = 0;

                if (block.canSilkHarvest(worldObj, null, x, y, z, meta)) {
                    matter += MatterHelper.getMatterAmountFromItem(BlockHelper.createStackedBlock(block, meta));
                } else {
                    for (ItemStack stack : block.getDrops(worldObj, x, y, z, meta, 0))
                    {
                        matter+= MatterHelper.getMatterAmountFromItem(stack);
                    }
                }

                worldObj.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));

                List<EntityItem> result = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 2, y - 2, z - 2, x + 3, y + 3, z + 3));
                for (int i = 0; i < result.size(); i++) {
                    EntityItem entity = result.get(i);
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

    public boolean cleanLiquids(Block block,int x,int y,int z)
    {
        if (block instanceof IFluidBlock)
        {
            if (FALLING_BLOCKS)
            {
                EntityFallingBlock fallingBlock = new EntityFallingBlock(worldObj, x + 0.5, y + 0.5, z + 0.5, block, worldObj.getBlockMetadata(x, y, z));
                fallingBlock.field_145812_b = 1;
                fallingBlock.noClip = true;
                worldObj.spawnEntityInWorld(fallingBlock);
                return true;
            }
            else
            {

            }

            worldObj.setBlock(x, y, z, Blocks.air, 0, 2);
        }

        return false;
    }

    public boolean cleanFlowingLiquids(Block block,int x,int y,int z) {
        if (block == Blocks.flowing_water || block == Blocks.flowing_lava) {
            return worldObj.setBlock(x, y, z, Blocks.air, 0, 2);
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
