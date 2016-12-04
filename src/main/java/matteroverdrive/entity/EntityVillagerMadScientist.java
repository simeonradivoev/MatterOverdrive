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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogQuestGiver;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.events.MOEventDialogConstruct;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.client.render.conversation.DialogShot;
import matteroverdrive.data.dialog.*;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.tasks.EntityAITalkToPlayer;
import matteroverdrive.entity.tasks.EntityAIWatchDialogPlayer;
import matteroverdrive.init.MatterOverdriveDialogs;
import matteroverdrive.init.MatterOverdriveEntities;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.network.packet.server.PacketManageConversation;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityVillagerMadScientist extends EntityVillager implements IDialogNpc, IDialogQuestGiver
{
	private static final DataParameter<Boolean> VARIANT = EntityDataManager.createKey(EntityVillagerMadScientist.class, DataSerializers.BOOLEAN);
	public static DialogMessage cocktailOfAscensionComplete;
	private static DialogMessage convertMe;
	private static DialogMessage canYouConvert;
	private static DialogMessage whatDidYouDo;
	private static DialogMessage cocktailOfAscension;
	private EntityPlayer dialogPlayer;
	private IDialogMessage startMessage;

	public EntityVillagerMadScientist(World world)
	{
		super(world, VillagerRegistry.getId(MatterOverdriveEntities.MAD_SCIENTIST_PROFESSION));
		this.tasks.addTask(1, new EntityAITalkToPlayer(this));
		this.tasks.addTask(1, new EntityAIWatchDialogPlayer(this));
	}

	//region Dialog Functions
	public static void registerDialogMessages(IDialogRegistry registry, Side side)
	{
		//region Human
		convertMe = new DialogMessageQuestOnObjectivesCompleted(null, "dialog.mad_scientist.convert.question", new QuestStack(MatterOverdriveQuests.punyHumans), new int[] {0}).setUnlocalized(true);
		registry.registerMessage(convertMe);

		canYouConvert = new DialogMessageQuestGive("dialog.mad_scientist.requirements.line", "dialog.mad_scientist.requirements.question", new QuestStack(MatterOverdriveQuests.punyHumans));
		registry.registerMessage(canYouConvert);
		canYouConvert.addOption(convertMe);
		canYouConvert.addOption(MatterOverdriveDialogs.backHomeMessage);
		canYouConvert.setUnlocalized(true);
		//endregion

		//region Android
		DialogMessage undo = new DialogMessage("dialog.mad_scientist.undo.line", "dialog.mad_scientist.undo.question");
		registry.registerMessage(undo);
		undo.setUnlocalized(true);
		undo.addOption(MatterOverdriveDialogs.trade);
		undo.addOption(MatterOverdriveDialogs.backHomeMessage);

		whatDidYouDo = new DialogMessageAndroidOnly("dialog.mad_scientist.whatDidYouDo.line", "dialog.mad_scientist.whatDidYouDo.question");
		registry.registerMessage(whatDidYouDo);
		whatDidYouDo.setUnlocalized(true);
		whatDidYouDo.addOption(undo);
		whatDidYouDo.addOption(MatterOverdriveDialogs.backHomeMessage);
		//endregion

		//region Junkie
		DialogMessage acceptCocktail = new DialogMessageQuestGive(null, "dialog.mad_scientist.junkie.cocktail_quest.question.accept", new QuestStack(MatterOverdriveQuests.cocktailOfAscension)).setReturnToMain(true).setUnlocalized(true);
		registry.registerMessage(acceptCocktail);
		DialogMessage declineCocktail = new DialogMessageBackToMain(null, "dialog.mad_scientist.junkie.cocktail_quest.question.decline").setUnlocalized(true);
		registry.registerMessage(declineCocktail);
		DialogMessage[] cocktailQuest = MatterOverdrive.dialogFactory.constructMultipleLineDialog(DialogMessageQuestStart.class, "dialog.mad_scientist.junkie.cocktail_quest", 8, ". . . . . .");
		((DialogMessageQuestStart)cocktailQuest[0]).setQuest(new QuestStack(MatterOverdriveQuests.cocktailOfAscension));
		cocktailOfAscension = cocktailQuest[0];
		DialogMessage lastLine = cocktailQuest[cocktailQuest.length - 1];
		lastLine.addOption(acceptCocktail);
		lastLine.addOption(declineCocktail);

		cocktailOfAscensionComplete = new DialogMessageQuestOnObjectivesCompleted("dialog.mad_scientist.junkie.cocktail_quest.line", "dialog.mad_scientist.junkie.cocktail_quest.complete.question", new QuestStack(MatterOverdriveQuests.cocktailOfAscension), new int[] {0, 1, 2}).setUnlocalized(true);
		DialogMessage areYouOk = new DialogMessageQuit(null, "dialog.mad_scientist.junkie.cocktail_quest.are_you_ok.question").setUnlocalized(true);
		cocktailOfAscensionComplete.addOption(areYouOk);
		//endregion

		if (side == Side.CLIENT)
		{
			canYouConvert.setShots(DialogShot.closeUp);
			undo.setShots(DialogShot.closeUp);
			whatDidYouDo.setShots(DialogShot.fromBehindLeftClose);
			for (DialogMessage aCocktailQuest : cocktailQuest)
			{
				MatterOverdrive.dialogFactory.addRandomShots(aCocktailQuest);
			}
		}
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(VARIANT, false);
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
		villager.onInitialSpawn(worldObj.getDifficultyForLocation(getPosition()), null);
		return villager;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (worldObj.isRemote)
		{
			MatterOverdrive.packetPipeline.sendToServer(new PacketManageConversation(this, true));
			return true;
		}

		return false;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		super.onInitialSpawn(difficulty, livingdata);
		setJunkie(rand.nextBoolean());
		return livingdata;
	}

	@Override
	public IDialogMessage getStartDialogMessage(EntityPlayer player)
	{
		return this.startMessage;
	}

	private IDialogMessage assembleStartingMessage(EntityPlayer player)
	{
		if (getJunkie())
		{
			DialogMessage mainJunkieMessage = new DialogMessage(MOStringHelper.formatVariations("dialog.mad_scientist.junkie.main", "line", 2), null).setUnlocalized(true);
			MatterOverdrive.dialogFactory.addOnlyVisibleOptions(player, this, mainJunkieMessage, canYouConvert, MatterOverdriveDialogs.trade, cocktailOfAscension, cocktailOfAscensionComplete, MatterOverdriveDialogs.quitMessage);
			return mainJunkieMessage;
		}
		else
		{
			if (MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid())
			{
				DialogMessage mainAndroidMessage = new DialogMessage(MOStringHelper.formatVariations("dialog.mad_scientist.main.line", "android", 3), null);
				mainAndroidMessage.setUnlocalized(true);
				mainAndroidMessage.addOption(whatDidYouDo);
				mainAndroidMessage.addOption(MatterOverdriveDialogs.trade);
				mainAndroidMessage.addOption(MatterOverdriveDialogs.quitMessage);
				return mainAndroidMessage;
			}
			else
			{
				DialogMessage mainHumanMessage = new DialogMessage(MOStringHelper.formatVariations("dialog.mad_scientist.main.line", "human", 3), null);
				mainHumanMessage.setUnlocalized(true);
				mainHumanMessage.addOption(canYouConvert);
				mainHumanMessage.addOption(MatterOverdriveDialogs.quitMessage);
				return mainHumanMessage;
			}
		}
	}

	@Override
	public EntityPlayer getDialogPlayer()
	{
		return dialogPlayer;
	}

	@Override
	public void setDialogPlayer(EntityPlayer player)
	{
		dialogPlayer = player;
		if (player != null)
		{
			if (!MinecraftForge.EVENT_BUS.post(new MOEventDialogConstruct.Pre(this, player, startMessage)))
			{
				startMessage = assembleStartingMessage(player);
				MinecraftForge.EVENT_BUS.post(new MOEventDialogConstruct.Post(this, player, startMessage));
			}
		}
		else
		{
			startMessage = null;
		}
	}

	@Override
	public boolean canTalkTo(EntityPlayer player)
	{
		return MOPlayerCapabilityProvider.GetAndroidCapability(player) == null || !MOPlayerCapabilityProvider.GetAndroidCapability(player).isTurning();
	}

	@Override
	public EntityLiving getEntity()
	{
		return this;
	}

	@Override
	public void onPlayerInteract(EntityPlayer player, DialogMessage dialogMessage)
	{
		if (dialogMessage == cocktailOfAscensionComplete)
		{
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("wither"), 1000, 1));
			worldObj.playSound(null, this.posX, this.posY, this.posZ, MatterOverdriveSounds.failedAnimalDie, SoundCategory.NEUTRAL, 1, 1);
			//worldObj.createExplosion(this,posX,posY,posZ,3,false);
			this.setDead();
			EntityMutantScientist mutantScientist = new EntityMutantScientist(worldObj);
			mutantScientist.spawnExplosionParticle();
			mutantScientist.setPosition(posX, posY, posZ);
			mutantScientist.onInitialSpawn(worldObj.getDifficultyForLocation(getPosition()), null);
			worldObj.spawnEntityInWorld(mutantScientist);
		}
		else if (dialogMessage == convertMe)
		{
			MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
			for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests())
			{
				if (questStack.getQuest() == MatterOverdriveQuests.punyHumans)
				{
					questStack.markComplited(player, false);
					MOPlayerCapabilityProvider.GetAndroidCapability(player).startConversion();
				}
			}
		}
	}

	@Override
	public void giveQuest(IDialogMessage message, QuestStack questStack, EntityPlayer entityPlayer)
	{
		if (questStack != null)
		{
			MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(entityPlayer);
			if (extendedProperties != null && questStack.getQuest().canBeAccepted(questStack, entityPlayer))
			{
				QuestStack newQuestStack = questStack.copy();
				newQuestStack.setGiver(this);
				extendedProperties.addQuest(newQuestStack);
			}
		}
	}

	public boolean getJunkie()
	{
		return this.dataManager.get(VARIANT);
	}

	public void setJunkie(boolean junkie)
	{
		this.dataManager.set(VARIANT, junkie);
		if (junkie)
		{
			this.setCustomNameTag(MOStringHelper.translateToLocal("entity.mo.mad_scientist.junkie.name"));
		}
	}
	//endregion
}
