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

package matteroverdrive.blocks;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlock;
import net.minecraft.block.material.Material;

/**
 * Created by Simeon on 10/6/2015.
 * @since 0.4.0
 */
public class BlockDecorative extends MOBlock {

    public BlockDecorative(Material material, String name,String iconName,float hardness,int harvestLevel,float resistance) {
        super(material, name);
        setHardness(hardness);
        setHarvestLevel("pickaxe",harvestLevel);
        setResistance(resistance);
        this.setBlockTextureName(Reference.MOD_ID + ":" + iconName);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative);
    }
}
