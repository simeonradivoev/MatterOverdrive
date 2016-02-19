package matteroverdrive.client.render.weapons.modules;

import com.google.common.collect.ImmutableMap;
import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.weapons.WeaponRenderHandler;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 2/18/2016.
 */
public class ModuleSniperScopeRender extends ModuleRenderAbstract
{
    private ResourceLocation scopeModelLocation = new ResourceLocation(Reference.PATH_MODEL + "item/sniper_scope.obj");
    private OBJModel scopeModel;
    private IFlexibleBakedModel scopeBakedModelBase;
    private IFlexibleBakedModel scopeBakedModelWindow;

    public ModuleSniperScopeRender(WeaponRenderHandler weaponRenderer)
    {
        super(weaponRenderer);
    }

    @Override
    public void renderModule(WeaponMetadataSection weaponMeta, ItemStack weaponStack, ItemStack moduleStack, float ticks, int pass)
    {
        Vec3 scopePos = weaponMeta.getModulePosition("default_scope");
        if (scopePos != null)
        {
            GlStateManager.translate(scopePos.xCoord,scopePos.yCoord,scopePos.zCoord);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_ONE,GL11.GL_ONE);
            weaponRenderer.renderModel(scopeBakedModelWindow,weaponStack);
            GlStateManager.disableBlend();

            weaponRenderer.renderModel(scopeBakedModelBase,weaponStack);
        }
    }

    @Override
    public void transformWeapon(WeaponMetadataSection weaponMeta, ItemStack weaponStack, ItemStack moduleStack, float ticks,float zoomValue)
    {
        Vec3 scopePos = weaponMeta.getModulePosition("default_scope");
        if (scopePos != null)
        {
            GlStateManager.translate(MOMathHelper.Lerp(0, -0.0005, zoomValue), MOMathHelper.Lerp(0, -scopePos.yCoord/5.5f, zoomValue), 0);
        }
    }

    @Override
    public void onModelBake(TextureMap textureMap,RenderHandler renderHandler)
    {
        List<String> visibleGroups = new ArrayList<>();
        visibleGroups.add("sniper_scope");
        scopeBakedModelBase = scopeModel.bake(new OBJModel.OBJState(visibleGroups,true),DefaultVertexFormats.ITEM,RenderHandler.modelTextureBakeFunc);
        visibleGroups.clear();
        visibleGroups.add("sniper_scope_window");
        scopeBakedModelWindow = scopeModel.bake(new OBJModel.OBJState(visibleGroups,true),DefaultVertexFormats.ITEM,RenderHandler.modelTextureBakeFunc);
    }

    @Override
    public void onTextureStich(TextureMap textureMap,RenderHandler renderHandler)
    {
        scopeModel = renderHandler.getObjModel(scopeModelLocation,new ImmutableMap.Builder<String,String>().put("flip-v","true").put("ambient","false").build());
        renderHandler.registerModelTextures(textureMap,scopeModel);
    }
}
