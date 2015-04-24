package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternDrive extends MOBaseItem implements IMatterPatternStorage
{
    private IIcon storageFull;
    private IIcon storagePartiallyFull;
    int capacity;

    public PatternDrive(String name, int capacity)
    {
        super(name);
        this.capacity = capacity;
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.itemIcon = p_94581_1_.registerIcon(this.getIconString());
        this.storageFull = p_94581_1_.registerIcon(this.getIconString() + "_full");
        this.storagePartiallyFull = p_94581_1_.registerIcon(this.getIconString() + "_partially_full");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        switch (damage)
        {
            case 2:
                return storageFull;
            case 1:
                return storagePartiallyFull;
            default:
                return itemIcon;
        }
    }

    public int getDamage(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            if (stack.getTagCompound().func_150296_c().size() > 0)
            {
                if (stack.getTagCompound().func_150296_c().size() < getCapacity(stack))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        if(itemstack.hasTagCompound())
        {
            NBTTagList list = MatterDatabaseHelper.GetItemsTagList(itemstack);
            if (list != null) {
                for (int i = 0;i < list.tagCount();i++)
                {
                    int progress = MatterDatabaseHelper.GetProgressFromNBT(list.getCompoundTagAt(i));
                    infos.add(getPatternInfoColor(progress) + MatterDatabaseHelper.GetItemStackFromNBT(list.getCompoundTagAt(i)).getDisplayName() + " [" + progress + "%]");
                }
            }
        }
    }

    private EnumChatFormatting getPatternInfoColor(int progress)
    {
        EnumChatFormatting color = EnumChatFormatting.GRAY;

        if (progress > 0 && progress <= 20)
            color = EnumChatFormatting.RED;
        else if (progress > 20 && progress <= 40)
            color = EnumChatFormatting.GOLD;
        else if (progress > 40 && progress <= 60)
            color = EnumChatFormatting.YELLOW;
        else if (progress > 40 && progress <= 80)
            color = EnumChatFormatting.AQUA;
        else if (progress > 80 && progress <= 100)
            color = EnumChatFormatting.GREEN;
        else
            color = EnumChatFormatting.GREEN;

        return color;
    }

    public void InitTagCompount(ItemStack stack)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort(MatterDatabaseHelper.CAPACITY_TAG_NAME, (short) capacity);
        NBTTagList itemList = new NBTTagList();
        tagCompound.setTag(MatterDatabaseHelper.ITEMS_TAG_NAME,itemList);
        stack.setTagCompound(tagCompound);
    }

    @Override
    public NBTTagList getItemsAsNBT(ItemStack storage)
    {
        TagCompountCheck(storage);
        return storage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME, 10);
    }

    @Override
    public boolean addItem(ItemStack storage, ItemStack itemStack,int initialAmount,boolean simulate)
    {
        TagCompountCheck(storage);

        NBTTagList itemList = getItemsAsNBT(storage);
        if(itemList.tagCount() < getCapacity(storage))
        {
            if(MatterHelper.CanScan(itemStack))
            {
                int itemProgress = MatterDatabaseHelper.GetItemProgress(storage, itemStack);

                if(itemProgress < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
                {
                    if (!simulate)
                    {
                        MatterDatabaseHelper.writeToNBT(storage, itemStack, initialAmount);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public NBTTagCompound getItemAsNBT(ItemStack storage, ItemStack item)
    {
        TagCompountCheck(storage);
        return  MatterDatabaseHelper.GetItemAsNBT(storage, item);
    }

    @Override
    public int getCapacity(ItemStack item)
    {
        TagCompountCheck(item);
        return item.getTagCompound().getShort(MatterDatabaseHelper.CAPACITY_TAG_NAME);
    }

    public void clearStorage(ItemStack itemStack)
    {
        if (MatterHelper.isMatterPatternStorage(itemStack))
        {
            itemStack.setTagCompound(null);
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            clearStorage(itemStack);
        }
        return itemStack;
    }
}
