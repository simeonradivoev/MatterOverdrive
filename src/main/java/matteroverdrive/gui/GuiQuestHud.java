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

import cofh.lib.audio.SoundBase;
import cofh.lib.gui.GuiColor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import matteroverdrive.Reference;
import matteroverdrive.data.quest.QuestStack;
import matteroverdrive.util.animation.MOAnimationTimeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 11/20/2015.
 */
public class GuiQuestHud
{
    private static final float QUEST_NOTIFICATION_TIME = 400;
    private static final float QUEST_FADE_IN_TIME = 30;
    private static final float QUEST_FADE_OUT_TIME = 60;
    private static final float OBJECTIVES_NOTIFICATION_TIME = 200;
    private static final float OBJECTIVES_FADE_TIME = 20;
    private Queue<QuestStack> completedQuestQueue;
    private Queue<QuestStack> startedQuestQueue;
    private Queue<QuestStack[]> objectivesChangedQueue;
    private String completeQuestName;
    private int completeQuestXp;
    private String newQuestName;
    private String[] objectivesChanged;
    private MOAnimationTimeline completeQuestTimeline;
    private MOAnimationTimeline startedQuestTimeline;
    private MOAnimationTimeline objectivesTimeline;

    public GuiQuestHud()
    {
        completedQuestQueue = new ArrayDeque<>();
        startedQuestQueue = new ArrayDeque<>();
        objectivesChangedQueue = new ArrayDeque<>();

        completeQuestTimeline = new MOAnimationTimeline(QUEST_NOTIFICATION_TIME,false,false,1);
        completeQuestTimeline.setAutoLength(true);
        completeQuestTimeline.addSlice(new MOAnimationTimeline.Slice(0,1,0,QUEST_FADE_IN_TIME,new MOAnimationTimeline.Easing.QuadEaseIn()));
        completeQuestTimeline.addSlice(new MOAnimationTimeline.Slice(1,1,QUEST_FADE_IN_TIME,QUEST_NOTIFICATION_TIME-QUEST_FADE_OUT_TIME,null));
        completeQuestTimeline.addSlice(new MOAnimationTimeline.Slice(1,0,QUEST_NOTIFICATION_TIME-QUEST_FADE_OUT_TIME,QUEST_FADE_OUT_TIME,new MOAnimationTimeline.Easing.QuadEaseOut()));

        startedQuestTimeline = new MOAnimationTimeline(QUEST_NOTIFICATION_TIME,false,false,1);
        startedQuestTimeline.setAutoLength(true);
        startedQuestTimeline.addSlice(new MOAnimationTimeline.Slice(0,1,0,QUEST_FADE_IN_TIME,new MOAnimationTimeline.Easing.QuadEaseIn()));
        startedQuestTimeline.addSlice(new MOAnimationTimeline.Slice(1,1,QUEST_FADE_IN_TIME,QUEST_NOTIFICATION_TIME-QUEST_FADE_OUT_TIME,null));
        startedQuestTimeline.addSlice(new MOAnimationTimeline.Slice(1,0,QUEST_NOTIFICATION_TIME-QUEST_FADE_OUT_TIME,OBJECTIVES_FADE_TIME,new MOAnimationTimeline.Easing.QuadEaseOut()));

        objectivesTimeline = new MOAnimationTimeline(OBJECTIVES_NOTIFICATION_TIME,false,false,1);
        objectivesTimeline.setAutoLength(true);
        objectivesTimeline.addSlice(new MOAnimationTimeline.Slice(0,1,0,OBJECTIVES_FADE_TIME,new MOAnimationTimeline.Easing.QuadEaseIn()));
        objectivesTimeline.addSlice(new MOAnimationTimeline.Slice(1,1,QUEST_FADE_IN_TIME,QUEST_NOTIFICATION_TIME-QUEST_FADE_OUT_TIME,null));
        objectivesTimeline.addSlice(new MOAnimationTimeline.Slice(1,0,OBJECTIVES_NOTIFICATION_TIME-OBJECTIVES_FADE_TIME,OBJECTIVES_FADE_TIME,new MOAnimationTimeline.Easing.QuadEaseOut()));
    }

    @SubscribeEvent()
    public void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        //glEnable(GL_BLEND);
        if (event.type.equals(RenderGameOverlayEvent.ElementType.ALL))
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

