package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.IScannable;
import com.MO.MatterOverdrive.client.RenderHandler;
import com.MO.MatterOverdrive.client.render.IWorldLastRenderer;
import com.MO.MatterOverdrive.client.render.tileentity.TileEntityRendererPatternMonitor;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/11/2015.
 */
public class RenderMatterScannerInfoHandler implements IWorldLastRenderer
{
    public ResourceLocation spinnerTexture = new ResourceLocation(Reference.PATH_ELEMENTS + "spinner.png");

    public void onRenderWorldLast(RenderHandler renderHandler,RenderWorldLastEvent event)
    {
        ItemStack helditem = Minecraft.getMinecraft().thePlayer.getHeldItem();

        if (helditem != null && helditem.getItem() instanceof MatterScanner && Minecraft.getMinecraft().thePlayer.isUsingItem())
        {
            glPushMatrix();
            renderInfo(Minecraft.getMinecraft().thePlayer,helditem,event.partialTicks);
            glPopMatrix();
        }
        else if (AndroidPlayer.get(Minecraft.getMinecraft().thePlayer).isAndroid())
        {
            renderInfo(Minecraft.getMinecraft().thePlayer,Minecraft.getMinecraft().objectMouseOver,null,event.partialTicks);
        }
    }

    private void renderInfo(EntityPlayer player,ItemStack scanner,float ticks)
    {
        MatterScanner scannerItem = (MatterScanner)scanner.getItem();
        MovingObjectPosition position = scannerItem.getScanningPos(player);
        renderInfo(player,position,scanner,ticks);
    }

