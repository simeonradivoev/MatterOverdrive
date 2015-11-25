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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Simeon on 8/28/2015.
 */
public class DataPad extends MOBaseItem
{
    public DataPad(String name)
    {
        super(name);
        setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (world.isRemote)
        {
            openGui(itemstack);
        }
        return itemstack;
    }

    @Override
    public boolean hasDetails(ItemStack stack)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(ItemStack stack)
    {
        try {
            Minecraft.getMinecraft().displayGuiScreen(new GuiDataPad(stack));
        }
        catch (Exception e)
        {
            MatterOverdrive.log.error("There was a problem while trying to open the Data Pad Gui",e);
        }

    }

    //region Setters
    public void setOrdering(ItemStack stack,int order)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("Ordering", order);
    }

    public void setOpenGuide(ItemStack stack,int guideID)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("guideID",guideID);
    }

    public void setOpenPage(ItemStack stack,int page)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("page",page);
    }
    public void setCategory(ItemStack stack,String category)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setString("Category",category);
    }
    public void setSelectedActiveQuest(ItemStack itemStack,int quest)
    {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setShort("SelectedActiveQuest",(short) quest);
    }
    //endregion

    //region Getters
    public int getGuideID(ItemStack stack)
    {
        TagCompountCheck(stack);
        if (hasOpenGuide(stack))
        {
            return stack.getTagCompound().getInteger("guideID");
        }
        return -1;
    }
    public int getPage(ItemStack stack)
    {
        TagCompountCheck(stack);
        return stack.getTagCompound().getInteger("page");
    }
    public boolean hasOpenGuide(ItemStack stack)
    {
        TagCompountCheck(stack);
        return stack.getTagCompound().hasKey("guideID", Constants.NBT.TAG_INT);
    }
    public int getOrdering(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Ordering", Constants.NBT.TAG_STRING))
        {
            return stack.getTagCompound().getInteger("Ordering");
        }
        return 2;
    }
    public String getCategory(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return stack.getTagCompound().getString("Category");
        }
        return "";
    }
    public int getActiveSelectedQuest(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return stack.getTagCompound().getShort("SelectedActiveQuest");
        }
        return 0;
    }
    //endregion
}
