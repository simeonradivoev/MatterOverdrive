package com.MO.MatterOverdrive.client;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.client.render.ItemRendererPhaser;
import com.MO.MatterOverdrive.items.Phaser;
import com.MO.MatterOverdrive.items.WeaponColorModule;
import com.MO.MatterOverdrive.util.MOPhysicsHelper;
import com.MO.MatterOverdrive.util.WeaponHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/17/2015.
 */
public class RenderHandler
{
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        glPushMatrix();

        glTranslated(-Minecraft.getMinecraft().thePlayer.posX, -Minecraft.getMinecraft().thePlayer.posY, -Minecraft.getMinecraft().thePlayer.posZ);
        renderClient();
        renderOthers();
        glPopMatrix();
    }

    public void renderOthers()
    {
        for (Object playerObj : Minecraft.getMinecraft().theWorld.getLoadedEntityList())
        {
            if (playerObj instanceof EntityOtherPlayerMP)
            {
                EntityOtherPlayerMP playerMP = (EntityOtherPlayerMP)playerObj;
                if (playerMP.isUsingItem() && playerMP.getItemInUse().getItem() instanceof Phaser)
                {
                    renderBeam(playerMP,playerMP.worldObj,new Vector3f(-0.23f, 0.2f, 0.7f),playerMP.getEyeHeight() - 0.5f,-0.3f);
                }
            }
        }
    }

    public void renderClient()
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player.isUsingItem() && player.getItemInUse().getItem() instanceof Phaser)
        {
            renderBeam(player,player.worldObj,new Vector3f(-0.1f, -0.1f, 0.25f),0,0.25f);
        }
    }

    public void renderBeam(EntityPlayer viewer,World world,Vector3f offset,float height,float distanceOffset)
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
        MovingObjectPosition hit = MOPhysicsHelper.rayTrace(viewer, world, getPhaserRange(viewer), 1.0f, Vec3.createVectorHelper(0,height,0));
        double distance = 450;
        if (hit != null)
        {
            Vec3 hitVector = hit.hitVec;
            distance = hitVector.distanceTo(viewer.getPosition(1.0f));
            Block b = world.getBlock(hit.blockX,hit.blockY,hit.blockZ);
            if (b != null && b != Blocks.air)
            {
                viewer.worldObj.spawnParticle("smoke",hitVector.xCoord,hitVector.yCoord,hitVector.zCoord,0,0,0);
            }
            else if (hit.entityHit != null)
            {
                viewer.worldObj.spawnParticle("reddust",hitVector.xCoord,hitVector.yCoord,hitVector.zCoord,0,0,0);
            }

        }
        glScaled(1, 1, distance + distanceOffset);
        ItemRendererPhaser.phaserModel.renderPart("beam");
        glPopMatrix();

        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public GuiColor getPhaserColor(EntityPlayer player)
    {
        GuiColor color = WeaponColorModule.defaultColor;
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
}
