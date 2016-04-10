package matteroverdrive.client.render.weapons.modules;

import matteroverdrive.client.render.weapons.WeaponRenderHandler;

/**
 * Created by Simeon on 2/18/2016.
 */
public abstract class ModuleRenderAbstract implements IModuleRender
{
	protected final WeaponRenderHandler weaponRenderer;

	public ModuleRenderAbstract(WeaponRenderHandler weaponRenderer)
	{
		this.weaponRenderer = weaponRenderer;
	}
}
