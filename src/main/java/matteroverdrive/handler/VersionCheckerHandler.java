package matteroverdrive.handler;

import matteroverdrive.Reference;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import com.google.gson.Gson;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Simeon on 5/7/2015.
 */
public class VersionCheckerHandler implements IConfigSubscriber {
    private boolean updateInfoDisplayed = false;
    public Future<String> download;
    public static final String[] mirrors = new String[]{Reference.VERSIONS_FILE_URL, Reference.VERSIONS_FILE_URL_MIRROR};
    private int currentMirror = 0;
    int lastPoll = 400;
    private boolean checkForUpdates;
    ExecutorService threadPool;

    public VersionCheckerHandler() {
        threadPool = Executors.newFixedThreadPool(2);
    }

    //Called when a player ticks.
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !checkForUpdates) {
            return;
        }

        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
            if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(event.player.getGameProfile())) {
                return;
            }
        }

        if (lastPoll > 0) {
            --lastPoll;
            return;
        }
        lastPoll = 400;

        if (updateInfoDisplayed)
            return;

        if (currentMirror < mirrors.length) {
            if (download == null) {
                download = threadPool.submit(new VersionCheckThread(mirrors[currentMirror]));
                currentMirror++;
            }
        }

        if (download != null && download.isDone()) {
            String result = null;

            try {
                result = download.get();
            } catch (InterruptedException e) {
                MOLog.log(Level.ERROR, e, "Version Checking from '%1$s' was interrupted",mirrors[currentMirror - 1]);
            } catch (ExecutionException e) {
                MOLog.log(Level.ERROR, e, "Version Checking from '%1$s' has failed",mirrors[currentMirror-1]);
            } finally {
                if (result != null)
                {
                    updateInfoDisplayed = constructVersionAndCheck(result, event.player);
                }

                download.cancel(false);
                download = null;
            }
        }
    }

    private static class VersionCheckThread implements Callable<String>
    {
        String url;

        public VersionCheckThread(String url)
        {
            this.url = url;
        }

        @Override
        public String call() throws Exception
        {
            return readFromUrl(url);
        }

        private String readFromUrl(String url) throws IOException {
            URL orlObj = new URL(url);
            InputStream inputStream = orlObj.openStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            return readAll(rd);
        }

        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
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

    public boolean constructVersionAndCheck(String jsonText,EntityPlayer player) {
        Gson gson = new Gson();
        Versions versions = gson.fromJson(jsonText, Versions.class);
        if (versions.versions.size() > 0) {
            Version version = versions.versions.get(0);

            if (version != null) {
                if (!version.version.equals(Reference.VERSION)) {
                    ChatComponentText chat = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.WHITE + MOStringHelper.translateToLocal("alert.new_update"));
                    ChatStyle style = new ChatStyle();
                    player.addChatMessage(chat);

                    chat = new ChatComponentText("");
                    chat.appendText(EnumChatFormatting.AQUA + "Matter Overdrive " + version.version + " ");
                    chat.appendText(EnumChatFormatting.WHITE + "[");
                    style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, version.url));
                    style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("info." + Reference.MOD_ID + ".updater.hover").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
                    style.setColor(EnumChatFormatting.GREEN);
                    chat.appendSibling(new ChatComponentTranslation("info." + Reference.MOD_ID + ".updater.download")).setChatStyle(style);
                    chat.appendText(EnumChatFormatting.WHITE + "]");
                    player.addChatMessage(chat);

                    chat = new ChatComponentText(version.changes);
                    style = new ChatStyle();
                    style.setColor(EnumChatFormatting.GRAY);
                    chat.setChatStyle(style);
                    player.addChatMessage(chat);
                    return true;

                } else {
                    MOLog.log(Level.INFO, "Matter Overdrive Version %1$s is up to date. From '%2$s'",version.version,mirrors[currentMirror-1]);
                }
            }
        }
        return false;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        String comment = "Should Matter Overdrive check for newer versions, every time the world starts";
        checkForUpdates = config.getBool(ConfigurationHandler.KEY_VERSION_CHECK, ConfigurationHandler.CATEGORY_CLIENT,true,comment);
        config.config.get(ConfigurationHandler.CATEGORY_CLIENT, ConfigurationHandler.KEY_VERSION_CHECK,true).comment = comment;
    }
}
