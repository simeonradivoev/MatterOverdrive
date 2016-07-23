package matteroverdrive.blocks.includes;

import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;
import net.shadowfacts.shadowmc.item.OreDictItem;

/**
 * @author shadowfacts
 */
public class MOBlockOre extends MOBlock implements OreDictItem
{

	private final String oreDict;

	public MOBlockOre(Material material, String name, String oreDict)
	{
		super(material, name);
		this.oreDict = oreDict;
	}

	@Override
	public void registerOreDict()
	{
		OreDictionary.registerOre(oreDict, this);
	}
}
