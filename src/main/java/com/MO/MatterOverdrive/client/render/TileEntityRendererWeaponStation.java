package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TileEntityRendererWeaponStation extends TileEntityRendererStation<TileEntityWeaponStation> {

    EntityItem itemEntity;

    public TileEntityRendererWeaponStation()
    {
        super();
    }

    @Override
    protected void renderHologram(TileEntityWeaponStation weaponStation, double x, double y, double z,float partialTicks,double noise)
    {
        if (isUsable(weaponStation))
        {
            ItemStack stack = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT);
            if (stack != null) {
                if (itemEntity == null) {
                    itemEntity = new EntityItem(weaponStation.getWorldObj(), weaponStation.xCoord, weaponStation.yCoord, weaponStation.zCoord, stack);
                } else if (!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack)) {
                    itemEntity.setEntityItemStack(stack);
                }

                beginHolo(weaponStation);

                itemEntity.hoverStart = weaponStation.getWorldObj().getWorldTime() * 0.05f + (float) noise * 10;
                RenderManager.instance.func_147939_a(itemEntity, x + 0.5f, y + 0.8f, z + 0.5f, 0, 0, true);
                endHolo();
            }
        }else
        {
            super.renderHologram(weaponStation,x,y,z,partialTicks, noise);
        }
    }
}

