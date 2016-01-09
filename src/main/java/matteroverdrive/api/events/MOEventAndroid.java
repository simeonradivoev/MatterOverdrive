package matteroverdrive.api.events;

import matteroverdrive.api.android.IAndroid;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Simeon on 1/9/2016.
 */
public class MOEventAndroid extends PlayerEvent
{
    public final IAndroid android;

    public MOEventAndroid(IAndroid android)
    {
        super(android.getPlayer());
        this.android = android;
    }

    /**
     * Called when the Players star the android transformation.
     * When canceled, transformation never occurs.
     */
    public static class Transformation extends MOEventAndroid
    {
        public Transformation(IAndroid android)
        {
            super(android);
        }

        @Override
        public boolean isCancelable()
        {
            return true;
        }
    }
}
