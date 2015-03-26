package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.items.Phaser;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/11/2015.
 */
public class ItemRendererPhaser implements IItemRenderer
{
    private static final String TEXTURE_STUN = Reference.PATH_ITEM + "phaser_stun.png";
    private static final String TEXTURE_KILL = Reference.PATH_ITEM + "phaser_kill.png";
    private static final String MODEL = Reference.PATH_MODEL + "item/phaser.obj";
    public static final float SCALE = 1 / 4.5f;
    public static final float THIRD_PERSON_SCALE = 1 / 3.5f;
    public static final float ITEM_SCALE = 1 / 3.5f;
    public static final float SCALE_DROP = 1 / 5.0f;

    IModelCustom phaserModel;
    ResourceLocation phaserTextureStun;
    ResourceLocation phaserTextureKill;

    public ItemRendererPhaser()
    {
    	phaserTextureStun = new ResourceLocation(TEXTURE_STUN);
    	phaserTextureKill = new ResourceLocation(TEXTURE_KILL);
        phaserModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
        	RenderFirstPerson(item);
        }
        else if(type == ItemRenderType.INVENTORY)
        {
        	RenderItem(item);
        }
        else if(type == ItemRenderType.ENTITY)
        {
        	RenderDrop(item);
        }
        else
        {
        	RenderThirdPerson(item);
        }
    }
    
    void RenderItem(ItemStack item)
    {
    	GL11.glPushMatrix();
        GL11.glScaled(ITEM_SCALE,ITEM_SCALE,ITEM_SCALE);
        GL11.glTranslated(-0.2, 0.5, 0);
        GL11.glRotated(0,0,1,0);
        RenderGun(item);
        GL11.glPopMatrix();
    }
    
    void RenderThirdPerson(ItemStack item)
    {
        GL11.glPushMatrix();
        GL11.glScaled(THIRD_PERSON_SCALE,THIRD_PERSON_SCALE,THIRD_PERSON_SCALE);
        GL11.glTranslated(2, 1, 2);
        GL11.glRotated(-130,0,1,0);
        GL11.glRotated(40,1,0,0);
        RenderGun(item);
        GL11.glPopMatrix();
    }
    
    void RenderDrop(ItemStack item)
    {
        GL11.glPushMatrix();
        GL11.glScaled(SCALE_DROP,SCALE_DROP,SCALE_DROP);
        GL11.glTranslated(0, 1, 0);
        //GL11.glRotated(-130,0,1,0);
        GL11.glRotated(40,1,0,0);
        RenderGun(item);
        GL11.glPopMatrix();
    }

    void RenderFirstPerson(ItemStack item)
    {
        GL11.glPushMatrix();
        GL11.glScaled(SCALE,SCALE,SCALE);
        GL11.glTranslated(0.5, 4, 1.5);
        GL11.glRotated(140,0,1,0);
        GL11.glRotated(-10,1,0,0);
        RenderGun(item);
        GL11.glPopMatrix();
    }
    
    void RenderGun(ItemStack item)
    {
    	if(Phaser.isKillMode(item))
    	{
    		Minecraft.getMinecraft().renderEngine.bindTexture(phaserTextureKill);
    	}
    	else
    	{
    		Minecraft.getMinecraft().renderEngine.bindTexture(phaserTextureStun);
    	}
    	
    	
    	phaserModel.renderAll();
    }
}
