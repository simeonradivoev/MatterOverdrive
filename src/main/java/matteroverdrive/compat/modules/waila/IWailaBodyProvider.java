package matteroverdrive.compat.modules.waila;

import net.minecraftforge.fml.common.Optional;

/**
 * @author shadowfacts
 */
@Optional.Interface(modid = "Waila", iface = "mcp.mobius.waila.api.IWailaDataProvider")
public interface IWailaBodyProvider //extends IWailaDataProvider
{
	/*@Override
	@Optional.Method(modid = "Waila")
	default ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return accessor.getStack();
	}

	@Override
	@Optional.Method(modid = "Waila")
	default List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	default List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	default NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
	{
		if (te != null) te.writeToNBT(tag);
		return tag;
	}*/
}
