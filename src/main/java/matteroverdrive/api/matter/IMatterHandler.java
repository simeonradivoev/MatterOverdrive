package matteroverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMatterHandler extends IMatterProvider,IMatterReceiver
{
	@Override
	int getMatterStored();
	@Override
	int getMatterCapacity();
	@Override
	int receiveMatter(ForgeDirection side,int amount, boolean simulate);
	@Override
	int extractMatter(ForgeDirection direction,int amount,boolean simulate);
}
