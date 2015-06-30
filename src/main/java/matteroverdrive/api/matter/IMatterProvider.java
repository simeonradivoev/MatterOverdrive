package matteroverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMatterProvider 
{
	int getMatterStored();
	int getMatterCapacity();
	int extractMatter(ForgeDirection direction,int amount,boolean simulate);
}
