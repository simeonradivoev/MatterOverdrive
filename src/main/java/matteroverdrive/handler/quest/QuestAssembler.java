package matteroverdrive.handler.quest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matteroverdrive.Reference;
import matteroverdrive.api.exceptions.MOQuestParseException;
import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.data.quest.GenericMultiQuest;
import matteroverdrive.data.quest.GenericQuest;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.data.quest.logic.*;
import matteroverdrive.data.quest.rewards.EntityReward;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.data.quest.rewards.QuestStackReward;
import matteroverdrive.data.quest.rewards.SoundReward;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 1/25/2016.
 */
public class QuestAssembler
{
    private static final ResourceLocation questsLocation = new ResourceLocation(Reference.MOD_ID + ":" + "quests/quests.json");
    private static final String customQuestsLocation = "custom_quests.json";
    private Map<String,Class<? extends IQuestLogic>> questLogicClassMap;
    private Map<String,Class<? extends IQuestReward>> questRewardClassMap;
    private String currentQuestName;

    public QuestAssembler()
    {
        this.questLogicClassMap = new HashMap<>();
        this.questRewardClassMap = new HashMap<>();
        loadBasicQuestLogics();
        loadBasicQuestRewards();
    }

    protected void loadBasicQuestLogics()
    {
        addQuestLogicClass("kill_creature", QuestLogicKillCreature.class);
        addQuestLogicClass("collect_item", QuestLogicCollectItem.class);
        addQuestLogicClass("craft", QuestLogicCraft.class);
        addQuestLogicClass("mine", QuestLogicMine.class);
        addQuestLogicClass("event_trigger", QuestLogicSingleEvent.class);
        addQuestLogicClass("teleport",QuestLogicTeleport.class);
        addQuestLogicClass("place",QuestLogicPlaceBlock.class);
        addQuestLogicClass("scan",QuestLogicScanBlock.class);
        addQuestLogicClass("interact_block",QuestLogicBlockInteract.class);
        addQuestLogicClass("interact_item",QuestLogicItemInteract.class);
        addQuestLogicClass("conversation",QuestLogicConversation.class);
    }

    protected void loadBasicQuestRewards()
    {
        addQuestRewardClass("item", ItemStackReward.class);
        addQuestRewardClass("quest",QuestStackReward.class);
        addQuestRewardClass("entity", EntityReward.class);
        addQuestRewardClass("sound", SoundReward.class);
    }

    public void loadQuests(Quests quests)
    {
        String path = "/assets/"+questsLocation.getResourceDomain()+"/" + questsLocation.getResourcePath();
        InputStream inputStream = QuestAssembler.class.getResourceAsStream(path);
        loadQuests(inputStream,quests);
    }

    public void loadCustomQuests(Quests quests)
    {
        File file = new File(customQuestsLocation);
        if(file.exists())
        {
            try
            {
                FileInputStream inputStream = new FileInputStream(file);
                loadQuests(inputStream,quests);
            } catch (FileNotFoundException e)
            {
                MOLog.log(Level.ERROR,e,"Cannot find Custom Quests file at %s.",file.getAbsolutePath());
            }
        }else
        {
            MOLog.info("No Custom Quests file found at %s",file.getAbsolutePath());
        }
    }

    private void loadQuests(InputStream inputStream,Quests quests)
    {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject mainQuestsObject = jsonParser.parse(inputStreamReader).getAsJsonObject();
        mainQuestsObject.entrySet().stream().filter(entry -> entry.getValue().isJsonObject()).forEach(entry -> {
            currentQuestName = entry.getKey();
            GenericQuest quest = parseQuest(entry.getKey(), entry.getValue().getAsJsonObject());
            if (quest != null)
            {
                quests.registerQuest(quest.getName(), quest);
                int contractWeight = MOJsonHelper.getInt(entry.getValue().getAsJsonObject(), "contract_market", 0);
                if (contractWeight > 0)
                {
                    MatterOverdriveQuests.contractGeneration.add(new WeightedRandomQuest(quest, contractWeight));
                }
            }
        });
    }

