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

package matteroverdrive.items;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.Quest;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.gui.GuiQuestPreview;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 11/22/2015.
 */
public class Contract extends MOBaseItem
{
    public Contract(String name)
    {
        super(name);
    }

    public QuestStack getQuest(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            return QuestStack.loadFromNBT(itemStack.getTagCompound());
        }
        return null;
    }

    @Override
    public boolean hasDetails(ItemStack stack){return true;}

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        QuestStack questStack = QuestStack.loadFromNBT(itemstack.getTagCompound());
        if (questStack != null) {
            for (int i = 0; i < questStack.getObjectivesCount(player); i++) {
                infos.add(MatterOverdrive.questFactory.getFormattedQuestObjective(player, questStack, i));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            QuestStack questStack = QuestStack.loadFromNBT(itemStack.getTagCompound());
            return questStack.getTitle();
        }
        return super.getItemStackDisplayName(itemStack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (worldIn.isRemote)
        {
            openGui(itemStackIn);
            return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
        }else
        {
            QuestStack questStack = getQuest(itemStackIn);
            if (questStack == null)
            {
                Quest quest = ((WeightedRandomQuest) WeightedRandom.getRandomItem(itemRand, MatterOverdriveQuests.contractGeneration)).getQuest();
                questStack = MatterOverdrive.questFactory.generateQuestStack(itemRand,quest);
                NBTTagCompound questTag = new NBTTagCompound();
                questStack.writeToNBT(questTag);
                itemStackIn.setTagCompound(questTag);
                return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS,itemStackIn);
    }

    @SideOnly(Side.CLIENT)
    private void openGui(ItemStack stack)
    {
        QuestStack questStack = getQuest(stack);
        if (questStack != null)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiQuestPreview(getQuest(stack)));
        }
    }
}
