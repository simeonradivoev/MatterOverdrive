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

package matteroverdrive.client.render;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.item.ItemRendererPhaser;
import matteroverdrive.client.sound.PhaserSound;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/13/2015.
 */
public class RendererPhaserBeam implements IWorldLastRenderer
{
    Map<Entity,PhaserSound> soundMap = new HashMap<Entity, PhaserSound>();
    public static ResourceLocation phaserSoundLocation = new ResourceLocation(Reference.MOD_ID + ":" +"phaser_beam_1");

    public void onRenderWorldLast(RenderHandler renderHandler,RenderWorldLastEvent event)
    {
        glPushMatrix();
        glTranslated(-Minecraft.getMinecraft().thePlayer.posX, -Minecraft.getMinecraft().thePlayer.posY, -Minecraft.getMinecraft().thePlayer.posZ);
        renderClient(renderHandler);
        renderOthers(renderHandler);
        glPopMatrix();
    }

    public void renderOthers(RenderHandler renderHandler)
    {
        for (Object playerObj : Minecraft.getMinecraft().theWorld.getLoadedEntityList())
        {
            if (playerObj instanceof EntityOtherPlayerMP)
            {
                EntityOtherPlayerMP playerMP = (EntityOtherPlayerMP)playerObj;
                if (playerMP.isUsingItem() && playerMP.getItemInUse().getItem() instanceof Phaser)
                {
                    renderBeam(playerMP.getItemInUse(),playerMP,playerMP.worldObj,new Vector3f(-0.23f, 0.2f, 0.7f),playerMP.getEyeHeight() - 0.5f,-0.3f);
                    PlayPhaserSound(playerMP,renderHandler.getRandom());
                }
                else
                {
                    StopPhaserSound(playerMP);
                }
            }
        }
    }

    public void renderClient(RenderHandler renderHandler)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player.isUsingItem() && player.getItemInUse().getItem() instanceof Phaser)
        {
            renderBeam(player.getItemInUse(),player,player.worldObj,new Vector3f(-0.1f, -0.1f, 0.15f),0,0.25f);
            PlayPhaserSound(player,renderHandler.getRandom());
        }
        else
        {
            StopPhaserSound(player);
        }
    }

    public void renderBeam(ItemStack phaser,EntityPlayer viewer,World world,Vector3f offset,float height,float distanceOffset)
    {
        RenderHelper.bindTexture(ItemRendererPhaser.phaserTexture);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDisable(GL_LIGHTING);

        GuiColor color = getPhaserColor(viewer);
        glColor4f(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
        glPushMatrix();
        glTranslated(viewer.posX, viewer.posY + height, viewer.posZ);
        glRotated(-viewer.getRotationYawHead(), 0, 1, 0);
        glRotated(viewer.rotationPitch, 1, 0, 0);
        glTranslatef(offset.x, offset.y, offset.z);
        MovingObjectPosition hit = MOPhysicsHelper.rayTrace(viewer, world, getPhaserRange(viewer), 0, Vec3.createVectorHelper(0, height, 0), false, true,MatterOverdriveItems.phaser.getPlayerLook(viewer,phaser));
        double distance = 450;
        if (hit != null)
        {
            Vec3 hitVector = hit.hitVec;
            distance = hitVector.distanceTo(viewer.getPosition(1.0f));
            MatterOverdriveItems.phaser.spawnParticle(hit,phaser,world);
        }
        Vec3 rots = MatterOverdriveItems.phaser.getBeamRotation(phaser,world);
        glRotated(Math.toDegrees(rots.xCoord),1,0,0);
        glRotated(Math.toDegrees(rots.yCoord),0,1,0);
        glRotated(Math.toDegrees(rots.zCoord),0,0,1);
        glScaled(1, 1, distance + distanceOffset);
        ItemRendererPhaser.phaserModel.renderPart("beam");
        glPopMatrix();

        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public GuiColor getPhaserColor(EntityPlayer player)
    {
        GuiColor color = WeaponModuleColor.defaultColor;
        ItemStack color_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_COLOR, player.getItemInUse());
        if (color_module != null)
        {
            IWeaponModule module = (IWeaponModule)color_module.getItem();
            Object colorObject = module.getValue(color_module);
            if(colorObject instanceof GuiColor)
            {
                color = (GuiColor)colorObject;
            }
        }
        return color;
    }

    private int getPhaserRange(EntityPlayer player)
    {
        int range = Phaser.RANGE;
        ItemStack phaserStack = player.getItemInUse();
        if (phaserStack != null && phaserStack.getItem() instanceof Phaser)
        {
            range = ((Phaser) phaserStack.getItem()).getRange(phaserStack);
        }
        return range;
    }

    private void PlayPhaserSound(Entity entity,Random random)
    {
        if (!soundMap.containsKey(entity))
        {
            PhaserSound sound = new PhaserSound(phaserSoundLocation,(float)entity.posX,(float)entity.posY,(float)entity.posZ,random.nextFloat() * 0.1f + 0.3f,1);
            soundMap.put(entity,sound);
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        }
        else if (soundMap.get(entity).isDonePlaying())
        {
            StopPhaserSound(entity);
            PlayPhaserSound(entity,random);
        }
        else
        {
            soundMap.get(entity).setPosition((float)entity.posX,(float)entity.posY,(float)entity.posZ);
        }
    }

    private void StopPhaserSound(Entity entity)
    {
        if (soundMap.containsKey(entity))
        {
            PhaserSound sound = soundMap.get(entity);
            sound.stopPlaying();
            Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
            soundMap.remove(entity);

        }
    }
}
