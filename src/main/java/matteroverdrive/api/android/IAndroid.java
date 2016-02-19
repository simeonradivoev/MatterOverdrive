package matteroverdrive.api.android;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Created by Simeon on 1/9/2016.
 */
public interface IAndroid extends IInventory, IExtendedEntityProperties
{
    String EXT_PROP_NAME = "AndroidPlayer";

    /**
     * Checks if the Player is an Android
     * @return is the player an Android.
     */
    boolean isAndroid();

    /**
     * Checks if a given stat with a given level is unlocked by the Android Player.
     * @param stat the Bionic stat.
     * @param level the level of the bionic stat.
     * @return returns true if the given bionic stat at the given level is unlocked.
     */
    boolean isUnlocked(IBioticStat stat, int level);

    /**
     * Gets the unlocked level for a given biotic stat.
     * returns 0 if the bionic stat is not unlocked.
     * @param stat the bionic stat.
     * @return returns the unlocked level of the given bionic stat. Returns 0 if the stat is not unlocked.
     */
    int getUnlockedLevel(IBioticStat stat);

    /**
     * Is the Android Player currently turning into an Android.
     * This is true while the transformation music and animation are playing.
     * @return
     */
    boolean isTurning();

    /**
     * Gets the player.
     * @return the player.
     */
    EntityPlayer getPlayer();

    /**
     * Gets the currently active/selected biotic stat.
     * The active stat is chosen by the player trough the ability wheel.
     * Return null if there is no active stat.
     * @return the active/selected biotic stat.
     */
    IBioticStat getActiveStat();

    void onEffectsUpdate(int effectId);

    //region Helper function
    static IAndroid getAndroid(EntityPlayer entityPlayer)
    {
        if (entityPlayer != null)
            return (IAndroid)entityPlayer.getExtendedProperties(EXT_PROP_NAME);
        return null;
    }
    static boolean isPlayerAnAndroid(EntityPlayer entityPlayer)
    {
        IAndroid android = getAndroid(entityPlayer);
        if (android != null)
            return android.isAndroid();
        return false;
    }
    //endregion
}
