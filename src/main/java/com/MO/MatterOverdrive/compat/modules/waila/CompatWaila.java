package com.MO.MatterOverdrive.compat.modules.waila;

import com.MO.MatterOverdrive.blocks.BlockStarMap;
import com.MO.MatterOverdrive.blocks.BlockWeaponStation;
import com.MO.MatterOverdrive.blocks.TransporterBlock;
import com.MO.MatterOverdrive.compat.Compat;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;
import com.MO.MatterOverdrive.tile.TileEntityMachineTransporter;
import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Compatibility for WAILA
 *
 * @author shadowfacts
 */
@Compat("Waila")
public class CompatWaila {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", "com.MO.MatterOverdrive.compat.modules.waila.CompatWaila.registerCallback");
	}

	public static void registerCallback(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new TileEntityWeaponStation(), BlockWeaponStation.class);
		registrar.registerBodyProvider(new TileEntityMachineStarMap(), BlockStarMap.class);
		registrar.registerBodyProvider(new TileEntityMachineTransporter(), TransporterBlock.class);
	}
}
