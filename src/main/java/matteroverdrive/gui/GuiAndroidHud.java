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

package matteroverdrive.gui;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.animation.AnimationSegmentText;
import matteroverdrive.animation.AnimationTextTyping;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.gui.android.*;
import matteroverdrive.gui.config.EnumConfigProperty;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Mouse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/26/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiAndroidHud extends Gui implements IConfigSubscriber
{
    public static final ResourceLocation glitch_tex = new ResourceLocation(Reference.PATH_GUI + "glitch.png");
    public static final ResourceLocation spinner_tex = new ResourceLocation(Reference.PATH_ELEMENTS + "spinner.png");
    public static final ResourceLocation top_element_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "android_bg_element.png");
    public static final ResourceLocation cloak_overlay = new ResourceLocation(Reference.PATH_ELEMENTS + "cloak_overlay.png");
    private AnimationTextTyping textTyping;
    private Minecraft mc;
    private Random random;
    private ShaderGroup hurtShader;
    private List<IBionicStat> stats = new ArrayList<>();
    public static boolean showRadial = false;
    public static double radialDeltaX,radialDeltaY,radialAngle;
    private static double radialAnimationTime;
    private IIcon crosshairIcon;
    private List<IAndroidHudElement> hudElements;
    public final AndroidHudMinimap hudMinimap;
    public final AndroidHudStats hudStats;
    public final AndroidHudBionicStats bionicStats;
    public GuiColor baseGuiColor;
    public float opacity;

    public GuiAndroidHud(Minecraft mc)
    {
        super();
        this.mc = mc;
        random = new Random();
        textTyping = new AnimationTextTyping(false,AndroidPlayer.TRANSFORM_TIME);
        String info;
        for (int i = 0;i < 5;i++)
        {
            info = MOStringHelper.translateToLocal("gui.android_hud.transforming.line." + i);
            textTyping.addSeqmentSequential(new AnimationSegmentText(info,0,1).setLengthPerCharacter(2));
            textTyping.addSeqmentSequential(new AnimationSegmentText(info,0,0).setLengthPerCharacter(2));
        }

        info = MOStringHelper.translateToLocal("gui.android_hud.transforming.line.final");
        textTyping.addSeqmentSequential(new AnimationSegmentText(info, 0, 1).setLengthPerCharacter(2));
        textTyping.addSeqmentSequential(new AnimationSegmentText(info, AndroidPlayer.TRANSFORM_TIME, 0));

        hudElements = new ArrayList<>();
        hudMinimap = new AndroidHudMinimap(AndroidHudPosition.BOTTOM_LEFT,"android_minimap");
        hudStats = new AndroidHudStats(AndroidHudPosition.TOP_LEFT,"android_stats");
        bionicStats = new AndroidHudBionicStats(AndroidHudPosition.TOP_RIGHT,"android_biotic_stats");
        hudElements.add(hudMinimap);
        hudElements.add(hudStats);
        hudElements.add(bionicStats);

        baseGuiColor = Reference.COLOR_HOLO.multiply(0.5f);
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        if (showRadial)
        {
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderExperienceBar(RenderGameOverlayEvent event)
    {
        AndroidPlayer android = AndroidPlayer.get(mc.thePlayer);

        if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog && !event.type.equals(RenderGameOverlayEvent.ElementType.ALL) && event.isCancelable())
        {
            event.setCanceled(true);
            return;
        }

        if ((android.isAndroid() && (event.type == RenderGameOverlayEvent.ElementType.FOOD || event.type == RenderGameOverlayEvent.ElementType.AIR || event.type == RenderGameOverlayEvent.ElementType.HEALTH)))
        {
            event.setCanceled(true);
            return;
        }

        if (android.isAndroid() && event.isCancelable() && event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            event.setCanceled(true);

            if (!showRadial)
            {
                renderCrosshair(event);
            }

            mc.getTextureManager().bindTexture(Gui.icons);
        }

        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            return;
        }

        renderHud(event);

        //System.out.println(showRadial);
        if (android.isAndroid())
        {
            if (showRadial)
            {
                GuiAndroidHud.radialAnimationTime = Math.min(1,GuiAndroidHud.radialAnimationTime+event.partialTicks*0.2);
            }else
            {
                GuiAndroidHud.radialAnimationTime = Math.max(0,GuiAndroidHud.radialAnimationTime-event.partialTicks*0.2);
            }

            if (GuiAndroidHud.radialAnimationTime > 0)
            {
                renderRadialMenu(event);
            }
        }
    }

    public void renderCrosshair(RenderGameOverlayEvent event)
    {
        glPushMatrix();
        float scale = 8 + ClientProxy.weaponHandler.getEquippedWeaponHeatPercent(Minecraft.getMinecraft().thePlayer)*16;
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);
        glEnable(GL_ALPHA_TEST);
        //RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO,0.5f);
        glColor3d(1, 1, 1);
        crosshairIcon = ClientProxy.holoIcons.getIcon("crosshair").getIcon();
        glTranslated(event.resolution.getScaledWidth() / 2, event.resolution.getScaledHeight() / 2, 0);
        glPushMatrix();
        ClientProxy.holoIcons.bindSheet();
        glRotated(90, 0, 0, 1);
        RenderHelper.renderIcon(-1.5,-scale,0,crosshairIcon,3,3);
        glRotated(90, 0, 0, 1);
        RenderHelper.renderIcon(-1.5,-scale,0,crosshairIcon,3,3);
        glRotated(90,0,0,1);
        RenderHelper.renderIcon(-1.5,-scale,0,crosshairIcon,3,3);
        glRotated(90,0,0,1);
        RenderHelper.renderIcon(-1.5,-scale,0,crosshairIcon,3,3);
        glPopMatrix();
        glPopMatrix();
    }

    public void renderRadialMenu(RenderGameOverlayEvent event)
    {
        glPushMatrix();
        glTranslated(event.resolution.getScaledWidth() / 2, event.resolution.getScaledHeight() / 2, 0);
        double scale = MOMathHelper.easeIn(GuiAndroidHud.radialAnimationTime,0,1,1);
        glScaled(scale, scale, scale);
        ClientProxy.holoIcons.bindSheet();
        AndroidPlayer androidPlayer = AndroidPlayer.get(Minecraft.getMinecraft().thePlayer);

        stats.clear();
        for (IBionicStat stat : MatterOverdrive.statRegistry.getStats())
        {
            if (stat.showOnWheel(androidPlayer, androidPlayer.getUnlockedLevel(stat)) && androidPlayer.isUnlocked(stat,0))
            {
                stats.add(stat);
            }
        }

        glColor3f(1, 1, 1);
        glDepthMask(false);
        //glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glPushMatrix();
        glRotated(radialAngle, 0, 0, -1);
        RenderUtils.applyColorWithMultipy(baseGuiColor, 0.4f);
        ClientProxy.holoIcons.renderIcon("up_arrow_large", -9, -50);
        glPopMatrix();

        int i = 0;
        for (IBionicStat stat : stats)
        {
            double angleSeg = (Math.PI*2/stats.size());
            double angle,x,y,radiusMin,radiusMax,angleAb,angleCircle;
            double radius = 80;
            angle = angleSeg * i;
            angle += Math.toRadians(180D);

            radiusMin = radius - 16;
            radiusMax = radius + 16;

            glDisable(GL_TEXTURE_2D);
            glDisable(GL_ALPHA_TEST);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            if (androidPlayer.getActiveStat() != null && androidPlayer.getActiveStat().equals(stat))
            {
                radiusMax = radius + 20;
                radiusMin = radius - 16;
                glColor4d(0, 0, 0, 0.6);
            }else
            {
                glColor4d(0, 0, 0, 0.4);
            }


            Tessellator.instance.startDrawingQuads();
            for (int c = 0;c < 32;c++)
            {
                angleAb = ((angleSeg)/32d);
                angleCircle = c * angleAb + angle - angleSeg/2;
                Tessellator.instance.addVertex(Math.sin(angleCircle) * radiusMax,Math.cos(angleCircle) * radiusMax,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle + angleAb) * radiusMax,Math.cos(angleCircle + angleAb) * radiusMax,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle + angleAb) * radiusMin,Math.cos(angleCircle + angleAb) * radiusMin,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle) * radiusMin,Math.cos(angleCircle) * radiusMin,-1);
            }
            Tessellator.instance.draw();

            radiusMax = radius - 20;
            radiusMin = radius - 25;
            Tessellator.instance.startDrawingQuads();
            glColor4d(0, 0, 0, 0.2);
            for (int c = 0;c < 32;c++)
            {
                angleAb = ((Math.PI*2)/32d);
                angleCircle = c * angleAb;
                Tessellator.instance.addVertex(Math.sin(angleCircle) * radiusMax,Math.cos(angleCircle) * radiusMax,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle + angleAb) * radiusMax,Math.cos(angleCircle + angleAb) * radiusMax,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle + angleAb) * radiusMin,Math.cos(angleCircle + angleAb) * radiusMin,-1);
                Tessellator.instance.addVertex(Math.sin(angleCircle) * radiusMin,Math.cos(angleCircle) * radiusMin,-1);
            }
            Tessellator.instance.draw();
            glEnable(GL_TEXTURE_2D);

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glEnable(GL_ALPHA_TEST);
            glEnable(GL_DEPTH_TEST);
            glDepthMask(false);

            ClientProxy.holoIcons.bindSheet();
            if (androidPlayer.getActiveStat() != null)
            {
                if (androidPlayer.getActiveStat().equals(stat))
                {
                    RenderUtils.applyColorWithMultipy(baseGuiColor, 1);
                    x = Math.sin(angle) * radius;
                    y = Math.cos(angle) * radius;
                    RenderHelper.renderIcon(-12 + x, -12 + y, 10, stat.getIcon(0), 24, 24);
                    String statName = stat.getDisplayName(androidPlayer, androidPlayer.getUnlockedLevel(stat));
                    int statNameWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(statName);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    Minecraft.getMinecraft().fontRenderer.drawString(statName, -statNameWidth / 2, -5, baseGuiColor.getColor());
                }
                else
                {
                    x = Math.sin(angle) * radius;
                    y = Math.cos(angle) * radius;
                    RenderUtils.applyColorWithMultipy(baseGuiColor, 0.2f);
                    RenderHelper.renderIcon(-12 + x, -12 + y, 10, stat.getIcon(0), 24, 24);
                }
            }

            i++;
        }
        glPopMatrix();
    }

    public static float hudRotationYawSmooth;
    public static float hudRotationPitchSmooth;

    public void renderHud(RenderGameOverlayEvent event)
    {
        AndroidPlayer android = AndroidPlayer.get(mc.thePlayer);

        if (android != null) {

            if (android.isAndroid()) {

                glPushMatrix();

                if (MatterOverdriveBioticStats.cloak.isActive(android,0)) {
                    glDepthMask(false);
                    glBlendFunc(GL_DST_COLOR, GL_ZERO);
                    mc.renderEngine.bindTexture(cloak_overlay);
                    func_146110_a(0, 0, 0, 0, event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
                }

                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                glDisable(GL_ALPHA_TEST);

                hudRotationYawSmooth = hudRotationYawSmooth * 0.4f + mc.thePlayer.rotationYaw * 0.6f;
                hudRotationPitchSmooth = hudRotationPitchSmooth * 0.4f + mc.thePlayer.rotationPitch * 0.6f;
                glTranslated((hudRotationYawSmooth - mc.thePlayer.rotationYaw) * 6, (hudRotationPitchSmooth - mc.thePlayer.rotationPitch) * 6, 0);

                for (IAndroidHudElement element : hudElements)
                {
                    if (element.isVisible(android))
                    {
                        glPushMatrix();
                        int elementWidth =  (int)(element.getWidth(event.resolution) * element.getPosition().x);
                        glTranslated(element.getPosition().x * event.resolution.getScaledWidth_double() - elementWidth, element.getPosition().y * event.resolution.getScaledHeight_double() - element.getHeight(event.resolution) * element.getPosition().y, 0);
                        element.setBaseColor(baseGuiColor);
                        element.drawElement(android, event.mouseX, event.mouseY, event.resolution, event.partialTicks);
                        glPopMatrix();
                    }
                }

                ClientProxy.holoIcons.bindSheet();

                if (android.getPlayer().getHeldItem() != null && android.getPlayer().getHeldItem().getItem() instanceof IWeapon) {
                    renderWeaponHud(event,(IWeapon)android.getPlayer().getHeldItem().getItem(),android.getPlayer().getHeldItem(),baseGuiColor);
                }

                glAlphaFunc(GL_GREATER, 0.5f);
                glEnable(GL_ALPHA_TEST);
                glDisable(GL_BLEND);
                glPopMatrix();

                renderHurt(android, event);
            }else
            {
                if (android.isTurning())
                {
                    renderTransformAnimation(android,event);
                }
            }
        }
    }

    private void renderWeaponHud(RenderGameOverlayEvent event,IWeapon weapon,ItemStack weaponStack,GuiColor color)
    {
        int x = 12;
        int y = 44;

        //region Ammo
        float percent = (float)weapon.getAmmo(weaponStack) / (float)weapon.getMaxAmmo(weaponStack);
        GuiColor lerpedColor = RenderUtils.lerp(Reference.COLOR_HOLO_RED, color, percent);
        RenderUtils.applyColorWithAlpha(lerpedColor);
        ClientProxy.holoIcons.renderIcon("ammo",x,y);
        x += 18;
        String info = DecimalFormat.getPercentInstance().format(percent);
        mc.fontRenderer.drawString(info, x, y+4, lerpedColor.getColor());
        x += mc.fontRenderer.getStringWidth(info);
        int energyPackCount = 0;
        for (ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory)
        {
            if (stack != null && stack.getItem() instanceof IEnergyPack)
            {
                energyPackCount += stack.stackSize;
            }
        }
        info = " | " + Integer.toString(energyPackCount);
        mc.fontRenderer.drawString(info,x,y + 4,color.getColor());
        x += 4 + mc.fontRenderer.getStringWidth(info);
        //endregion

        //region Temperature
        if (weapon.getMaxHeat(weaponStack) > 0) {
            percent = weapon.getHeat(weaponStack) / weapon.getMaxHeat(weaponStack);
            lerpedColor = RenderUtils.lerp(Reference.COLOR_HOLO_RED, color, 1-percent);
            RenderUtils.applyColorWithAlpha(lerpedColor);
            ClientProxy.holoIcons.renderIcon("temperature",x,y);
            x += 18;
            info = DecimalFormat.getPercentInstance().format(percent);
            mc.fontRenderer.drawString(info, x, y + 4, lerpedColor.getColor());
        }
        //endregion
    }

    private void renderTransformAnimation(AndroidPlayer player,RenderGameOverlayEvent event)
    {
        int centerX = event.resolution.getScaledWidth() / 2;
        int centerY = event.resolution.getScaledHeight() / 2;
        int maxTime = AndroidPlayer.TRANSFORM_TIME;
        int time = maxTime - player.getEffects().getInteger(AndroidPlayer.EFFECT_KEY_TURNING);
        textTyping.setTime(time);

        if (time % 40 > 0 &&  time % 40 < 3)
        {
            renderGlitch(player,event);
        }

        String info = textTyping.getString();
        int width = mc.fontRenderer.getStringWidth(info);
        mc.fontRenderer.drawString(info,centerX - width / 2,centerY - 28,Reference.COLOR_HOLO.getColor());

        mc.renderEngine.bindTexture(spinner_tex);
        glPushMatrix();
        glTranslated(centerX, centerY,0);
        glRotated(mc.theWorld.getWorldTime() * 10,0,0,-1);
        glTranslated(-16,-16,0);
        func_146110_a(0, 0, 0, 0, 32, 32, 32, 32);
        glPopMatrix();

        mc.fontRenderer.drawString(Math.round(textTyping.getPercent() * 100) + "%",centerX - 6,centerY - 3,Reference.COLOR_HOLO.getColor());

        if (!Minecraft.getMinecraft().entityRenderer.isShaderActive())
        {
            createHurtShader();
            bindHurtShader();
        }
    }

    public void renderHurt(AndroidPlayer player,RenderGameOverlayEvent event)
    {
        if (player.getEffects().getInteger("GlitchTime") > 0)
        {
            renderGlitch(player,event);
            if (!Minecraft.getMinecraft().entityRenderer.isShaderActive())
            {
                createHurtShader();
                bindHurtShader();
            }
        }else
        {
            disableHurtShader();
        }
    }

    private void createHurtShader()
    {
        if (hurtShader == null && Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            try {
                hurtShader = new ShaderGroup(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("shaders/post/deconverge.json"));
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindHurtShader()
    {
        if (hurtShader != null && Minecraft.getMinecraft().gameSettings.fancyGraphics)
        {
            Minecraft.getMinecraft().entityRenderer.theShaderGroup = hurtShader;
            hurtShader.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        }
    }

    private void disableHurtShader()
    {
        if (Minecraft.getMinecraft().entityRenderer.theShaderGroup != null && Minecraft.getMinecraft().entityRenderer.theShaderGroup == hurtShader) {
            Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
        }
    }

    public void renderGlitch(AndroidPlayer player,RenderGameOverlayEvent event) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glColor3d(1, 1, 1);
        mc.renderEngine.bindTexture(glitch_tex);
        func_146110_a(0, 0, random.nextInt(1280), random.nextInt(720), event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
        glDisable(GL_BLEND);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        Property prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD,hudMinimap.getName()+".position",hudMinimap.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.minimap.position");
        hudMinimap.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD,hudStats.getName()+".position",hudStats.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.stats.position");
        hudStats.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD,bionicStats.getName()+".position",bionicStats.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.bionicStats.position");
        bionicStats.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        GuiColor color = Reference.COLOR_HOLO;
        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD,"hud_color",Integer.toHexString(color.getColor()));
        prop.setLanguageKey("config.android_hud.color");
        try {
            baseGuiColor = new GuiColor(Integer.parseInt(prop.getString(),16));
        }
        catch (Exception e)
        {
            baseGuiColor = Reference.COLOR_HOLO;
        }

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD,"hud_opacity",0.5f,"The Opacity of the HUD in %",0,1);
        prop.setLanguageKey("config.android_hud.opacity");
        baseGuiColor = new GuiColor(baseGuiColor.getIntR(),baseGuiColor.getIntG(),baseGuiColor.getIntB(),(int)(255 * prop.getDouble()));
    }
}
