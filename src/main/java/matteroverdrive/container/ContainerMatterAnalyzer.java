package matteroverdrive.container;

import matteroverdrive.container.slot.SlotDatabase;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotMatter;
import matteroverdrive.tile.TileEntityMachineMatterAnalyzer;
import matteroverdrive.util.MOContainerHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ContainerMatterAnalyzer extends ContainerMachine<TileEntityMachineMatterAnalyzer>
{
    private int lastAnalyzeTime;

    public ContainerMatterAnalyzer(InventoryPlayer inventory,TileEntityMachineMatterAnalyzer analyzer)
    {
        super(inventory, analyzer);
    }

    @Override
    public  void init(InventoryPlayer inventory)
    {
        this.addSlotToContainer(new SlotMatter(machine,machine.input_slot,8,55));
        this.addSlotToContainer(new SlotDatabase(machine,machine.database_slot,8,82));
        this.addSlotToContainer(new SlotEnergy(machine,machine.getEnergySlotID(),8,109));

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.machine.analyzeTime);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastAnalyzeTime != this.machine.analyzeTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.analyzeTime);
            }

            this.lastAnalyzeTime = this.machine.analyzeTime;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.machine.analyzeTime = newValue;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
