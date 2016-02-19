package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.client.PacketSpawnParticle;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Simeon on 1/1/2016.
 */
public class BioticStatShockwave extends AbstractBioticStat
{
    private static final int DELAY = 20*12;
    private static final int ENERGY = 512;

    public BioticStatShockwave(String name, int xp)
    {
        super(name, xp);
        this.showOnWheel = true;
    }

    @Override
    public String getDetails(int level)
    {
        String keyName = EnumChatFormatting.AQUA + GameSettings.getKeyDisplayString(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode()) + EnumChatFormatting.GRAY;
        return String.format(super.getDetails(level), EnumChatFormatting.YELLOW + Integer.toString(10) + EnumChatFormatting.GRAY,keyName);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (android.getActiveStat() != null && android.getActiveStat().equals(this) && !android.getPlayer().onGround && android.getPlayer().motionY < 0 && android.getPlayer().isSneaking())
        {
            Vec3 motion = new Vec3(android.getPlayer().motionX,android.getPlayer().motionY,android.getPlayer().motionZ).subtract(new Vec3(0,1,0)).normalize();
            android.getPlayer().addVelocity(motion.xCoord*0.2,motion.yCoord*0.2,motion.zCoord*0.2);
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
    {
        if (androidPlayer.getActiveStat() == this && server)
        {
            createShockwave(androidPlayer,androidPlayer.getPlayer(),5);
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if ((event instanceof LivingFallEvent || event instanceof PlayerFlyableFallEvent) && event.entityLiving.isSneaking() && androidPlayer.getActiveStat().equals(this))
        {
            if (event instanceof LivingFallEvent)
            {
                createShockwave(androidPlayer,event.entityLiving,((LivingFallEvent) event).distance);
            }else
            {
                createShockwave(androidPlayer,event.entityLiving,((PlayerFlyableFallEvent) event).distance);
            }
        }
    }

    private void createShockwave(AndroidPlayer androidPlayer,EntityLivingBase entityPlayer, float distance)
    {
        if (getLastShockwaveTime(androidPlayer) < androidPlayer.getPlayer().worldObj.getTotalWorldTime())
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this,androidPlayer.getUnlockedLevel(this),androidPlayer)))
            {
                if (!entityPlayer.worldObj.isRemote)
                {
                    float range = MathHelper.clamp_float(distance, 5, 10);
                    float power = MathHelper.clamp_float(distance, 1, 3) * 0.8f;
                    if (androidPlayer.hasEnoughEnergyScaled((int) (range * ENERGY)))
                    {
                        AxisAlignedBB area = new AxisAlignedBB(entityPlayer.posX - range, entityPlayer.posY - range, entityPlayer.posZ - range, entityPlayer.posX + range, entityPlayer.posY + range, entityPlayer.posZ + range);
                        List<EntityLivingBase> entities = entityPlayer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, area);
                        for (EntityLivingBase entityLivingBase : entities)
                        {
                            if (entityLivingBase != entityPlayer)
                            {
                                Vec3 dir = entityLivingBase.getPositionVector().subtract(entityPlayer.getPositionVector());
                                double localDistance = dir.lengthVector();
                                double distanceMultiply = range / Math.max(1, localDistance);
                                dir = dir.normalize();
                                entityLivingBase.addVelocity(dir.xCoord * power * distanceMultiply, power * 0.2f, dir.zCoord * power * distanceMultiply);
                                entityLivingBase.velocityChanged = true;
                                ShockwaveDamage damageSource = new ShockwaveDamage("android_shockwave", entityPlayer);
                                entityLivingBase.attackEntityFrom(damageSource, power * 3);
                            }
                        }
                        setLastShockwaveTime(androidPlayer, androidPlayer.getPlayer().worldObj.getTotalWorldTime() + DELAY);
                        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
                        entityPlayer.worldObj.playSoundAtEntity(entityPlayer, Reference.MOD_ID + ":" + "shockwave", 1, 0.9f + entityPlayer.getRNG().nextFloat() * 0.1f);
                        for (int i = 0; i < 20; ++i)
                        {
                            double d0 = entityPlayer.getRNG().nextGaussian() * 0.02D;
                            double d1 = entityPlayer.getRNG().nextGaussian() * 0.02D;
                            double d2 = entityPlayer.getRNG().nextGaussian() * 0.02D;
                            double d3 = 10.0D;
                            entityPlayer.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entityPlayer.posX + (double) (entityPlayer.getRNG().nextFloat() * entityPlayer.width * 2.0F) - (double) entityPlayer.width - d0 * d3, entityPlayer.posY + (double) (entityPlayer.getRNG().nextFloat() * entityPlayer.height) - d1 * d3, entityPlayer.posZ + (double) (entityPlayer.getRNG().nextFloat() * entityPlayer.width * 2.0F) - (double) entityPlayer.width - d2 * d3, d0, d1, d2);
                        }
                        androidPlayer.extractEnergyScaled((int)(range*ENERGY));
                        MatterOverdrive.packetPipeline.sendToAllAround(new PacketSpawnParticle("shockwave", androidPlayer.getPlayer().posX, androidPlayer.getPlayer().posY + androidPlayer.getPlayer().getEyeHeight() / 2, androidPlayer.getPlayer().posZ, 1, RenderParticlesHandler.Blending.Additive, 10), androidPlayer.getPlayer(), 64);
                    } else
                    {

                    }
                }
            }
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {

    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level)
    {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return false;
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level)
    {
        return android.getActiveStat().equals(this) || getDelay(android,level) > 0;
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level)
    {
        return super.isEnabled(android,level) && getDelay(android,level) <= 0 && android.hasEnoughEnergyScaled(10*ENERGY);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        long shockwaveTime = getLastShockwaveTime(androidPlayer) - androidPlayer.getPlayer().worldObj.getTotalWorldTime();
        if (shockwaveTime > 0)
        {
            return (int)shockwaveTime;
        }
        return 0;
    }

    public class ShockwaveDamage extends DamageSource
    {
        private final EntityLivingBase source;
        public ShockwaveDamage(String p_i1566_1_,EntityLivingBase source)
        {
            super(p_i1566_1_);
            this.source = source;
            setExplosion();
            setDamageBypassesArmor();
        }

        @Override
        public Entity getEntity()
        {
            return source;
        }
    }

    private long getLastShockwaveTime(AndroidPlayer androidPlayer)
    {
        return androidPlayer.getAndroidEffects().getEffectLong(AndroidPlayer.EFFECT_SHOCK_LAST_USE);
    }

    private void setLastShockwaveTime(AndroidPlayer androidPlayer, long time)
    {
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHOCK_LAST_USE,time);
    }
}
