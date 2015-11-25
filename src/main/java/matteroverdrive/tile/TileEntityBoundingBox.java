package matteroverdrive.tile;

import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class TileEntityBoundingBox extends TileEntity implements IMOTileEntity {

	private int tick = 0;
	private BlockPosition ownerPos;
	private Block ownerBlock;

	@Override
	public void updateEntity() {
		tick++;
		if (tick == 80) { // update every 4 seconds (assuming 20 TPS)
			tick = 0;

			if (!ownerPresent()) {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}

		}
	}

	@Override
	public boolean canUpdate() {
		return worldObj != null;
	}

	private boolean ownerPresent() {
		return worldObj.getBlock(ownerPos.x, ownerPos.y, ownerPos.z) == ownerBlock;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		ownerPos = new BlockPosition(tag);

		String ownerModid = tag.getString("owner_block_modid");
		String ownerName = tag.getString("owner_block_name");
		Block block = GameRegistry.findBlock(ownerModid, ownerName);
		if (block == null) {
			MOLog.error("Missing owner block " + ownerModid + ":" + ownerName);
		} else {
			ownerBlock = block;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (ownerPos != null) {
			ownerPos.writeToNBT(tag);
		}

		if (ownerBlock != null) {
			UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(ownerBlock);
			tag.setString("owner_block_modid", id.modId);
			tag.setString("owner_block_name", id.name);
		}
	}

	public BlockPosition getOwnerPos() {
		return ownerPos;
	}

	public void setOwnerPos(BlockPosition ownerPos) {
		this.ownerPos = ownerPos;
	}

	public Block getOwnerBlock() {
		return ownerBlock;
	}

	public void setOwnerBlock(Block ownerBlock) {
		this.ownerBlock = ownerBlock;
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

	@Override
	public void onNeighborBlockChange() {

	}

	@Override
	public void writeToDropItem(ItemStack itemStack) {

	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack) {

	}
}
