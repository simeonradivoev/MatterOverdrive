package matteroverdrive.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Simeon on 4/16/2015.
 */
public class EntityDamageSourcePhaser extends EntityDamageSource
{
    protected final Entity damageSourceEntity;

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

    public ITextComponent func_151519_b(EntityLivingBase entity)
    {
		String normalMsg = "death.attack." + damageType;
		String itemMsg = normalMsg + ".item";

		if (damageSourceEntity instanceof EntityLivingBase) {
			ItemStack itemStack = ((EntityLivingBase)damageSourceEntity).getActiveItemStack();
			if (itemStack != null &&
					itemStack.hasDisplayName() &&
					MOStringHelper.hasTranslation(itemMsg)) {
				return new TextComponentTranslation(itemMsg, entity.getDisplayName().getFormattedText(), damageSourceEntity.getDisplayName().getFormattedText(), itemStack.getChatComponent());
			}
		}

		return new TextComponentTranslation(normalMsg, entity.getDisplayName(), damageSourceEntity.getDisplayName());
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled()
    {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
}
