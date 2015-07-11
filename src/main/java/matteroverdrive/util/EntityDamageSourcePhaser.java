package matteroverdrive.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;

/**
 * Created by Simeon on 4/16/2015.
 */
public class EntityDamageSourcePhaser extends EntityDamageSource
{
    protected Entity damageSourceEntity;

    public EntityDamageSourcePhaser(Entity p_i1567_2_)
    {
        super("phaser",p_i1567_2_);
        this.damageSourceEntity = p_i1567_2_;
        this.setProjectile();
    }

    public Entity getEntity()
    {
        return damageSourceEntity;
    }

    public IChatComponent func_151519_b(EntityLivingBase entity)
    {
		String normalMsg = "death.attack." + damageType;
		String itemMsg = normalMsg + ".item";

		if (damageSourceEntity instanceof EntityLivingBase) {
			ItemStack itemStack = ((EntityLivingBase)damageSourceEntity).getHeldItem();
			if (itemStack != null &&
					itemStack.hasDisplayName() &&
					MOStringHelper.hasTranslation(itemMsg)) {
				return new ChatComponentTranslation(itemMsg, entity.func_145748_c_(), damageSourceEntity.func_145748_c_(), itemStack.func_151000_E());
			}
		}

		return new ChatComponentTranslation(normalMsg, entity.func_145748_c_(), damageSourceEntity.func_145748_c_());
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled()
    {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
}
