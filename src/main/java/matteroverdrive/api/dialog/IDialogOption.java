package matteroverdrive.api.dialog;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 1/27/2016.
 */
public interface IDialogOption
{
    /**
     * Called when the option is chosen from all the option of the patten from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * Not to be confused with {@link matteroverdrive.api.dialog.IDialogMessage#onOptionsInteract(IDialogNpc, EntityPlayer, int)} which is called on the parent.
     * This method is called after {@link matteroverdrive.api.dialog.IDialogMessage#onOptionsInteract(IDialogNpc, EntityPlayer, int)}.
     * @param npc
     * @param player
     */
    void onInteract(IDialogNpc npc, EntityPlayer player);
    /**
     * Used to display the question (option) message.
     * This is used when the parent message is active and shows all children from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * @param npc The NPC Entity.
     * @param player The Player
     * @return The question (option) text.
     */
    String getQuestionText(IDialogNpc npc, EntityPlayer player);
    /**
     * Can the player interact with this option. For messages means if it can be chosen as the next active message from the parent's options.
     * @param npc The NPC Entity
     * @param player The Player
     * @return Can the message be clicked (chosen) as an option.
     */
    boolean canInteract(IDialogNpc npc, EntityPlayer player);
    /**
     * Is the option visible.
     * @param npc The NPC entity.
     * @param player The Player.
     * @return Is the message visible as an option.
     */
    boolean isVisible(IDialogNpc npc, EntityPlayer player);
    /**
     * @param npc the npc
     * @param player the player
     * @return The holo icon, {@code null} if there isn't one
     */
    String getHoloIcon(IDialogNpc npc, EntityPlayer player);

    boolean equalsOption(IDialogOption other);
}
