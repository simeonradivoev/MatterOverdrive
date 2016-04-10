package matteroverdrive.network.packet;


import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Simeon on 4/22/2015.
 */
public abstract class AbstractBiPacketHandler<T extends IMessage> extends AbstractPacketHandler<T>
{
	public AbstractBiPacketHandler()
	{
	}
}
