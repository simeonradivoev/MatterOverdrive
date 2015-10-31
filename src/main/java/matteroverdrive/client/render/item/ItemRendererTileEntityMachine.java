package matteroverdrive.client.render.item;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/5/2015.
 */
public class ItemRendererTileEntityMachine implements IItemRenderer
{
    private TileEntitySpecialRenderer renderer;
    private TileEntity tileEntity;
    private double scale = 1;
    Vec3 offfset;

    public ItemRendererTileEntityMachine(TileEntitySpecialRenderer renderer, TileEntity tileEntity)
    {
        this.renderer = renderer;
        this.tileEntity = tileEntity;
    }

    public ItemRendererTileEntityMachine(TileEntitySpecialRenderer renderer, TileEntity tileEntity, double scale)
    {
        this(renderer,tileEntity);
        this.scale = scale;
    }

    public ItemRendererTileEntityMachine(TileEntitySpecialRenderer renderer, TileEntity tileEntity, double scale, Vec3 offfset)
    {
        this(renderer,tileEntity,scale);
        this.offfset = offfset;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        glPushMatrix();
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            renderer.renderTileEntityAt(tileEntity, 0, 0, 0, 0);
        }
        else
        {
            if (offfset != null)
                glTranslated(offfset.xCoord,offfset.yCoord,offfset.zCoord);
            glTranslated(-0.5,-0.5,-0.5);
            glScaled(scale, scale, scale);
            renderer.renderTileEntityAt(tileEntity, 0, 0, 0, 0);
        }
        glPopMatrix();
    }
}
