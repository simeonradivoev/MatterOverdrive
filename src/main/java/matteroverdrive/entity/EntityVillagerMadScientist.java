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
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.client.render.conversation.DialogShot;
import matteroverdrive.dialog.DialogMessage;
import matteroverdrive.dialog.DialogMessageAndroidTransformation;
import matteroverdrive.dialog.DialogMessageRandom;
import matteroverdrive.dialog.DialogMessageTrade;
import matteroverdrive.entity.tasks.EntityAITalkToPlayer;
import matteroverdrive.entity.tasks.EntityAIWatchDialogPlayer;
import matteroverdrive.init.MatterOverdriveDialogs;
import matteroverdrive.network.packet.server.PacketManageConversation;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityVillagerMadScientist extends EntityVillager implements IDialogNpc
{
    private static DialogMessageRandom mainHumanMessage;
    private static DialogMessageRandom mainAndroidMessage;
    private EntityPlayer dialogPlayer;

    public EntityVillagerMadScientist(World world)
    {
        super(world, 666);
        this.tasks.addTask(1,new EntityAITalkToPlayer(this));
        this.tasks.addTask(1, new EntityAIWatchDialogPlayer(this));
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return data;
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
        DialogMessageAndroidTransformation convertMe = new DialogMessageAndroidTransformation("");
        convertMe.loadQuestionFromLocalization("dialog.mad_scientist.convert.question");

        DialogMessage canYouConvert = new DialogMessage("");
        canYouConvert.loadMessageFromLocalization("dialog.mad_scientist.requirements.line");
        canYouConvert.loadQuestionFromLocalization("dialog.mad_scientist.requirements.question");
        canYouConvert.addOption(convertMe);
        canYouConvert.addOption(MatterOverdriveDialogs.backMessage);

        mainHumanMessage = new DialogMessageRandom("");
        mainHumanMessage.loadMessageFromLocalization("dialog.mad_scientist.main.line.human");
        mainHumanMessage.addOption(canYouConvert);
        mainHumanMessage.addOption(MatterOverdriveDialogs.quitMessage);
        //endregion

        //region Android
        DialogMessageTrade trade = new DialogMessageTrade("");
        trade.loadQuestionFromLocalization("dialog.generic.trade.questions");

        DialogMessage undo = new DialogMessage("");
        undo.loadMessageFromLocalization("dialog.mad_scientist.undo.line");
        undo.loadQuestionFromLocalization("dialog.mad_scientist.undo.question");
        undo.addOption(trade);
        undo.addOption(MatterOverdriveDialogs.backMessage);

        DialogMessage whatDidYouDo = new DialogMessage("");
        whatDidYouDo.loadMessageFromLocalization("dialog.mad_scientist.whatDidYouDo.line");
        whatDidYouDo.loadQuestionFromLocalization("dialog.mad_scientist.whatDidYouDo.question");
        whatDidYouDo.addOption(undo);
        whatDidYouDo.addOption(MatterOverdriveDialogs.backMessage);

        mainAndroidMessage = new DialogMessageRandom("");
        mainAndroidMessage.loadMessageFromLocalization("dialog.mad_scientist.main.line.android");
        mainAndroidMessage.addOption(whatDidYouDo);
        mainAndroidMessage.addOption(trade);
        mainAndroidMessage.addOption(MatterOverdriveDialogs.quitMessage);
        //endregion

        whatDidYouDo.setParent(mainAndroidMessage);
        undo.setParent(whatDidYouDo);
        canYouConvert.setParent(mainHumanMessage);
        convertMe.setParent(canYouConvert);
        trade.setParent(mainAndroidMessage);

        if (side == Side.CLIENT)
        {
            trade.setHoloIcon("trade");
            canYouConvert.setShots(DialogShot.closeUp);
            undo.setShots(DialogShot.closeUp);
            whatDidYouDo.setShots(DialogShot.fromBehindLeftClose);
        }
    }

    @Override
    public IDialogMessage getStartDialogMessage(EntityPlayer player)
    {
        return AndroidPlayer.get(player).isAndroid ? mainAndroidMessage : mainHumanMessage;
    }

    @Override
    public void setDialogPlayer(EntityPlayer player) {
        dialogPlayer = player;
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
    public boolean onPlayerInteract(EntityPlayer player)
    {
        return true;
    }
    //endregion
}
