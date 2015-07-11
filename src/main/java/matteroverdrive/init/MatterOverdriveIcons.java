package matteroverdrive.init;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import matteroverdrive.Reference;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

/**
 * Created by Simeon on 3/17/2015.
 */
public class MatterOverdriveIcons
{
    public static IIcon Base;
    public static IIcon Recycler;
    public static IIcon Vent;
    public static IIcon Vent2;
    public static IIcon Matter_tank_full;
    public static IIcon Matter_tank_empty;
    public static IIcon Transperant;
    public static IIcon Network_port_square;
    public static IIcon Monitor_back;
    public static IIcon YellowStripes;
    public static IIcon Coil;
    public static IIcon charging_station;
    public static IIcon pattern_storage;
    public static IIcon replicator;

    public static IIcon particle_steam;

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent event)
    {
        switch (event.map.getTextureType())
        {
            case 0:
                initBlockIcons(event.map);
                break;
            case 4:
                ClientProxy.holoIcons.registerIcons(event.map);
                break;
        }
    }

    private void initBlockIcons(IIconRegister r)
    {
        Vent = register(r, "vent");
        Base = register(r, "base");
        Matter_tank_empty = register(r, "tank_empty");
        Matter_tank_full = register(r, "tank_full");
        Transperant = register(r, "transperant");
        Network_port_square = register(r,"network_port");
        Vent2 = register(r,"vent2");
        Monitor_back = register(r,"holo_monitor");
        YellowStripes = register(r,"base_stripes");
        Recycler = register(r,"recycler_side");
        Coil = register(r,"base_coil");
        charging_station = register(r,"charging_station");
        pattern_storage = register(r,"pattern_storage");
        replicator = register(r,"replicator");
    }

    private void initParticleIcons(IIconRegister r)
    {
        particle_steam = register(r,"particle_steam");

    }

    private void initItems(IIconRegister r)
    {
        register(r, "vent");
        register(r, "base");
        register(r, "tank_empty");
        register(r, "tank_full");
    }

    public static IIcon register(IIconRegister register,String name)
    {
        return register.registerIcon(Reference.MOD_ID + ":" + name);
    }
}
