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

package matteroverdrive.guide;

import cofh.api.energy.IEnergyContainerItem;
import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Simeon on 8/31/2015.
 */
public abstract class GuideElementTextAbstract extends GuideElementAbstract
{
    private static String shortcodePattern = "\\[(.*?)\\]";
    private static String shortcodePatternSplitter = "((?<="+shortcodePattern+")|(?="+shortcodePattern+"))";

    @Override
    public void loadElement(MOGuideEntry entry, Element element,Map<String,String> styleSheetMap,int width,int height)
    {
        getFontRenderer().setUnicodeFlag(true);
        Map<String,String> styleMap = buildStyleMap(styleSheetMap, element);
        loadStyles(entry,element,styleMap,width,height);
        this.width = calculateWidth(styleMap,width);
        loadContent(entry, element, width, height);
        calculateDimentions(entry,element,styleMap,width,height);
        getFontRenderer().setUnicodeFlag(false);
    }

    protected List<TextLine> handleTextFormatting(MOGuideEntry entry,String text,int width)
    {
        List<Object> shortCodeSplits = new ArrayList<>();
        Matcher matcher = Pattern.compile(shortcodePattern).matcher(text);
        int lastEnd = 0;
        while (matcher.find())
        {
            shortCodeSplits.add(text.substring(lastEnd,matcher.start()));
            String shortcode = matcher.group();
            shortCodeSplits.add(handleShortCode(decodeShortcode(shortcode)));
            lastEnd = matcher.end();
        }

        shortCodeSplits.add(text.substring(lastEnd, text.length()));

        List<TextChunk> textChunks = new ArrayList<>();
        for (Object o : shortCodeSplits)
        {
            if (o instanceof String)
            {
                for (String s : ((String) o).split(" "))
                {
                    if (!s.isEmpty()) {
                        textChunks.add(new TextChunk(handleVariables(s.trim(), entry), getFontRenderer()));
                    }
                }
            }
            else if (o instanceof TextChunk)
            {
                textChunks.add((TextChunk)o);
            }
        }

        List<TextLine> lines = new ArrayList<>();
        TextLine line = new TextLine();
        lines.add(line);
        for (int i = 0; i < textChunks.size();i++)
        {
            int w = calculateWidth(null,textChunks.get(i),null);
            if (i > 0 && i < textChunks.size()-1)
            {
                w = calculateWidth(textChunks.get(i-1),textChunks.get(i),textChunks.get(i+1));
            }

            if (line.getWidth() + w > width)
            {
                line = new TextLine();
                lines.add(line);
            }

            line.addChunk(textChunks.get(i));
        }
        return lines;
    }

    protected TextChunk handleShortCode(Map<String,String> shortcodeMap)
    {
        if (shortcodeMap.get("type").equalsIgnoreCase("block") || shortcodeMap.get("type").equalsIgnoreCase("item"))
        {
            ItemStack stack = shortCodeToStack(shortcodeMap);
            if (stack != null && stack.getItem() != null)
            {
                String guideName = shortcodeMap.containsKey("guide") ? shortcodeMap.get("guide") : null;
                int guidePage = shortcodeMap.containsKey("page") ? Integer.parseInt(shortcodeMap.get("page")) : 0;
                return new ItemstackTextLinkChunk(ChatFormatting.GREEN + stack.getDisplayName() + ChatFormatting.RESET,getFontRenderer(),stack,guideName,guidePage);
            }
        }
        else if(shortcodeMap.get("type").equalsIgnoreCase("rf"))
        {
            if (shortcodeMap.containsKey("itemType"))
                shortcodeMap.put("type",shortcodeMap.get("itemType"));

            ItemStack stack = shortCodeToStack(shortcodeMap);
            if (stack != null && stack.getItem() != null && stack.getItem() instanceof IEnergyContainerItem)
            {
                return new TextChunk(((IEnergyContainerItem) stack.getItem()).getMaxEnergyStored(stack) + MOEnergyHelper.ENERGY_UNIT,getFontRenderer());
            }
        }
        else if (shortcodeMap.get("type").equalsIgnoreCase("guide"))
        {
            MOGuideEntry entry = MatterOverdriveGuide.findGuide(shortcodeMap.get("name"));
            if (entry != null)
            {
                int page = shortcodeMap.containsKey("page") ? Integer.parseInt(shortcodeMap.get("page")) : 0;
                return new GuideTextLinkChunk(ChatFormatting.YELLOW + entry.getDisplayName() + ChatFormatting.RESET,getFontRenderer(),entry,page);
            }
        }

        return null;
    }

