package matteroverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMatterStorage 
{
	int getMatterStored();
	int getCapacity();
	int receiveMatter(ForgeDirection side,int amount, boolean simulate);
	int extractMatter(ForgeDirection direction,int amount,boolean simulate);
	void setMatterStored(int amount);
	
}
