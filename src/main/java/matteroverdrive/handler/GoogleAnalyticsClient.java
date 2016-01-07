package matteroverdrive.handler;

import com.brsanthu.googleanalytics.GoogleAnalyticsRequest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 1/7/2016.
 */
@SideOnly(Side.CLIENT)
public class GoogleAnalyticsClient extends GoogleAnalyticsCommon
{
    @Override
    public GoogleAnalyticsRequest changeUserID(GoogleAnalyticsRequest request, EntityPlayer entityPlayer)
    {
        if (entityPlayer == null && Minecraft.getMinecraft().thePlayer != null)
        {
            request.userId(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString());
            return request;
        }
        return super.changeUserID(request,entityPlayer);
    }
}
