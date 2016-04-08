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

package matteroverdrive.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Simeon on 12/22/2015.
 */
public class MOBlockHelper
{
    public static final int[][] SIDE_COORD_MOD = new int[][]{{0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}};
    public static final byte[] SIDE_LEFT = new byte[]{(byte)4, (byte)5, (byte)5, (byte)4, (byte)2, (byte)3};
    public static final byte[] SIDE_RIGHT = new byte[]{(byte)5, (byte)4, (byte)4, (byte)5, (byte)3, (byte)2};
    public static final byte[] SIDE_OPPOSITE = new byte[]{(byte)1, (byte)0, (byte)3, (byte)2, (byte)5, (byte)4};
    public static final byte[] SIDE_ABOVE = new byte[]{(byte)3, (byte)2, (byte)1, (byte)1, (byte)1, (byte)1};
    public static final byte[] SIDE_BELOW = new byte[]{(byte)2, (byte)3, (byte)0, (byte)0, (byte)0, (byte)0};

    public static EnumFacing getLeftSide(EnumFacing side) {
        return EnumFacing.VALUES[SIDE_LEFT[side.ordinal()]];
    }

    public static EnumFacing getRightSide(EnumFacing side) {
        return EnumFacing.VALUES[SIDE_RIGHT[side.ordinal()]];
    }

    public static EnumFacing getOppositeSide(EnumFacing side) {
        return EnumFacing.VALUES[SIDE_OPPOSITE[side.ordinal()]];
    }

    public static EnumFacing getAboveSide(EnumFacing side) {
        return EnumFacing.VALUES[SIDE_ABOVE[side.ordinal()]];
    }

    public static int getBelowSide(EnumFacing side) {
        return SIDE_BELOW[side.ordinal()];
    }

    public static EnumFacing determineXZPlaceFacing(EntityLivingBase placer) {
        int rotation = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        switch(rotation) {
            case 0:
                return EnumFacing.UP;
            case 1:
                return EnumFacing.EAST;
            case 2:
                return EnumFacing.SOUTH;
            case 3:
                return EnumFacing.WEST;
            default:
                return EnumFacing.SOUTH;
        }
    }

    public static final class RotationType {
        public static final int PREVENT = -1;
        public static final int FOUR_WAY = 1;
        public static final int SIX_WAY = 2;
        public static final int RAIL = 3;
        public static final int PUMPKIN = 4;
        public static final int STAIRS = 5;
        public static final int REDSTONE = 6;
        public static final int LOG = 7;
        public static final int SLAB = 8;
        public static final int CHEST = 9;
        public static final int LEVER = 10;
        public static final int SIGN = 11;

        public RotationType() {
        }
    }
}
