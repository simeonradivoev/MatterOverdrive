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

import cofh.lib.audio.SoundBase;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketSendAndroidAnction;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.input.Keyboard;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Simeon on 6/9/2015.
 */
public class BioticStatShield extends AbstractBioticStat implements IConfigSubscriber
{
    public static int ENERGY_PER_TICK = 64;
    public static int ENERGY_PER_DAMAGE = 256;
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
        setShowOnWheel(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (android.getPlayer().worldObj.isRemote)
        {
            if (android.getEffects().getBoolean(TAG_SHIELD))
            {
                if (!ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed())
                {
                    android.setActionToServer(PacketSendAndroidAnction.ACTION_SHIELD, false);
                    android.getEffects().setBoolean(TAG_SHIELD, false);
                    android.getEffects().removeTag(TAG_HITS);
                }
            }else
            {
                if (ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed() && !MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this,level, android)))
                {
                    setShield(android,true);
                    android.setActionToServer(PacketSendAndroidAnction.ACTION_SHIELD, true);
                }
            }

        }else
        {
            if (android.getEffects().getBoolean(TAG_SHIELD))
            {
                android.extractEnergy(ENERGY_PER_TICK, false);
            }

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
                } else {
                    android.getEffects().removeTag(TAG_HITS);
                }

                android.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, KeyBinding keyBinding)
    {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    public void setShield(AndroidPlayer androidPlayer,boolean on)
    {
        androidPlayer.getEffects().setBoolean(TAG_SHIELD,on);
        androidPlayer.sync(PacketSyncAndroid.SYNC_EFFECTS,true);
    }

    public String getDetails(int level)
    {
        String key = "Unknown";
        try
        {
            key = Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode());
        }catch (Exception e)
        {

        }
        return String.format(super.getDetails(level), key);
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
            if (androidPlayer.getPlayer().worldObj.isRemote)
            {
                stopShieldSound();

            }
        }else
        {
            if (androidPlayer.getPlayer().worldObj.isRemote)
            {
                playShieldSound();
            }

            if (androidPlayer.getPlayer().worldObj.isRemote)
            {
                if (!ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed() || !isEnabled(androidPlayer, level)) {
                    androidPlayer.setActionToServer(PacketSendAndroidAnction.ACTION_SHIELD, false);
                }
            }
        }
    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level)
    {
        //Multimap multimap = HashMultimap.create();
        //multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),modifyer);
        //return multimap;
        return null;
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
        return super.isEnabled(androidPlayer,level) && androidPlayer.extractEnergy(ENERGY_PER_TICK,true) >= ENERGY_PER_TICK && (androidPlayer.getActiveStat() != null && androidPlayer.getActiveStat().equals(this));
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return getShieldState(androidPlayer);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android,int level)
    {
        return android.getActiveStat() != null && android.getActiveStat().equals(this);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        ENERGY_PER_DAMAGE = config.getInt("shield_energy_per_damage",ConfigurationHandler.CATEGORY_ABILITIES,256,"The energy cost of each hit to the shield");
        ENERGY_PER_TICK = config.getInt("shield_energy_per_tick",ConfigurationHandler.CATEGORY_ABILITIES,64,"The energy cost of the shield per tick");
    }
}
