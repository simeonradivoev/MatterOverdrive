package matteroverdrive.api.quest;

/**
 * Created by Simeon on 3/1/2016.
 */
public class QuestLogicState
{
    private final QuestState.Type type;
    private final boolean showOnHud;

    public QuestLogicState(QuestState.Type type,boolean showOnHud)
    {
        this.type = type;
        this.showOnHud = showOnHud;
    }

    public QuestState.Type getType()
    {
        return type;
    }

    public boolean isShowOnHud()
    {
        return showOnHud;
    }
}
