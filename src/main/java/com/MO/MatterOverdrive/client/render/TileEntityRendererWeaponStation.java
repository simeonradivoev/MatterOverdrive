package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TileEntityRendererWeaponStation extends TileEntitySpecialRenderer
{
    EntityItem itemEntity;

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        if (tileEntity instanceof TileEntityWeaponStation)
        {
            TileEntityWeaponStation weaponStation = (TileEntityWeaponStation)tileEntity;

            glPushMatrix();
            //RenderUtils.rotateFromBlock(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            renderItem(weaponStation, x, y, z);
            glPopMatrix();
        }
    }

    private void renderItem(TileEntityWeaponStation weaponStation,double x,double y,double z)
    {
        ItemStack stack = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT);
        if(stack != null)
        {
            if(itemEntity == null)
            {
                itemEntity = new EntityItem(weaponStation.getWorldObj(),weaponStation.xCoord,weaponStation.yCoord,weaponStation.zCoord,stack);
            }else if(!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(),stack))
            {
                itemEntity.setEntityItemStack(stack);
            }

            int metadata = weaponStation.getWorldObj().getBlockMetadata(weaponStation.xCoord, weaponStation.yCoord, weaponStation.zCoord);
            ForgeDirection direction = ForgeDirection.values()[metadata];

            itemEntity.hoverStart = 1f;
            RenderManager.instance.func_147939_a(itemEntity, x + 0.5f, y + 0.5f, z + 0.5f, 0, 0, true);
        }
    }
}

