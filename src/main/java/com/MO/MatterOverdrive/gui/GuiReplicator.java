package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.gui.pages.PageTasks;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.network.packet.server.PacketRemoveTask;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerReplicator;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import net.minecraft.entity.player.InventoryPlayer;

import java.util.List;

public class GuiReplicator extends MOGuiMachine<TileEntityMachineReplicator>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled replicate_progress;
    ElementSlot outputSlot;
    ElementSlot seccoundOutputSlot;
    PageTasks pagePackets;
    MOElementButton packetsButton;
    ElementItemPattern itemPattern;

	public GuiReplicator(InventoryPlayer inventoryPlayer,TileEntityMachineReplicator entity)
    {
		super(new ContainerReplicator(inventoryPlayer, entity),entity);
        name = "replicator";
		matterElement = new ElementMatterStored(this,141,39,machine.getMatterStorage());
		energyElement = new MOElementEnergy(this,167,39,machine.getEnergyStorage());
		replicate_progress = new ElementDualScaled(this,32,52);
        outputSlot = new ElementInventorySlot(this,this.getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),22,22,"big");
        seccoundOutputSlot = new ElementInventorySlot(this,this.getContainer().getSlotAt(machine.SECOUND_OUTPUT_SLOT_ID),22,22,"big");

        pagePackets = new PageTasks(this,10,0,xSize,ySize,machine.getQueue((byte) 0));
        pages.add(pagePackets);

        packetsButton = new MOElementButton(this,this,6,8,"Tasks",0,0,24,0,24,0,24,24,"");
        packetsButton.setTexture(Reference.PATH_GUI_ITEM + "tasks.png", 48, 24);
        packetsButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.page.tasks"));
        pageButtons.add(packetsButton);

        itemPattern = new ElementItemPattern(this, entity.getInternalPatternStorage(), "big_main", 37, 22);
        slotsList.setPosition(5, 49);
        slotsList.addElementAt(0, itemPattern);

        replicate_progress.setMode(1);
        replicate_progress.setSize(24, 16);
        replicate_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
	}
	
	@Override
	public void initGui() {
        super.initGui();
        homePage.addElement(outputSlot);
        homePage.addElement(seccoundOutputSlot);
        homePage.addElement(matterElement);
        homePage.addElement(energyElement);
		this.addElement(replicate_progress);
        AddHotbarPlayerSlots(inventorySlots, this);
        AddMainPlayerSlots(inventorySlots, homePage);
	}

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y)
    {
        List list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            String info = (String)list.get(k);

            if (k == 0)
            {
                list.set(k, stack.getRarity().rarityColor + info);
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + info);
            }
        }

        ItemStack itemStack = stack;
        if(MatterHelper.isMatterScanner(stack))
        {
            IMatterDatabase database = MatterScanner.getLink(Minecraft.getMinecraft().theWorld,stack);
            if(database != null)
            {
                itemStack = MatterDatabaseHelper.GetItemStackFromNBT(database.getItemAsNBT(MatterScanner.getSelectedAsItem(stack)));
            }
        }

        //MatterHelper.DrawMatterInfoTooltip(itemStack, TileEntityMachineReplicator.REPLICATE_SPEED_PER_MATTER, TileEntityMachineReplicator.REPLICATE_ENERGY_PER_TICK, list);

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
    }
	
	@Override
	public void drawGuiContainerForegroundLayer(int part1,int part2)
	{
        super.drawGuiContainerForegroundLayer(part1,part2);
        ManageReqiremnetsTooltips();
	}

    void ManageReqiremnetsTooltips()
    {
        NBTTagCompound itemAsNBT = machine.getInternalPatternStorage();

        if(itemAsNBT != null)
        {
            ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);

            int matterAmount = MatterHelper.getMatterAmountFromItem(item);
            matterElement.setDrain(-matterAmount);
            energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
            energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
        }
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        MatterNetworkTaskReplicatePattern task = machine.getQueue((byte)0).peek();
        if (task != null)
        {
            NBTTagCompound nbt = machine.getInternalPatternStorage();
            itemPattern.setAmount(task.getAmount());
            if (task.getItemID() == nbt.getShort("id") && task.getItemMetadata() == nbt.getShort("Damage"))
            {
                itemPattern.setTagCompound(nbt);
            }
            else
            {
                itemPattern.setTagCompound(null);
            }
        }
        else
            itemPattern.setAmount(0);
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName,mouseButton);
        if (buttonName == "DropTask")
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("TaskID",mouseButton);
            MatterOverdrive.packetPipeline.sendToServer(new PacketRemoveTask(machine,mouseButton,(byte)0,Reference.TASK_STATE_INVALID));
        }
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) 
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

		replicate_progress.setQuantity(MathHelper.round(((float)this.machine.replicateProgress / 100f) * 24));
	}
	
}