    private void renderInfo(EntityPlayer player,MovingObjectPosition position,ItemStack scanner,float ticks)
    {
        Vec3 playerPos = player.getPosition(ticks);

        glDisable(GL_DEPTH_TEST);

        if (position != null) {

            if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {

                Block block = player.worldObj.getBlock(position.blockX, position.blockY, position.blockZ);
                if (block != null)
                {
                    renderBlockInfo(block,position,player,playerPos,scanner,scanner == null);
                }
            }
            else if (position.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            {
                if (position.entityHit != null)
                {
                    renderEntityInfo(position.entityHit,position,player,playerPos,ticks);
                }
            }
        }
        glEnable(GL_DEPTH_TEST);
    }

    private void renderBlockInfo(Block block,MovingObjectPosition position,EntityPlayer player,Vec3 playerPos,ItemStack scanner,boolean infoOnly)
    {
        double offset = 0.1;
        ForgeDirection side = ForgeDirection.getOrientation(position.sideHit);
        List<String> infos = new ArrayList<String>();
        if (block instanceof IScannable)
        {
            ((IScannable) block).addInfo(player.worldObj,position.blockX,position.blockY,position.blockZ,infos);

        }
        else if (player.worldObj.getTileEntity(position.blockX,position.blockY,position.blockZ) instanceof IScannable)
        {
            ((IScannable) player.worldObj.getTileEntity(position.blockX,position.blockY,position.blockZ)).addInfo(player.worldObj,position.blockX,position.blockY,position.blockZ,infos);
        }
        else if (infoOnly)
        {
            return;
        }

        glPushMatrix();
        glTranslated(side.offsetX * 0.5, side.offsetY * 0.5, side.offsetZ * 0.5);
        glTranslated((position.blockX + 0.5) - playerPos.xCoord, (position.blockY + 0.5) - playerPos.yCoord, (position.blockZ + 0.5) - playerPos.zCoord);
        rotateFromSide(side, player.rotationYaw);
        glTranslated(-0.5, -0.5, -offset);
        drawInfoPlane(0);

        ItemStack blockItemStack = MatterDatabaseHelper.GetItemStackFromWorld(player.worldObj, position.blockX, position.blockY, position.blockZ);
        int matter = MatterHelper.getMatterAmountFromItem(blockItemStack);
        if (matter > 0) {
            infos.add("Matter: " + MatterHelper.formatMatter(matter));
        }
        String blockName = "Unknown";

        try {
            if (blockItemStack != null) {
                blockName = blockItemStack.getDisplayName();
            }
            else
            {
                blockName = player.worldObj.getBlock(position.blockX, position.blockY, position.blockZ).getLocalizedName();
            }
        }
        catch (Exception exeption)
        {
            blockName = player.worldObj.getBlock(position.blockX, position.blockY, position.blockZ).getLocalizedName();
        }
        finally
        {
            drawInfoList(blockName, infos);
        }

        if (!(block instanceof IScannable) && scanner != null)
        {
            drawProgressBar(scanner,player);
        }
        glPopMatrix();
    }

    private void drawProgressBar(ItemStack scanner,EntityPlayer player)
    {
        glPushMatrix();
        renderer().bindTexture(spinnerTexture);

        int count = player.getItemInUseCount();
        int maxCount = scanner.getMaxItemUseDuration();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, (float)count / (float)maxCount);
        glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.5f, Reference.COLOR_HOLO.getFloatG()* 0.5f, Reference.COLOR_HOLO.getFloatB()* 0.5f);
        glTranslated(0.35,0.45,-0.1);
        glScaled(0.3,0.3,0.3);
        RenderUtils.drawPlane(1);
        //glDisable(GL_ALPHA_TEST);
        glDisable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.5f);
        glPopMatrix();
    }

    private void renderEntityInfo(Entity entity,MovingObjectPosition position,EntityPlayer player,Vec3 playerPos,float ticks)
    {
        double offset = 0.1;
        ForgeDirection side = ForgeDirection.getOrientation(position.subHit);
        Vec3 hitPos = position.hitVec;
        Vec3 entityPos;
        String name = entity.getCommandSenderName();
        List<String> infos = new ArrayList<String>();
        if (entity instanceof EntityLivingBase)
        {
            entityPos = ((EntityLivingBase) entity).getPosition(ticks);
            entityPos.yCoord += + entity.getEyeHeight();
            infos.add("Health: " + ((EntityLivingBase) entity).getHealth() + " / " + ((EntityLivingBase) entity).getMaxHealth());
        }
        else
        {
            entityPos = Vec3.createVectorHelper(entity.posX,entity.posY + entity.getEyeHeight(),entity.posZ);
        }

        glPushMatrix();
        glTranslated(entityPos.xCoord - playerPos.xCoord, entityPos.yCoord - playerPos.yCoord, entityPos.zCoord - playerPos.zCoord);
        glRotated(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * ticks , 0, -1, 0);
        glRotated(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * ticks, 1, 0, 0);
        glTranslated(1, 0, 0);
        glRotated(180, 0, 0, 1);
        glTranslated(-0.5, -0.5, -offset);
        drawInfoPlane(0.5);
        drawInfoList(name, infos);
        glPopMatrix();
    }

    private void drawInfoPlane(double opacity) {

        glPushMatrix();
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (opacity > 0)
        {
            glDisable(GL_TEXTURE_2D);
            glColor4d(0, 0, 0, opacity);
            RenderUtils.drawPlane(1);
            glEnable(GL_TEXTURE_2D);
        }


        glTranslated(0, 0, -0.01);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        glColor3d(Reference.COLOR_HOLO.getFloatR() * 0.05f, Reference.COLOR_HOLO.getFloatG() * 0.05f, Reference.COLOR_HOLO.getFloatB() * 0.05f);
        renderer().bindTexture(TileEntityRendererPatternMonitor.screenTextureBack);
        RenderUtils.drawPlane(1);

        //Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityRendererPatternMonitor.screenTexture);
        //glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.7f, Reference.COLOR_HOLO.getFloatG() * 0.7f, Reference.COLOR_HOLO.getFloatB() * 0.7f);
        //drawPlane();

        glDisable(GL_BLEND);
        glPopMatrix();

    }

    private void drawInfoList(String name,List<String> infos)
    {
        glPushMatrix();
        int width = fontRenderer().getStringWidth(name);
        glTranslated(0.5, 0.5, -0.05);
        glScaled(0.01, 0.01, 0.01);
        fontRenderer().drawString(name, -width / 2, -40, Reference.COLOR_HOLO.getColor());

        for (int i = 0;i < infos.size();i++)
        {
            width = fontRenderer().getStringWidth(infos.get(i));
            fontRenderer().drawString(infos.get(i), -width / 2, -24 + 16 * i, Reference.COLOR_HOLO.getColor());
        }
        glPopMatrix();
    }

    public static void rotateFromSide(ForgeDirection side,float Yaw)
    {
        if (side == ForgeDirection.UP)
        {
            glRotatef(Math.round(Yaw / 90) * 90 - 180, 0, -1, 0);
            glRotated(90,1,0,0);
        }else if (side == ForgeDirection.DOWN)
        {
            glRotatef(Math.round(Yaw / 90) * 90 - 180, 0, -1, 0);
            glRotated(-90, 1, 0, 0);
        }else if (side == ForgeDirection.WEST)
        {
            glRotated(90,0,1,0);
            glRotated(180,0,0,1);
        }else if (side == ForgeDirection.EAST)
        {
            glRotated(-90,0,1,0);
            glRotated(180,0,0,1);
        }else if (side == ForgeDirection.NORTH)
        {
            glRotated(180,0,0,1);
        }
        else if (side == ForgeDirection.SOUTH)
        {
            glRotated(180,0,1,0);
            glRotated(180,0,0,1);
        }
    }

    private FontRenderer fontRenderer()
    {
        return Minecraft.getMinecraft().fontRenderer;
    }

    private TextureManager renderer()
    {
        return Minecraft.getMinecraft().renderEngine;
    }
}
