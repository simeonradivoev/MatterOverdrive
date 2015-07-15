package matteroverdrive.matter_network.tasks;

import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkTaskReplicatePattern extends MatterNetworkTask
{
    NBTTagCompound pattern;

    public MatterNetworkTaskReplicatePattern()
    {
        super();

    }

    public MatterNetworkTaskReplicatePattern(IMatterNetworkConnection sender,short itemID,short itemMetadata,byte amount)
    {
        super(sender);
        pattern = new NBTTagCompound();
        pattern.setShort("id", itemID);
        pattern.setShort("Damage", itemMetadata);
        pattern.setByte("Count", amount);
    }

    public MatterNetworkTaskReplicatePattern(IMatterNetworkConnection sender,NBTTagCompound pattern)
    {
        super(sender);
        this.pattern = pattern;
    }

    @Override
    protected void init()
    {
        setUnlocalizedName("replicate_pattern");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound != null)
        {
            pattern = compound.getCompoundTag("Pattern");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (compound != null)
        {
            compound.setTag("Pattern", pattern);
        }
    }

    //region Getters and setters
    @Override
    public String getName()
    {
        return pattern.getByte("Count") + " " + MOStringHelper.translateToLocal(Item.getItemById(pattern.getShort("id")).getUnlocalizedName() + ".name");
    }

    public int getAmount() {
        return pattern.getByte("Count");
    }

    public void setAmount(int amount) {
        pattern.setByte("Count",(byte)amount);
    }

    public int getItemMetadata() {
        return pattern.getShort("Damage");
    }

    public int getItemID() {
        return pattern.getShort("id");
    }
    //endregion
}
