package matteroverdrive.handler;

import com.brsanthu.googleanalytics.*;
import matteroverdrive.Reference;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 1/7/2016.
 */
public class GoogleAnalyticsCommon implements IConfigSubscriber
{
    private String lastScreen;
    private GoogleAnalyticsConfig config;
    private static final String APP_ID = "UA-3322335-6";
    protected com.brsanthu.googleanalytics.GoogleAnalytics googleAnalytics;
    public static final String EVENT_CATEGORY_BIOTIC_STATS = "Biotic Stats";
    public static final String EVENT_CATEGORY_MACHINES = "Machines";
    public static final String EVENT_CATEGORY_ENTITIES = "Entities";
    public static final String EVENT_CATEGORY_BLOCK_PLACEING = "Block Placing";
    public static final String TIMING_CATEGORY_MATTER_REGISTRY = "Matter Registry";
    public static final String EVENT_CATEGORY_ITEMS = "Items";

    public static final String EVENT_ACTION_BIOTIC_STAT_UNLOCK = "Unlock";
    public static final String EVENT_ACTION_KILL = "Kill";
    public static final String EVENT_ACTION_REPLICATE = "Replicate";
    public static final String EVENT_ACTION_BIOTIC_STAT_USE = "Use";
    public static final String EVENT_ACTION_CRAFT_ITEMS = "Craft";
    public static final String EVENT_ACTION_PLAYER_DEATH = "Player Death";

    public static final String TIMING_VAR_MATTER_REGISTRY_CALCULATION = "Calculation";
    public static final String TIMING_VAR_MATTER_REGISTRY_SAVING_TO_DISK = "Saving To Disk";

    public static final String PAGE_PATH_GUIDE_ENTIRES = "Guide Entries";

    public GoogleAnalyticsCommon()
    {
        config = new GoogleAnalyticsConfig();
        //config.setGatherStats(true);
    }

    public void sendEventHit(String category, String action, String label, Integer value, EntityPlayer entityPlayer)
    {
        if (googleAnalytics != null)
            googleAnalytics.postAsync(changeUserID(new EventHit(category,action,label,value),entityPlayer));
    }

    public void sendEventHit(String category,String action,String label,EntityPlayer entityPlayer)
    {
        if (googleAnalytics != null)
            googleAnalytics.postAsync(changeUserID(new EventHit(category,action,label,null),entityPlayer));
    }

    public void sendTimingHit(String category,String var,int time,EntityPlayer entityPlayer)
    {
        if (googleAnalytics != null)
            googleAnalytics.postAsync(changeUserID(new TimingHit().userTimingCategory(category).userTimingVariableName(var).userTimingTime(time),entityPlayer));
    }

    public void sendScreenHit(String screen,EntityPlayer entityPlayer)
    {
        if (googleAnalytics != null && lastScreen != screen)
        {
            googleAnalytics.postAsync(changeUserID((GoogleAnalyticsRequest) new GoogleAnalyticsRequest("screenview").contentDescription(screen), entityPlayer));
            lastScreen = screen;
        }
    }

    public void setPageHit(String pageTitle,String pagePath,EntityPlayer entityPlayer)
    {
        if (googleAnalytics != null)
            googleAnalytics.postAsync(changeUserID(new PageViewHit(null,pageTitle).documentPath(pagePath),entityPlayer));
    }

    public GoogleAnalyticsRequest changeUserID(GoogleAnalyticsRequest request,EntityPlayer entityPlayer)
    {
        if (entityPlayer != null)
        {
            request.userId(entityPlayer.func_146094_a(entityPlayer.getGameProfile()).toString());
        }
        return request;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        this.config.setEnabled(config.getBool("google_analytics",ConfigurationHandler.CATEGORY_SERVER,true,"Enable Google Analytics Anonymous Statistics Gathering"));
    }

    public void unload()
    {
        googleAnalytics.close();
        googleAnalytics = null;
    }

    public void load()
    {
        googleAnalytics = new com.brsanthu.googleanalytics.GoogleAnalytics(config,APP_ID, Reference.MOD_NAME,Reference.VERSION);
    }
}
