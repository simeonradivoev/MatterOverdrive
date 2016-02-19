package matteroverdrive.data.matter_network;

import matteroverdrive.api.matter.IMatterDatabase;

/**
 * Created by Simeon on 1/30/2016.
 */
public class MatterDatabaseEvent
{
    public final IMatterDatabase database;

    public MatterDatabaseEvent(IMatterDatabase database)
    {
        this.database = database;
    }

    public static class Removed extends MatterDatabaseEvent
    {
        public Removed(IMatterDatabase database)
        {
            super(database);
        }
    }

    public static class Added extends MatterDatabaseEvent
    {
        public Added(IMatterDatabase database)
        {
            super(database);
        }
    }

    public static class PatternStorageChanged extends MatterDatabaseEvent implements IMatterNetworkEvent
    {
        public final int storageID;

        public PatternStorageChanged(IMatterDatabase database,int storageID)
        {
            super(database);
            this.storageID = storageID;
        }
    }

    public static class PatternChanged extends MatterDatabaseEvent implements IMatterNetworkEvent
    {
        public int patternStorageId;
        public int patternId;

        public PatternChanged(IMatterDatabase database,int patternStorageId,int patternId)
        {
            super(database);
            this.patternStorageId = patternStorageId;
            this.patternId = patternId;
        }
    }
}
