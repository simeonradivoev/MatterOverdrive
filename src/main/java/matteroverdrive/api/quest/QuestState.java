package matteroverdrive.api.quest;

/**
 * Created by Simeon on 3/1/2016.
 */
public class QuestState
{
    private final Type type;
    private final int[] objectiveIds;
    private final boolean showOnHud;

    public QuestState(Type type, int[] objectiveIds, boolean showOnHud)
    {
        this.type = type;
        this.objectiveIds = objectiveIds;
        this.showOnHud = showOnHud;
    }

    public int[] getObjectiveIds()
    {
        return objectiveIds;
    }

    public boolean isShowOnHud()
    {
        return showOnHud;
    }

    public Type getType()
    {
        return type;
    }

    public enum Type
    {
        COMPLETE,
        UPDATE
    }
}
