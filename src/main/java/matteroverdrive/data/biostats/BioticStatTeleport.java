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
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.network.packet.server.PacketTeleportPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.HashSet;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatTeleport extends AbstractBioticStat implements IConfigSubscriber {

    public static final String EFFECT_KEY_LAST_TELEPORT = "LastTeleport";
    public static final int TELEPORT_DELAY = 40;
    public static int ENERGY_PER_TELEPORT = 4096;
    public static int MAX_TELEPORT_HEIGHT_CHECK = 8;
    public static int MAX_TELEPORT_DISTANCE = 32;
    private HashSet<String> blackListedBlocks;
    @SideOnly(Side.CLIENT)
    private boolean hasPressedKey;
    public BioticStatTeleport(String name, int xp)
    {
        super(name, xp);
        setShowOnHud(true);
        setShowOnWheel(true);
        blackListedBlocks = new HashSet<>();
    }

    @Override
    public String getDetails(int level)
    {
        String keyName = GameSettings.getKeyDisplayString(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode());
        return String.format(super.getDetails(level),keyName,EnumChatFormatting.YELLOW.toString() + ENERGY_PER_TELEPORT + " RF" + EnumChatFormatting.GRAY);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {
        if (android.getPlayer().worldObj.isRemote)
        {
            manageActivate(android);
        }
    }

    @SideOnly(Side.CLIENT)
    private void manageActivate(AndroidPlayer androidPlayer)
    {
        if (ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getIsKeyPressed() && androidPlayer.getActiveStat() != null && androidPlayer.getActiveStat().equals(this))
        {
            hasPressedKey = true;
        }
        else if (hasPressedKey)
        {
            Vec3 pos = getPos(androidPlayer);
            if (pos != null) {
                MatterOverdrive.packetPipeline.sendToServer(new PacketTeleportPlayer(pos.xCoord, pos.yCoord, pos.zCoord));
                hasPressedKey = false;
            }
            hasPressedKey = false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, KeyBinding keyBinding)
    {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    public Vec3 getPos(AndroidPlayer androidPlayer) {
        MovingObjectPosition position = MOPhysicsHelper.rayTraceForBlocks(androidPlayer.getPlayer(), androidPlayer.getPlayer().worldObj, MAX_TELEPORT_DISTANCE, 0, Vec3.createVectorHelper(0, 0, 0), true, true);
        if (position != null && position.typeOfHit != MovingObjectPosition.MovingObjectType.MISS)
        {
            Vec3 pos = getTopSafeBlock(androidPlayer.getPlayer().worldObj, position.blockX, position.blockY, position.blockZ, position.sideHit);
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
        int heightCheck = MAX_TELEPORT_HEIGHT_CHECK;
        if (side == 1)
        {
            heightCheck = 3;
        }
        int height = Math.min(y + heightCheck, world.getActualHeight());
        Block block;
        for (int i = y;i < height;i++)
        {
            block = world.getBlock(x,i,z);
            String unlocalizedName = block.getUnlocalizedName().substring(5);
            if (blackListedBlocks.contains(unlocalizedName))
            {
                return null;
            }

            if (!block.isBlockSolid(world,x,i,z,world.getBlockMetadata(x,i,z)) || block instanceof IFluidBlock)
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

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        this.blackListedBlocks.clear();
        String[] blackListedBlocks = config.config.getStringList("teleport_blacklist",config.CATEGORY_ABILITIES,new String[]{"hellsand","barrier","bedrock"},"The Unlocalized names of the blacklist blocks that the player can't teleport to");
        for (String block : blackListedBlocks)
        {
            this.blackListedBlocks.add(block);
        }
        MAX_TELEPORT_HEIGHT_CHECK = config.getInt("teleport_max_height_check",config.CATEGORY_ABILITIES,8,"The max height amount that the teleport ability checks if there is no 2 blocks air space");
        ENERGY_PER_TELEPORT = config.getInt("teleport_energy_cost",config.CATEGORY_ABILITIES,4096,"The Energy cost of each Teleportation");
        MAX_TELEPORT_DISTANCE = config.getInt("teleport_max_distance",config.CATEGORY_ABILITIES,32,"The maximum distance in blocks, the player can teleport to");
    }
}
