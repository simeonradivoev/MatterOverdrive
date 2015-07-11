package matteroverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMatterReceiver 
{
	int getMatterStored();
	int getMatterCapacity();
	int receiveMatter(ForgeDirection side,int amount, boolean simulated);
}
