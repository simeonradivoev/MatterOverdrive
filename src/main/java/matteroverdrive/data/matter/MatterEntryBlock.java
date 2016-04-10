package matteroverdrive.data.matter;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Simeon on 1/18/2016.
 */
public class MatterEntryBlock extends MatterEntryAbstract<Block, IBlockState>
{
	public MatterEntryBlock(Block block)
	{
		super(block);
	}

	@Override
	public void writeTo(DataOutput output) throws IOException
	{

	}

	@Override
	public void writeTo(NBTTagCompound tagCompound)
	{

	}

	@Override
	public void readFrom(DataInput input) throws IOException
	{

	}

	@Override
	public void readFrom(NBTTagCompound tagCompound)
	{

	}

	@Override
	public void readKey(String data)
	{

	}

	@Override
	public String writeKey()
	{
		return null;
	}

	@Override
	public boolean hasCached()
	{
		return false;
	}
}
