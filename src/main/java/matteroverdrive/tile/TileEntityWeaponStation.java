package matteroverdrive.tile;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.ItemInventoryWrapper;
import matteroverdrive.data.inventory.ModuleSlot;
import matteroverdrive.data.inventory.WeaponSlot;
import matteroverdrive.util.MatterHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 4/13/2015.
 */
public class TileEntityWeaponStation extends MOTileEntityMachine implements IWailaBodyProvider
{
    public int INPUT_SLOT;
    public int BATTERY_MODULE;
    public int COLOR_MODULE;
    public int BARREL_MODULE;
    public int SIGHTS_MODULE;
    public int OTHER_MODULE;

    public TileEntityWeaponStation()
    {
        super(0);
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        BATTERY_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_BATTERY));
        COLOR_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_COLOR));
        BARREL_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_BARREL));
        SIGHTS_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_SIGHTS));
        OTHER_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_OTHER));
        INPUT_SLOT = inventory.AddSlot(new WeaponSlot(true));
        redstoneMode = Reference.MODE_REDSTONE_LOW;
        super.RegisterSlots(inventory);
    }

    public IInventory getActiveInventory()
    {
        if (inventory.getSlot(INPUT_SLOT).getItem() != null && MatterHelper.isWeapon(inventory.getSlot(INPUT_SLOT).getItem()))
        {
            return new ItemInventoryWrapper(inventory.getSlot(INPUT_SLOT).getItem(),5);
        }
        return inventory;
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    //region Inventory Functions
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().getStackInSlot(slot);
        }else
        {
            return super.getStackInSlot(slot);
        }
    }

    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().isItemValidForSlot(slot,item);
        }else
        {
            return super.isItemValidForSlot(slot, item);
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().decrStackSize(slot, size);
        }else
        {
            return super.decrStackSize(slot, size);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().getStackInSlotOnClosing(slot);
        }else
        {
            return super.getStackInSlotOnClosing(slot);
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        if (slot != INPUT_SLOT)
        {
            getActiveInventory().setInventorySlotContents(slot, itemStack);
        }else
        {
            super.setInventorySlotContents(slot,itemStack);
        }
    }
    //endregion

    //region Upgrades
    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }
    //endregion

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord + 1,yCoord + 2,zCoord + 1);
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

//	WAILA
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof TileEntityWeaponStation) {
			TileEntityWeaponStation weaponStation = (TileEntityWeaponStation)te;

			if (weaponStation.getStackInSlot(INPUT_SLOT) != null) {
				String name = weaponStation.getStackInSlot(INPUT_SLOT).getDisplayName();
				currenttip.add(EnumChatFormatting.YELLOW + "Current Weapon: " + EnumChatFormatting.WHITE + name);
			}

		} else {
			throw new RuntimeException("Weapon Station WAILA provider is being used for something that is not a Weapon Station: " + te.getClass());
		}

		return currenttip;
	}
}
