package matteroverdrive.items.includes;

import net.minecraftforge.oredict.OreDictionary;
import net.shadowfacts.shadowmc.item.OreDictItem;

/**
 * @author shadowfacts
 */
public class MOItemOre extends MOBaseItem implements OreDictItem
{

	private final String oreDict;

	public MOItemOre(String name, String oreDict)
	{
		super(name);
		this.oreDict = oreDict;
	}

	@Override
	public void registerOreDict()
	{
		OreDictionary.registerOre(oreDict, this);
	}

}
