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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.item.ItemRendererOmniTool;
import matteroverdrive.client.render.tileentity.TileEntityRendererStation;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.items.weapon.OmniTool;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/13/2015.
 */
@SideOnly(Side.CLIENT)
public class RenderWeaponsBeam extends RenderBeam<EntityPlayer>
{
    Map<Entity, WeaponSound> soundMap = new HashMap<>();
    public static ResourceLocation beamTexture = new ResourceLocation(Reference.PATH_FX + "plasmabeam.png");
    public static ResourceLocation xbeam = new ResourceLocation(Reference.PATH_FX + "xbeam.png");

    public void onRenderWorldLast(RenderHandler renderHandler,RenderWorldLastEvent event)
    {
        glPushMatrix();
        glTranslated(-Minecraft.getMinecraft().thePlayer.posX, -Minecraft.getMinecraft().thePlayer.posY, -Minecraft.getMinecraft().thePlayer.posZ);
        renderClient(renderHandler, event.partialTicks);
        renderOthers(renderHandler, event.partialTicks);
        glPopMatrix();
    }

	@SuppressWarnings("unchecked")
    public void renderOthers(RenderHandler renderHandler, float ticks)
    {


		Minecraft.getMinecraft().theWorld.getLoadedEntityList().stream()
				.filter(o -> o instanceof EntityPlayer)
				.filter(player -> !player.equals(Minecraft.getMinecraft().thePlayer))
				.forEach(o -> {
					EntityPlayer player = (EntityPlayer)o;
					if (shouldRenderBeam(player))
					{
						renderRaycastedBeam(player.getPosition(ticks).addVector(0, player.getEyeHeight(), 0), player.getLook(0), Vec3.createVectorHelper(-0.5, -0.3, 1), player);
					}
					else
					{
						stopWeaponSound(player);
					}
				});
    }

    public void renderClient(RenderHandler renderHandler, float ticks)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (shouldRenderBeam(player))
        {
            Vec3 pos = player.getPosition(1);
            Vec3 look = player.getLook(0);
            renderRaycastedBeam(pos, look, Vec3.createVectorHelper(-0.1, -0.1, 0.15), player);
        }
        else
        {
            stopWeaponSound(player);
        }
    }

    @SideOnly(Side.CLIENT)
    private void playWeaponSound(EntityPlayer player, Random random)
    {
        if (!soundMap.containsKey(player))
        {
            ItemStack weaponStack = player.getItemInUse();
            if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
            {
                //WeaponSound sound = new WeaponSound(new ResourceLocation(((IWeapon)weaponStack.getItem()).getFireSound(weaponStack, player)), (float)player.posX, (float)player.posY, (float)player.posZ, random.nextFloat() * 0.05f + 0.2f, 1);
                WeaponSound sound = ((IWeapon) weaponStack.getItem()).getFireSound(weaponStack,player);
                soundMap.put(player, sound);
				Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			}

        }
        else if (soundMap.get(player).isDonePlaying())
        {
            stopWeaponSound(player);
            playWeaponSound(player, random);
        }
        else
        {
            soundMap.get(player).setPosition((float)player.posX, (float)player.posY, (float)player.posZ);
        }
    }

    private void stopWeaponSound(EntityPlayer entity)
    {
        if (soundMap.containsKey(entity))
        {
            WeaponSound sound = soundMap.get(entity);
            sound.stopPlaying();
            Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
            soundMap.remove(entity);

        }
    }

    @Override
    protected boolean shouldRenderBeam(EntityPlayer entity) {
        return entity.isUsingItem() &&
				(entity.getItemInUse().getItem() instanceof Phaser ||
					entity.getItemInUse().getItem() instanceof OmniTool);
    }

    @Override
    protected void onBeamRaycastHit(MovingObjectPosition hit, EntityPlayer caster)
    {
        ItemStack weaponStack = caster.getItemInUse();
        if (weaponStack != null && weaponStack.getItem() instanceof EnergyWeapon)
        {
            ((EnergyWeapon) weaponStack.getItem()).onProjectileHit(hit, weaponStack, caster.worldObj, 1);
            if (weaponStack.getItem() instanceof OmniTool && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                glPushMatrix();
                RenderUtils.applyColorWithMultipy(getBeamColor(caster), 0.5f + (float)(1+Math.sin(caster.worldObj.getWorldTime() * 0.5f)) * 0.5f);
                Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityRendererStation.glowTexture);
                glDisable(GL_LIGHTING);
                glDisable(GL_CULL_FACE);
                glEnable(GL_BLEND);
                glBlendFunc(GL_ONE,GL_ONE);
                ForgeDirection side = ForgeDirection.getOrientation(hit.sideHit);
                glTranslated(hit.blockX+0.5,hit.blockY+0.5,hit.blockZ+0.5);
                glTranslated(side.offsetX*0.5,side.offsetY*0.5,side.offsetZ*0.5);
                if (side == ForgeDirection.SOUTH)
                {
                    glRotated(90, 1, 0, 0);

                }
				else if (side == ForgeDirection.NORTH)
                {
                    glRotated(90, -1, 0, 0);
                }
				else if (side == ForgeDirection.EAST)
                {
                    glRotated(90, 0, 0, -1);
                }
				else if (side ==ForgeDirection.WEST)
                {
                    glRotated(90, 0, 0, 1);
                }
				else if (side == ForgeDirection.DOWN)
                {
                    glRotated(180, 1, 0, 0);
                }
                glScaled(1, 1.5 + Math.sin(caster.worldObj.getWorldTime() * 0.5) * 0.5, 1);
                ItemRendererOmniTool.omniToolModel.renderPart("dig_effect");
                glDisable(GL_BLEND);
                glEnable(GL_CULL_FACE);
                glPopMatrix();
            }
        }
    }

    @Override
    protected void onBeamRender(EntityPlayer caster)
    {
        playWeaponSound(caster, random);
    }

    @Override
    protected GuiColor getBeamColor(EntityPlayer caster)
    {
        GuiColor color = WeaponModuleColor.defaultColor;
        ItemStack color_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_COLOR, caster.getItemInUse());
        if (color_module != null)
        {
            IWeaponModule module = (IWeaponModule)color_module.getItem();
            Object colorObject = module.getValue(color_module);
            if (colorObject instanceof GuiColor)
            {
                color = (GuiColor)colorObject;
            }
        }
        return color;
    }

    @Override
    protected ResourceLocation getBeamTexture(EntityPlayer caster)
    {
        ItemStack weaponStack = caster.getItemInUse();
        if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
        {
           if (weaponStack.getItem() instanceof Phaser)
           {
               return beamTexture;

           }else if (weaponStack.getItem() instanceof OmniTool)
           {
                return beamTexture;
           }
        }
        return null;
    }

    @Override
    protected float getBeamMaxDistance(EntityPlayer caster)
    {
        int range = Phaser.RANGE;
        ItemStack weaponStack = caster.getItemInUse();
        if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
        {
            range = ((IWeapon) weaponStack.getItem()).getRange(weaponStack);
        }
        return range;
    }

    @Override
    protected float getBeamThickness(EntityPlayer caster)
    {
        ItemStack weaponStack = caster.getItemInUse();
        if (weaponStack != null && weaponStack.getItem() instanceof IWeapon)
        {
            if (weaponStack.getItem() instanceof Phaser)
            {
                return 0.03f;
            }
			else if (weaponStack.getItem() instanceof OmniTool)
            {
                return 0.07f;
            }
        }
        return 0.05f;
    }
}
