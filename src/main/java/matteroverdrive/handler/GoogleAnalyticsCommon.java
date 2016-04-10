package matteroverdrive.handler;

import com.brsanthu.googleanalytics.*;
import matteroverdrive.Reference;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 1/7/2016.
 */
public class GoogleAnalyticsCommon implements IConfigSubscriber
{
	public static final String EVENT_CATEGORY_BIOTIC_STATS = "Biotic Stats";
	public static final String EVENT_CATEGORY_MACHINES = "Machines";
	public static final String EVENT_CATEGORY_ENTITIES = "Entities";
	public static final String EVENT_CATEGORY_BLOCK_PLACEING = "Block Placing";
	public static final String TIMING_CATEGORY_MATTER_REGISTRY = "Matter Registry";
	public static final String EVENT_CATEGORY_ITEMS = "Items";
	public static final String EVENT_CATEGORY_QUESTS = "Quests";
	public static final String EVENT_CATEGORY_CONFIG = "Config";
	public static final String EVENT_ACTION_BIOTIC_STAT_UNLOCK = "Unlock";
	public static final String EVENT_ACTION_KILL = "Kill";
	public static final String EVENT_ACTION_REPLICATE = "Replicate";
	public static final String EVENT_ACTION_REPLICATION_FAIL = "Replication Fail";
	public static final String EVENT_ACTION_BIOTIC_STAT_USE = "Use";
	public static final String EVENT_ACTION_CRAFT_ITEMS = "Craft";
	public static final String EVENT_ACTION_PLAYER_DEATH = "Player Death";
	public static final String EVENT_ACTION_QUEST_COMPLETE = "Complete";
	public static final String EVENT_ACTION_QUEST_ABANDON = "Abandon";
	public static final String EVENT_ACTION_QUEST_ACCEPT = "Accept";
	public static final String EVENT_ACTION_VALUES = "Values";
	public static final String TIMING_VAR_MATTER_REGISTRY_CALCULATION = "Calculation";
	public static final String TIMING_VAR_MATTER_REGISTRY_SAVING_TO_DISK = "Saving To Disk";
	public static final String PAGE_PATH_GUIDE_ENTIRES = "Guide Entries";
	private static final String APP_ID = "UA-3322335-6";
	private final GoogleAnalyticsConfig config;
	protected com.brsanthu.googleanalytics.GoogleAnalytics googleAnalytics;
	private String lastScreen;

	public GoogleAnalyticsCommon()
	{
		config = new GoogleAnalyticsConfig();
		//config.setGatherStats(true);
	}

	public void sendEventHit(String category, String action, String label, Integer value, EntityPlayer entityPlayer)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new EventHit(category, action, label, value), entityPlayer));
		}
	}

	public void sendEventHit(String category, String action, String label, EntityPlayer entityPlayer)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new EventHit(category, action, label, null), entityPlayer));
		}
	}

	public void sendTimingHit(String category, String var, int time, EntityPlayer entityPlayer)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new TimingHit().userTimingCategory(category).userTimingVariableName(var).userTimingTime(time), entityPlayer));
		}
	}

	public void sendScreenHit(String screen, EntityPlayer entityPlayer)
	{
		if (googleAnalytics != null && !lastScreen.equals(screen))
		{
			googleAnalytics.postAsync(changeUserID((GoogleAnalyticsRequest)new GoogleAnalyticsRequest("screenview").contentDescription(screen), entityPlayer));
			lastScreen = screen;
		}
	}

	public void setExceptionHit(String e)
	{
		setExceptionHit(e, false);
	}

	public void setExceptionHit(Exception e)
	{
		setExceptionHit(e, false);
	}

	public void setExceptionHit(Exception e, boolean fatal)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new ExceptionHit(e.getMessage(), fatal), null));
		}
	}

	public void setExceptionHit(String e, boolean fatal)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new ExceptionHit(e, fatal), null));
		}
	}

	public void setPageHit(String pageTitle, String pagePath, EntityPlayer entityPlayer)
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.postAsync(changeUserID(new PageViewHit(null, pageTitle).documentPath(pagePath), entityPlayer));
		}
	}

	public GoogleAnalyticsRequest changeUserID(GoogleAnalyticsRequest request, EntityPlayer entityPlayer)
	{
		if (entityPlayer != null)
		{
			request.userId(EntityPlayer.getUUID(entityPlayer.getGameProfile()).toString());
		}
		return request;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		this.config.setEnabled(config.getBool("google_analytics", ConfigurationHandler.CATEGORY_SERVER, true, "Enable Google Analytics Anonymous Statistics Gathering"));
	}

	public void unload()
	{
		if (googleAnalytics != null)
		{
			googleAnalytics.close();
			googleAnalytics = null;
		}
	}

	public void load()
	{
		if (this.config.isEnabled() && FMLCommonHandler.instance().getSide() != Side.SERVER)
		{
			googleAnalytics = new com.brsanthu.googleanalytics.GoogleAnalytics(config, APP_ID, Reference.MOD_NAME, Reference.VERSION);
		}
	}
}
