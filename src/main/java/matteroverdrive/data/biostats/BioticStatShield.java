package matteroverdrive.data.biostats;

import cofh.lib.audio.SoundBase;
import matteroverdrive.Reference;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import matteroverdrive.proxy.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.input.Keyboard;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Simeon on 6/9/2015.
 */
public class BioticStatShield extends AbstractBioticStat
{
    public static final int ENERGY_PER_TICK = 32;
    public static final int ENERGY_PER_DAMAGE = 128;
    public static final String TAG_SHIELD = "Shield";
    public static final String TAG_HITS = "Hits";
    @SideOnly(Side.CLIENT)
    private SoundBase shieldSound;
    private AttributeModifier modifyer;
    private Random random;
    public BioticStatShield(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
        modifyer = new AttributeModifier(UUID.fromString("ead117ad-105a-43fe-ab22-a31aee6adc42"),"Shield Slowdown",-0.8,2);
        random = new Random();
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (android.getPlayer().worldObj.isRemote)
        {
            if (android.getEffects().getBoolean(TAG_SHIELD))
            {
                android.extractEnergy(ENERGY_PER_TICK,false);

                if (!ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed() || !android.getPlayer().isSneaking())
                {
                    android.setActionToServer(PacketSendAndroidAnction.ACTION_SHIELD, false, 0);
                    android.getEffects().setBoolean(TAG_SHIELD, false);
                    android.getEffects().removeTag(TAG_HITS);
                }
            }else
            {
                if (ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed() && android.getPlayer().isSneaking())
                {
                    setShield(android,true);
                    android.setActionToServer(PacketSendAndroidAnction.ACTION_SHIELD,true,0);
                }
            }

        }else
        {
            if (android.getEffects().hasKey(TAG_HITS)) {
                NBTTagList attackList = android.getEffects().getTagList(TAG_HITS, 10);

                if (attackList.tagCount() > 0)
                {
                    if (attackList.getCompoundTagAt(0).getInteger("time") > 0)
                    {
                        attackList.getCompoundTagAt(0).setInteger("time",attackList.getCompoundTagAt(0).getInteger("time") - 1);
                    }
                    else
                    {
                        attackList.removeTag(0);
                    }
                }else
                {
                    android.getEffects().removeTag(TAG_HITS);
                }

                android.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
            }
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {
        if (androidPlayer.getPlayer().isSneaking() && keycode == ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode() && down)
        {

        }
    }

    public void setShield(AndroidPlayer androidPlayer,boolean on)
    {
        androidPlayer.getEffects().setBoolean(TAG_SHIELD,on);
        androidPlayer.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
    }

    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode()));
    }

    public boolean getShieldState(AndroidPlayer androidPlayer)
    {
        return androidPlayer.getEffects().getBoolean(TAG_SHIELD);
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if (event instanceof LivingAttackEvent)
        {
            DamageSource source = ((LivingAttackEvent) event).source;
            if (getShieldState(androidPlayer))
            {
                int energyReqired = MathHelper.ceiling_float_int(((LivingAttackEvent) event).ammount * ENERGY_PER_DAMAGE);

                if (isDamageValid(source) && event.isCancelable())
                {
                    if (source.getSourceOfDamage() != null) {

                        NBTTagCompound attack = new NBTTagCompound();
                        NBTTagList attackList = androidPlayer.getEffects().getTagList(TAG_HITS, 10);
                        attack.setDouble("x", source.getSourceOfDamage().posX - event.entityLiving.posX);
                        attack.setDouble("y", source.getSourceOfDamage().posY - (event.entityLiving.posY + 1.5));
                        attack.setDouble("z", source.getSourceOfDamage().posZ - event.entityLiving.posZ);
                        attack.setInteger("time", 10);
                        attackList.appendTag(attack);
                        androidPlayer.getEffects().setTag(TAG_HITS, attackList);
                        androidPlayer.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
                        androidPlayer.getPlayer().worldObj.playSoundAtEntity(androidPlayer.getPlayer(),Reference.MOD_ID + ":" + "shield_hit_" + random.nextInt(2),0.5f,0.9f + random.nextFloat() * 0.2f);
                    }

                    int energyExtracted = androidPlayer.extractEnergy(energyReqired, true);
                    if (energyExtracted == energyReqired)
                    {
                        event.setCanceled(true);
                        androidPlayer.extractEnergy(energyReqired, false);
                    }
                }
            }
        }else if (event instanceof LivingHurtEvent)
        {
            DamageSource source = ((LivingHurtEvent) event).source;
            if (getShieldState(androidPlayer)) {
                int energyReqired = MathHelper.ceiling_float_int(((LivingHurtEvent) event).ammount * ENERGY_PER_DAMAGE);

                if (isDamageValid(source)) {
                    int energyExtracted = androidPlayer.extractEnergy(energyReqired, true);
                    ((LivingHurtEvent) event).ammount *= (float) energyExtracted / (float) energyReqired;
                }
            }
        }
        else if (event instanceof LivingEvent.LivingJumpEvent)
        {
            if (getShieldState(androidPlayer))
            {
                event.entityLiving.motionY -= 0.5;
            }
        }
    }

    public boolean isDamageValid(DamageSource damageSource)
    {
        return damageSource.isExplosion() || damageSource.isProjectile();
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        if (!androidPlayer.getEffects().getBoolean(TAG_SHIELD))
        {
            if (androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(modifyer.getID()) != null)
            {
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(modifyer);
            }

            if (androidPlayer.getPlayer().worldObj.isRemote)
            {
                stopShieldSound();

            }
        }else if (androidPlayer.getEffects().getBoolean(TAG_SHIELD))
        {
            if (androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(modifyer.getID()) == null)
            {
                androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(modifyer);
            }

            if (androidPlayer.getPlayer().worldObj.isRemote)
            {
                playShieldSound();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void playShieldSound()
    {
        if(shieldSound == null && !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(shieldSound))
        {
            shieldSound = new SoundBase(Reference.MOD_ID + ":" + "shield_loop",1f,1,true,0);
            Minecraft.getMinecraft().getSoundHandler().playSound(shieldSound);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void stopShieldSound()
    {
        if(shieldSound != null && Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(shieldSound))
        {
            Minecraft.getMinecraft().getSoundHandler().stopSound(shieldSound);
            shieldSound = null;
        }
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer,int level)
    {
        return super.isEnabled(androidPlayer,level) && androidPlayer.extractEnergy(ENERGY_PER_TICK,true) >= ENERGY_PER_TICK;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return getShieldState(androidPlayer);
    }
}
