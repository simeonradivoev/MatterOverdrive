package com.MO.MatterOverdrive.gui;

import cofh.lib.audio.SoundBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.animation.AnimationTextTyping;
import com.MO.MatterOverdrive.animation.TextAnimationSegment;
import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/26/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiAndroidHud extends Gui
{
    public static final int STATS_PER_ROW = 6;
    public static final ResourceLocation glitch_tex = new ResourceLocation(Reference.PATH_GUI + "glitch.png");
    public static final ResourceLocation spinner_tex = new ResourceLocation(Reference.PATH_ELEMENTS + "spinner.png");
    public static final ResourceLocation top_element_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "android_bg_element.png");
    public static final ResourceLocation icon_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "android_feature_icon_bg.png");
    public static final ResourceLocation battery_icon = new ResourceLocation(Reference.PATH_GUI_ITEM + "battery.png");
    public static final ResourceLocation person_icon = new ResourceLocation(Reference.PATH_GUI_ITEM + "person.png");
    private AnimationTextTyping textTyping;
    private Minecraft mc;
    private Random random;
    private ShaderGroup hurtShader;
    private ShaderGroup transformShader;

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
            textTyping.addSeqmentSequential(new TextAnimationSegment(info,0,1).setLengthPerCharacter(2));
            textTyping.addSeqmentSequential(new TextAnimationSegment(info,0,0).setLengthPerCharacter(2));
        }

        info = MOStringHelper.translateToLocal("gui.android_hud.transforming.line.final");
        textTyping.addSeqmentSequential(new TextAnimationSegment(info,0,1).setLengthPerCharacter(2));
        textTyping.addSeqmentSequential(new TextAnimationSegment(info,AndroidPlayer.TRANSFORM_TIME,0));
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderExperienceBar(RenderGameOverlayEvent event)
    {
        AndroidPlayer android = AndroidPlayer.get(mc.thePlayer);

        if (android.isAndroid() && event.type == RenderGameOverlayEvent.ElementType.FOOD || event.type == RenderGameOverlayEvent.ElementType.AIR)
        {
            event.setCanceled(true);
            return;
        }
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            return;
        }

        renderHud(event);
    }

    public static float hudRotationYawSmooth;
    public static float hudRotationPitchSmooth;

    public void renderHud(RenderGameOverlayEvent event)
    {
        AndroidPlayer android = AndroidPlayer.get(mc.thePlayer);

        if (android != null) {

            if (android.isAndroid()) {
                glPushMatrix();
                double energy_perc = (double) android.getEnergyStored() / (double) android.getMaxEnergyStored();
                GuiColor enabledColor = new GuiColor(Reference.COLOR_HOLO.getIntR() / 2, Reference.COLOR_HOLO.getIntG() / 2, Reference.COLOR_HOLO.getIntB() / 2, Reference.COLOR_HOLO.getIntA());
                GuiColor disabledColor = new GuiColor(Reference.COLOR_HOLO.getIntR() / 5, Reference.COLOR_HOLO.getIntG() / 5, Reference.COLOR_HOLO.getIntB() / 5, Reference.COLOR_HOLO.getIntA());
                GuiColor color = enabledColor;

                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                glDisable(GL_ALPHA_TEST);

                hudRotationYawSmooth = hudRotationYawSmooth * 0.4f + mc.thePlayer.rotationYaw * 0.6f;
                hudRotationPitchSmooth = hudRotationPitchSmooth * 0.4f + mc.thePlayer.rotationPitch * 0.6f;
                glTranslated((hudRotationYawSmooth - mc.thePlayer.rotationYaw) * 6, (hudRotationPitchSmooth - mc.thePlayer.rotationPitch) * 6, 0);


                glColor3f(enabledColor.getFloatR() / 2, enabledColor.getFloatG() / 2, enabledColor.getFloatB() / 2);
                mc.renderEngine.bindTexture(top_element_bg);
                func_146110_a(0, 10, 0, 0, 174, 11, 174, 11);

                mc.renderEngine.bindTexture(top_element_bg);
                func_146110_a(event.resolution.getScaledWidth() - 174, 10, 0, 0, 174, 11, 174, 11);

                //region energy
                GuiColor energyColor = RenderUtils.lerp(Reference.COLOR_HOLO_RED, enabledColor, (float) energy_perc);
                mc.fontRenderer.drawString(Math.round((energy_perc) * 100d) + "%", 32, 28, energyColor.getColor());
                RenderUtils.applyColor(energyColor);
                mc.renderEngine.bindTexture(battery_icon);
                func_146110_a(12, 20, 0, 0, 22, 22, 22, 22);
                //endregion

                //region speed
                glColor3f(enabledColor.getFloatR(), enabledColor.getFloatG(), enabledColor.getFloatB());
                mc.fontRenderer.drawString(Integer.toString((int) Math.round(android.getSpeedMultiply() * 100)) + "%", 76, 28, enabledColor.getColor());
                mc.renderEngine.bindTexture(person_icon);
                func_146110_a(58, 22, 0, 0, 18, 18, 18, 18);
                //endregion

                int count = 0;
                for (int i = 0; i < android.getSizeInventory(); i++) {
                    if (android.getStackInSlot(i) != null) {
                        drawAndroidPart(android.getStackInSlot(i), enabledColor, event.resolution.getScaledWidth() - getX(count), getY(count));
                        count++;
                    }
                }

                for (Object object : android.getUnlocked().func_150296_c()) {
                    IBionicStat stat = AndroidStatRegistry.getStat(object.toString());
                    if (stat != null) {
                        int level = android.getUnlockedLevel(stat);
                        if (stat.showOnHud(android, level)) {
                            if (stat.isEnabled(android,level))
                            {
                                if (stat.isActive(android,level))
                                {
                                    color = enabledColor;
                                }else
                                {
                                    color = disabledColor;
                                }
                            }else
                            {
                                color = Reference.COLOR_HOLO_RED;
                            }
                            drawBioticStat(stat, level, color, event.resolution.getScaledWidth() - getX(count), getY(count));
                            count++;
                        }
                    }
                }

                glAlphaFunc(GL_GREATER, 0.5f);
                glEnable(GL_ALPHA_TEST);
                glDisable(GL_BLEND);
                glPopMatrix();

                renderHurt(android,event);
            }else
            {
                if (android.isTurning())
                {
                    renderTransformAnimation(android,event);
                }
            }
        }
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
        if (hurtShader == null) {
            try {
                hurtShader = new ShaderGroup(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("shaders/post/deconverge.json"));
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindHurtShader()
    {
        if (hurtShader != null)
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

    public void renderGlitch(AndroidPlayer player,RenderGameOverlayEvent event)
    {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE);
        glColor3d(1,1,1);
        mc.renderEngine.bindTexture(glitch_tex);
        func_146110_a(0, 0, random.nextInt(1280), random.nextInt(720), event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
        glDisable(GL_BLEND);
    }

    public int getX(int count)
    {
        return 44 + (22 * (count % STATS_PER_ROW));
    }

    public int getY(int count)
    {
        return 23 + 22 * (count / STATS_PER_ROW);
    }

    public void drawAndroidPart(ItemStack stack,GuiColor color,int x,int y)
    {
        drawStatBG(color,x,y);
        glEnable(GL_BLEND);
        glColor4f(1,1,1,0.5f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.bindItemTexture(stack);
        RenderUtils.renderStack(x + 2,y + 2,stack);
        glDisable(GL_BLEND);
        //drawTexturedModelRectFromIcon(x + 1, y + 1, stack.getIconIndex(), 18, 18);
    }

    public void drawBioticStat(IBionicStat stat,int level,GuiColor color,int x,int y)
    {
        drawStatBG(color,x,y);
        glEnable(GL_BLEND);
        mc.renderEngine.bindTexture(stat.getIcon(level));
        func_146110_a(x + 1, y + 1, 0, 0, 18, 18, 18, 18);
        glDisable(GL_BLEND);
    }

    public void drawStatBG(GuiColor color,int x,int y)
    {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glColor3f(color.getFloatR(), color.getFloatG(), color.getFloatB());
        mc.renderEngine.bindTexture(icon_bg);
        func_146110_a(x, y, 0, 0, 20, 20, 20, 20);
        glDisable(GL_BLEND);
    }
}
