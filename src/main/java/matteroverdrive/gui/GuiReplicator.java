package matteroverdrive.gui;

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.container.ContainerReplicator;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.pages.PageTasks;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.server.PacketRemoveTask;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityMachineReplicator;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GuiReplicator extends MOGuiNetworkMachine<TileEntityMachineReplicator>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled replicate_progress;
    ElementSlot outputSlot;
    ElementSlot seccoundOutputSlot;
    PageTasks pagePackets;
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
		this.addElement(replicate_progress);
        pages.get(0).addElement(outputSlot);
        pages.get(0).addElement(seccoundOutputSlot);
        pages.get(0).addElement(matterElement);
        pages.get(0).addElement(energyElement);
        AddHotbarPlayerSlots(inventorySlots, this);
        AddMainPlayerSlots(inventorySlots, pages.get(0));
	}

    @Override
    public void registerPages(MOBaseContainer container,TileEntityMachineReplicator machine)
    {
        super.registerPages(container,machine);

        pagePackets = new PageTasks(this,10,0,xSize,ySize,machine.getTaskQueue((byte) 0));
        pagePackets.setName("Tasks");
        AddPage(pagePackets, ClientProxy.holoIcons.getIcon("page_icon_tasks"), MOStringHelper.translateToLocal("gui.tooltip.page.tasks")).setIconColor(Reference.COLOR_MATTER);
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
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        super.drawGuiContainerBackgroundLayer(partialTick,x,y);

        replicate_progress.setQuantity(MathHelper.round(((float)this.machine.replicateProgress / 100f) * 24));
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

        MatterNetworkTaskReplicatePattern task = machine.getTaskQueue((byte) 0).peek();
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
            MatterOverdrive.packetPipeline.sendToServer(new PacketRemoveTask(machine,mouseButton,(byte)0, MatterNetworkTaskState.INVALID));
        }
    }
	
}
