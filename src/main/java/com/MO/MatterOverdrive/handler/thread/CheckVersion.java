package com.MO.MatterOverdrive.handler.thread;

import com.MO.MatterOverdrive.Reference;
import com.google.gson.Gson;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Simeon on 5/7/2015.
 */
public class CheckVersion implements Runnable {

    public static final String URL_MAIN = "http://simeon.co.vu/Mods/MatterOverdrive/Versions.txt";
    public static final String URL_MIRROR = "https://raw.githubusercontent.com/simeonradivoev/MatterOverdrive/master/Versions.txt";
    EntityPlayer player;

    public CheckVersion(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    public void run()
    {
        try {
            readFromUrl(URL_MAIN);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Trying to get versions from mirror");

            try {
                readFromUrl(URL_MIRROR);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    static class Version
    {
        String version;
        String changes;
        String type;
        String date;
        String url;
    }

    static class Versions
    {
        List<Version> versions;
    }

    private void readFromUrl(String url) throws IOException {
        InputStream inputStream = new URL(URL_MAIN).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        Gson gson = new Gson();
        Versions versions = gson.fromJson(jsonText,Versions.class);
        if (versions.versions.size() > 0)
        {
            Version version = versions.versions.get(0);

            if (version != null)
            {
                if (!version.version.equals(Reference.VERSION))
                {
                    ChatComponentText chat = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.WHITE+ "A new version is available!");
                    ChatStyle style = new ChatStyle();
                    player.addChatMessage(chat);

                    chat = new ChatComponentText("");
                    chat.appendText(EnumChatFormatting.AQUA + "Matter Overdrive " + version.version + " ");
                    chat.appendText(EnumChatFormatting.WHITE + "[");
                    style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, version.url));
                    style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ChatComponentTranslation("info." + Reference.MOD_ID + ".updater.hover").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
                    style.setColor(EnumChatFormatting.GREEN);
                    chat.appendSibling(new ChatComponentTranslation("info." + Reference.MOD_ID + ".updater.download")).setChatStyle(style);
                    chat.appendText(EnumChatFormatting.WHITE + "]");
                    player.addChatMessage(chat);

                    chat = new ChatComponentText(version.changes);
                    style = new ChatStyle();
                    style.setColor(EnumChatFormatting.GRAY);
                    chat.setChatStyle(style);
                    player.addChatMessage(chat);

                }
            }
        }
    }
}