    protected String handleVariables(String text,MOGuideEntry entry)
    {
        text = text.replace("$itemName",formatVariableReplace("$itemName",((entry.getStackIcons().length > 0 && entry.getStackIcons()[0] != null) ? entry.getStackIcons()[0].getDisplayName() : entry.getDisplayName())));
        return text;
    }

    protected String formatVariableReplace(String variable,String replace)
    {
        return replace;
    }

    protected int calculateWidth(TextChunk before,TextChunk main,TextChunk after)
    {
        if (after != null && (after.getText().matches("[.,!\"')}]")))
        {
            return main.getWidth();
        }
        if (main.getText().matches("[({]"))
        {
            return main.getWidth();
        }
        else
        {
            return main.getWidth() + getFontRenderer().getCharWidth(' ');
        }
    }

    protected class TextLine
    {
        List<TextChunk> chunks;

        public TextLine()
        {
            this.chunks = new ArrayList<>();
        }

        public void addChunk(TextChunk chunk)
        {
            chunks.add(chunk);
        }

        public int getWidth()
        {
            int width = 0;
            for (TextChunk chunk : chunks)
            {
                width += chunk.getWidth() + getFontRenderer().getCharWidth(' ');
            }
            return width;
        }
    }

    public class TextChunk
    {
        String text;
        int width;

        public TextChunk(String text,FontRenderer fontRenderer)
        {
            this.text = text;
            width = fontRenderer.getStringWidth(text);
        }

        public int getWidth()
        {
            return width;
        }

        public String getText(){return text;}
    }

    public abstract class TextChunkLink extends TextChunk
    {
        public TextChunkLink(String text,FontRenderer fontRenderer)
        {
            super(text,fontRenderer);
        }

        public abstract void onClick(GuiDataPad guiDataPad);
    }

    public class GuideTextLinkChunk extends TextChunkLink
    {
        MOGuideEntry entry;
        int page;

        public GuideTextLinkChunk(String text,FontRenderer fontRenderer,MOGuideEntry entry,int page) {
            super(text,fontRenderer);
            this.entry = entry;
            this.page = page;
        }

        @Override
        public void onClick(GuiDataPad guiDataPad) {
            ((GuiDataPad) gui).getGuideDescription().OpenGuide(entry.getId(),page,true);
        }
    }

    public class ItemstackTextLinkChunk extends TextChunkLink
    {
        String guideEntryName;
        int page;
        ItemStack stack;

        public ItemstackTextLinkChunk(String text,FontRenderer fontRenderer,ItemStack stack) {
            super(text,fontRenderer);
            this.stack = stack;
        }

        public ItemstackTextLinkChunk(String text,FontRenderer fontRenderer,ItemStack stack,String guideEntryName,int page) {
            super(text,fontRenderer);
            this.stack = stack;
            this.guideEntryName = guideEntryName;
            this.page = page;
        }

        @Override
        public void onClick(GuiDataPad guiDataPad)
        {
            MOGuideEntry entry;
            if (guideEntryName != null)
            {
                entry = MatterOverdriveGuide.findGuide(guideEntryName);
            }else
            {
                entry = MatterOverdriveGuide.findGuide(stack.getUnlocalizedName());
            }
            if (entry != null)
            {
                ((GuiDataPad) gui).getGuideDescription().OpenGuide(entry.getId(),page,true);
            }
        }
    }

}
