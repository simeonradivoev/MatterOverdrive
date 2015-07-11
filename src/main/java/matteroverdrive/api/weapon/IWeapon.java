package matteroverdrive.api.weapon;

import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Simeon on 4/13/2015.
 */
public interface IWeapon
{
    Vector2f getSlotPosition(int slot,ItemStack weapon);
    Vector2f getModuleScreenPosition(int slot,ItemStack weapon);
    boolean supportsModule(int slot,ItemStack weapon);
}
