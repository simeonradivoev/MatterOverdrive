package matteroverdrive.machines.dimensional_pylon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.data.Inventory;
import matteroverdrive.fx.Lightning;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.network.packet.client.PacketSpawnParticle;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/10/2016.
 */
public class ComponentPowerGeneration extends MachineComponentAbstract<TileEntityMachineDimensionalPylon> implements ITickable
{
    public static int CHARGE_DECREASE_ON_HIT = 16;
    public static int CHARGE_INCREASE_RATE = 64;
    public static int CHARGE_ENERGY_INCREASE = 128;
    public static int CHARGE_MATTER_INCREASE = 5;
    public static int MAX_POWER_GEN_PER_TICK = 256;
    private Random random;
    private int energyGenPerTick;
    private int matterDrainPerSec;

    public ComponentPowerGeneration(TileEntityMachineDimensionalPylon machine)
    {
        super(machine);
        this.random = new Random();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {

    }

    @Override
    public void registerSlots(Inventory inventory)
    {

    }

    @Override
    public void update()
    {
        if (!getWorld().isRemote)
        {
            manageServerLightning();
            manageCharge();
            managePowerGeneration();
        }
    }

    //region Power Generation
    public void managePowerGeneration()
    {
        float dimValue = machine.getDimensionalValue();
        float chargePercent = (float) machine.charge / (float)TileEntityMachineDimensionalPylon.MAX_CHARGE;
        energyGenPerTick = (int) (dimValue * MAX_POWER_GEN_PER_TICK) + (int)(CHARGE_ENERGY_INCREASE * chargePercent);
        matterDrainPerSec = MathHelper.ceiling_float_int(dimValue * 20) + (int)(CHARGE_MATTER_INCREASE * chargePercent);

        if (machine.getMatterStorage().getMatterStored() >= matterDrainPerSec && machine.getEnergyStorage().getEnergyStored() < machine.getEnergyStorage().getMaxEnergyStored())
        {
            machine.getEnergyStorage().modifyEnergyStored(energyGenPerTick);
            machine.getMatterStorage().modifyMatterStored(matterDrainPerSec);
            machine.UpdateClientPower();
        }
    }
    //endregion

    //region Lightning
    public void manageServerLightning()
    {
        if (machine.isMainStructureBlock() && machine.isActive())
        {
            double y, dirX, dirZ;
            Color color = Reference.COLOR_MATTER.multiplyWithoutAlpha(0.5f);
            float dimValue = machine.getDimensionalValue();

            if (getWorld().getWorldTime() % 10 == 0)
            {
                if (random.nextFloat() < (0.2f * dimValue))
                {
                    List<Entity> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos(), getPos()).expand(4, 4, 4));
                    for (Entity entity : entities)
                    {
                        boolean hasFullIron = false;
                        if (entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityPlayer = ((EntityPlayer) entity);
                            if (entityPlayer.capabilities.disableDamage)
                                continue;
                        }
                        if (entity instanceof EntityLivingBase)
                        {
                            hasFullIron = true;
                            for (int i = 0;i < 4;i++)
                            {
                                ItemStack eqStack = ((EntityLivingBase)entity).getCurrentArmor(i);
                                if (!isIronMaterial(eqStack))
                                {
                                    hasFullIron = false;
                                }
                            }
                        }

                        y = getPos().getY() + random.nextDouble() * 2;
                        dirX = random.nextGaussian() * 0.2;
                        dirZ = random.nextGaussian() * 0.2;
                        Vec3 start = new Vec3(getPos().getX() + dirX, y, getPos().getZ() + dirZ);
                        Vec3 destination = entity.getPositionEyes(1);
                        spawnSpark(color,start,destination);
                        if (!hasFullIron)
                        {
                            DamageSource damageSource = new DamageSource("pylon_lightning");
                            entity.attackEntityFrom(damageSource, dimValue * 2);
                        }

                        if (entity instanceof EntityCreeper)
                        {
                            entity.getDataWatcher().updateObject(17, Byte.valueOf((byte)1));
                        }else if (entity instanceof EntityPig)
                        {
                            entity.onStruckByLightning(null);
                        }
                    }
                }
            }

            BlockPos destPos = machine.mainBlock.add((int) (random.nextGaussian() * 7), (int) (random.nextGaussian() * 6), (int) (random.nextGaussian() * 7));
            IBlockState state = getWorld().getBlockState(destPos);
            if (state.getBlock() == MatterOverdriveBlocks.pylon)
            {
                TileEntity tileEntity = getWorld().getTileEntity(destPos);
                if (tileEntity instanceof TileEntityMachineDimensionalPylon)
                {
                    TileEntityMachineDimensionalPylon dimensionalPylon = (TileEntityMachineDimensionalPylon) tileEntity;
                    if (dimensionalPylon.mainBlock != null && !dimensionalPylon.mainBlock.equals(machine.mainBlock))
                    {
                        float otherDimValue = dimensionalPylon.getDimensionalValue();
                        if (random.nextFloat() < dimValue + otherDimValue)
                        {
                            y = getPos().getY() + random.nextDouble() * 2;
                            dirX = random.nextGaussian() * 0.2;
                            dirZ = random.nextGaussian() * 0.2;
                            Vec3 start = new Vec3(getPos().getX() + dirX, y, getPos().getZ() + dirZ);
                            Vec3 destination = new Vec3(((TileEntityMachineDimensionalPylon) tileEntity).mainBlock).addVector(0, 1, 0);
                            spawnSpark(color,start,destination);
                            machine.removeCharge(MathHelper.ceiling_float_int(CHARGE_DECREASE_ON_HIT * dimValue));
                            dimensionalPylon.addCharge(MathHelper.ceiling_float_int(CHARGE_INCREASE_RATE * dimValue));
                        }
                    }
                }
            }else if (state.getBlock().getMaterial().equals(Material.iron) || state.getBlock().getMaterial().equals(Material.iron))
            {
                y = getPos().getY() + random.nextDouble() * 2;
                dirX = random.nextGaussian() * 0.2;
                dirZ = random.nextGaussian() * 0.2;
                Vec3 start = new Vec3(getPos().getX() + dirX, y, getPos().getZ() + dirZ);
                Vec3 destination = new Vec3(destPos).addVector(0.5, 0.5, 0.5);
                spawnSpark(color,start,destination);
            }
        }
    }

    private void spawnSpark(Color color,Vec3 from,Vec3 to)
    {
        Lightning lightning = new Lightning(getWorld(), from, to, 1, 1f);
        lightning.setColorRGBA(color);
        MatterOverdrive.packetPipeline.sendToAllAround(new PacketSpawnParticle("lightning", new double[]{from.xCoord, from.yCoord, from.zCoord, to.xCoord, to.yCoord, to.zCoord}, 1, RenderParticlesHandler.Blending.LinesAdditive, 0), machine, 64);
        getWorld().playSoundEffect(to.xCoord, to.yCoord, to.zCoord, Reference.MOD_ID + ":" + "electric_arc", 0.8f + random.nextFloat() * 0.2f, 0.8f + random.nextFloat() * 0.4f);
    }

    private boolean isIronMaterial(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() instanceof ItemArmor)
        {
            String materialName = ((ItemArmor) itemStack.getItem()).getArmorMaterial().getName();
            return materialName.matches("(?i)(iron|chainmail|tritanium)");
        }
        return false;
    }
    //endregion

    //region Charge
    public void manageCharge()
    {
        if (machine.charge > 0)
        {
            if (getWorld().getWorldTime() % 40 == 0)
            {
                machine.charge-=1+Math.round(machine.charge*0.005f);
                machine.forceSync();
            }
        }
    }
    //endregion

    //region Getters and Setters
    public int getEnergyGenPerTick()
    {
        return energyGenPerTick;
    }

    public int getMatterDrainPerSec()
    {
        return matterDrainPerSec;
    }
    //endregion

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return false;
    }

    @Override
    public void onMachineEvent(MachineEvent event)
    {

    }
}
