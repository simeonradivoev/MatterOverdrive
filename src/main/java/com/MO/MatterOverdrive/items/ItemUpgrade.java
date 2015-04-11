package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.IUpgrade;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.util.MOStringHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 4/10/2015.
 */
public class ItemUpgrade extends MOBaseItem implements IUpgrade
{
    public static final String[] subItemNames = {"base","speed","power","failsafe"};
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemUpgrade(String name)
    {
        super(name);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        int damage = itemStack.getItemDamage();
        return damage != 0;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        Map<UpgradeTypes,Double> stats = getUpgrades(itemstack);
        for (final Map.Entry<UpgradeTypes, Double> entry : stats.entrySet())
        {
            infos.add(MOStringHelper.toInfo(entry.getKey(),entry.getValue()));
        }
    }

    @Override
    public void Register(String name)
    {
        super.Register(name);
        GameRegistry.addRecipe(new ItemStack(this, 1, 0), new Object[]{" R ", " C ", " T ", 'G', Blocks.glass, 'R', Items.redstone, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0)});
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0; i < subItemNames.length;i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp_int(damage, 0, (icons.length-1));
        return this.icons[j];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, (subItemNames.length-1));
        return super.getUnlocalizedName() + "." + subItemNames[i];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[subItemNames.length];

        for (int i = 0; i < subItemNames.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + subItemNames[i]);
        }

        this.itemIcon = this.icons[0];
    }


    @Override
    public Map<UpgradeTypes, Double> getUpgrades(ItemStack itemStack)
    {
        HashMap<UpgradeTypes,Double> upgrades = new HashMap<UpgradeTypes, Double>();
        int damage = itemStack.getItemDamage();
        switch (damage)
        {
            case 1:
                //the speed upgrade
                upgrades.put(UpgradeTypes.Speed,0.75);
                upgrades.put(UpgradeTypes.PowerUsage,1.5);
                break;
            case 2:
                //less power upgrade
                upgrades.put(UpgradeTypes.Speed,1.5);
                upgrades.put(UpgradeTypes.PowerUsage,0.5);
                upgrades.put(UpgradeTypes.Fail,1.25);
                break;
            case 3:
                //less chance to fail upgrade
                upgrades.put(UpgradeTypes.Fail,0.5);
                upgrades.put(UpgradeTypes.Speed,1.25);
                upgrades.put(UpgradeTypes.PowerUsage,1.25);
                break;
        }
        return upgrades;
    }
}
