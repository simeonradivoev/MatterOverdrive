package com.MO.MatterOverdrive.items;

import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.MO.MatterOverdrive.util.MOStringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/10/2015.
 */
public class SecurityProtocol extends MOBaseItem {

    IIcon[] icons;
    String[] types = new String[]{"empty","claim","access","remove"};

    public SecurityProtocol(String name)
    {
        super(name);
        setMaxStackSize(16);
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack, player, infos);

        if (itemstack.hasTagCompound())
        {
            infos.add(MOStringHelper.translateToLocal(getUnlocalizedName() + "." + types[MathHelper.clampI(itemstack.getItemDamage(),0,types.length)] + ".description"));
            String owner = itemstack.getTagCompound().getString("Owner");
            infos.add(EnumChatFormatting.YELLOW + "Owner: " + owner);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        icons = new IIcon[types.length];

        for (int i = 0;i < types.length;i++)
        {
            icons[i] = p_94581_1_.registerIcon(this.getIconString() + "_" + types[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + types[MathHelper.clampI(stack.getItemDamage(),0,types.length)];
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!stack.hasTagCompound())
        {
            if (player.isSneaking()) {
                TagCompountCheck(stack);
                stack.getTagCompound().setString("Owner", player.getGameProfile().getName());
                stack.setItemDamage(1);
            }
        }
        else if (stack.getTagCompound().getString("Owner").equals(player.getGameProfile().getName()) || player.capabilities.isCreativeMode)
        {
            if (player.isSneaking())
            {
                int damage = stack.getItemDamage() + 1;
                if (damage >= types.length)
                    damage = 1;

                stack.setItemDamage(damage);
            }
        }

        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof MOTileEntityMachine) {
                if (stack.getItemDamage() == 1) {
                    if (((MOTileEntityMachine) tileEntity).claim(stack)) {
                        stack.stackSize--;
                        return true;
                    }
                } else if (stack.getItemDamage() == 3) {
                    if (((MOTileEntityMachine) tileEntity).unclaim(stack)) {
                        stack.stackSize--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasDetails(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return stack.getTagCompound().hasKey("Owner");
        }
        return false;
    }
}
