/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.MOEventScan;
import matteroverdrive.api.inventory.IBlockScanner;
import matteroverdrive.client.sound.MachineSound;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Simeon on 8/28/2015.
 */
public class DataPad extends MOBaseItem implements IBlockScanner
{
    @SideOnly(Side.CLIENT)
    public static MachineSound scanningSound;

    public DataPad(String name)
    {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (world.isRemote && hasGui(itemstack))
        {
            openGui(itemstack);
        }
        return itemstack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!entityPlayer.isSneaking() && world.getBlock(x,y,z) != Blocks.air && canScan(itemStack,world.getBlock(x,y,z)))
        {
            entityPlayer.setItemInUse(itemStack,getMaxItemUseDuration(itemStack));
            if (world.isRemote)
            {
                playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
            }else
            {
                setLastBlock(itemStack,world.getBlock(x,y,z));
            }
            return true;
        }
        return false;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack scanner)
    {
        return 20*2;
    }

    @Override
    public boolean hasDetails(ItemStack stack)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(ItemStack stack)
    {
        try {
            Minecraft.getMinecraft().displayGuiScreen(new GuiDataPad(stack));
        }
        catch (Exception e)
        {
            MatterOverdrive.log.error("There was a problem while trying to open the Data Pad Gui",e);
        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        super.onUpdate(itemStack,world,entity,p_77663_4_,p_77663_5_);

        if (world.isRemote) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.isUsingItem())
                {

                } else
                {
                    stopScanSounds();
                }
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack scanner, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventScan(player,scanner,getScanningPos(scanner,player))))
            {
                stopScanSounds();
            }
        }else
        {
            MOEventScan event = new MOEventScan(player,scanner,getScanningPos(scanner,player));
            if (!MinecraftForge.EVENT_BUS.post(event))
            {
                if (destroysBlocks(scanner) && world.canMineBlock(player,event.position.blockX,event.position.blockY,event.position.blockZ))
                {
                    world.setBlockToAir(event.position.blockX, event.position.blockY, event.position.blockZ);
                }
                SoundHandler.PlaySoundAt(world, "scanner_success", player);
            }
        }
        return scanner;
    }

    @Override
    public void onUsingTick(ItemStack scanner, EntityPlayer player, int count)
    {
        MovingObjectPosition hit = getScanningPos(scanner,player);

        if (hit != null) {

            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block lastBlock = getLastBlock(scanner);
                if (lastBlock != null && lastBlock != player.worldObj.getBlock(hit.blockX,hit.blockY,hit.blockZ))
                {
                    //player.setItemInUse(scanner,getMaxItemUseDuration(scanner));
                    player.clearItemInUse();
                    //player.stopUsingItem();
                }
            }
        }
        else
        {
            if (player.worldObj.isRemote)
            {
                stopScanSounds();
                player.stopUsingItem();
            }
        }
    }

    public Block getLastBlock(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            return Block.getBlockById(itemStack.getTagCompound().getInteger("LastBlock"));
        }
        return null;
    }

    public void setLastBlock(ItemStack itemStack,Block block)
    {
        if (itemStack.getTagCompound() == null)
            itemStack.setTagCompound(new NBTTagCompound());

        int blockID = block.getIdFromBlock(block);
        if (itemStack.getTagCompound().getInteger("LastBlock") != blockID)
            itemStack.getTagCompound().setInteger("LastBlock",blockID);
    }

    public void onPlayerStoppedUsing(ItemStack scanner, World world, EntityPlayer player, int count)
    {
        if (world.isRemote)
            stopScanSounds();
    }

    @SideOnly(Side.CLIENT)
    private void playSound(double x,double y,double z)
    {
        if(scanningSound == null)
        {
            scanningSound = new MachineSound(new ResourceLocation(Reference.MOD_ID + ":" +"scanner_scanning"),(float)x,(float)y,(float)z,0.6f,1);
            Minecraft.getMinecraft().getSoundHandler().playSound(scanningSound);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopScanSounds()
    {
        if(scanningSound != null)
        {
            scanningSound.stopPlaying();
            scanningSound = null;
        }
    }

    @Override
    public MovingObjectPosition getScanningPos(ItemStack itemStack,EntityPlayer player)
    {
        return MOPhysicsHelper.rayTrace(player, player.worldObj, 5, 0, Vec3.createVectorHelper(0, player.worldObj.isRemote ? 0 : player.getEyeHeight(), 0), true, false);
    }


    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.block;
    }

    public void addToScanWhitelist(ItemStack itemStack,Block block)
    {
        String id = Block.blockRegistry.getNameForObject(block);
        if (id != null)
        {
            NBTTagList list = itemStack.getTagCompound().getTagList("whitelist", Constants.NBT.TAG_STRING);
            list.appendTag(new NBTTagString(id));
            itemStack.getTagCompound().setTag("whitelist",list);
        }
    }

    //region Setters
    public void setOrdering(ItemStack stack,int order)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("Ordering", order);
    }

    public void setOpenGuide(ItemStack stack,int guideID)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("guideID",guideID);
    }

    public void setOpenPage(ItemStack stack,int page)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setInteger("page",page);
    }
    public void setCategory(ItemStack stack,String category)
    {
        TagCompountCheck(stack);
        stack.getTagCompound().setString("Category",category);
    }
    public void setSelectedActiveQuest(ItemStack itemStack,int quest)
    {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setShort("SelectedActiveQuest",(short) quest);
    }
    //endregion

    //region Getters
    public int getGuideID(ItemStack stack)
    {
        TagCompountCheck(stack);
        if (hasOpenGuide(stack))
        {
            return stack.getTagCompound().getInteger("guideID");
        }
        return -1;
    }
    public int getPage(ItemStack stack)
    {
        TagCompountCheck(stack);
        return stack.getTagCompound().getInteger("page");
    }
    public boolean hasOpenGuide(ItemStack stack)
    {
        TagCompountCheck(stack);
        return stack.getTagCompound().hasKey("guideID", Constants.NBT.TAG_INT);
    }
    public int getOrdering(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Ordering", Constants.NBT.TAG_STRING))
        {
            return stack.getTagCompound().getInteger("Ordering");
        }
        return 2;
    }
    public String getCategory(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return stack.getTagCompound().getString("Category");
        }
        return "";
    }
    public int getActiveSelectedQuest(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return stack.getTagCompound().getShort("SelectedActiveQuest");
        }
        return 0;
    }
    @Override
    public boolean destroysBlocks(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            return itemStack.getTagCompound().getBoolean("Destroys");
        }
        return false;
    }
    public boolean canScan(ItemStack itemStack,Block block)
    {
        if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("whitelist", Constants.NBT.TAG_LIST))
        {
            NBTTagList tagList = itemStack.getTagCompound().getTagList("whitelist", Constants.NBT.TAG_STRING);
            for (int i = 0;i < tagList.tagCount();i++)
            {
                if (tagList.getStringTagAt(i).equals(Block.blockRegistry.getNameForObject(block)))
                {
                    return true;
                }
            }

            return false;
        }
        return true;
    }
    public boolean hasGui(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            return !itemStack.getTagCompound().getBoolean("nogui");
        }
        return true;
    }
    //endregion
}
