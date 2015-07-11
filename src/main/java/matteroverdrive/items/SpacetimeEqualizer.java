package matteroverdrive.items;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by Simeon on 5/19/2015.
 */
public class SpacetimeEqualizer extends ItemArmor
{
    public SpacetimeEqualizer(String name)
    {
        super(ItemArmor.ArmorMaterial.IRON,0,1);
        setUnlocalizedName(name);
        this.setCreativeTab(MatterOverdrive.tabMatterOverdrive);
        this.setTextureName(Reference.MOD_ID + ":" + name);
    }

    public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            addDetails(itemstack,player,infos);
        }
        else
        {
            infos.add(MOStringHelper.MORE_INFO);
        }
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        if (MOStringHelper.hasTranslation(getUnlocalizedName() + ".details"))
        {
            infos.add(EnumChatFormatting.GRAY + MOStringHelper.translateToLocal(getUnlocalizedName() + ".details"));
        }
    }

    public void Register(String name)
    {
        GameRegistry.registerItem(this, name);
    }

    public void Register()
    {
        this.Register(this.getUnlocalizedName().substring(5));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return Reference.PATH_ARMOR + this.getUnlocalizedName().substring(5) + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        player.motionX *= 0.5;
        if(player.motionY > 0)
            player.motionY *= 0.9;
        player.motionZ *= 0.5;
    }

}
