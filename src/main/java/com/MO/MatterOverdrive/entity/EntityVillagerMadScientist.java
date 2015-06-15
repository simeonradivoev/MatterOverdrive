package com.MO.MatterOverdrive.entity;

import cofh.lib.audio.SoundBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.util.MOStringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityVillagerMadScientist extends EntityVillager
{

    public EntityVillagerMadScientist(World p_i1748_1_)
    {
        super(p_i1748_1_, 666);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return p_110161_1_;
    }

    @Override
    public EntityVillager createChild(EntityAgeable p_90011_1_)
    {
        EntityVillagerMadScientist entityvillager = new EntityVillagerMadScientist(this.worldObj);
        entityvillager.onSpawnWithEgg(null);
        return entityvillager;
    }

    @Override
    public boolean interact(EntityPlayer player)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(player);
        if (androidPlayer != null && androidPlayer.isAndroid())
        {
            return super.interact(player);
        }
        else if (!androidPlayer.isTurning())
        {
            if (player.worldObj.isRemote)
            {
                IChatComponent chatComponent = new ChatComponentText(EnumChatFormatting.GOLD + "<Mad Scientist>" + EnumChatFormatting.RESET + MOStringHelper.translateToLocal("entity.mad_scientist.line." + rand.nextInt(3)));
                player.addChatMessage(chatComponent);
            }

            tryConvertToAndroid(player);
        }
        return false;
    }

    void tryConvertToAndroid(EntityPlayer player)
    {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory();i++)
        {
            if(player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdriveItems.androidParts)
            {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length)
                {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (int i = 0;i < hasParts.length;i++)
        {
            if (!hasParts[i])
            {
                if (!player.worldObj.isRemote) {
                    ChatComponentText componentText = new ChatComponentText(EnumChatFormatting.GOLD + "<Mad Scientist>" + EnumChatFormatting.RED + MOStringHelper.translateToLocal("entity.mad_scientist.line.fail." + rand.nextInt(4)));
                    componentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    player.addChatMessage(componentText);
                }
                return;
            }
        }

        if (!player.worldObj.isRemote) {
            for (int i = 0; i < slots.length; i++)
            {
                player.inventory.decrStackSize(slots[i],1);
            }
        }

        convertToAndroid(player);
    }

    void convertToAndroid(EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            ChatComponentText componentText = new ChatComponentText(EnumChatFormatting.GOLD + "<Mad Scientist>" + EnumChatFormatting.GREEN + MOStringHelper.translateToLocal("entity.mad_scientist.line.success." + rand.nextInt(4)));
            componentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
            player.addChatMessage(componentText);
            playTransformMusic();
        }
        else
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            androidPlayer.startTurningToAndroid();
        }
    }

    @SideOnly(Side.CLIENT)
    public void playTransformMusic()
    {
        SoundBase transform_music = new SoundBase(Reference.MOD_ID + ":" + "transformation_music", 1, 1,false,0,0,0,0, ISound.AttenuationType.NONE);

        if (!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(transform_music))
        {
            Minecraft.getMinecraft().getSoundHandler().playSound(transform_music);
        }
    }
}
