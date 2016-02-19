package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;

/**
 * Created by Simeon on 1/26/2016.
 */
public abstract class DialogLogic
{
    public abstract boolean trigger(IDialogMessage dialogMessage, IDialogNpc dialogNpc);
}
