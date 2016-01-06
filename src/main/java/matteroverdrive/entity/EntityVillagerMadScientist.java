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

package matteroverdrive.entity;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogQuestGiver;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.client.render.conversation.DialogShot;
import matteroverdrive.dialog.*;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.tasks.EntityAITalkToPlayer;
import matteroverdrive.entity.tasks.EntityAIWatchDialogPlayer;
import matteroverdrive.init.MatterOverdriveDialogs;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.network.packet.server.PacketManageConversation;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityVillagerMadScientist extends EntityVillager implements IDialogNpc, IDialogQuestGiver
{
    private static DialogMessageQuestOnObjectivesCompleted convertMe;
    private static DialogMessage canYouConvert;
    private static DialogMessage whatDidYouDo;
    private static DialogMessage cocktailOfAscension;
    public static DialogMessage cocktailOfAscensionComplete;
    private EntityPlayer dialogPlayer;
    private IDialogMessage startMessage;

    public EntityVillagerMadScientist(World world)
    {
        super(world, 666);
        this.tasks.addTask(1,new EntityAITalkToPlayer(this));
        this.tasks.addTask(1, new EntityAIWatchDialogPlayer(this));
        setJunkie(rand.nextBoolean());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(13, new Byte((byte) 0));
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return data;
    }

    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("junkie", getJunkie());
    }

    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setJunkie(nbtTagCompound.getBoolean("junkie"));
    }

    @Override
    public EntityVillager createChild(EntityAgeable entity)
    {
        EntityVillagerMadScientist villager = new EntityVillagerMadScientist(this.worldObj);
        villager.onSpawnWithEgg(null);
        return villager;
    }

    @Override
    public boolean interact(EntityPlayer player)
    {
        if (worldObj.isRemote)
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketManageConversation(this, true));
            return true;
        }

        return false;
    }

    public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        return super.getRecipes(player);
    }

    //region Dialog Functions
    public static void registerDialogMessages(IDialogRegistry registry, Side side)
    {
        //region Human
        convertMe = new DialogMessageQuestOnObjectivesCompleted(new QuestStack(MatterOverdriveQuests.punyHumans),new int[]{0});
        registry.registerMessage(convertMe);
        convertMe.loadQuestionFromLocalization("dialog.mad_scientist.convert.question");

        canYouConvert = new DialogMessageQuestGive(new QuestStack(MatterOverdriveQuests.punyHumans));
        registry.registerMessage(canYouConvert);
        canYouConvert.loadMessageFromLocalization("dialog.mad_scientist.requirements.line");
        canYouConvert.loadQuestionFromLocalization("dialog.mad_scientist.requirements.question");
        canYouConvert.addOption(convertMe);
        canYouConvert.addOption(MatterOverdriveDialogs.backHomeMessage);
        //endregion

        //region Android
        DialogMessage undo = new DialogMessage();
        registry.registerMessage(undo);
        undo.loadMessageFromLocalization("dialog.mad_scientist.undo.line");
        undo.loadQuestionFromLocalization("dialog.mad_scientist.undo.question");
        undo.addOption(MatterOverdriveDialogs.trade);
        undo.addOption(MatterOverdriveDialogs.backHomeMessage);

        whatDidYouDo = new DialogMessageAndroidOnly();
        registry.registerMessage(whatDidYouDo);
        whatDidYouDo.loadMessageFromLocalization("dialog.mad_scientist.whatDidYouDo.line");
        whatDidYouDo.loadQuestionFromLocalization("dialog.mad_scientist.whatDidYouDo.question");
        whatDidYouDo.addOption(undo);
        whatDidYouDo.addOption(MatterOverdriveDialogs.backHomeMessage);
        //endregion

        //region Junkie
        DialogMessage acceptCocktail = new DialogMessageQuestGive(new QuestStack(MatterOverdriveQuests.cocktailOfAscension)).setReturnToMain(true).loadQuestionFromLocalization("dialog.mad_scientist.junkie.cocktail_quest.question.accept");
        registry.registerMessage(acceptCocktail);
        DialogMessage declineCocktail = new DialogMessageBackToMain().loadQuestionFromLocalization("dialog.mad_scientist.junkie.cocktail_quest.question.decline");
        registry.registerMessage(declineCocktail);
        DialogMessage[] cocktailQuest = MatterOverdrive.dialogFactory.constructMultipleLineDialog(DialogMessageQuestStart.class,"dialog.mad_scientist.junkie.cocktail_quest",8,". . . . . .");
        ((DialogMessageQuestStart)cocktailQuest[0]).setQuest(new QuestStack(MatterOverdriveQuests.cocktailOfAscension));
        cocktailOfAscension = cocktailQuest[0];
        DialogMessage lastLine = cocktailQuest[cocktailQuest.length-1];
        lastLine.addOption(acceptCocktail);
        lastLine.addOption(declineCocktail);

        cocktailOfAscensionComplete = new DialogMessageQuestOnObjectivesCompleted(new QuestStack(MatterOverdriveQuests.cocktailOfAscension),new int[]{0,1,2});
        cocktailOfAscensionComplete.loadMessageFromLocalization("dialog.mad_scientist.junkie.cocktail_quest.line");
        cocktailOfAscensionComplete.loadQuestionFromLocalization("dialog.mad_scientist.junkie.cocktail_quest.complete.question");
        DialogMessage areYouOk = new DialogMessageQuit().loadQuestionFromLocalization("dialog.mad_scientist.junkie.cocktail_quest.are_you_ok.question");
        cocktailOfAscensionComplete.addOption(areYouOk);
        //endregion

        if (side == Side.CLIENT)
        {
            canYouConvert.setShots(DialogShot.closeUp);
            undo.setShots(DialogShot.closeUp);
            whatDidYouDo.setShots(DialogShot.fromBehindLeftClose);
            for (int i = 0;i < cocktailQuest.length;i++)
            {
                MatterOverdrive.dialogFactory.addRandomShots(cocktailQuest[i]);
            }
        }
    }

    @Override
    public IDialogMessage getStartDialogMessage(EntityPlayer player)
    {
        return this.startMessage;
    }

    @Override
    public void setDialogPlayer(EntityPlayer player)
    {
        dialogPlayer = player;
        if (player != null) {
            startMessage = assembleStartingMessage(player);
        }else
        {
            startMessage = null;
        }
    }

    private IDialogMessage assembleStartingMessage(EntityPlayer player)
    {
        if (getJunkie())
        {
            DialogMessage mainJunkieMessage = new DialogMessageRandom().loadMessageFromLocalization("dialog.mad_scientist.junkie.main.line");
            MatterOverdrive.dialogFactory.addOnlyVisibleOptions(player,this,mainJunkieMessage,canYouConvert,MatterOverdriveDialogs.trade,cocktailOfAscension,cocktailOfAscensionComplete,MatterOverdriveDialogs.quitMessage);
            return mainJunkieMessage;
        }else
        {
            if (AndroidPlayer.get(player).isAndroid())
            {
                DialogMessage mainAndroidMessage = new DialogMessageRandom();
                mainAndroidMessage.loadMessageFromLocalization("dialog.mad_scientist.main.line.android");
                mainAndroidMessage.addOption(whatDidYouDo);
                mainAndroidMessage.addOption(MatterOverdriveDialogs.trade);
                mainAndroidMessage.addOption(MatterOverdriveDialogs.quitMessage);
                return mainAndroidMessage;
            }
            else
            {
                DialogMessage mainHumanMessage = new DialogMessageRandom();
                mainHumanMessage.loadMessageFromLocalization("dialog.mad_scientist.main.line.human");
                mainHumanMessage.addOption(canYouConvert);
                mainHumanMessage.addOption(MatterOverdriveDialogs.quitMessage);
                return mainHumanMessage;
            }
        }
    }

    @Override
    public EntityPlayer getDialogPlayer() {
        return dialogPlayer;
    }

    @Override
    public boolean canTalkTo(EntityPlayer player)
    {
        return AndroidPlayer.get(player) == null || !AndroidPlayer.get(player).isTurning();
    }

    @Override
    public EntityLiving getEntity() {
        return this;
    }

    @Override
    public void onPlayerInteract(EntityPlayer player,DialogMessage dialogMessage)
    {
        if (dialogMessage == cocktailOfAscensionComplete)
        {
            this.addPotionEffect(new PotionEffect(Potion.wither.id,1000,1));
            worldObj.playSoundAtEntity(this, Reference.MOD_ID + ":" + "failed_animal_die_0",1,1);
            //worldObj.createExplosion(this,posX,posY,posZ,3,false);
            this.setDead();
            EntityMutantScientist mutantScientist = new EntityMutantScientist(worldObj);
            mutantScientist.spawnExplosionParticle();
            mutantScientist.setPosition(posX,posY,posZ);
            mutantScientist.onSpawnWithEgg(null);
            worldObj.spawnEntityInWorld(mutantScientist);
        }
        else if (dialogMessage == convertMe)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(player);
            for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests())
            {
                if (questStack.getQuest() == MatterOverdriveQuests.punyHumans)
                {
                    questStack.markComplited(player,false);
                    AndroidPlayer.get(player).startConversion();
                }
            }
        }
    }

    @Override
    public void giveQuest(IDialogMessage message,QuestStack questStack,EntityPlayer entityPlayer)
    {
        if (questStack != null)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
            if (extendedProperties != null && questStack.getQuest().canBeAccepted(questStack,entityPlayer))
            {
                QuestStack newQuestStack = questStack.copy();
                newQuestStack.setGiver(this);
                extendedProperties.addQuest(newQuestStack);
            }
        }
    }

    public void setJunkie(boolean junkie)
    {
        this.dataWatcher.updateObject(13, junkie ? (byte)1 : (byte)0);
        if (junkie)
        {
            this.setCustomNameTag(MOStringHelper.translateToLocal("entity.mad_scientist.junkie.name"));
        }
    }

    public boolean getJunkie()
    {
        return this.dataWatcher.getWatchableObjectByte(13) == (byte)1;
    }
    //endregion
}
