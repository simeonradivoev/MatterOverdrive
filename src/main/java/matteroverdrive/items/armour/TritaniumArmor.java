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

package matteroverdrive.items.armour;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.client.model.ModelTritaniumArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 11/1/2015.
 */
public class TritaniumArmor extends ItemArmor
{
    public static final ModelTritaniumArmor armorModel = new ModelTritaniumArmor(0);
    public static final ModelTritaniumArmor armorModelFeet = new ModelTritaniumArmor(0.5f);

    public TritaniumArmor(ArmorMaterial armorMaterial, int renderIndex, int renderType) {
        super(armorMaterial, renderIndex, renderType);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return String.format(Reference.PATH_ARMOR + "Tritanium_Armor2_layer_%d.png",slot == 3 ? 2 : 1);
        //return String.format(Reference.PATH_ARMOR + "tritanium_layer_%d%s.png",(slot == 2 ? 2 : 1),type == null ? "" : String.format("_%s", type));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        ModelTritaniumArmor armorModel = armorSlot == 3 ? TritaniumArmor.armorModelFeet : TritaniumArmor.armorModel;
        armorModel.bipedHead.showModel = armorSlot == 0;
        armorModel.bipedHeadwear.showModel = armorSlot == 0;
        armorModel.bipedBody.showModel = armorSlot == 1;
        armorModel.bipedRightArm.showModel = armorSlot == 1;
        armorModel.bipedLeftArm.showModel = armorSlot == 1;
        armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.FootLeft.showModel = armorModel.FootRight.showModel = armorSlot == 3;

        Render render = RenderManager.instance.getEntityRenderObject(entityLiving);

        if (render instanceof RenderPlayer)
        {
            RenderPlayer renderPlayer = (RenderPlayer)render;
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.heldItemRight = renderPlayer.modelArmor.heldItemRight;
            armorModel.heldItemLeft = renderPlayer.modelArmor.heldItemLeft;
            armorModel.aimedBow = renderPlayer.modelArmor.aimedBow;
            ItemStack heldItem = entityLiving.getHeldItem();
        }

        if (entityLiving instanceof EntityPlayer)
        {

        }
        return armorModel;
    }
}
