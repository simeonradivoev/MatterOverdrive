package matteroverdrive.blocks.world;

import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

/**
 * Created by Simeon on 3/23/2015.
 */
public class DilithiumOre extends MOBlock
{

    public DilithiumOre(Material material,String name)
    {
        super(material,name);
        this.setHardness(4.0f);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 3);
        //this.setStepSound(Block.soundTypePiston);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return MatterOverdriveItems.dilithium_ctystal;
    }

    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, random, fortune))
        {
            int j = random.nextInt(fortune) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }

    private final Random rand = new Random();
    @Override
    public int getExpDrop(IBlockState state,IBlockAccess world, BlockPos pos, int fortune)
    {
        if (this.getItemDropped(world.getBlockState(pos), rand, fortune) != Item.getItemFromBlock(this))
        {
            return MathHelper.getRandomIntegerInRange(rand, 2, 5);
        }
        return 0;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }
}
