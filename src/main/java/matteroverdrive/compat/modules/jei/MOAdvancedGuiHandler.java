package matteroverdrive.compat.modules.jei;

import matteroverdrive.gui.MOGuiBase;
import mezz.jei.api.gui.BlankAdvancedGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class MOAdvancedGuiHandler extends BlankAdvancedGuiHandler<MOGuiBase>
{

	@Nonnull
	@Override
	public Class<MOGuiBase> getGuiContainerClass()
	{
		return MOGuiBase.class;
	}

	@Nullable
	@Override
	public List<Rectangle> getGuiExtraAreas(MOGuiBase gui)
	{
		List<Rectangle> areas = new ArrayList<>();

		if (gui.getSidePannel().isOpen())
		{
			areas.add(new Rectangle(gui.getSidePannel().getPosX() + gui.getGuiLeft(), gui.getSidePannel().getPosY() + gui.getGuiTop(), gui.getSidePannel().getWidth(), gui.getSidePannel().getHeight()));
		}

		gui.getElements().stream()
				.map(e -> new Rectangle(e.getPosX(), e.getPosY(), e.getWidth(), e.getHeight()))
				.forEach(areas::add);


		return areas;
	}

}
