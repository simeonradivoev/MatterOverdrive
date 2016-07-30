package matteroverdrive.data.recipes;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.item.ItemStack;
import net.shadowfacts.shadowmc.recipe.Recipe;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author shadowfacts
 */
@Getter
@NoArgsConstructor
public class InscriberRecipe extends Recipe<TileEntityInscriber>
{

	private ItemStack primary;
	private ItemStack secondary;

	private ItemStack output;

	private int energy;
	private int time;

	@Override
	public boolean matches(TileEntityInscriber machine)
	{
		ItemStack primary = machine.getStackInSlot(TileEntityInscriber.MAIN_INPUT_SLOT_ID);
		ItemStack secondary = machine.getStackInSlot(TileEntityInscriber.SEC_INPUT_SLOT_ID);
		return ItemStack.areItemsEqual(primary, this.primary) &&
				ItemStack.areItemsEqual(secondary, this.secondary);
	}

	@Override
	public ItemStack getOutput(TileEntityInscriber machine)
	{
		return output.copy();
	}

	@Override
	public List<ItemStack> getInputs()
	{
		return ImmutableList.of(primary, secondary);
	}

	@Override
	public void fromXML(Element element) {
		energy = getIntAttr(element, "energy");
		time = getIntAttr(element, "time");

		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				switch (node.getNodeName()) {
					case "primary":
						primary = getStack((Element)node);
						break;
					case "secondary":
						secondary = getStack((Element)node);
						break;
					case "output":
						output = getStack((Element)node);
						break;
				}
			}
		}
	}

}
