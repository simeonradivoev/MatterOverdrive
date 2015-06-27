package com.MO.MatterOverdrive.client.render.item;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.items.Phaser;

import com.MO.MatterOverdrive.items.WeaponColorModule;
import com.MO.MatterOverdrive.util.RenderUtils;
import com.MO.MatterOverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTranslated;

/**
 * Created by Simeon on 3/11/2015.
 */
public class ItemRendererPhaser implements IItemRenderer
{
    public static final String TEXTURE = Reference.PATH_ITEM + "phaser.png";
    private static final String TEXTURE_COLOR_MASK = Reference.PATH_ITEM + "phaser_color_mask.png";
    public static final String MODEL = Reference.PATH_MODEL + "item/phaser.obj";
    public static final float SCALE = 3.2f;
    public static final float THIRD_PERSON_SCALE = 3f;
    public static final float ITEM_SCALE = 3;
    public static final float SCALE_DROP = 2.5f;

    public static IModelCustom phaserModel;
    public static ResourceLocation phaserTexture;
    public static ResourceLocation phaserTextureColorMask;

    public ItemRendererPhaser()
    {
    	phaserTexture = new ResourceLocation(TEXTURE);
        phaserTextureColorMask = new ResourceLocation(TEXTURE_COLOR_MASK);
        phaserModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
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
        	RenderThirdPerson(type,item);
        }
    }
    
    void RenderItem(ItemStack item)
    {
    	glPushMatrix();
        glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        glTranslated(0, 0.03, 0);
        glRotated(0, 0, 1, 0);
        RenderGun(ItemRenderType.INVENTORY,item);
        glPopMatrix();
    }
    
    void RenderThirdPerson(ItemRenderType renderType,ItemStack item)
    {
        glPushMatrix();
        glScaled(THIRD_PERSON_SCALE, THIRD_PERSON_SCALE, THIRD_PERSON_SCALE);
        glTranslated(0.22, 0.13, 0.22);
        glRotated(-135, 0, 1, 0);
        glRotated(60, 1, 0, 0);
        RenderGun(renderType,item);
        glPopMatrix();
    }
    
    void RenderDrop(ItemStack item)
    {
        glPushMatrix();
        glScaled(SCALE_DROP, SCALE_DROP, SCALE_DROP);
        glTranslated(0, 0, 0);
        //glRotated(-130,0,1,0);
        glRotated(15, 1, 0, 0);
        RenderGun(ItemRenderType.ENTITY,item);
        glPopMatrix();
    }

    void RenderFirstPerson(ItemStack item)
    {
        glPushMatrix();
        glScaled(SCALE, SCALE, SCALE);
        if(Minecraft.getMinecraft().thePlayer.isUsingItem())
        {
            glTranslated(0.2, 0.2, 0.08);
            glRotated(150, 0, 1, 0);
            glRotated(-26, 1, 0, 0);
            glRotated(-5, 1, 0, 1);
        }
        else
        {
            glTranslated(0.05, 0.25, 0.05);
            glRotated(137.5, 0, 1, 0);
            glRotated(-12, 1, 0, 0);
        }
        RenderGun(ItemRenderType.EQUIPPED_FIRST_PERSON,item);
        glPopMatrix();
    }
    
    void RenderGun(ItemRenderType renderType,ItemStack item)
    {
        //org.lwjgl.util.glu.GLU.gluPerspective(80,1,0.01f,10000f);
        //glClear(GL_COLOR_BUFFER_BIT);

        Minecraft.getMinecraft().renderEngine.bindTexture(phaserTexture);
    	phaserModel.renderPart("phaser");

        GuiColor color = WeaponColorModule.defaultColor;
        ItemStack color_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_COLOR,item);
        if (color_module != null)
        {
            IWeaponModule module = (IWeaponModule)color_module.getItem();
            Object colorObject = module.getValue(color_module);
            if(colorObject instanceof GuiColor)
            {
                color = (GuiColor)colorObject;
            }
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
        Minecraft.getMinecraft().renderEngine.bindTexture(phaserTextureColorMask);
        phaserModel.renderPart("phaser");

        glDisable(GL_LIGHTING);
        RenderUtils.disableLightmap();
        renderLevelSlider(item);
        renderKillIndicator(item);
        glEnable(GL_LIGHTING);
    }

    private void renderLevelSlider(ItemStack item)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(phaserTexture);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glColor4f(1, 1, 1, 1);
        phaserModel.renderPart("level_bg");
        setIndicatorColor(item);
        glPushMatrix();
        double power = ((double)item.getTagCompound().getByte("power") + 1) / (double)Phaser.MAX_LEVEL;
        glTranslated(0.035, 0, 0);
        glScaled(power, 1, 1);
        phaserModel.renderPart("level_slider");
        glPopMatrix();
        glEnable(GL_LIGHTING);
    }

    private void renderKillIndicator(ItemStack item)
    {
        glDisable(GL_LIGHTING);
        setIndicatorColor(item);
        phaserModel.renderPart("kill_indicator");
        glEnable(GL_LIGHTING);
    }

    private void setIndicatorColor(ItemStack item)
    {
        if (Phaser.isKillMode(item))
        {
            glColor3d(1, 0, 0);
        }
        else
        {
            glColor3d(0, 1, 0);
        }
    }
}
