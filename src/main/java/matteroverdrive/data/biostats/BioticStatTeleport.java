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

package matteroverdrive.data.biostats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.server.PacketTeleportPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatTeleport extends AbstractBioticStat {

    public static final String EFFECT_KEY_LAST_TELEPORT = "LastTeleport";
    public static final int TELEPORT_DELAY = 40;
    public static final int ENERGY_PER_TELEPORT = 4096;
    @SideOnly(Side.CLIENT)
    private boolean hasPressedKey;
    public BioticStatTeleport(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
        setShowOnWheel(true);
    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level),Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode()),EnumChatFormatting.YELLOW.toString() + ENERGY_PER_TELEPORT + " RF" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {
        if (keycode == ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode())
        {
            if (!down && hasPressedKey) {
                Vec3 pos = getPos(androidPlayer);
                if (pos != null) {
                    MatterOverdrive.packetPipeline.sendToServer(new PacketTeleportPlayer(pos.xCoord, pos.yCoord, pos.zCoord));
                    hasPressedKey = false;
                }
            }else
            {
                hasPressedKey = true;
            }
        }
    }

    public Vec3 getPos(AndroidPlayer androidPlayer) {
        MovingObjectPosition position = MOPhysicsHelper.rayTraceForBlocks(androidPlayer.getPlayer(), androidPlayer.getPlayer().worldObj, 32, 0, Vec3.createVectorHelper(0, 0, 0), true, true);
        if (position != null && position.typeOfHit != MovingObjectPosition.MovingObjectType.MISS)
        {
            ForgeDirection side = ForgeDirection.getOrientation(position.sideHit);
            Vec3 pos = getTopSafeBlock(androidPlayer.getPlayer().worldObj, position.blockX, position.blockY, position.blockZ,position.sideHit);
            if (pos != null) {
                return Vec3.createVectorHelper(pos.xCoord + 0.5, pos.yCoord, pos.zCoord + 0.5);
            }
            return null;
        }

        position = MOPhysicsHelper.rayTrace(androidPlayer.getPlayer(), androidPlayer.getPlayer().worldObj, 6, 0, Vec3.createVectorHelper(0, 0, 0),true,true);
        if (position != null)
        {
            return position.hitVec;
        }
        return null;
    }

    public Vec3 getTopSafeBlock(World world,int x,int y,int z,int side)
    {
        int airBlockCount = 0;
        int height = Math.min(y + 8, world.getActualHeight());
        for (int i = y;i < height;i++)
        {
            if (!world.getBlock(x,i,z).isBlockSolid(world,x,i,z,world.getBlockMetadata(x,i,z)) || world.getBlock(x,i,z) instanceof IFluidBlock)
            {
                airBlockCount++;
            }else
            {
                airBlockCount = 0;
            }

            if (airBlockCount >= 2)
            {
                return Vec3.createVectorHelper(x,i-1,z);
            }
        }

        ForgeDirection direction = ForgeDirection.getOrientation(side);
        x += direction.offsetX;
        y += direction.offsetY;
        z += direction.offsetZ;

        if (!world.getBlock(x,y+1,z).isBlockSolid(world,x,y,z,world.getBlockMetadata(x,y+1,z)) && !world.getBlock(x,y+2,z).isBlockSolid(world,x,y,z,world.getBlockMetadata(x,y+2,z)))
        {
            return Vec3.createVectorHelper(x,y,z);
        }

        return null;
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled)
    {
        if (androidPlayer.getPlayer().worldObj.isRemote) {
            if (!isEnabled(androidPlayer, level)) {
                hasPressedKey = false;
            }
        }
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level)
    {
        return super.isEnabled(android,level) && android.getEffectLong(EFFECT_KEY_LAST_TELEPORT) <= android.getPlayer().worldObj.getTotalWorldTime() && android.extractEnergy(ENERGY_PER_TELEPORT,true) == ENERGY_PER_TELEPORT && android.getActiveStat() != null && android.getActiveStat().equals(this);
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean getHasPressedKey()
    {
        return hasPressedKey;
    }

    @Override
    public boolean showOnHud(AndroidPlayer android,int level)
    {
        return android.getActiveStat() != null && android.getActiveStat().equals(this);
    }
}
