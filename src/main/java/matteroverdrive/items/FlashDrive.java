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

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 8/5/2015.
 */
public class FlashDrive extends MOBaseItem
{
    protected final Color color;

    public FlashDrive(String name,Color color) {
        super(name);
        this.color = color;
        setMaxStackSize(1);
    }

  /*  @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.itemIcon = p_94581_1_.registerIcon(Reference.MOD_ID + ":" + "flash_drive");
        overlay = p_94581_1_.registerIcon(Reference.MOD_ID + ":" + "flash_drive_overlay");
    }*/

    public boolean hasDetails(ItemStack stack){return true;}
/*
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1)
        {
            return overlay;
        }else
        {
            return itemIcon;
        }
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        if (pass == 1)
        {
            return color.getColor();
        }
        return super.getColorFromItemStack(itemStack,pass);
    }
}