            if (completeQuestTimeline.isPlaying() && completeQuestName != null)
            {
                float time = completeQuestTimeline.getCurrentValue();
                GuiColor color = new GuiColor(Reference.COLOR_HOLO.getIntR(), Reference.COLOR_HOLO.getIntG(), Reference.COLOR_HOLO.getIntB(), 20 + (int) (235f * time));
                glPushMatrix();
                int y = (int)(event.resolution.getScaledHeight() * 0.15);
                int titleWidth = (int) (fontRenderer.getStringWidth(completeQuestName) * 1.5);
                glTranslated(event.resolution.getScaledWidth() - titleWidth - 30 - time * 30, y - 20, 0);
                glScaled(1.5, 1.5, 1.5);
                fontRenderer.drawStringWithShadow(EnumChatFormatting.BOLD + completeQuestName, 0, 40, color.getColor());
                glPopMatrix();
                fontRenderer.drawStringWithShadow("Completed:", event.resolution.getScaledWidth() - titleWidth - 20 - (int) (time * 40), y + 28, color.getColor());
                if (completeQuestXp > 0) {
                    fontRenderer.drawStringWithShadow(String.format("+%dxp", (int) (time * completeQuestXp)), event.resolution.getScaledWidth() - 50 - (int) (20 * time), y + 58, color.getColor());
                }
            }
            if (startedQuestTimeline.isPlaying() && newQuestName != null)
            {
                float time = startedQuestTimeline.getCurrentValue();
                GuiColor color = new GuiColor(Reference.COLOR_HOLO.getIntR(), Reference.COLOR_HOLO.getIntG(), Reference.COLOR_HOLO.getIntB(), 20 + (int) (235f * time));
                glPushMatrix();
                int y = (int)(event.resolution.getScaledHeight() * 0.65);
                String title = "Dead! We're all dead!";
                glTranslated(-10 + time * 30, y, 0);
                glScaled(1.5, 1.5, 1.5);
                fontRenderer.drawStringWithShadow(EnumChatFormatting.BOLD + newQuestName, 0, 0, color.getColor());
                glPopMatrix();
                fontRenderer.drawStringWithShadow("Started:",(int) (time * 20), y-12, color.getColor());
            }
            if (objectivesTimeline.isPlaying() && objectivesChanged != null)
            {
                float time = objectivesTimeline.getCurrentValue();
                GuiColor color = new GuiColor(Reference.COLOR_HOLO.getIntR(), Reference.COLOR_HOLO.getIntG(), Reference.COLOR_HOLO.getIntB(), 20 + (int) (235f * time));
                int objectivesY = 0;
                for (int i = 0;i < objectivesChanged.length;i++)
                {
                    if (objectivesChanged[i] != null) {
                        fontRenderer.drawStringWithShadow(String.format("[ %s ]", objectivesChanged[i]), (int) (time * 20), (int) (event.resolution.getScaledHeight() * 0.5) + objectivesY, color.getColor());
                        objectivesY += fontRenderer.FONT_HEIGHT + 2;
                    }
                }
            }

            startedQuestTimeline.tick(event.partialTicks);
            completeQuestTimeline.tick(event.partialTicks);
            objectivesTimeline.tick(event.partialTicks);
        }
    }

    public void onTick() {
        if (!startedQuestQueue.isEmpty() && !startedQuestTimeline.isPlaying()) {
            startNewQuestNotification(startedQuestQueue.poll());
        }
        if (!completedQuestQueue.isEmpty() && !completeQuestTimeline.isPlaying()) {
            startCompleteQuestNotification(completedQuestQueue.poll());
        }
        if (!objectivesChangedQueue.isEmpty() && !objectivesTimeline.isPlaying()) {
            QuestStack[] questStacks = objectivesChangedQueue.poll();
            startObjectivesChanged(questStacks[0], questStacks[1]);
        }
    }

    public void startCompleteQuestNotification(QuestStack questStack)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SoundBase(new ResourceLocation(Reference.MOD_ID + ":" + "quest_complete"),1,1));
        if (questStack != null) {
            completeQuestName = questStack.getTitle(Minecraft.getMinecraft().thePlayer);
            //addObjectivesChanged(questStack);
        }
        else
        {
            completeQuestName = "Test. This is a test";
            completeQuestXp = 256;
        }

        completeQuestTimeline.getSlice(1).setLength(completeQuestName.length() * 15);
        completeQuestTimeline.sort();
        completeQuestTimeline.replay();
    }

    public void startNewQuestNotification(QuestStack questStack)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SoundBase(new ResourceLocation(Reference.MOD_ID + ":" + "quest_started"),1,1));
        if (questStack != null)
        {
            newQuestName = questStack.getTitle(Minecraft.getMinecraft().thePlayer);
        }else
        {
            newQuestName = "Test. This is a test";
        }

        startedQuestTimeline.getSlice(1).setLength(newQuestName.length() * 15);
        startedQuestTimeline.sort();
        startedQuestTimeline.replay();
    }

    public void startObjectivesChanged(QuestStack oldQuestStack,QuestStack newQuestStack)
    {
        int showTime = 0;
        if (newQuestStack != null)
        {
            int objectiveCount = newQuestStack.getObjectivesCount(Minecraft.getMinecraft().thePlayer);
            objectivesChanged = new String[objectiveCount];
            for (int i = 0;i < objectiveCount;i++)
            {
                String newObjective = newQuestStack.getObjective(Minecraft.getMinecraft().thePlayer,i);
                String oldObjective = oldQuestStack.getObjective(Minecraft.getMinecraft().thePlayer,i);
                if (!newObjective.contentEquals(oldObjective))
                {
                    objectivesChanged[i] = newObjective;
                    showTime = Math.max(showTime,objectivesChanged[i].length() * 4);
                }
            }

        }
        else
        {
            objectivesChanged = new String[]{"Objectives changed 0/5","Objectives changed 0/5"};
        }

        objectivesTimeline.getSlice(1).setLength(showTime);
        objectivesTimeline.sort();
        objectivesTimeline.replay();
    }

    public void addCompletedQuest(QuestStack questStack)
    {
        completedQuestQueue.add(questStack);
    }

    public void addStartedQuest(QuestStack questStack)
    {
        startedQuestQueue.add(questStack);
    }

    public void addObjectivesChanged(QuestStack oldQuestStack,QuestStack newQestStack)
    {
        objectivesChangedQueue.add(new QuestStack[]{oldQuestStack,newQestStack});
        if (objectivesTimeline.getTime() < OBJECTIVES_NOTIFICATION_TIME-OBJECTIVES_FADE_TIME)
            objectivesTimeline.setTime(OBJECTIVES_NOTIFICATION_TIME-OBJECTIVES_FADE_TIME);
    }
}
