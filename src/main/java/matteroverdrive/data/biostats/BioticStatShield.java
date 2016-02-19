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

package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.Reference;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.entity.android_player.AndroidAttributes;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Simeon on 6/9/2015.
 */
public class BioticStatShield extends AbstractBioticStat implements IConfigSubscriber
{
    private static int ENERGY_PER_TICK = 64;
    private static int ENERGY_PER_DAMAGE = 256;
    private static final int SHIELD_COOLDOWN = 20*16;
    private static final int SHIELD_TIME = 20*8;
    @SideOnly(Side.CLIENT)
    private MOPositionedSound shieldSound;
    private final AttributeModifier modifyer;
    private final Random random;
    public BioticStatShield(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
        modifyer = new AttributeModifier(UUID.fromString("ead117ad-105a-43fe-ab22-a31aee6adc42"),"Shield Slowdown",-0.4,2);
        random = new Random();
        setShowOnWheel(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (!android.getPlayer().worldObj.isRemote)
        {
            if (android.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD))
            {
                android.extractEnergyScaled(ENERGY_PER_TICK);
            }

            /*if (android.hasEffect(AndroidPlayer.NBT_HITS)) {
                NBTTagList attackList = android.getEffectTagList(AndroidPlayer.NBT_HITS, Constants.NBT.TAG_COMPOUND);

                if (attackList.tagCount() > 0)
                {
                    if (attackList.getCompoundTagAt(0).getInteger("t") > 0)
                    {
                        attackList.getCompoundTagAt(0).setInteger("t",attackList.getCompoundTagAt(0).getInteger("t") - 1);
                    }
                    else
                    {
                        attackList.removeTag(0);
                    }
                } else {
                    android.removeEffect(AndroidPlayer.NBT_HITS);
                }

                android.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
            }*/
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server)
    {
        if (this.equals(androidPlayer.getActiveStat()) && canActivate(androidPlayer) && !MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this,level, androidPlayer)))
        {
            setShield(androidPlayer,true);
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    void setShield(AndroidPlayer androidPlayer, boolean on)
    {
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD,on);
        setLastShieldTime(androidPlayer,androidPlayer.getPlayer().worldObj.getTotalWorldTime() + SHIELD_COOLDOWN + SHIELD_TIME);
        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
        androidPlayer.getPlayer().worldObj.playSoundAtEntity(androidPlayer.getPlayer(),Reference.MOD_ID + ":" + "shield_power_up",0.6f + random.nextFloat()*0.2f,1);
        //androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).removeModifier(modifyer);
        //androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).applyModifier(modifyer);
    }

    public String getDetails(int level)
    {
        String key = "Unknown";
        try
        {
            key = Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode());
        }catch (Exception ignored)
        {

        }
        return String.format(super.getDetails(level), key);
    }

    public boolean getShieldState(AndroidPlayer androidPlayer)
    {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD);
    }

    private long getLastShieldTime(AndroidPlayer androidPlayer)
    {
        return androidPlayer.getAndroidEffects().getEffectLong(AndroidPlayer.EFFECT_SHIELD_LAST_USE);
    }

    private void setLastShieldTime(AndroidPlayer androidPlayer, long time)
    {
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD_LAST_USE,time);
    }

    boolean canActivate(AndroidPlayer androidPlayer)
    {
        return getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().worldObj.getTotalWorldTime() <= 0;
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

                        //NBTTagCompound attack = new NBTTagCompound();
                        //NBTTagList attackList = androidPlayer.getEffectTagList(AndroidPlayer.NBT_HITS, 10);
                        //attack.setDouble("x", source.getSourceOfDamage().posX - event.entityLiving.posX);
                        //attack.setDouble("y", source.getSourceOfDamage().posY - (event.entityLiving.posY + 1.5));
                        //attack.setDouble("z", source.getSourceOfDamage().posZ - event.entityLiving.posZ);
                        //attack.setInteger("time", 10);
                        //attackList.appendTag(attack);
                        //androidPlayer.setEffectsTag(AndroidPlayer.NBT_HITS, attackList);
                        //androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
                        androidPlayer.getPlayer().worldObj.playSoundAtEntity(androidPlayer.getPlayer(),Reference.MOD_ID + ":" + "shield_hit",0.5f,0.9f + random.nextFloat() * 0.2f);
                    }

                    if (androidPlayer.hasEnoughEnergyScaled(energyReqired))
                    {
                        androidPlayer.extractEnergyScaled(energyReqired);
                        event.setCanceled(true);
                    }
                }
            }
        }else if (event instanceof LivingHurtEvent)
        {
            DamageSource source = ((LivingHurtEvent) event).source;
            if (getShieldState(androidPlayer)) {
                int energyReqired = MathHelper.ceiling_float_int(((LivingHurtEvent) event).ammount * ENERGY_PER_DAMAGE);

                if (isDamageValid(source)) {
                    double energyMultiply = androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(AndroidAttributes.attributeBatteryUse).getAttributeValue();
                    energyReqired *= energyMultiply;
                    int energyExtracted = androidPlayer.extractEnergy(energyReqired, true);
                    ((LivingHurtEvent) event).ammount *= (float) energyExtracted / (float) energyReqired;
                }
            }
        }
    }

    boolean isDamageValid(DamageSource damageSource)
    {
        return damageSource.isExplosion() || damageSource.isProjectile();
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        if (androidPlayer.getPlayer().worldObj.isRemote)
        {
            if (!androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD))
            {
                stopShieldSound();
            } else
            {
                playShieldSound();
            }
        }else
        {
            long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().worldObj.getTotalWorldTime();
            if (shieldTime < SHIELD_COOLDOWN && androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD))
            {
                androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD,false);
                //androidPlayer.removeEffect(AndroidPlayer.NBT_HITS);
                //androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
                androidPlayer.getPlayer().worldObj.playSoundAtEntity(androidPlayer.getPlayer(),Reference.MOD_ID + ":" + "shield_power_down",0.6f + random.nextFloat() * 0.2f,1);
                //androidPlayer.init(androidPlayer.getPlayer(),androidPlayer.getPlayer().worldObj);
            }
        }
    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level)
    {
        //Multimap multimap = HashMultimap.create();
        //multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),modifyer);
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void playShieldSound()
    {
        if(shieldSound == null && !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(shieldSound))
        {
            shieldSound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "shield_loop"),0.3f + random.nextFloat() * 0.2f,1);
            shieldSound.setRepeat(true);
            Minecraft.getMinecraft().getSoundHandler().playSound(shieldSound);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopShieldSound()
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
        long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().worldObj.getTotalWorldTime();
        return super.isEnabled(androidPlayer,level) && androidPlayer.hasEnoughEnergyScaled(ENERGY_PER_TICK) && (shieldTime <= 0 || shieldTime > SHIELD_COOLDOWN);
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return getShieldState(androidPlayer);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android,int level)
    {
        return this.equals(android.getActiveStat()) || getShieldState(android);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().worldObj.getTotalWorldTime();
        if (shieldTime > 0)
        {
            return (int)shieldTime;
        }
        return 0;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        ENERGY_PER_DAMAGE = config.getInt("shield_energy_per_damage",ConfigurationHandler.CATEGORY_ABILITIES,256,"The energy cost of each hit to the shield");
        ENERGY_PER_TICK = config.getInt("shield_energy_per_tick",ConfigurationHandler.CATEGORY_ABILITIES,64,"The energy cost of the shield per tick");
    }
}
