package matteroverdrive.compat.modules;

import matteroverdrive.compat.Compat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveBioticStats;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Simeon on 2/26/2016.
 */
@Compat("GalacticraftCore")
public class CompatGalacticraft
{
    @Compat.Init
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Optional.Method(modid = "GalacticraftCore")
    @SubscribeEvent
    public void onLivingEvent(GCCoreOxygenSuffocationEvent event)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer) event.entityLiving);
        if (event.entityLiving instanceof EntityPlayer && event.isCancelable() && androidPlayer.isAndroid() && androidPlayer.isUnlocked(MatterOverdriveBioticStats.oxygen,1) && MatterOverdriveBioticStats.oxygen.isActive(androidPlayer,1))
        {
            event.setCanceled(true);
        }
    }
}
