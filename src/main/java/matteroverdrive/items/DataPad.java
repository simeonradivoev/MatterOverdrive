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

import matteroverdrive.api.events.MOEventScan;
import matteroverdrive.api.inventory.IBlockScanner;
import matteroverdrive.client.sound.MachineSound;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 8/28/2015.
 */
public class DataPad extends MOBaseItem implements IBlockScanner
{
    @SideOnly(Side.CLIENT)
    private static MachineSound scanningSound;

    public DataPad(String name)
    {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (worldIn.isRemote && hasGui(itemStackIn))
        {
            openGui(hand,itemStackIn);
            return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS,itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!playerIn.isSneaking() && worldIn.getBlockState(pos).getBlock() != Blocks.air && canScan(stack,worldIn.getBlockState(pos)))
        {
            playerIn.setActiveHand(hand);
            if (worldIn.isRemote)
            {
                playSound(playerIn.getPosition());
            }else
            {
                setLastBlock(stack,worldIn.getBlockState(pos).getBlock());
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
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
    private void openGui(EnumHand hand,ItemStack stack)
    {
        try {
            Minecraft.getMinecraft().displayGuiScreen(new GuiDataPad(hand,stack));
        }
        catch (Exception e)
        {
            MOLog.error("There was a problem while trying to open the Data Pad Gui",e);
        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        super.onUpdate(itemStack,world,entity,p_77663_4_,p_77663_5_);

        if (world.isRemote) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.isHandActive())
                {

                } else
                {
                    stopScanSounds();
                }
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (!(entityLiving instanceof EntityPlayer)) return stack;
        if (worldIn.isRemote)
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventScan((EntityPlayer)entityLiving,stack,getScanningPos(stack,(EntityPlayer)entityLiving))))
            {
                stopScanSounds();
            }
        }else
        {
            MOEventScan event = new MOEventScan((EntityPlayer)entityLiving,stack,getScanningPos(stack,(EntityPlayer)entityLiving));
            if (!MinecraftForge.EVENT_BUS.post(event))
            {
                if (destroysBlocks(stack) && worldIn.isBlockModifiable((EntityPlayer)entityLiving,event.position.getBlockPos()))
                {
                    worldIn.setBlockToAir(event.position.getBlockPos());
                }
                SoundHandler.PlaySoundAt(worldIn, MatterOverdriveSounds.scannerSuccess,SoundCategory.PLAYERS, entityLiving);
            }
        }
        return stack;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        if (!(player instanceof EntityPlayer)) return;

        RayTraceResult hit = getScanningPos(stack,(EntityPlayer) player);

        if (hit != null) {

            if (hit.typeOfHit == RayTraceResult.Type.BLOCK) {
                Block lastBlock = getLastBlock(stack);
                if (lastBlock != null && lastBlock != player.worldObj.getBlockState(hit.getBlockPos()).getBlock())
                {
                    //player.setItemInUse(scanner,getMaxItemUseDuration(scanner));
                    player.resetActiveHand();
                    //player.stopUsingItem();
                }
            }
        }
        else
        {
            if (player.worldObj.isRemote)
            {
                stopScanSounds();
                player.resetActiveHand();
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

        int blockID = Block.getIdFromBlock(block);
        if (itemStack.getTagCompound().getInteger("LastBlock") != blockID)
            itemStack.getTagCompound().setInteger("LastBlock",blockID);
    }

    public void onPlayerStoppedUsing(ItemStack scanner, World world, EntityPlayer player, int count)
    {
        if (world.isRemote)
            stopScanSounds();
    }

    @SideOnly(Side.CLIENT)
    private void playSound(BlockPos pos)
    {
        if(scanningSound == null)
        {
            scanningSound = new MachineSound(MatterOverdriveSounds.scannerScanning,SoundCategory.PLAYERS,pos,0.6f,1);
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
    public RayTraceResult getScanningPos(ItemStack itemStack, EntityLivingBase player)
    {
        return MOPhysicsHelper.rayTrace(player, player.worldObj, 5, 0, new Vec3d(0, player.getEyeHeight(), 0), true, false);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }


    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.NONE;
    }

    public void addToScanWhitelist(ItemStack itemStack,Block block)
    {
        String id = block.getRegistryName().toString();
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
        return itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Destroys");
    }

    @Override
    public boolean showsGravitationalWaves(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            if (itemStack.getTagCompound().hasKey("showGravWaves"))
            {
                return itemStack.getTagCompound().getBoolean("showGravWaves");
            }
        }
        return true;
    }

    public boolean canScan(ItemStack itemStack,IBlockState state)
    {
        if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("whitelist", Constants.NBT.TAG_LIST))
        {
            NBTTagList tagList = itemStack.getTagCompound().getTagList("whitelist", Constants.NBT.TAG_STRING);
            for (int i = 0;i < tagList.tagCount();i++)
            {
                if (tagList.getStringTagAt(i).equals(state.getBlock().getRegistryName()))
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
        return itemStack.getTagCompound() == null || !itemStack.getTagCompound().getBoolean("nogui");
    }
    //endregion
}
