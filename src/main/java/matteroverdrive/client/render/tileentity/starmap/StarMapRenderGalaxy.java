package matteroverdrive.client.render.tileentity.starmap;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import matteroverdrive.Reference;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRenderGalaxy extends StarMapRendererStars
{
    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance)
    {
        for (Quadrant quadrant : galaxy.getQuadrants())
        {
            renderStars(quadrant, starMap,2,2);
        }
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks, float opacity)
    {
        glEnable(GL_ALPHA_TEST);
        int ownedSystemCount = galaxy.getOwnedSystemCount(Minecraft.getMinecraft().thePlayer);
        int enemySystemCount = galaxy.getEnemySystemCount(Minecraft.getMinecraft().thePlayer);
        int freeSystemCount = galaxy.getStarCount() - ownedSystemCount - enemySystemCount;
        GuiColor color = Reference.COLOR_HOLO_GREEN;
        RenderUtils.applyColorWithMultipy(color, opacity);
        ClientProxy.holoIcons.bindSheet();
        RenderHelper.renderIcon(0, -30, 0, ClientProxy.holoIcons.getIcon("page_icon_star"), 20, 20);
        RenderUtils.drawString(String.format("x%s",ownedSystemCount),24,-23,color,opacity);

        color = Reference.COLOR_HOLO_RED;
        RenderUtils.applyColorWithMultipy(color,opacity);
        ClientProxy.holoIcons.bindSheet();
        RenderHelper.renderIcon(64, -30, 0, ClientProxy.holoIcons.getIcon("page_icon_star"), 20, 20);
        RenderUtils.drawString(String.format("x%s",enemySystemCount),88,-23,color,opacity);

        color = Reference.COLOR_HOLO;
        RenderUtils.applyColorWithMultipy(color,opacity);
        ClientProxy.holoIcons.bindSheet();
        RenderHelper.renderIcon(128, -30, 0, ClientProxy.holoIcons.getIcon("page_icon_star"), 20, 20);
        RenderUtils.drawString(String.format("x%s",freeSystemCount),152,-23,color,opacity);
        glDisable(GL_ALPHA_TEST);
    }

    @Override
    public double getHologramHeight() {
        return 2.5;
    }

}