    private GenericQuest parseQuest(String title,JsonObject object)
    {
        GenericQuest genericQuest = null;
        MOJsonHelper.setCurrentParentObject(title);
        String type = MOJsonHelper.getString(object,"type");
        if (type.equalsIgnoreCase("generic"))
        {
            if (object.has("logic") && object.get("logic").isJsonObject())
            {
                IQuestLogic questLogic = parseLogic(object.get("logic").getAsJsonObject());
                if (questLogic != null)
                {
                    genericQuest = new GenericQuest(title,object, questLogic);
                }else
                {
                    throw new MOQuestParseException("There was a problem while parsing Quest logic for: '%s'",title);
                }
            }else
            {
                throw new MOQuestParseException("Missing Quest Logic for Quest: '%s'",title);
            }
        }
        else if (type.equalsIgnoreCase("generic_multi"))
        {
            IQuestLogic[] logics;
            if (object.has("logic") && object.get("logic").isJsonArray())
            {
                JsonArray logicElements = object.getAsJsonArray("logic");
                logics = new IQuestLogic[logicElements.size()];

                for (int i = 0;i < logicElements.size();i++)
                {
                    IQuestLogic questLogic = parseLogic(logicElements.get(i).getAsJsonObject());
                    if (questLogic != null)
                    {
                        logics[i] = questLogic;
                    }else throw new MOQuestParseException("There was a problem parsing one of the Quest logics in: '%s'",title);
                }
            }else
            {
                throw new MOQuestParseException("Missing Quest Logic for Quest: '%s'",title);
            }

            genericQuest = new GenericMultiQuest(title,object,logics);
        }

        if (genericQuest != null)
        {
            if (object.has("rewards") && object.get("rewards").isJsonArray())
            {
                genericQuest.addQuestRewards(parseRewards(object.get("rewards").getAsJsonArray()));
            }
        }

        return genericQuest;
    }

    private IQuestLogic parseLogic(JsonObject object)
    {
        String type = MOJsonHelper.getString(object,"type");

        Class<? extends IQuestLogic> logicClass = questLogicClassMap.get(type);
        if (logicClass != null)
        {
            try
            {
                IQuestLogic logic = logicClass.newInstance();
                logic.loadFromJson(object);
                return logic;
            } catch (InstantiationException e)
            {
                MOLog.log(Level.ERROR,e,"Could not create Quest Logic with default constructor");
            } catch (IllegalAccessException e)
            {
                MOLog.log(Level.ERROR,e,"Could not create Quest Logic with private constructor");
            }
        }
        MOLog.log(Level.ERROR,"Could not find quest logic of type: %s",type);
        return null;
    }

    public List<IQuestReward> parseRewards(JsonArray rewards)
    {
        List<IQuestReward> rewardList = new ArrayList<>();

        for (JsonElement element : rewards)
        {
            if (element.isJsonObject())
            {
                String type;
                try
                {
                    type = element.getAsJsonObject().get("type").getAsString();
                }catch (Exception e)
                {
                    throw new MOQuestParseException(String.format("Could not parse reward type in quest: '%s'",currentQuestName),e);
                }
                Class<? extends IQuestReward> rewardClass = questRewardClassMap.get(type);
                if (rewardClass != null)
                {
                    try
                    {
                        IQuestReward reward = rewardClass.newInstance();
                        reward.loadFromJson(element.getAsJsonObject());
                        rewardList.add(reward);
                    }
                    catch (InstantiationException e)
                    {
                        MOLog.log(Level.ERROR,e,"Could not instantiate Quest Reward");
                    } catch (IllegalAccessException e)
                    {
                        MOLog.log(Level.ERROR,e,"Could not call private constructor on Quest Reward");
                    }
                }else
                {
                    throw new MOQuestParseException(String.format("No such reward type as: '%s' in quest: '%s'",type,currentQuestName));
                }
            }
        }

        return rewardList;
    }

    public void addQuestLogicClass(String name,Class<? extends IQuestLogic> questLogicClass)
    {
        this.questLogicClassMap.put(name,questLogicClass);
    }

    public void addQuestRewardClass(String name,Class<? extends IQuestReward> questRewardClass)
    {
        this.questRewardClassMap.put(name,questRewardClass);
    }
}
