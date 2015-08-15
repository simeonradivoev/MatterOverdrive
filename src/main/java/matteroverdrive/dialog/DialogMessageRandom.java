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

package matteroverdrive.dialog;

import matteroverdrive.api.dialog.IDialogMessageSeedable;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 8/10/2015.
 */
public class DialogMessageRandom extends DialogMessage implements IDialogMessageSeedable
{
    public static Random random = new Random();
    protected long seed;
    protected List<String> messages;
    protected List<String> questions;


    public DialogMessageRandom()
    {
        super();
        init();
    }
    public DialogMessageRandom(String message, String question) {
        super(message, question);
        init();
    }

    public DialogMessageRandom(String message) {
        super(message);
        init();
    }

    private void init()
    {
        questions = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public DialogMessageRandom addQuestionVariation(String question)
    {
        questions.add(question);
        return this;
    }

    public DialogMessageRandom addQuestionVariation(String... questions)
    {
        for (String question : questions)
        {
            this.questions.add(question);
        }
        return this;
    }

    public DialogMessageRandom addMessageVariation(String message)
    {
        this.messages.add(message);
        return this;
    }

    public DialogMessageRandom addMessageVariation(String... messages)
    {
        for (String message : messages)
        {
            this.messages.add(message);
        }
        return this;
    }

    @Override
    public String getMessageText(IDialogNpc npc,EntityPlayer player)
    {
        if (messages.size() > 0) {
            random.setSeed(seed);
            return formatMessage(messages.get(random.nextInt(messages.size())),npc,player);
        }
        return message;
    }

    @Override
    public String getQuestionText(IDialogNpc npc,EntityPlayer player)
    {
        if (questions.size() > 0) {
            random.setSeed(seed);
            return formatQuestion(questions.get(random.nextInt(questions.size())),npc,player);
        }
        return question;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }

    @Override
    public DialogMessage loadMessageFromLocalization(String key)
    {
        addMessageVariation(MOStringHelper.translateToLocal(key).split(";"));
        return this;
    }
    @Override
    public DialogMessage loadQuestionFromLocalization(String key)
    {
        addQuestionVariation(MOStringHelper.translateToLocal(key).split(";"));
        return this;
    }
}
